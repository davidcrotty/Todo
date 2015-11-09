package com.david.todo.view;

import android.content.Context;
import android.widget.LinearLayout;

import com.david.todo.R;

/**
 * Created by David Crotty on 08/11/2015.
 */
public class NoTodoItemsView extends LinearLayout {

    public NoTodoItemsView(Context context) {
        super(context);
        inflate(getContext(), R.layout.no_todo_items_view, this);
    }
}
