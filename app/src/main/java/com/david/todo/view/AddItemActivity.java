package com.david.todo.view;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;

import com.david.todo.R;
import com.david.todo.model.AnimateLocationCoordinatesModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class AddItemActivity extends BaseActivity {

    public static String ANIMATE_START_INTENT_KEY = "ANIMATE_START_INTENT_KEY";

    @Bind(R.id.add_item_root)
    CoordinatorLayout _rootView;

    @Bind(R.id.toolbar)
    Toolbar _toolbar;
    
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(_circularReveal != null) {
            _circularReveal.reverse();
        }
    }
}
