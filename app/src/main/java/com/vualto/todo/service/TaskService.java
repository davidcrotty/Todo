package com.vualto.todo.service;

import android.util.Log;

import com.vualto.todo.repository.DataRepository;

/**
 * Created by David on 06/09/2015.
 */
public class TaskService implements ITaskService {

    private DataRepository _dataRepository;

    public TaskService(DataRepository dataRepository) {
        _dataRepository = dataRepository;
    }

    @Override
    public void createTaskItem() {
        Log.d("TaskService", "createTaskItem");
    }
}
