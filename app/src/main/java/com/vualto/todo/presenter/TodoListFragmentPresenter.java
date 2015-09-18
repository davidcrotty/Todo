package com.vualto.todo.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.vualto.todo.R;
import com.vualto.todo.fragment.AddItemFragment;
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

    public void showAddItemFragment(FragmentActivity activity) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        Fragment prev = activity.getSupportFragmentManager().findFragmentByTag(AddItemFragment.class.getName());
        if(prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ft.add(R.id.todolist_fragment_container, new AddItemFragment(), AddItemFragment.class.getName());
        ft.commit();
    }

    public void removeTodoListFragment(FragmentActivity activity, TodoListFragment fragment) {
        activity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }
}
