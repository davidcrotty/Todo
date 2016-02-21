package com.david.todo.view;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.david.todo.R;
import com.david.todo.model.AnimateLocationCoordinatesModel;
import com.david.todo.presenter.AddItemPresenter;
import com.david.todo.view.eventlisteners.EditTextChangeListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import timber.log.Timber;

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

    public void setExpandedTitleText(String text) {
        _expandedTitleText.setText(text);
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
