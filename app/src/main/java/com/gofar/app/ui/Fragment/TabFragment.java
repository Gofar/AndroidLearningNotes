package com.gofar.app.ui.Fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.gofar.app.App;
import com.gofar.app.R;
import com.gofar.app.db.entity.CatalogEntity;
import com.gofar.app.ui.adapter.CatalogAdapter;

import java.util.ArrayList;
import java.util.List;

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
        initData();
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
}
