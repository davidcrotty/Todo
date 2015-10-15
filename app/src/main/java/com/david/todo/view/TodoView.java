package com.david.todo.view;

import android.support.design.widget.Snackbar;

/**
 * Created by David on 29/09/2015.
 */
public interface TodoView {
    void showTodoItems();
    void showSnackbar(Snackbar snackbar);
    void noTodoItems(String text);
}
