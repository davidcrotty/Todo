package com.david.todo.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.david.todo.R;
import com.david.todo.model.AnimateLocationCoordinatesModel;
import com.david.todo.model.EventModel;
import com.david.todo.presenter.AddItemPresenter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class EventView extends RelativeLayout implements View.OnClickListener {

    public static final String PRESERVE_VIEW = "PRESERVE_VIEW";

    @Bind(R.id.root_view)
    LinearLayout _rootView;

    @Bind(R.id.today_image)
    ImageView _todayImage;
    @Bind(R.id.tomorrow_image)
    ImageView _tomorrowImage;
    @Bind(R.id.week_image)
    ImageView _weekImage;
    @Bind(R.id.custom_day_image)
    ImageView _customDayImage;

    @Bind(R.id.today_card)
    CardView _todayCard;
    @Bind(R.id.tomorrow_card)
    CardView _tomorrowCard;
    @Bind(R.id.next_week_card)
    CardView _nextWeekCard;
    @Bind(R.id.date_pick_card)
    CardView _datePickCard;

    private final AddItemPresenter _presenter;
    private final int _today = 1;
    private final int _nextWeek = 7;
    private final String _dateFormat = "MMM - dd";
    private final int _circularRevealDurationMs = 500;
    private SupportAnimator _circularReveal;
    private AnimateLocationCoordinatesModel _animateModel;
    private EventModel _eventModel;

    private boolean _firedAnimation = false;

    public EventView(Context context,
                     AnimateLocationCoordinatesModel animateModel,
                     AddItemPresenter presenter) {
        super(context);
        _presenter = presenter;
        inflate(context, R.layout.event_view, this);
        ButterKnife.bind(this);
        init();
        _animateModel = animateModel;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (_firedAnimation == false) {
                    circularRevealLayout(_animateModel);
                    _firedAnimation = true;
                }
            }
        });
    }

    private void init() {
        Resources resources = getResources();

        //directly grabbing resource will affect ALL usages of this drawable
        Drawable drawable = resources.getDrawable(R.drawable.event_white).getConstantState().newDrawable().mutate();
        drawable.setColorFilter(resources.getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
        _todayImage.setImageDrawable(drawable);

        Drawable drawable2 = resources.getDrawable(R.drawable.sun_icon);
        drawable2.setColorFilter(resources.getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
        _tomorrowImage.setImageDrawable(drawable2);

        Drawable drawable3 = resources.getDrawable(R.drawable.date_range_white);
        drawable3.setColorFilter(resources.getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
        _weekImage.setImageDrawable(drawable3);

        Drawable drawable4 = resources.getDrawable(R.drawable.custom_date);
        drawable4.setColorFilter(resources.getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
        _customDayImage.setImageDrawable(drawable4);
        _rootView.setOnClickListener(this);
        _todayCard.setOnClickListener(this);
        _tomorrowCard.setOnClickListener(this);
        _nextWeekCard.setOnClickListener(this);
        _datePickCard.setOnClickListener(this);

        //This is logic and should be set by the presenter.
        _eventModel = _presenter.getDateModelIntent();
    }

    private void circularRevealLayout(AnimateLocationCoordinatesModel animateModel) {
        int cx = animateModel._x + animateModel._width  / 2;
        int cy = animateModel._y + animateModel._height / 2;

        _circularReveal = ViewAnimationUtils.createCircularReveal(_rootView, cx, cy, 0, animateModel._finalRadius);
        _circularReveal.setDuration(_circularRevealDurationMs);

        _rootView.setVisibility(View.VISIBLE);
        _circularReveal.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.root_view:
                SupportAnimator animator = _circularReveal.reverse();
                animator.addListener(new SupportAnimator.SimpleAnimatorListener() {
                    @Override
                    public void onAnimationEnd() {
                        _presenter.removeEventView();
                    }
                });
                animator.start();
                break;
            case R.id.today_card:
                String todayText = getResources().getString(R.string.today_text);
                _presenter.updateEvent(new DateTime().toDate(), todayText);
                break;
            case R.id.tomorrow_card:
                DateTime tomorrowsDateTime = new DateTime().plusDays(_today);
                String dayOfWeekText = tomorrowsDateTime.dayOfWeek().getAsText();
                _presenter.updateEvent(tomorrowsDateTime.toDate(), dayOfWeekText);
                break;
            case R.id.next_week_card:
                DateTime dateTime = new DateTime().plusDays(_nextWeek);
                String dateFormatText = dateTime.toString(DateTimeFormat.forPattern(_dateFormat));
                _presenter.updateEvent(dateTime.toDate(), dateFormatText);
                break;
            case R.id.date_pick_card:
                _presenter.delegateDatePickerCreation();
                break;
        }
    }
}
