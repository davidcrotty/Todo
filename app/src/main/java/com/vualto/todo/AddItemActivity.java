package com.vualto.todo;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by David on 20/09/2015.
 */
public class AddItemActivity extends BaseActivity implements View.OnLayoutChangeListener,
                                                             Animator.AnimatorListener {

    @Bind(R.id.notes_edit_text)
    EditText _notesText;

    @Bind(R.id.save_item_button)
    FloatingActionButton _saveItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
        _saveItemButton.addOnLayoutChangeListener(this);
    }

    @OnClick(R.id.save_item_button)
    void onClick() {
        int startRadius = Math.max(_saveItemButton.getWidth(), _saveItemButton.getHeight());
        int cx = _saveItemButton.getWidth() / 2;
        int cy = _saveItemButton.getHeight() / 2;

        Animator anim = ViewAnimationUtils.createCircularReveal(_saveItemButton, cx, cy, startRadius, 0);
        anim.addListener(this);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(700);
        anim.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        int finalRadius = Math.max(_saveItemButton.getWidth(), _saveItemButton.getHeight());
        int cx = _saveItemButton.getWidth() / 2;
        int cy = _saveItemButton.getHeight() / 2;

        Animator anim = ViewAnimationUtils.createCircularReveal(_saveItemButton, cx, cy, 0, finalRadius);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(700);
        anim.start();
    }
}
