package com.gofar.app.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.gofar.app.Catalog;
import com.gofar.app.db.dao.CatalogDao;
import com.gofar.app.db.entity.CatalogEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lcf
 * @date 2018/7/9 15:39
 * @since 1.0
 */
@Database(entities = {CatalogEntity.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase sInstance;

    public static final String DATABASE_NAME = "catalog-db";

    public abstract CatalogDao getCatalogDao();

    public static AppDataBase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppDataBase.class) {
                if (sInstance == null) {
                    sInstance = buildDataBase(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private static AppDataBase buildDataBase(Context context) {
        return Room.databaseBuilder(context, AppDataBase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    AppDataBase appDataBase = AppDataBase.getInstance(context);
                                    InputStream is = context.getAssets().open("catalog.json");
                                    Gson gson = new Gson();
                                    List<Catalog> data = gson.fromJson(new InputStreamReader(is), new TypeToken<Catalog>() {
                                    }.getType());
                                    List<CatalogEntity> catalogs = new ArrayList<>();
                                    for (Catalog c : data) {
                                        for (Catalog c1 : c.getData()) {
                                            for (Catalog c2 : c1.getData()) {
                                                CatalogEntity entity = new CatalogEntity();
                                                entity.setCatalog(c.getTitle());
                                                entity.setSecCatalog(c1.getTitle());
                                                entity.setTitle(c2.getTitle());
                                                entity.setPath(c.getPath() + c1.getPath() + c2.getPath());
                                                catalogs.add(entity);
                                            }
                                        }
                                    }
                                    appDataBase.getCatalogDao().insertAll(catalogs);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                })
                .build();
    }
}