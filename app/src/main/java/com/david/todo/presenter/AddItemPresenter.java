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
