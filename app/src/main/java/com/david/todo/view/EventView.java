package com.david.todo.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.david.todo.R;
import com.david.todo.model.AnimateLocationCoordinatesModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class EventView extends RelativeLayout {

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
    @Bind(R.id.custom_time_image)
    ImageView _customTimeImage;

    private SupportAnimator _circularReveal;
    private AnimateLocationCoordinatesModel _animateModel;
    float _finalRadius;

    private boolean _firedAnimation = false;

    public EventView(Context context,
                     AnimateLocationCoordinatesModel animateModel,
                     float finalRadius) {
        super(context);
        inflate(context, R.layout.event_view, this);
        ButterKnife.bind(this);
        init();
        _finalRadius = finalRadius;
        _animateModel = animateModel;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(_firedAnimation == false) {
                    circularRevealLayout(_animateModel, _finalRadius);
                    _firedAnimation = true;
                }
            }
        });
    }

    private void init() {
        Resources resources = getResources();

        Drawable drawable = resources.getDrawable(R.drawable.event_white);
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

        Drawable drawable5 = resources.getDrawable(R.drawable.custom_time);
        drawable5.setColorFilter(resources.getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
        _customTimeImage.setImageDrawable(drawable5);
    }

    private void circularRevealLayout(AnimateLocationCoordinatesModel animateModel,
                                      float finalRadius) {
        int cx = animateModel.getX() + animateModel.getWidth()  / 2;
        int cy = animateModel.getY() + animateModel.getHeight() / 2;

        _circularReveal = ViewAnimationUtils.createCircularReveal(_rootView, cx, cy, 0, finalRadius);
        _circularReveal.setDuration(500);

        _rootView.setVisibility(View.VISIBLE);
        _circularReveal.start();
        _circularReveal.reverse();
    }
}
