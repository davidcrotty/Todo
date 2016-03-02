package com.david.todo.view;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.david.todo.R;
import com.david.todo.model.AnimateLocationCoordinatesModel;
import com.david.todo.presenter.AddItemPresenter;
import com.david.todo.view.eventlisteners.EditTextChangeListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class AddItemActivity extends BaseActivity implements View.OnClickListener,
                                                              NestedScrollView.OnScrollChangeListener {

    public static String ANIMATE_START_INTENT_KEY = "ANIMATE_START_INTENT_KEY";
    public static String TITLE_TEXT_INTENT_KEY = "TITLE_TEXT_INTENT_KEY";

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
    @Bind(R.id.collapsed_title_text)
    TextView _collapsedTitleText;
    @Bind(R.id.collapsed_description_text)
    TextView _collapsedDescriptionText;
    @Bind(R.id.expanded_title_text)
    EditText _expandedTitleText;
    @Bind(R.id.expanded_description_text)
    EditText _expandedDescriptionText;
    @Bind(R.id.action_content_container)
    RelativeLayout _actionContentContainer;
    @Bind(R.id.scroll_view)
    NestedScrollView _scrollView;
    @Bind(R.id.focused_action_fab)
    FloatingActionButton _actionFab;
    @Bind(R.id.action_container)
    FrameLayout _actionContainer;

    private EventView _eventView;
    private SupportAnimator _circularReveal;
    private AddItemPresenter _addItemPresenter;
    private int _checkListScrollThreshold = 0;
    private int _commentsScrollThreshold = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_full_view);
        ButterKnife.bind(this);
        _addItemPresenter = new AddItemPresenter(this);
        _rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                circularRevealLayout();
            }
        });

        addCollapsingToolbarBehaviour();
        _addItemPresenter.updateTitleWithIntent();
        addItemActions();
        loadFabScrollThresholds();
        _scrollView.setOnScrollChangeListener(this);
        _actionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float finalRadius = Math.max(_rootView.getWidth(), _rootView.getHeight());
                int[] fabLocation = new int[2];
                _actionFab.getLocationOnScreen(fabLocation);
                addEventView(new AnimateLocationCoordinatesModel(fabLocation[0],
                        fabLocation[1],
                        _actionFab.getWidth(),
                        _actionFab.getHeight(), finalRadius));
            }
        });
    }

    /**
     * @param coordinatesModel - nullable as this can be called on resume, animation would be unnecessary
     */
    private void addEventView(@Nullable AnimateLocationCoordinatesModel coordinatesModel) {
        _eventView = null;
        if(coordinatesModel == null) {
            _eventView = new EventView(this, _addItemPresenter);
        } else {
            _eventView = new EventView(AddItemActivity.this, coordinatesModel, _addItemPresenter);
        }

        _actionContainer.bringToFront();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        _actionContainer.addView(_eventView, params);
        getIntent().putExtra(EventView.PRESERVE_VIEW, true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.orange_ripple));
    }

    public void removeEventView() {
        _actionContainer.removeView(_eventView);
        getIntent().removeExtra(EventView.PRESERVE_VIEW);
        getWindow().setStatusBarColor(getResources().getColor(R.color.add_activity_status_bar));
        _eventView = null;
    }

    private void loadFabScrollThresholds() {
        Resources resources = getResources();
        _checkListScrollThreshold = resources.getDimensionPixelSize(R.dimen.link_line)
                + resources.getDimensionPixelSize(R.dimen.small_fab)
                + (resources.getDimensionPixelOffset(R.dimen.link_margin) * 2);

        _commentsScrollThreshold = _checkListScrollThreshold * 2;
    }

    private void addCollapsingToolbarBehaviour() {
        new CollapsingToolbarViewStrategy(this,
                _appBarLayout,
                _toolbar,
                _collapsedToolbarTitleLayout,
                _headerInputContainer);
        _backArrowImage.setOnClickListener(this);
        _expandedTitleText.addTextChangedListener(new EditTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                _collapsedTitleText.setText(s.toString());
            }
        });
        _expandedDescriptionText.addTextChangedListener(new EditTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                _collapsedDescriptionText.setText(s.toString());
            }
        });
    }

    private void addItemActions() {
        AddItemActionsView addItemActionsView = new AddItemActionsView(this, _addItemPresenter);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.item_main_action_list),
                                                                      RelativeLayout.LayoutParams.MATCH_PARENT);
        _actionContentContainer.addView(addItemActionsView, 0, layoutParams);
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

        int cx = animateModel._x + animateModel._width  / 2;
        int cy = animateModel._y + animateModel._height / 2;

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

    public void setExpandedTitleText(String text) {
        _expandedTitleText.setText(text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().hasExtra(EventView.PRESERVE_VIEW)) {
            addEventView(null);
        }
    }

    @Override
    public void onBackPressed() {
        if(_circularReveal != null) {
            _circularReveal.reverse();
        }
        if(_eventView != null) {
            removeEventView();
        } else {
            super.onBackPressed();
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

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int commentsScrollThreshold = _checkListScrollThreshold * 2;
        if(_checkListScrollThreshold == 0) return;
        Resources resources = getResources();

        if(scrollY > _checkListScrollThreshold && scrollY < commentsScrollThreshold) {
            _actionFab.setImageDrawable(resources.getDrawable(R.drawable.check_box_white));
            _actionFab.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.green)));
            _actionFab.setRippleColor(resources.getColor(R.color.green_ripple));
        } else if(scrollY > _checkListScrollThreshold) {
            _actionFab.setImageDrawable(resources.getDrawable(R.drawable.mode_comment));
            _actionFab.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.light_blue)));
            _actionFab.setRippleColor(resources.getColor(R.color.light_blue_ripple));
        } else {
            _actionFab.setImageDrawable(resources.getDrawable(R.drawable.event_white));
            _actionFab.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.orange)));
            _actionFab.setRippleColor(resources.getColor(R.color.orange_ripple));
        }
    }
}
