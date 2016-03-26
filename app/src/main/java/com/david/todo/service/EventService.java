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
     * @param dateTime
     * @return
     */
    public String retreiveDateDisplayText(DateTime dateTime, Resources resources) {
        if (dateTime == null) return null;

        //if is less than
        LocalDate localDate = new LocalDate();

        if (dateTime.toLocalDate().isEqual(localDate)) {
            return resources.getString(R.string.today_text);
        } else if (dateTime.toLocalDate().isBefore(localDate)) {
            return formatDate(dateTime);
        } else if(dateTime.toLocalDate().isEqual(localDate.plusDays(1))) {
            return resources.getString(R.string.tomorrow_text);
        } else {
            //Check if > today && < 7 days
            DateTime dateRangeCheck = new DateTime();
            if(isWithinSevenDaysFromNow(dateRangeCheck)) {
                return dateTime.dayOfWeek().getAsText();
            } else {
                //return as format
                return formatDate(dateTime);
            }
        }
    }

    public String formatDate(DateTime dateTime) {
        return dateTime.toString(DateTimeFormat.forPattern(BuildConfig.DATE_FORMAT));
    }

    private boolean isWithinSevenDaysFromNow(DateTime dateTime) {
        return dateTime.isAfter(dateTime.plusDays(1)) && dateTime.isBefore(dateTime.plusDays(7));
    }
}
