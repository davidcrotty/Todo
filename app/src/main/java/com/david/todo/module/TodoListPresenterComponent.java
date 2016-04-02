package com.david.todo.module;

import com.david.todo.view.activity.TodoListActivity;
import com.david.todo.presenter.TodoListPresenter;

import dagger.Component;

/**
 * Created by David on 08/09/2015.
 */
@Component(dependencies = DataRepositoryComponent.class,
           modules = TodoListPresenterModule.class)
public interface TodoListPresenterComponent {
    TodoListPresenter TodoListPresenter();
    void inject(TodoListActivity activity);
}
