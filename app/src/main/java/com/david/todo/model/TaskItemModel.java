package com.david.todo.model;

/**
 * Created by David Crotty on 13/10/2015.
 */
public class TaskItemModel{

    public String _description;
    public String _title;

    public TaskItemModel(String title, String description) {
        _description = description;
        _title = title;
    }
}
