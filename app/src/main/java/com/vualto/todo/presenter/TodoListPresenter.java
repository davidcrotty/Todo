package com.vualto.todo.presenter;

import android.util.Log;

import com.vualto.todo.TodoListActivity;
import com.vualto.todo.service.TaskService;

import javax.inject.Inject;

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

    public void doStuff() {
        _taskService.createTaskItem();
    }
}
