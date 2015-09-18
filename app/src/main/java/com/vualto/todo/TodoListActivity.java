package com.vualto.todo;

import android.os.Bundle;

import com.vualto.todo.module.DaggerDataRepositoryComponent;
import com.vualto.todo.module.DaggerTodoListPresenterComponent;
import com.vualto.todo.module.DataRepositoryComponent;
import com.vualto.todo.module.DataRepositoryModule;
import com.vualto.todo.module.TodoListPresenterComponent;
import com.vualto.todo.module.TodoListPresenterModule;
import com.vualto.todo.presenter.TodoListPresenter;

import javax.inject.Inject;

import dagger.Lazy;

public class TodoListActivity extends BaseActivity  {

    private DataRepositoryComponent _dataRepositoryComponent;
    private TodoListPresenterComponent _component;
    @Inject Lazy<TodoListPresenter> _presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         _dataRepositoryComponent = DaggerDataRepositoryComponent.builder()
                                   .dataRepositoryModule(new DataRepositoryModule()).build();

        DaggerTodoListPresenterComponent.builder()
        .dataRepositoryComponent(_dataRepositoryComponent)
        .todoListPresenterModule(new TodoListPresenterModule(this))
        .build().inject(this);
        _presenter.get().launchTodoListFragment(this);
    }
}
