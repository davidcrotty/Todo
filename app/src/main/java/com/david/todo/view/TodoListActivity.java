package com.david.todo.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import dagger.Lazy;

public class TodoListActivity extends BaseActivity implements TodoView {

    private DataRepositoryComponent _dataRepositoryComponent;
    @Inject Lazy<TodoListPresenter> _presenter;

    @Bind(R.id.todolist_container)
    CoordinatorLayout _activityContainer;

    @Bind(R.id.todo_item_view_frame)
    LinearLayout _todoItemViewFrame;

    @Bind(R.id.items_pane)
    FrameLayout _itemsPane;

    @Bind(R.id.toolbar)
    Toolbar _toolbar;

    AddItemShortView _addItemShortView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initialiseInjector();
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        addItemShortView();
        _presenter.get().fetchTodoItems(getApplicationContext());
    }

    private void addItemShortView() {
        if(_addItemShortView == null) {
            _addItemShortView = new AddItemShortView(this, _presenter.get());
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            _todoItemViewFrame.addView(_addItemShortView, 0,layoutParams); // z-ordering
//            _shortNoteText = (EditText) findViewById(R.id.short_note_text); //TODO move this behaviour to inside view
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        _toolbar.inflateMenu(R.menu.menu_main);
        return true;
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

    @Override
    public void showTodoItems(TodoItemListAdapter adapter) {
        _itemsPane.removeAllViews();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(this).inflate(R.layout.todo_list, null);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams( FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT );
        layoutParams.gravity = Gravity.CENTER;
        _itemsPane.addView(recyclerView, layoutParams);
    }

    @Override
    public void showSnackbar(Snackbar snackbar) {
        snackbar.show();
    }

    @Override
    public void noTodoItems(String text) {
        _itemsPane.removeAllViews();
        NoTodoItemsView noTodoItemsView = new NoTodoItemsView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT );
        layoutParams.gravity = Gravity.CENTER;
        _itemsPane.addView(noTodoItemsView, layoutParams);
    }

    @Override
    public void showPostOption() {
        if(_toolbar.findViewById(R.id.post_text) != null) return;
        TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.post_text, null, false);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.RIGHT;
        _toolbar.addView(textView, layoutParams);
    }

    @Override
    public void hidePostOption() {
        TextView textView = (TextView) findViewById(R.id.post_text);
        if(textView != null) {
            _toolbar.removeView(textView);
        }
    }

    public void hideKeyboard() {
        TextView addItemShortView = (TextView) findViewById(R.id.short_note_text);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(addItemShortView.getWindowToken(), 0);
    }
}
