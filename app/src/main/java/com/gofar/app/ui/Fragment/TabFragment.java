package com.gofar.app.ui.Fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.gofar.app.App;
import com.gofar.app.R;
import com.gofar.app.db.entity.CatalogEntity;
import com.gofar.app.ui.adapter.CatalogAdapter;
import com.google.gson.Gson;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author lcf
 * @date 2018/7/10 14:17
 * @since 1.0
 */
public class TabFragment extends Fragment {
    private ExpandableListView mExpandableListView;

    private String mCatalog;
    private List<String> mGroups;
    private List<List<CatalogEntity>> mItems;
    private CatalogAdapter mAdapter;

    public static TabFragment newInstance(String catalog) {
        Bundle args = new Bundle();
        args.putString("catalog", catalog);
        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCatalog = bundle.getString("catalog");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        mExpandableListView = view.findViewById(R.id.expanded_lv);
        initView();
//        initData();
        //       newInit();
        newInitF();
        //test();
        return view;
    }

    private void initView() {
        mGroups = new ArrayList<>();
        mItems = new ArrayList<>();
        mAdapter = new CatalogAdapter(getContext(), mGroups, mItems);
        mExpandableListView.setAdapter(mAdapter);
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CatalogEntity catalog = (CatalogEntity) mAdapter.getChild(groupPosition, childPosition);
                Toast.makeText(getContext(), catalog.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void initData() {
        LiveData<List<CatalogEntity>> secCatalogs = App.getApp().getRepository().getSecCatalogByCatalog(mCatalog);
        MediatorLiveData<List<CatalogEntity>> mediatorSecCatalogs = new MediatorLiveData<>();
        mediatorSecCatalogs.addSource(secCatalogs, new Observer<List<CatalogEntity>>() {
            @Override
            public void onChanged(@Nullable List<CatalogEntity> catalogEntities) {
                mediatorSecCatalogs.postValue(catalogEntities);
            }
        });
        mediatorSecCatalogs.observe(this, new Observer<List<CatalogEntity>>() {
            @Override
            public void onChanged(@Nullable List<CatalogEntity> catalogEntities) {
                if (catalogEntities != null) {
                    for (CatalogEntity entity : catalogEntities) {
                        mGroups.add(entity.getSecCatalog());
                        initSecCatalog(entity.getSecCatalog());
                    }
                }
            }
        });
    }

    private void initSecCatalog(String secCatalog) {
        LiveData<List<CatalogEntity>> catalogs = App.getApp().getRepository().getCatalogsBySecCatalog(secCatalog);
        MediatorLiveData<List<CatalogEntity>> mediatorCatalogs = new MediatorLiveData<>();
        mediatorCatalogs.addSource(catalogs, new Observer<List<CatalogEntity>>() {
            @Override
            public void onChanged(@Nullable List<CatalogEntity> catalogEntities) {
                mediatorCatalogs.postValue(catalogEntities);
            }
        });
        mediatorCatalogs.observe(this, new Observer<List<CatalogEntity>>() {
            @Override
            public void onChanged(@Nullable List<CatalogEntity> catalogEntities) {
                if (catalogEntities != null) {
                    List<CatalogEntity> list = new ArrayList<>(catalogEntities);
                    mItems.add(list);
                }
            }
        });
    }

    private void newInit() {
        Disposable d = Observable.create(new ObservableOnSubscribe<List<CatalogEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CatalogEntity>> emitter) throws Exception {
                emitter.onNext(App.getApp().getRepository().getSecCatalogByCatalog(mCatalog).getValue());
                emitter.onComplete();
            }
        }).map(new Function<List<CatalogEntity>, List<String>>() {
            @Override
            public List<String> apply(List<CatalogEntity> catalogEntities) throws Exception {
                for (CatalogEntity entity : catalogEntities) {
                    mGroups.add(entity.getSecCatalog());
                }
                return mGroups;
            }
        }).map(new Function<List<String>, List<List<CatalogEntity>>>() {
            @Override
            public List<List<CatalogEntity>> apply(List<String> strings) throws Exception {
                for (String s : strings) {
                    mItems.add(App.getApp().getRepository().getCatalogsBySecCatalog(s).getValue());
                }
                return mItems;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<List<CatalogEntity>>>() {
                    @Override
                    public void accept(List<List<CatalogEntity>> lists) throws Exception {
                        Log.e("Tab", "init end!");
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }


    private void newInitF() {
        Disposable d = App.getApp().getDataBase().getCatalogDao().getSecCatalogsByCatalogF(mCatalog)
                .map(new Function<List<CatalogEntity>, List<String>>() {
                    @Override
                    public List<String> apply(List<CatalogEntity> catalogEntities) throws Exception {
                        List<String> list = new ArrayList<>();
                        for (CatalogEntity entity : catalogEntities) {
                            list.add(entity.getSecCatalog());
                        }
                        Log.e("tab", "group size:" + mGroups.size());
                        mGroups.clear();
                        mGroups.addAll(list);
                        return list;
                    }
                }).flatMap(new Function<List<String>, Publisher<String>>() {

                    @Override
                    public Publisher<String> apply(List<String> strings) throws Exception {
                        return Flowable.fromIterable(strings);
                    }
                }).concatMap(new Function<String, Publisher<List<CatalogEntity>>>() {

                    @Override
                    public Publisher<List<CatalogEntity>> apply(String s) throws Exception {
                        Log.e("tab", "sec:" + s);
                        return App.getApp().getDataBase().getCatalogDao().getCatalogsBySecCatalogF(s);
                    }
                }).toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<List<CatalogEntity>>>() {
                    @Override
                    public void accept(List<List<CatalogEntity>> lists) throws Exception {
                        Log.e("tab", "item size:" + lists.size());
                        mItems.clear();
                        mItems.addAll(lists);
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private void test() {
        LiveData<List<CatalogEntity>> catalogs = App.getApp().getRepository().getCatalogsBySecCatalog("Android基础知识");
        MediatorLiveData<List<CatalogEntity>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.addSource(catalogs, new Observer<List<CatalogEntity>>() {
            @Override
            public void onChanged(@Nullable List<CatalogEntity> catalogEntities) {
                mediatorLiveData.postValue(catalogEntities);
            }
        });
        mediatorLiveData.observe(this, new Observer<List<CatalogEntity>>() {
            @Override
            public void onChanged(@Nullable List<CatalogEntity> catalogEntities) {
                Gson gson = new Gson();
                String s = gson.toJson(catalogEntities);
                Log.e("tab", "all:" + s);
            }
        });
    }
}
