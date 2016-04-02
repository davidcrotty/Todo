package com.david.todo.view;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.david.todo.R;
import com.david.todo.presenter.AddItemPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddItemActionsView extends RelativeLayout implements View.OnClickListener {

    private final AddItemPresenter _addItemPresenter;
    @Bind(R.id.task_list_fab)
    FloatingActionButton _taskListButton;

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
        ButterKnife.bind(this);
        _taskListButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.task_list_fab:
                _addItemPresenter.delegateLaunchTaskListActivity();
                break;
        }
    }
}
