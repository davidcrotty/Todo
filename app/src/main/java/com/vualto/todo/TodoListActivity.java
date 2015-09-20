package com.vualto.todo;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;

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

public class TodoListActivity extends BaseActivity implements View.OnLayoutChangeListener,
                                                              Animator.AnimatorListener {

    private DataRepositoryComponent _dataRepositoryComponent;
    private TodoListPresenterComponent _component;
    @Inject Lazy<TodoListPresenter> _presenter;

    @Bind(R.id.add_item_button)
    FloatingActionButton _addItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        _addItemButton.addOnLayoutChangeListener(this);
         _dataRepositoryComponent = DaggerDataRepositoryComponent.builder()
                                   .dataRepositoryModule(new DataRepositoryModule()).build();

        DaggerTodoListPresenterComponent.builder()
        .dataRepositoryComponent(_dataRepositoryComponent)
        .todoListPresenterModule(new TodoListPresenterModule(this))
        .build().inject(this);
    }

    @OnClick(R.id.add_item_button)
    void onClick() {
        int startRadius = Math.max(_addItemButton.getWidth(), _addItemButton.getHeight());
        int cx = _addItemButton.getWidth() / 2;
        int cy = _addItemButton.getHeight() / 2;

        Animator anim = ViewAnimationUtils.createCircularReveal(_addItemButton, cx, cy, startRadius, 0);
        anim.addListener(this);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(700);
        anim.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        _addItemButton.setVisibility(View.INVISIBLE);
        _presenter.get().launchAdditemActivity();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        _addItemButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        int finalRadius = Math.max(_addItemButton.getWidth(), _addItemButton.getHeight());
        int cx = _addItemButton.getWidth() / 2;
        int cy = _addItemButton.getHeight() / 2;

        Animator anim = ViewAnimationUtils.createCircularReveal(_addItemButton, cx, cy, 0, finalRadius);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(700);
        anim.start();
    }
}
