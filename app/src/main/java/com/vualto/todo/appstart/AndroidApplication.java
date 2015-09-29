package com.vualto.todo.appstart;

import android.app.Application;

import com.vualto.todo.module.DataRepositoryComponent;

/**
 * Created by David on 29/09/2015.
 */
public class AndroidApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public DataRepositoryComponent getDataRepositoryComponent() {
        return null;
    }
}
