package com.david.todo.view;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.david.todo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DavidHome on 10/01/2016.
 */
public class AddItemActivity extends Activity {

    @Bind(R.id.add_item_root)
    LinearLayout _rootView;

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

    public void circularRevealLayout() {
        int cx = _rootView.getWidth() / 2;
        int cy = _rootView.getHeight() / 2;

        float finalRadius = Math.max(_rootView.getWidth(), _rootView.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(_rootView, cx, cy, 0, finalRadius);
        circularReveal.setDuration(1000);

        // make the view visible and start the animation
        _rootView.setVisibility(View.VISIBLE);
        circularReveal.start();
    }
}
