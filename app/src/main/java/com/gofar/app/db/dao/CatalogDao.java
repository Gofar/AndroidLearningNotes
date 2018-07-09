package com.gofar.app.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.gofar.app.db.entity.CatalogEntity;

import java.util.List;

/**
 * @author lcf
 * @date 2018/7/9 17:00
 * @since 1.0
 */
public interface CatalogDao {
    @Query("SELECT * FROM catalogs")
    LiveData<List<CatalogEntity>> getAllCatalogs();

    @Query("SELECT * FROM catalogs WHERE catalog= :catalog")
    LiveData<List<CatalogEntity>> getCatalogsByCatalog(String catalog);

    @Query("SELECT * FROM catalogs WHERE secCatalog= :secCatalog")
    LiveData<List<CatalogEntity>> getCatalosBySecCatalog(String secCatalog);

    @Query("SELECT * FROM catalogs WHERE id= :id")
    LiveData<CatalogEntity> getCatalogById(int id);

    @Query("SELECT * FROM catalogs WHERE title= :title")
    LiveData<CatalogEntity> getCatalogByTitle(String title);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CatalogEntity> catalogs);
}
