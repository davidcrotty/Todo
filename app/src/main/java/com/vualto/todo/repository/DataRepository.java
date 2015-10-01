package com.vualto.todo.repository;

import android.util.Log;

import com.vualto.todo.appstart.AndroidApplication;

/**
 * Created by David on 06/09/2015.
 */
public class DataRepository {

    private AndroidApplication _applicationContext;

    public DataRepository(AndroidApplication applicationContext) {
//        Log.d("DataRepository", "Data repo constructed");
        _applicationContext = applicationContext;
    }
}
