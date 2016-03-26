package com.david.todo.presenter;

import android.content.Intent;
import android.content.res.Resources;

import com.david.todo.model.DateHolderModel;
import com.david.todo.model.EventModel;
import com.david.todo.service.EventService;
import com.david.todo.view.AddItemActivity;

import org.joda.time.DateTime;

import java.util.Date;

public class AddItemPresenter {

    private final AddItemActivity _addItemActivity;
    private final EventService _eventService;

    public AddItemPresenter(AddItemActivity addItemActivity,
                            EventService eventService) {
        _addItemActivity = addItemActivity;
        _eventService = eventService;
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

    public void delegateDatePickerCreation() {
        _addItemActivity.createDatePicker();
    }

    /**
     * Checks data store or if there is an event intent to be used to fill out some/all of
     * the forms contents, called onResume or on date change.
     */
    public void updateListText() {
        EventModel eventModel = getDateModelIntent();
        if(eventModel == null) return;
        Resources resources = _addItemActivity.getResources();
        DateHolderModel dateModel = _eventService.retreiveDateDisplayText(new DateTime(eventModel._date), resources);
        _addItemActivity.updateDateWith(dateModel.getDateText(), dateModel.getColourResource());
    }

    public void removeEventView() {
        _addItemActivity.removeAllActionViews();
    }

    public void updateEventMemoryModel(Date date, String displayText) {
        _addItemActivity.setEventIntentKey(new EventModel(date, displayText));
    }

    public EventModel getDateModelIntent() {
        return _addItemActivity.getDateModelIntent();
    }
}
