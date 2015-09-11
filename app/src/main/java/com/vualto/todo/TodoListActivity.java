package com.vualto.todo;

import android.os.Bundle;

import com.vualto.todo.module.DaggerTodoListPresenterComponent;
import com.vualto.todo.module.TodoListPresenterComponent;
import com.vualto.todo.module.TodoListPresenterModule;
import com.vualto.todo.presenter.TodoListPresenter;

import javax.inject.Inject;

public class TodoListActivity extends BaseActivity {

    private TodoListPresenterComponent _component;
    @Inject TodoListPresenter _presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerTodoListPresenterComponent.builder()
        .todoListPresenterModule(new TodoListPresenterModule(this))
        .build().inject(this);

        _presenter.doStuff();
    }
}
