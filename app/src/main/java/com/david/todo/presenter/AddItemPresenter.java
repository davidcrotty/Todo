package com.david.todo.presenter;

import android.content.Intent;

import com.david.todo.model.EventModel;
import com.david.todo.view.AddItemActivity;

import java.util.Date;

public class AddItemPresenter {

    private AddItemActivity _addItemActivity;

    public AddItemPresenter(AddItemActivity addItemActivity) {
        _addItemActivity = addItemActivity;
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
     * the forms contents
     */
    public void updateListText() {
        EventModel eventModel = getDateModelIntent();
        if(eventModel == null) return;
        //if is today or tomorrow display
        //if > tomorrow && <= a week display weekday
        //else MMM - dd format

    }

    public void removeEventView() {
        _addItemActivity.removeAllActionViews();
    }

    public void updateEvent(Date date, String displayText) {
        _addItemActivity.setEventIntentKey(new EventModel(date, displayText));
    }

    public EventModel getDateModelIntent() {
        return _addItemActivity.getDateModelIntent();
    }
}
