package com.david.todo.view;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.david.todo.R;
import com.david.todo.adapter.TodoItemListAdapter;
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
                                                              TodoView {

    private DataRepositoryComponent _dataRepositoryComponent;
    @Inject Lazy<TodoListPresenter> _presenter;

    @Bind(R.id.add_item_button)
    FloatingActionButton _addItemButton;

    @Bind(R.id.todolist_container)
    CoordinatorLayout _activityContainer;

    @Bind(R.id.todo_item_container)
    FrameLayout _todoItemContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        _addItemButton.setEnabled(true);
        initialiseInjector();
        _addItemButton.addOnLayoutChangeListener(this);
        _presenter.get().fetchTodoItems(getApplicationContext());
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
        _addItemButton.setEnabled(false);
        _presenter.get().launchAdditemActivity(this /* Activity */);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _addItemButton.setVisibility(View.VISIBLE);
        _addItemButton.setEnabled(true);
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
    public void showTodoItems(TodoItemListAdapter adapter) {
        _todoItemContainer.removeAllViews();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(this).inflate(R.layout.todo_list, null);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams( FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT );
        layoutParams.gravity = Gravity.CENTER;
        _todoItemContainer.addView(recyclerView, layoutParams);
    }

    @Override
    public void showSnackbar(Snackbar snackbar) {
        snackbar.show();
    }

    @Override
    public void noTodoItems(String text) {
        _todoItemContainer.removeAllViews();
        TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.no_todo_textview, null);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams( FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT );
        layoutParams.gravity = Gravity.CENTER;
        _todoItemContainer.addView(textView, layoutParams);
        textView.setTextColor(getResources().getColor(R.color.ColorPrimaryText));
        textView.setText(text);
    }
}
