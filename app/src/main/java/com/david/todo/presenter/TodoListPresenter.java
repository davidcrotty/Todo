package com.david.todo.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.david.todo.R;
import com.david.todo.model.TaskItemModel;
import com.david.todo.view.AddItemActivity;
import com.david.todo.view.TodoView;
import com.david.todo.service.TaskService;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by David on 03/09/2015.
 */
public class TodoListPresenter {

    public static final int ADD_ITEM_ACTIVITY = 1;
    public static final String ITEM_WAS_ADDED = "ITEM_WAS_ADDED";

    public TodoView _todoView;
    public TaskService _taskService;

    public TodoListPresenter(TaskService taskService) {
        _taskService = taskService;
    }

    public TodoListPresenter(TodoView view, TaskService taskService) {
        _todoView = view;
        _taskService = taskService;
    }

    public void launchAdditemActivity(Activity activityContext) {
        Intent intent = new Intent(activityContext, AddItemActivity.class);
        activityContext.startActivityForResult(intent, ADD_ITEM_ACTIVITY);
    }

    public void addItem(String title, String description, AddItemActivity activity) {
        if(title.isEmpty()) {
            activity.showError(activity.getString(R.string.title_empty));
            return;
        }
        _taskService.createTaskItem(title, description);
        activity.setResult(ADD_ITEM_ACTIVITY, new Intent().putExtra(ITEM_WAS_ADDED, true));
        activity.finish();
    }

    public void notifySuccessfulItemAdd(int requestCode, View coordinatorLayout, Activity activityContext, Intent intent) {
        switch (requestCode) {
            case ADD_ITEM_ACTIVITY:
                if(intent != null) {
                    if(intent.hasExtra(ITEM_WAS_ADDED)) {
                        _todoView.showSnackbar(Snackbar.make(coordinatorLayout, activityContext.getString(R.string.item_added_succesfully), Snackbar.LENGTH_SHORT));
                    }
                }
                break;
        }
    }

    public void fetchTodoItems(final Context context) {
        _taskService.getAllTaskItems()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ArrayList<TaskItemModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ArrayList<TaskItemModel> taskItemModelList) {
                        if(taskItemModelList.size() == 0) {
                            _todoView.noTodoItems(context.getString(R.string.no_items));
                        } else {
                            _todoView.showTodoItems();
                        }
                    }
            });
    }
}