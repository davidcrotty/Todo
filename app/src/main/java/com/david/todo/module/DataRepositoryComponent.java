package com.david.todo.module;

import com.david.todo.repository.DataRepository;

import dagger.Component;

/**
 * Created by David on 13/09/2015.
 */
@Component(modules = DataRepositoryModule.class)
public interface DataRepositoryComponent {
    DataRepository dataRepository();
}
