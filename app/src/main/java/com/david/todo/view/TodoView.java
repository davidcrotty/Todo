package com.david.todo.view;

import android.support.design.widget.Snackbar;

import com.david.todo.adapter.TodoItemListAdapter;

/**
 * Created by David on 29/09/2015.
 */
public interface TodoView {
    void showTodoItems(TodoItemListAdapter todoItemListAdapter);
    void showSnackbar(Snackbar snackbar);
    void noTodoItems(String text);
    void showPostOption();
    void hidePostOption();
}
