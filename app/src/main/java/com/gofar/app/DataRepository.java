package com.gofar.app;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.gofar.app.db.AppDataBase;
import com.gofar.app.db.entity.CatalogEntity;

import java.util.List;

/**
 * @author lcf
 * @date 2018/7/10 11:28
 * @since 1.0
 */
public class DataRepository {

    private static DataRepository sInstance;

    private AppDataBase mAppDataBase;
    private MediatorLiveData<List<CatalogEntity>> mCatalogList;

    public DataRepository(AppDataBase appDataBase) {
        mAppDataBase = appDataBase;
        mCatalogList = new MediatorLiveData<>();
        mCatalogList.addSource(appDataBase.getCatalogDao().getAllCatalogs()
                , catalogEntities -> mCatalogList.postValue(catalogEntities));
    }

    public static DataRepository getInstance(AppDataBase appDataBase) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(appDataBase);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<CatalogEntity>> getCatalogs() {
        return mCatalogList;
    }

    public LiveData<List<CatalogEntity>> getCatalogsByCatalog(String catalog) {
        return mAppDataBase.getCatalogDao().getCatalogsByCatalog(catalog);
    }

    public LiveData<List<CatalogEntity>> getCatalogsBySecCatalog(String secCatalog) {
        return mAppDataBase.getCatalogDao().getCatalosBySecCatalog(secCatalog);
    }

    public LiveData<CatalogEntity> getCataLogById(int catalogId) {
        return mAppDataBase.getCatalogDao().getCatalogById(catalogId);
    }

    public LiveData<List<CatalogEntity>> getSecCatalogByCatalog(String catalog) {
        return mAppDataBase.getCatalogDao().getSecCatologsByCatalog(catalog);
    }
}
