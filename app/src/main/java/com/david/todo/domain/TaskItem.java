package com.david.todo.domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by David on 01/10/2015.
 */
public class TaskItem extends RealmObject {

    @PrimaryKey
    private String id;
    private String description;

    public TaskItem() {
    }

    public TaskItem(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
