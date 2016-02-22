package com.david.todo.view;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.david.todo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DateActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);
        ButterKnife.bind(this);
        Resources resources = getResources();
        getWindow().setStatusBarColor(resources.getColor(R.color.orange_ripple));

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
}
