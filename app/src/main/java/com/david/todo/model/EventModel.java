package com.david.todo.model;

import java.io.Serializable;
import java.util.Date;

public class EventModel implements Serializable{

    public final String _dateText;
    public Date _date; //also holds time

    public EventModel(Date date, String dateText) {
        _dateText = dateText;
        _date = date;
    }

    public void setTime(Date timeFromMidnight) {
        _date = timeFromMidnight;
    }
}
