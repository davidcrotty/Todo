package com.vualto.todo.module;

import android.app.Application;

import com.vualto.todo.appstart.AndroidApplication;
import com.vualto.todo.repository.DataRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by David on 13/09/2015.
 */
@Module
public class DataRepositoryModule {

    private final AndroidApplication _applicationContext;

    public DataRepositoryModule(AndroidApplication applicationContext) {
        _applicationContext = applicationContext;
    }

    @Provides
    DataRepository provideDataRepository() {
        return new DataRepository(_applicationContext);
    }
}
