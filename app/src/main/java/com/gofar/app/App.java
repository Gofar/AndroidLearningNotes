package com.gofar.app;

import android.app.Application;

import com.gofar.app.db.AppDataBase;

/**
 * @author lcf
 * @date 2018/7/10 10:29
 * @since 1.0
 */
public class App extends Application {
    private static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }

    public static App getApp() {
        return sApp;
    }

    public AppDataBase getDataBase() {
        return AppDataBase.getInstance(this);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDataBase());
    }
}
