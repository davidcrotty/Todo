package com.vualto.todo.presenter;

import android.content.Context;
import android.content.Intent;

import com.vualto.todo.view.AddItemActivity;
import com.vualto.todo.view.TodoView;
import com.vualto.todo.service.TaskService;

/**
 * Created by David on 03/09/2015.
 */
public class TodoListPresenter {

    public TodoView _todoView;
    public TaskService _taskService;

    public TodoListPresenter(TodoView view, TaskService taskService) {
        _todoView = view;
        _taskService = taskService;
    }

    public void launchAdditemActivity(Context activityContext) {
        Intent intent = new Intent(activityContext, AddItemActivity.class);
        activityContext.startActivity(intent);
    }
}
