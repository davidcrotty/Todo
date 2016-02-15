package com.david.todo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.david.todo.R;

public class AddItemActionsView extends RelativeLayout {
    public AddItemActionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.add_item_actions, this);
    }
}
