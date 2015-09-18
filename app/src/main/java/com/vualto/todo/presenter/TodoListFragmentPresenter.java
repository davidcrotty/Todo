package com.vualto.todo.presenter;

import com.vualto.todo.fragment.TodoListFragment;
import com.vualto.todo.service.TaskService;

/**
 * Created by David on 18/09/2015.
 */
public class TodoListFragmentPresenter {

    private TodoListFragment _todoListFragment;

    public TodoListFragmentPresenter(TodoListFragment todoListFragment) {
        _todoListFragment = todoListFragment;
    }
}
