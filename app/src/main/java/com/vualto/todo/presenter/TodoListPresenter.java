package com.vualto.todo.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.vualto.todo.R;
import com.vualto.todo.TodoListActivity;
import com.vualto.todo.fragment.TodoListFragment;
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

    public void launchTodoListFragment(FragmentActivity activity) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        Fragment prev = activity.getSupportFragmentManager().findFragmentByTag(TodoListFragment.class.getName());
        if(prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ft.add(R.id.todolist_fragment_container, new TodoListFragment(), TodoListFragment.class.getName());
        ft.commit();
    }
}
