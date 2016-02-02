package com.david.todo.view;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

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

    @Bind(R.id.app_bar_layout)
    AppBarLayout _collapsingContainer;

    @Bind(R.id.title_input_layout)
    TextInputLayout _titleInputLayout;

    @Bind(R.id.collapsing_container)
    CollapsingToolbarLayout _toolbarLayout;

    @Bind(R.id.description_input_layout)
    TextInputLayout _descriptionInputLayout;

    @Bind(R.id.toolbar)
    Toolbar _toolbar;

    @Bind(R.id.title_short_container)
    LinearLayout _titleShortContainer;

    @Bind(R.id.header_input_container)
    LinearLayout _headerInputContainer;
    
    private SupportAnimator _circularReveal;
    private int _verticalOffsetMaxHeight = -1;

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

        _collapsingContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                _verticalOffsetMaxHeight = _collapsingContainer.getHeight() - _toolbar.getHeight();
            }
        });

        _collapsingContainer.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                Log.d("AddItemActivity", "offset: " + verticalOffset + " | collapsing container height: " + _collapsingContainer.getHeight());
                if(_verticalOffsetMaxHeight == -1) return;
                if(verticalOffset == 0) return;
                float delta = ((float)Math.abs(verticalOffset) / (float)_verticalOffsetMaxHeight) * 100f;

                if ((_collapsingContainer.getHeight() + verticalOffset) <= _toolbar.getHeight()) {
                    _titleInputLayout.setVisibility(View.INVISIBLE);
                    _descriptionInputLayout.setVisibility(View.INVISIBLE);
                } else {
                    float percentScrolledUp = (delta / 100);
                    float alpha = 1.0f - percentScrolledUp;
                    _headerInputContainer.setAlpha(alpha);
                    Log.d("AddItemActivity", "Alpha: " + Math.abs(alpha));
                    _titleInputLayout.setVisibility(View.VISIBLE);
                    _descriptionInputLayout.setVisibility(View.VISIBLE);
                    if(percentScrolledUp > 0.7f) {
                        float a = 1.0f - percentScrolledUp;
                        float b = a * 3f;

                        _titleShortContainer.setVisibility(View.VISIBLE);
//                        _titleShortContainer.setAlpha(percentScrolledUp);
                        _titleShortContainer.setAlpha(Math.abs(1.0f - b));
                    } else {
                        _titleShortContainer.setVisibility(View.INVISIBLE);
                    }
                }
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
