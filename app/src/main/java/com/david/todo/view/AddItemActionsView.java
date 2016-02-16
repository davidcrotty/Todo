package com.david.todo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import com.david.todo.R;
import com.david.todo.presenter.AddItemPresenter;

public class AddItemActionsView extends RelativeLayout {

    private final AddItemPresenter _addItemPresenter;

    public AddItemActionsView(Context context, AddItemPresenter addItemPresenter) {
        super(context);
        _addItemPresenter = addItemPresenter;
        init();
    }

    /**
     * Usage for view visualisation in parent layout ONLY
     * @param context
     * @param attrs
     */
    @Deprecated
    public AddItemActionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        _addItemPresenter = null;
    }

    private void init() {
        inflate(getContext(), R.layout.add_item_actions, this);
    }
}
