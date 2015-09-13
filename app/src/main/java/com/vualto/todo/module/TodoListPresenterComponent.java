package com.vualto.todo.module;

import com.vualto.todo.TodoListActivity;
import com.vualto.todo.presenter.TodoListPresenter;
import com.vualto.todo.service.TaskService;

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
