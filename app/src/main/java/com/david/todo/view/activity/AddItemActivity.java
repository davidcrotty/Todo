package com.david.todo.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.david.todo.BuildConfig;
import com.david.todo.R;
import com.david.todo.model.AnimateLocationCoordinatesModel;
import com.david.todo.model.EventModel;
import com.david.todo.presenter.AddItemPresenter;
import com.david.todo.view.AddItemActionsView;
import com.david.todo.view.BaseActivity;
import com.david.todo.view.CollapsingToolbarViewStrategy;
import com.david.todo.view.EventView;
import com.david.todo.view.eventlisteners.AddEventViewClickListener;
import com.david.todo.view.eventlisteners.AddTaskListClickListener;
import com.david.todo.view.eventlisteners.EditTextChangeListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;

public class AddItemActivity extends BaseActivity implements View.OnClickListener,
                                                             NestedScrollView.OnScrollChangeListener,
                                                             DatePickerDialog.OnDateSetListener,
                                                             TimePickerDialog.OnTimeSetListener {

    public static String ANIMATE_START_INTENT_KEY = "ANIMATE_START_INTENT_KEY";
    public static String DATE_KEY = "DATE_KEY";
    public static String TITLE_TEXT_INTENT_KEY = "TITLE_TEXT_INTENT_KEY";
    public static String EVENT_INTENT_KEY = "EVENT_INTENT_KEY";
    public static final int ACTIVITY_LAUNCHED = 0;

    @Bind(R.id.reveal_layout)
    public RevealFrameLayout _revealLayout;
    @Bind(R.id.reveal_container)
    public FrameLayout _revealContainer;
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
    @Bind(R.id.date_text)
    TextView _dateText;
    @Bind(R.id.time_text)
    TextView _timeText;
    @Bind(R.id.time_select_container)
    LinearLayout _timeSelectContainer;
    @Bind(R.id.event_detail_short)
    RelativeLayout _eventDetailShort;

    private EventView _eventView;
    private SupportAnimator _circularReveal;
    private AddItemPresenter _addItemPresenter;
    private AnimateLocationCoordinatesModel _coordinatesModel;
    private int _checkListScrollThreshold = 0;
    private int _commentsScrollThreshold = 0;
    public AddItemActionsView _addItemActionsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_full_view);
        ButterKnife.bind(this);
        _addItemPresenter = new AddItemPresenter(this);
        loadFabScrollThresholds();
        addCollapsingToolbarBehaviour();
        _addItemPresenter.updateTitleWithIntent();
        addItemActions();
        _scrollView.setOnScrollChangeListener(this);
        _eventDetailShort.setOnClickListener(this);
        showTimePickButton();
        _rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                circularRevealLayout();
                delegateActionButtonEvent(_scrollView.getScrollY());
                calculateAnimationCoordinates();
                if (getIntent().hasExtra(EventView.PRESERVE_VIEW)) {
                    addEventView();
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        //Bug in library where date starts from 0, add 1 to compensate
        _addItemPresenter.updateEventMemoryModelWithDate(new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0).toDate(), null);
        _addItemPresenter.retreiveThenUpdateDateText();
        _eventView.reverseAnimation();
        _addItemPresenter.clearTime();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        _addItemPresenter.updateEventMemoryModelWithTime(hourOfDay, minute);
        _addItemPresenter.retreiveThenUpdateTimeText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _addItemPresenter.retreiveThenUpdateDateText();
        if(getIntent().hasExtra(DATE_KEY)) {
            _addItemPresenter.retreiveThenUpdateTimeText();
        }
    }

    @Override
    public void onBackPressed() {
        if(_circularReveal != null) {
            _circularReveal.reverse();
        }
        if(_eventView != null) {
            _eventView.reverseAnimation();
        } else {
            getIntent().removeExtra(EVENT_INTENT_KEY);
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_arrow_image:
                finish();
                break;
            case R.id.time_select_container:
                createTimePicker();
                break;
            case R.id.event_detail_short:
                addEventView();
                break;
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if(_checkListScrollThreshold == 0) return;
        Resources resources = getResources();

        if(scrollY > _checkListScrollThreshold && scrollY < _commentsScrollThreshold) {
            _actionFab.setImageDrawable(resources.getDrawable(R.drawable.check_box_white));
            _actionFab.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.green)));
            _actionFab.setRippleColor(resources.getColor(R.color.green_ripple));
            delegateActionButtonEvent(scrollY);
        } else if(scrollY > _checkListScrollThreshold) {
            _actionFab.setImageDrawable(resources.getDrawable(R.drawable.mode_comment));
            _actionFab.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.light_blue)));
            _actionFab.setRippleColor(resources.getColor(R.color.light_blue_ripple));
        } else {
            _actionFab.setImageDrawable(resources.getDrawable(R.drawable.event_white));
            _actionFab.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.orange)));
            _actionFab.setRippleColor(resources.getColor(R.color.orange_ripple));
            delegateActionButtonEvent(scrollY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTIVITY_LAUNCHED) {
            _revealContainer.setVisibility(View.INVISIBLE);
            if(_addItemActionsView != null) {
                _addItemActionsView.resetViews();
            }
        }
    }

    private void delegateActionButtonEvent(int scrollY) {
        if(scrollY > _checkListScrollThreshold && scrollY < _commentsScrollThreshold) {
            _actionFab.setOnClickListener(new AddTaskListClickListener(_addItemPresenter));
        } else {
            _actionFab.setOnClickListener(new AddEventViewClickListener(_addItemPresenter));
        }
    }

    private void showTimePickButton() {
        _timeText.setVisibility(View.VISIBLE);
        _timeSelectContainer.setVisibility(View.VISIBLE);
        _timeSelectContainer.setOnClickListener(this);
    }

    private void calculateAnimationCoordinates() {
        float finalRadius = Math.max(_rootView.getWidth(), _rootView.getHeight());
                int[] fabLocation = new int[2];
                _actionFab.getLocationOnScreen(fabLocation);
                _coordinatesModel = new AnimateLocationCoordinatesModel(fabLocation[0],
                                                                        fabLocation[1],
                                                                        _actionFab.getWidth(),
                                                                        _actionFab.getHeight(),
                                                                        finalRadius);
    }

    public void launchTaskListActivity() {
        Intent intent = new Intent(this, TaskListActivity.class);
        startActivityForResult(intent, ACTIVITY_LAUNCHED);
    }

    public void updateTimeWith(String textToDisplay, int colour) {
        _timeText.setText(textToDisplay);
        _timeText.setTextColor(colour);
    }

    public void removeAllActionViews() {
        _actionContainer.removeAllViews();
        if(getIntent().hasExtra(EventView.PRESERVE_VIEW)) {
            getIntent().removeExtra(EventView.PRESERVE_VIEW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.add_activity_status_bar));
        }
        _eventView = null;
    }

    public void setEventIntentKey(EventModel dateTimeModel) {
        getIntent().putExtra(EVENT_INTENT_KEY, dateTimeModel);
    }

    public EventModel getDateModelIntent() {
        return (EventModel) getIntent().getSerializableExtra(EVENT_INTENT_KEY);
    }
    public void clearTime() {
        getIntent().removeExtra(DATE_KEY);
        _timeText.setText("");
    }

    public void updateDateWith(String dateText, int colour) {
        _dateText.setText(dateText);
        _dateText.setTextColor(colour);
    }

    public void addEventView() {
        //on pause needs to store prior coords, but will be from prior rotation, so before going back needs to grab both land/port coords
        if(_eventView != null) {
            if(_eventView.getVisibility() == View.VISIBLE) return;
        }
        _eventView = new EventView(AddItemActivity.this, _coordinatesModel, _addItemPresenter, getIntent().hasExtra(EventView.PRESERVE_VIEW));

        _actionContainer.bringToFront();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        _actionContainer.addView(_eventView, params);
        getIntent().putExtra(EventView.PRESERVE_VIEW, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.orange_ripple));
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(imm.isAcceptingText() == false) return;
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void setExpandedTitleText(String text) {
        _expandedTitleText.setText(text);
    }

    public void createDatePicker() {
        DateTime datePickerTime = new DateTime();
        DatePickerDialog datePickerDialog =
                DatePickerDialog.newInstance(this,
                        datePickerTime.getYear(),
                        datePickerTime.getMonthOfYear() - 1, //bug in library where dates do not start from 0
                        datePickerTime.getDayOfMonth());
        datePickerDialog.setAccentColor(getResources().getColor(R.color.orange_ripple));
        datePickerDialog.setMinDate(datePickerTime.toCalendar(null));
        datePickerDialog.show(getFragmentManager(), DatePickerDialog.class.getName());
    }

    public Point getScreenSize() {
        Point screenSize = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(screenSize);
        return screenSize;
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
        _addItemActionsView = new AddItemActionsView(this, _addItemPresenter);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.item_main_action_list),
                                                                      RelativeLayout.LayoutParams.MATCH_PARENT);
        _actionContentContainer.addView(_addItemActionsView, 0, layoutParams);
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
        _circularReveal.setDuration(BuildConfig.FULL_SCREEN_TRANSITION);

        _rootView.setVisibility(View.VISIBLE);
        _circularReveal.start();
        _circularReveal.reverse();
        getIntent().removeExtra(ANIMATE_START_INTENT_KEY);
    }

    private void createTimePicker() {
        DateTime datePickerTime = new DateTime();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this,
                                                                        datePickerTime.getHourOfDay(),
                                                                        datePickerTime.getMinuteOfDay(),
                                                                        datePickerTime.getSecondOfDay(),
                                                                        true);
        timePickerDialog.setAccentColor(getResources().getColor(R.color.orange_ripple));
        timePickerDialog.show(getFragmentManager(), TimePickerDialog.class.getName());
    }
}
