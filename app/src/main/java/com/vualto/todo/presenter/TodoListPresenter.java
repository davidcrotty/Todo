package com.vualto.todo.presenter;

import android.content.Intent;

import com.vualto.todo.AddItemActivity;
import com.vualto.todo.TodoListActivity;
import com.vualto.todo.service.TaskService;

/**
 * Created by David on 03/09/2015.
 */
public class TodoListPresenter {

    public TodoListActivity _todolistActivity;
    public TaskService _taskService;

    public TodoListPresenter(TodoListActivity todolistActivity, TaskService taskService) {
        _todolistActivity = todolistActivity;
        _taskService = taskService;
    }

    public void launchAdditemActivity() {
        Intent intent = new Intent(_todolistActivity, AddItemActivity.class);
        _todolistActivity.startActivity(intent);
    }
}
