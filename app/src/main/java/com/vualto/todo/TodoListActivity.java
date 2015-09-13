package com.vualto.todo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vualto.todo.module.DaggerDataRepositoryComponent;
import com.vualto.todo.module.DaggerTodoListPresenterComponent;
import com.vualto.todo.module.DataRepositoryComponent;
import com.vualto.todo.module.DataRepositoryModule;
import com.vualto.todo.module.TodoListPresenterComponent;
import com.vualto.todo.module.TodoListPresenterModule;
import com.vualto.todo.presenter.TodoListPresenter;

import javax.inject.Inject;

import dagger.Lazy;

public class TodoListActivity extends BaseActivity implements View.OnClickListener {

    private DataRepositoryComponent _dataRepositoryComponent;
    private TodoListPresenterComponent _component;
    @Inject
    Lazy<TodoListPresenter> _presenter;
    private Button _instantiatePresenterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _instantiatePresenterButton = (Button) findViewById(R.id.instantiate_presenter_button);
        _instantiatePresenterButton.setOnClickListener(this);
        _dataRepositoryComponent = DaggerDataRepositoryComponent.builder()
                                   .dataRepositoryModule(new DataRepositoryModule()).build();

        DaggerTodoListPresenterComponent.builder()
        .dataRepositoryComponent(_dataRepositoryComponent)
        .todoListPresenterModule(new TodoListPresenterModule(this))
        .build().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(_presenter == null) {
            Toast.makeText(this, "No presenter", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        _presenter.get().doStuff();
    }
}
