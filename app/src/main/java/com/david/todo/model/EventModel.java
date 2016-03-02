package com.david.todo.model;

import java.io.Serializable;
import java.util.Date;

public class EventModel implements Serializable{

    public final String _dateText;
    public final Date _date;

    public EventModel(Date date, String dateText) {
        _dateText = dateText;
        _date = date;
    }
}
