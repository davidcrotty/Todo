package com.vualto.todo;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import com.vualto.todo.fragment.TodoListFragment;
import com.vualto.todo.module.DaggerDataRepositoryComponent;
import com.vualto.todo.module.DaggerTodoListPresenterComponent;
import com.vualto.todo.module.DataRepositoryComponent;
import com.vualto.todo.module.DataRepositoryModule;
import com.vualto.todo.module.TodoListPresenterComponent;
import com.vualto.todo.module.TodoListPresenterModule;
import com.vualto.todo.presenter.TodoListPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
