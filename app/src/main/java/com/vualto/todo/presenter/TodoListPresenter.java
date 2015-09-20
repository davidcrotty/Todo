package com.vualto.todo.presenter;

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
}
