package com.vualto.todo.module;

import com.vualto.todo.presenter.TodoListPresenter;
import com.vualto.todo.repository.DataRepository;
import com.vualto.todo.service.TaskService;
import com.vualto.todo.view.AddItemActivity;
import com.vualto.todo.view.TodoListActivity;

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
            return new TodoListPresenter((AddItemActivity)_activity, new TaskService(dataRepository));
        }
        return null;
    }
}
