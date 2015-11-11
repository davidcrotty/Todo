package com.david.todo.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.david.todo.R;

/**
 * Created by David Crotty on 11/11/2015.
 */
public class AddItemView extends LinearLayout {

    public AddItemView(Context context, int textBoxPadding) {
        super(context);
        init(textBoxPadding);
    }

    private void init(int textBoxPadding) {
        View view = inflate(getContext(), R.layout.add_item_view, this);
        LinearLayout editTextContainer = (LinearLayout) view.findViewById(R.id.edit_text_container);
        editTextContainer.setPadding(textBoxPadding, 0, textBoxPadding, 0);
    }
}
