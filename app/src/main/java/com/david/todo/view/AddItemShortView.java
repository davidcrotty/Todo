package com.david.todo.view;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.david.todo.R;
import com.david.todo.model.AnimateLocationCoordinatesModel;
import com.david.todo.presenter.TodoListPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by David Crotty on 18/11/2015.
 */
public class AddItemShortView extends LinearLayout implements Animation.AnimationListener,
                                                              View.OnClickListener,
        TextWatcher {

    @Bind(R.id.options_panel)
    LinearLayout _optionsPanel;

    @Bind(R.id.options_divider)
    View _optionsDivider;

    @Bind(R.id.inline_options_view)
    View _inlineOptionsView;

    @Bind(R.id.expand_edit_button)
    ImageView _expandEditButton;

    @Bind(R.id.short_note_text)
    EditText _shortNoteText;

    private Animation _fadeAnimation;
    private final int _animationDuration = 300;
    private TodoListPresenter _presenter;

    public AddItemShortView(Context context, TodoListPresenter presenter) {
        super(context);
        _presenter = presenter;
        init();
    }

    public void fadeOptions() {
        if(_fadeAnimation.hasStarted() || _optionsPanel.getVisibility() == View.INVISIBLE) return;
        _optionsPanel.startAnimation(_fadeAnimation);
        _optionsDivider.startAnimation(_fadeAnimation);
    }

    public void showOptions() {
        hideInlineOptions();
        _fadeAnimation.setAnimationListener(null);
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
        setId(R.id.add_item_short_view);
        inflate(getContext(), R.layout.add_item_short_view, this);
        ButterKnife.bind(this);
        _shortNoteText.addTextChangedListener(this);
        setupAnimation();
        _expandEditButton.setOnClickListener(this);
        if(_shortNoteText.getText().toString() == null || _shortNoteText.getText().toString().isEmpty()) {
            _presenter.textChanged(null, this);
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expand_edit_button:
                ((TodoListActivity) getContext()).hideKeyboard();

                Intent intent = new Intent(getContext(), AddItemActivity.class);
                int[] viewLocation = new int[2];
                _expandEditButton.getLocationOnScreen(viewLocation);
                intent.putExtra(AddItemActivity.ANIMATE_START_INTENT_KEY, new AnimateLocationCoordinatesModel(viewLocation[0],
                                                                                                              viewLocation[1],
                                                                                                              _expandEditButton.getWidth(),
                                                                                                              _expandEditButton.getHeight()));
                getContext().startActivity(intent);

                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        _presenter.textChanged(s, this); //TODO is it possible to grab the lazy version?
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
