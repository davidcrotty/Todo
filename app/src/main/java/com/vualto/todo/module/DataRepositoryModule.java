package com.vualto.todo.module;

import android.app.Application;

import com.vualto.todo.repository.DataRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by David on 13/09/2015.
 */
@Module
public class DataRepositoryModule {

    public DataRepositoryModule() {
    }

    @Provides
    DataRepository provideDataRepository() {
        return new DataRepository();
    }
}
