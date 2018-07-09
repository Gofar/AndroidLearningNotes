package com.gofar.app.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.gofar.app.db.dao.CatalogDao;
import com.gofar.app.db.entity.CatalogEntity;

/**
 * @author lcf
 * @date 2018/7/9 15:39
 * @since 1.0
 */
@Database(entities = {CatalogEntity.class},version = 1)
public abstract class AppDataBase extends RoomDatabase{
    private static AppDataBase sInstance;

    public static final String DATABASE_NAME="catalog-db";

    public abstract CatalogDao getCatalogDao();

    public static AppDataBase getInstance(Context context) {
        if (sInstance==null){
            synchronized (AppDataBase.class){
                if (sInstance==null){
                    sInstance= Room.databaseBuilder(context,AppDataBase.class,DATABASE_NAME)
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                }
                            })
                            .build();
                }
            }
        }
        return sInstance;
    }
}