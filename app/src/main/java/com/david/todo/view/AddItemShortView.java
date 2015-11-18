package com.david.todo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.david.todo.R;

/**
 * Created by David Crotty on 18/11/2015.
 */
public class AddItemShortView extends LinearLayout {
    public AddItemShortView(Context context) {
        super(context);
        init();
    }

    public AddItemShortView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.add_item_short_view, this);
    }
}
