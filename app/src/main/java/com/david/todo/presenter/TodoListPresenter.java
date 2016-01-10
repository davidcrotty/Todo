package com.david.todo.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.david.todo.R;
import com.david.todo.adapter.TodoItemListAdapter;
import com.david.todo.model.TaskItemModel;
import com.david.todo.service.TaskService;
import com.david.todo.service.TextEntryService;
import com.david.todo.view.AddItemShortView;
import com.david.todo.view.TodoView;

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

    private TodoView _todoView;
    private TaskService _taskService;
    private TextEntryService _textEntryService;

    public TodoListPresenter(TaskService taskService) {
        _taskService = taskService;
    }

    public TodoListPresenter(TodoView view, TaskService taskService, TextEntryService textEntryService) {
        _todoView = view;
        _taskService = taskService;
        _textEntryService = textEntryService;
    }

    public void textChanged(CharSequence sequence, AddItemShortView itemShortView) {
        if(_textEntryService.textHasBeenEntered(sequence)) {
            itemShortView.fadeOptions();
            _todoView.showPostOption();
        } else {
            itemShortView.showOptions();
            _todoView.hidePostOption();
        }
    }

    public void notifySuccessfulItemAdd(int requestCode, View coordinatorLayout, Activity activityContext, Intent intent) {
        switch (requestCode) {
            case ADD_ITEM_ACTIVITY:
                if(intent != null) {
                    if(intent.hasExtra(ITEM_WAS_ADDED)) {
                        _todoView.showSnackbar(Snackbar.make(coordinatorLayout, activityContext.getString(R.string.item_added_succesfully), Snackbar.LENGTH_SHORT));
                        fetchTodoItems(activityContext);
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
                            TodoItemListAdapter adapter= new TodoItemListAdapter(taskItemModelList);
                            _todoView.showTodoItems(adapter);
                        }
                    }
            });
    }
}
