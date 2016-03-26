package com.david.todo.service;

import android.content.res.Resources;

import com.david.todo.BuildConfig;
import com.david.todo.R;
import com.david.todo.model.DateHolderModel;

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
    public DateHolderModel retreiveDateDisplayText(DateTime dateTime, Resources resources) {
        if (dateTime == null) return null;

        //if is less than
        LocalDate localDate = new LocalDate();

        if (dateTime.toLocalDate().isEqual(localDate)) {
            return new DateHolderModel(resources.getString(R.string.today_text), resources.getColor(android.R.color.black));
        } else if (dateTime.toLocalDate().isBefore(localDate)) {
            return new DateHolderModel(formatDate(dateTime), resources.getColor(R.color.red));
        } else if(dateTime.toLocalDate().isEqual(localDate.plusDays(1))) {
            return new DateHolderModel(resources.getString(R.string.tomorrow_text), resources.getColor(android.R.color.black));
        } else {
            //Check if > today && < 7 days
            DateTime dateRangeCheck = new DateTime();
            if(isWithinSevenDaysFromNow(dateRangeCheck)) {
                return new DateHolderModel(dateTime.dayOfWeek().getAsText(), resources.getColor(android.R.color.black));
            } else {
                //return as format
                return new DateHolderModel(formatDate(dateTime), resources.getColor(android.R.color.black));
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
