package com.vualto.todo.module;

import com.vualto.todo.TodoListActivity;
import com.vualto.todo.presenter.TodoListPresenter;

import dagger.Component;

/**
 * Created by David on 08/09/2015.
 */
@Component(modules = TodoListPresenterModule.class)
public interface TodoListPresenterComponent {
    TodoListPresenter provideTodoListPresenter();
    void inject(TodoListActivity activity);
}
