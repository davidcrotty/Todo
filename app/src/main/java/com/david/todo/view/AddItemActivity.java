package com.david.todo.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.david.todo.R;
import com.david.todo.model.AnimateLocationCoordinatesModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class AddItemActivity extends BaseActivity implements View.OnClickListener {

    public static String ANIMATE_START_INTENT_KEY = "ANIMATE_START_INTENT_KEY";

    @Bind(R.id.add_item_root)
    CoordinatorLayout _rootView;

    @Bind(R.id.app_bar_layout)
    AppBarLayout _appBarLayout;

    @Bind(R.id.title_input_layout)
    TextInputLayout _expandedFormLayout;

    @Bind(R.id.collapsing_container)
    CollapsingToolbarLayout _toolbarLayout;

    @Bind(R.id.description_input_layout)
    TextInputLayout _descriptionInputLayout;

    @Bind(R.id.back_arrow_image)
    ImageView _backArrowImage;

    @Bind(R.id.toolbar)
    Toolbar _toolbar;

    @Bind(R.id.title_short_container)
    LinearLayout _collapsedToolbarTitleLayout;

    @Bind(R.id.header_input_container)
    LinearLayout _headerInputContainer;
    
    private SupportAnimator _circularReveal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_full_view);
        ButterKnife.bind(this);
        _rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                circularRevealLayout();
            }
        });

        new CollapsingToolbarViewStrategy(this,
                                          _appBarLayout,
                                          _toolbar,
                                          _collapsedToolbarTitleLayout,
                                          _headerInputContainer);
        _backArrowImage.setOnClickListener(this);
        //on scroll up, dismiss keyboard.
        //editing input text changes description text
        //update add item icon
        //take intent passed through and fill in title.
    }

    private void circularRevealLayout() {
        if(getIntent() == null) return;
        if(getIntent().hasExtra(ANIMATE_START_INTENT_KEY) == false) {
            _rootView.setVisibility(View.VISIBLE);
            return;
        }

        AnimateLocationCoordinatesModel animateModel = (AnimateLocationCoordinatesModel) getIntent()
                                                                                         .getExtras()
                                                                                         .getSerializable(ANIMATE_START_INTENT_KEY);

        int cx = animateModel.getX() + animateModel.getWidth()  / 2;
        int cy = animateModel.getY() + animateModel.getHeight() / 2;

        float finalRadius = Math.max(_rootView.getWidth(), _rootView.getHeight());

        _circularReveal = ViewAnimationUtils.createCircularReveal(_rootView, cx, cy, 0, finalRadius);
        _circularReveal.setDuration(500);

        _rootView.setVisibility(View.VISIBLE);
        _circularReveal.start();
        _circularReveal.reverse();
        getIntent().removeExtra(ANIMATE_START_INTENT_KEY);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(imm.isAcceptingText() == false) return;
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(_circularReveal != null) {
            _circularReveal.reverse();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_arrow_image:
                finish();
                break;
        }
    }
}
