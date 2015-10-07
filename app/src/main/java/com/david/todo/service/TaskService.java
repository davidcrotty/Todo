package com.david.todo.service;

import android.util.Log;

import com.david.todo.domain.TaskItem;
import com.david.todo.repository.DataRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by David on 06/09/2015.
 */
public class TaskService implements ITaskService {

    private DataRepository _dataRepository;

    public TaskService(DataRepository dataRepository) {
        _dataRepository = dataRepository;
    }

    @Override
    public void createTaskItem(String title, String description) {
        TaskItem taskItem = new TaskItem(UUID.randomUUID().toString(), title, description);
        _dataRepository.add(taskItem);
//        getAllItems();
    }

    private void getAllItems() {
        RealmResults<TaskItem> taskItems = _dataRepository.getAll(TaskItem.class);

        for(TaskItem taskItem : taskItems) {
            Log.d("sdf", taskItem.getDescription());
        }
    }
}
