package com.david.todo.view;

import android.content.Context;
import android.widget.LinearLayout;

import com.david.todo.R;

/**
 * Created by David Crotty on 11/11/2015.
 */
public class AddItemView extends LinearLayout {

    public AddItemView(Context context) {
        super(context);
        inflate(getContext(), R.layout.add_item_view, this);
    }
}
