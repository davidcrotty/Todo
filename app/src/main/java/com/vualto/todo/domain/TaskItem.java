package com.vualto.todo.domain;

import io.realm.RealmObject;

/**
 * Created by David on 01/10/2015.
 */
public class TaskItem extends RealmObject {

    private String description;

    public TaskItem() {
    }

    public TaskItem(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
