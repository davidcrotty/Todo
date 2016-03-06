package com.david.todo.service;

import android.content.res.Resources;

import com.david.todo.BuildConfig;
import com.david.todo.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

public class EventService {

    /**
     * Decides which date text to display given a date
     * @param date
     * @return
     */
    public String retreiveDateDisplayText(Date date, Resources resources) {
        if(date == null) return null;

        DateTime dateTime = new DateTime(date);

        if(dateTime.toLocalDate().isEqual(new LocalDate())) {
            return resources.getString(R.string.today_text);
        } else if(dateTime.toLocalDate().isEqual(new LocalDate().plusDays(1))) {
            return resources.getString(R.string.tomorrow_text);
        } else {
            //Check if > today && < 7 days
            DateTime dateRangeCheck = new DateTime();
            if(isWithinSevenDaysFromNow(dateRangeCheck)) {
                return dateTime.dayOfWeek().getAsText();
            } else {
                //return as format
                return dateTime.toString(DateTimeFormat.forPattern(BuildConfig.DATE_FORMAT));
            }
        }
    }

    private boolean isWithinSevenDaysFromNow(DateTime dateTime) {
        return dateTime.isAfter(dateTime.plusDays(1)) && dateTime.isBefore(dateTime.plusDays(7));
    }
}
