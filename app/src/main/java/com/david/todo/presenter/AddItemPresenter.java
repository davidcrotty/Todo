package com.david.todo.presenter;

import android.content.Intent;
import android.content.res.Resources;

import com.david.todo.model.DateHolderModel;
import com.david.todo.model.EventModel;
import com.david.todo.model.TimeHolderModel;
import com.david.todo.module.DaggerEventServiceComponent;
import com.david.todo.module.EventServiceModule;
import com.david.todo.service.EventService;
import com.david.todo.view.activity.AddItemActivity;

import org.joda.time.DateTime;

import java.util.Date;

import javax.inject.Inject;

public class AddItemPresenter {

    private final AddItemActivity _addItemActivity;
    @Inject EventService _eventService;

    public AddItemPresenter(AddItemActivity addItemActivity) {
        DaggerEventServiceComponent.builder().eventServiceModule(new EventServiceModule()).build().inject(this);
        _addItemActivity = addItemActivity;
    }

    public void delegateLaunchTaskListActivity() {
        _addItemActivity.launchTaskListActivity();
    }

    public void clearTime() {
        _addItemActivity.getIntent().removeExtra(AddItemActivity.DATE_KEY);
        _addItemActivity.clearTime();
    }

    public void updateTitleWithIntent() {
        Intent intent = _addItemActivity.getIntent();
        if(intent != null) {
            if(intent.hasExtra(AddItemActivity.TITLE_TEXT_INTENT_KEY)) {
                _addItemActivity.setExpandedTitleText(intent.getStringExtra(AddItemActivity.TITLE_TEXT_INTENT_KEY));
                _addItemActivity.getIntent().removeExtra(AddItemActivity.TITLE_TEXT_INTENT_KEY);
            }
        }
    }

    public void updateEventMemoryModelWithTime(int hourOfDay, int minuteOfDay) {
        EventModel eventModel = getDateModelIntent();
        if(eventModel == null) {
            eventModel = new EventModel(new DateTime().plusDays(1).toDate(), "Tomorrow");
        }
        DateTime date = new DateTime(eventModel._date);
        //reset to midnight from prior set
        date = date.withHourOfDay(0);
        date = date.withMinuteOfHour(0);
        //add minutes
        date = date.plusHours(hourOfDay);
        date = date.plusMinutes(minuteOfDay);
        eventModel.setTime(date.toDate());
        _addItemActivity.setEventIntentKey(eventModel);
        _addItemActivity.getIntent().putExtra(AddItemActivity.DATE_KEY, true);
    }

    public void delegateDatePickerCreation() {
        _addItemActivity.createDatePicker();
    }
    
    public void retreiveThenUpdateDateText() {
        EventModel eventModel = getDateModelIntent();
        if(eventModel == null) return;
        Resources resources = _addItemActivity.getResources();
        DateHolderModel dateModel = _eventService.retreiveDateDisplayText(new DateTime(eventModel._date), resources);
        _addItemActivity.updateDateWith(dateModel.getDateText(), dateModel.getColourResource());
    }

    public void retreiveThenUpdateTimeText() {
        EventModel eventModel = getDateModelIntent();
        if(eventModel == null) return;
        Resources resources = _addItemActivity.getResources();
        TimeHolderModel timeHolderModel = _eventService.retreiveTimeDisplaytext(new DateTime(eventModel._date), new DateTime(), resources);
        _addItemActivity.updateTimeWith(timeHolderModel.getTextToDisplay(), timeHolderModel.getColour());
    }

    public void removeEventView() {
        _addItemActivity.removeAllActionViews();
    }

    public void updateEventMemoryModelWithDate(Date date, String displayText) {
        _addItemActivity.getIntent().putExtra(AddItemActivity.DATE_KEY, true);
        _addItemActivity.setEventIntentKey(new EventModel(date, displayText));
    }

    public EventModel getDateModelIntent() {
        return _addItemActivity.getDateModelIntent();
    }
}
