package com.david.todo.presenter;

import android.content.Intent;

import com.david.todo.view.AddItemActivity;

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
}
