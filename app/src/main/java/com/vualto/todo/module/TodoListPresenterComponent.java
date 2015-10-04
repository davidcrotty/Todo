package com.vualto.todo.module;

import com.vualto.todo.view.AddItemActivity;
import com.vualto.todo.view.TodoListActivity;
import com.vualto.todo.presenter.TodoListPresenter;

import dagger.Component;

/**
 * Created by David on 08/09/2015.
 */
@Component(dependencies = DataRepositoryComponent.class,
           modules = TodoListPresenterModule.class)
public interface TodoListPresenterComponent {
    TodoListPresenter TodoListPresenter();
    void inject(TodoListActivity activity);
    void inject(AddItemActivity activity);
}
