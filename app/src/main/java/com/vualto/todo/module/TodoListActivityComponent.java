package com.vualto.todo.module;

import com.vualto.todo.TodoListActivity;

import dagger.Component;

/**
 * Created by David on 07/09/2015.
 */
@Activity
@Component(modules = {TodoListActivityModule.class})
public interface TodoListActivityComponent {
    void inject(TodoListActivity todoListActivity);
}
