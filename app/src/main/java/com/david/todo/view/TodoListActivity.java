package com.david.todo.view;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;

import com.david.todo.R;
import com.david.todo.appstart.AndroidApplication;
import com.david.todo.module.DaggerDataRepositoryComponent;
import com.david.todo.module.DaggerTodoListPresenterComponent;
import com.david.todo.module.DataRepositoryComponent;
import com.david.todo.module.DataRepositoryModule;
import com.david.todo.module.TodoListPresenterModule;
import com.david.todo.presenter.TodoListPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Lazy;

public class TodoListActivity extends BaseActivity implements View.OnLayoutChangeListener,
                                                              Animator.AnimatorListener,
                                                              TodoView {

    private DataRepositoryComponent _dataRepositoryComponent;
    @Inject Lazy<TodoListPresenter> _presenter;

    @Bind(R.id.add_item_button)
    FloatingActionButton _addItemButton;

    @Bind(R.id.todolist_container)
    CoordinatorLayout _activityContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initialiseInjector();
        _addItemButton.addOnLayoutChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        _presenter.get().notifySuccessfulItemAdd(requestCode,
                                                _activityContainer,
                                                this /* Activity */,
                                                data);
    }

    private void initialiseInjector() {
        _dataRepositoryComponent = DaggerDataRepositoryComponent.builder()
                .dataRepositoryModule(new DataRepositoryModule((AndroidApplication)getApplicationContext()))
                .build();

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
        _presenter.get().launchAdditemActivity(this /* Activity */);
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

    @Override
    public void showTodoItems() {

    }

    @Override
    public void showSnackbar(Snackbar snackbar) {
        snackbar.show();
    }
}
