package com.david.todo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.david.todo.R;
import com.david.todo.presenter.AddItemPresenter;
import com.david.todo.view.activity.AddItemActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddItemActionsView extends RelativeLayout implements View.OnClickListener {

    private final AddItemPresenter _addItemPresenter;
    @Bind(R.id.task_list_fab)
    FloatingActionButton _taskListButton;
    @Bind(R.id.main_action_view)
    RelativeLayout _mainActionView;

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

    private void setupTransitionAnimationWith(final FloatingActionButton fabToAnimate, FloatingActionButton originalFab) {
        originalFab.setVisibility(View.INVISIBLE);

        AddItemActivity additemActivity = (AddItemActivity) getContext();
        Window window = additemActivity.getWindow();
        WindowManager.LayoutParams windowLayoutParams = new WindowManager.LayoutParams();
        windowLayoutParams.gravity = Gravity.START;
        windowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        window.addContentView(fabToAnimate, windowLayoutParams);

        int[] xYCoordinates = new int[2];
        _taskListButton.getLocationOnScreen(xYCoordinates);

        final Point screenSize = additemActivity.getScreenSize();

        fabToAnimate.setTranslationX(xYCoordinates[0]);
        fabToAnimate.setTranslationY(xYCoordinates[1]);

        ValueAnimator curveAnimation = ValueAnimator.ofFloat(0, 1); // values from 0 to 1
        curveAnimation.setDuration(225); // 5 seconds duration from 0 to 1

        final float[] point = new float[2];

        final Path path = new Path();
        path.moveTo(xYCoordinates[0], xYCoordinates[1]);
        path.quadTo(screenSize.x / 4, screenSize.y / 4 , screenSize.x / 2, screenSize.y / 2);

        curveAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = animation.getAnimatedFraction();
                PathMeasure pathMeasure = new PathMeasure(path, false);
                pathMeasure.getPosTan(pathMeasure.getLength() * val, point, null);
                fabToAnimate.setX(point[0]);
                fabToAnimate.setY(point[1]);
            }
        });

        curveAnimation.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.task_list_fab:
                //Needs to be added as null as window does not have a view group
                FloatingActionButton translatingFab =
                        (FloatingActionButton) ((AddItemActivity) getContext()).getLayoutInflater().inflate(R.layout.task_list_fab, null);
                setupTransitionAnimationWith(translatingFab, _taskListButton);
                break;
        }
    }
}
