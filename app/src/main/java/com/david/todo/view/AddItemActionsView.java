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
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.david.todo.BuildConfig;
import com.david.todo.R;
import com.david.todo.presenter.AddItemPresenter;
import com.david.todo.view.activity.AddItemActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.codetail.animation.SupportAnimator;

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

        final AddItemActivity additemActivity = (AddItemActivity) getContext();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                                                             FrameLayout.LayoutParams.WRAP_CONTENT);
        additemActivity._revealLayout.addView(fabToAnimate, layoutParams);

        int[] xYCoordinates = new int[2];
        _taskListButton.getLocationOnScreen(xYCoordinates);

        final Point screenSize = additemActivity.getScreenSize();

        fabToAnimate.setTranslationX(xYCoordinates[0]);
        fabToAnimate.setTranslationY(xYCoordinates[1]);

        ValueAnimator curveAnimation = ValueAnimator.ofFloat(0, 1); // values from 0 to 1
        curveAnimation.setDuration(BuildConfig.EASE_IN_DURATION); // 5 seconds duration from 0 to 1

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
                fabToAnimate.setX(point[0] - (fabToAnimate.getWidth() / 2));
                fabToAnimate.setY(point[1]);
            }
        });

        curveAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startRevealCompatAnimationWith(fabToAnimate, screenSize, additemActivity);
            }
        });
        curveAnimation.start();
    }

    private void startRevealCompatAnimationWith(FloatingActionButton fabToAnimate, Point screenSize, final AddItemActivity addItemActivity) {
        FrameLayout layout = addItemActivity._revealContainer;
        float finalRadius = Math.max(layout.getWidth(), layout.getHeight());

        SupportAnimator circularReveal = io.codetail.animation.ViewAnimationUtils.createCircularReveal(layout,
                                                                                                       screenSize.x / 2,
                                                                                                       screenSize.y / 2,
                                                                                                       0,
                                                                                                       finalRadius);


        layout.bringToFront();
        layout.setVisibility(VISIBLE);
        fabToAnimate.setVisibility(INVISIBLE);
        circularReveal.setDuration(BuildConfig.FULL_SCREEN_TRANSITION);
        circularReveal.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                addItemActivity.launchTaskListActivity();
            }

            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
        circularReveal.start();
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
