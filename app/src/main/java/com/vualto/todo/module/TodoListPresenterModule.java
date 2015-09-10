package com.vualto.todo.module;

import com.vualto.todo.TodoListActivity;
import com.vualto.todo.presenter.TodoListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by David on 08/09/2015.
 */
@Module
public class TodoListPresenterModule {
    private final TodoListActivity _activity;
    public TodoListPresenterModule(TodoListActivity todoListActivity) {
        _activity = todoListActivity;
    }

    @Provides
    TodoListPresenter provideTodoListPresenter() {
        return new TodoListPresenter(_activity);
    }
}
