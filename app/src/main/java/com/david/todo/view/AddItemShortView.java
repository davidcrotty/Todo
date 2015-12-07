package com.david.todo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.david.todo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by David Crotty on 18/11/2015.
 */
public class AddItemShortView extends LinearLayout implements Animation.AnimationListener {

    @Bind(R.id.options_panel)
    LinearLayout _optionsPanel;

    @Bind(R.id.options_divider)
    View _optionsDivider;

    @Bind(R.id.inline_options_view)
    View _inlineOptionsView;

    private Animation _fadeAnimation;
    private final int _animationDuration = 300;

    public AddItemShortView(Context context) {
        super(context);
        init();
    }

    public AddItemShortView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void fadeOptions() {
        if(_fadeAnimation.hasStarted() || _optionsPanel.getVisibility() == View.INVISIBLE) return;
        _optionsPanel.startAnimation(_fadeAnimation);
        _optionsDivider.startAnimation(_fadeAnimation);
    }

    public void showOptions() {
        hideInlineOptions();
        _fadeAnimation.cancel();
        setupAnimation();
        _optionsPanel.setVisibility(View.VISIBLE);
        _optionsDivider.setVisibility(View.VISIBLE);
    }

    private void setupAnimation() {
        _fadeAnimation = new AlphaAnimation(1, 0);
        _fadeAnimation.setInterpolator(new AccelerateInterpolator());
        _fadeAnimation.setDuration(_animationDuration);
        _fadeAnimation.setAnimationListener(this);
    }

    private void init() {
        inflate(getContext(), R.layout.add_item_short_view, this);
        ButterKnife.bind(this);
        setupAnimation();
    }

    private void hideInlineOptions() {
        _inlineOptionsView.setVisibility(View.INVISIBLE);
    }

    private void showInlineOptions() {
        _inlineOptionsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        _optionsPanel.setVisibility(View.INVISIBLE);
        _optionsDivider.setVisibility(View.INVISIBLE);
        showInlineOptions();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
