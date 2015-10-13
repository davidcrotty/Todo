package com.david.todo.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

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

    public void addItem(String title, String description) {
        if(description.isEmpty()) return;
        if(title.isEmpty()) return;
        _taskService.createTaskItem(title, description);
    }

    public void fetchTodoItems() {
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
                        for(TaskItemModel taskItemModel : taskItemModelList) {
                            Log.d("TodoListPresenter", "Item: " + taskItemModel._title);
                        }
                    }
            });
    }
}
