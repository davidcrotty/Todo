package com.david.todo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.david.todo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by David Crotty on 18/11/2015.
 */
public class AddItemShortView extends LinearLayout {

    @Bind(R.id.options_panel)
    LinearLayout _optionsPanel;

    @Bind(R.id.options_divider)
    View _optionsDivider;

    public AddItemShortView(Context context) {
        super(context);
        init();
    }

    public AddItemShortView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void fadeOptions() {
        _optionsPanel.setVisibility(View.INVISIBLE);
        _optionsDivider.setVisibility(View.INVISIBLE);
    }

    public void showOptions() {
        if(_optionsPanel.getVisibility() == View.VISIBLE) return;
        _optionsPanel.setVisibility(View.VISIBLE);
        _optionsDivider.setVisibility(View.VISIBLE);
    }

    private void init() {
        inflate(getContext(), R.layout.add_item_short_view, this);
        ButterKnife.bind(this);
    }
}
