package com.vualto.todo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by David on 20/09/2015.
 */
public class AddItemActivity extends BaseActivity {

    @Bind(R.id.notes_edit_text)
    EditText _notesText;

    @Bind(R.id.save_item_button)
    FloatingActionButton _saveItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.save_item_button)
    void onClick() {

    }
}
