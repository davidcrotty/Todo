package com.david.todo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.david.todo.R;
import com.david.todo.presenter.AddItemPresenter;

public class AddItemCardListView extends RelativeLayout {
    private AddItemPresenter _addItemPresenter;

    public AddItemCardListView(Context context, AddItemPresenter addItemPresenter) {
        super(context);
        _addItemPresenter = addItemPresenter;
        init();
    }

    public AddItemCardListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.add_item_card_list_view, this);
    }
}
