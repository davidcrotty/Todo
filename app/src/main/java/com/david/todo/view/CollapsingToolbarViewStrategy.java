package com.david.todo.view;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class CollapsingToolbarViewStrategy implements AppBarLayout.OnOffsetChangedListener,
                                                      ViewTreeObserver.OnGlobalLayoutListener {

    private final float toolbarFadeInThreshold = 0.7f;
    private final float toolbarFadeInSpeed = 3f;
    private int _verticalOffsetMaxHeight = -1;

    private final AppBarLayout _appbarLayout;
    private final Toolbar _toolbar;
    private final ViewGroup _expandedFormLayout;
    private final ViewGroup _collapsedToolbarTitleLayout;
    private final AddItemActivity _addItemActivity;

    public CollapsingToolbarViewStrategy(AddItemActivity addItemActivity,
                                         AppBarLayout appBarLayout,
                                         Toolbar toolbar,
                                         ViewGroup collapsedToolbarTitleLayout,
                                         ViewGroup expandedFormLayout) {
        _addItemActivity = addItemActivity;
        _appbarLayout = appBarLayout;
        _toolbar = toolbar;
        _collapsedToolbarTitleLayout = collapsedToolbarTitleLayout;
        _expandedFormLayout = expandedFormLayout;
        init();
    }

    private void init() {
        _appbarLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        _appbarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if(_verticalOffsetMaxHeight == -1) return;
        if(verticalOffset == 0) return;
        float delta = ((float)Math.abs(verticalOffset) / (float)_verticalOffsetMaxHeight) * 100f;

        if ((appBarLayout.getHeight() + verticalOffset) <= _toolbar.getHeight()) {
            _expandedFormLayout.setVisibility(View.INVISIBLE);
            _collapsedToolbarTitleLayout.setAlpha(1.0f);
            _collapsedToolbarTitleLayout.setVisibility(View.VISIBLE);
            _addItemActivity.hideKeyboard();
        } else {
            float percentScrolledUp = (delta / 100);
            float alpha = 1.0f - percentScrolledUp;
            _expandedFormLayout.setAlpha(alpha);

            _expandedFormLayout.setVisibility(View.VISIBLE);
            if(percentScrolledUp > toolbarFadeInThreshold) {
                float parentScrolledDown = 1.0f - percentScrolledUp;
                float scalarAlpha = parentScrolledDown * toolbarFadeInSpeed;

                _collapsedToolbarTitleLayout.setVisibility(View.VISIBLE);
                _collapsedToolbarTitleLayout.setAlpha(Math.abs(1.0f - scalarAlpha));
            } else {
                _collapsedToolbarTitleLayout.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onGlobalLayout() {
        _verticalOffsetMaxHeight = _appbarLayout.getHeight() - _toolbar.getHeight();
    }
}
