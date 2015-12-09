package com.david.todo.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class TodoListActivity extends BaseActivity implements TodoView,
                                                              TextWatcher {

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

    @Bind(R.id.add_item_short_view)
    AddItemShortView _addItemView;

    @Bind(R.id.short_note_text)
    EditText _shortNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initialiseInjector();
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        _presenter.get().fetchTodoItems(getApplicationContext());
        _shortNoteText.addTextChangedListener(this);
        if(_shortNoteText.getText().toString() == null || _shortNoteText.getText().toString().isEmpty()) {
            _presenter.get().textChanged(null);
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
                .todoListPresenterModule(new TodoListPresenterModule(this, _addItemView))
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        _presenter.get().textChanged(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
