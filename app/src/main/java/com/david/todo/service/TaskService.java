package com.david.todo.service;

import com.david.todo.domain.TaskItem;
import com.david.todo.model.TaskItemModel;
import com.david.todo.repository.DataRepository;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

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

    public Observable<ArrayList<TaskItemModel>> getAllTaskItems() {
        return Observable.create(new Observable.OnSubscribe<RealmResults<TaskItem>>() {
            @Override
            public void call(Subscriber<? super RealmResults<TaskItem>> subscriber) {
                RealmResults<TaskItem> taskItems = _dataRepository.getAll(TaskItem.class);
                subscriber.onNext(taskItems);
            }
        }).map(new Func1<RealmResults<TaskItem>, ArrayList<TaskItemModel>>() {
            @Override
            public ArrayList<TaskItemModel> call(RealmResults<TaskItem> taskItemList) {
                ArrayList<TaskItemModel> modelList = new ArrayList();
                for(TaskItem taskItem : taskItemList) {
                    modelList.add(new TaskItemModel(taskItem.getTitle(), taskItem.getDescription()));
                }

                return modelList;
            }
        });
    }
}
