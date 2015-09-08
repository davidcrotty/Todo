package com.vualto.todo;

import android.os.Bundle;

import com.vualto.todo.module.DaggerTodoListActivityComponent;
import com.vualto.todo.module.TodoListActivityComponent;
import com.vualto.todo.module.TodoListActivityModule;
import com.vualto.todo.presenter.TodoListPresenter;

public class TodoListActivity extends BaseActivity {

    private TodoListActivityComponent _component;
    private TodoListPresenter _presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _component = DaggerTodoListActivityComponent.builder()
                     .todoListActivityModule(new TodoListActivityModule(this))
                     .build();

        _component.inject(this);

        _presenter.doStuff();
    }
}
