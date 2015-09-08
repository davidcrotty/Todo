package com.vualto.todo.module;

import android.content.Context;

import com.vualto.todo.TodoListActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by David on 07/09/2015.
 */
@Module
public class TodoListActivityModule {
    private final TodoListActivity _activity;

    public TodoListActivityModule(TodoListActivity activity) {
        _activity = activity;
    }

    @Provides
    @Activity
    TodoListActivity provideActivityContext() {
        return _activity;
    }
}
