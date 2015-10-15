package com.david.todo.module;

import com.david.todo.presenter.TodoListPresenter;
import com.david.todo.repository.DataRepository;
import com.david.todo.service.TaskService;
import com.david.todo.view.AddItemActivity;
import com.david.todo.view.TodoListActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by David on 08/09/2015.
 */
@Module
public class TodoListPresenterModule {
    private final android.app.Activity _activity;

    public TodoListPresenterModule(android.app.Activity todoListActivity) {
        _activity = todoListActivity;
    }

    @Provides
    TodoListPresenter provideTodoListPresenter(DataRepository dataRepository) {
        if(_activity instanceof TodoListActivity) {
            return new TodoListPresenter((TodoListActivity)_activity, new TaskService(dataRepository));
        } else if (_activity instanceof AddItemActivity) {
            return new TodoListPresenter(new TaskService(dataRepository));
        }
        return null;
    }
}
