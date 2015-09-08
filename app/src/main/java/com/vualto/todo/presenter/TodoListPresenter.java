package com.vualto.todo.presenter;

import android.util.Log;

import com.vualto.todo.TodoListActivity;

import javax.inject.Inject;

/**
 * Created by David on 03/09/2015.
 */
public class TodoListPresenter {

    public TodoListActivity _todolistActivity;

    @Inject
    public TodoListPresenter(TodoListActivity todolistActivity) {
        _todolistActivity = todolistActivity;
    }

    public void doStuff() {
        Log.d("TodoListPresenter", "working");
    }
}
