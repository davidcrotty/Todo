package com.david.todo.service;

import android.content.res.Resources;

import com.david.todo.BuildConfig;
import com.david.todo.R;
import com.david.todo.model.DateHolderModel;
import com.david.todo.model.TimeHolderModel;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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

    public TimeHolderModel retreiveTimeDisplaytext(DateTime dateTimeOfEvent,
                                                   DateTime currentDateTime,
                                                   Resources resources) {
        Period p = new Period(currentDateTime, dateTimeOfEvent);
        int daysFromNow = Days.daysBetween(currentDateTime.toLocalDate(), dateTimeOfEvent.toLocalDate()).getDays();
        int hoursFromNow = p.getHours();
        int hours = dateTimeOfEvent.getHourOfDay();
        int minutes = dateTimeOfEvent.getMinuteOfDay();

        if(currentDateTime.isBefore(dateTimeOfEvent)) {
            if(daysFromNow >= 1) {
                return new TimeHolderModel(String.format("%02d:%02d", hours, minutes - (hours * 60)),
                        resources.getColor(R.color.secondary_list_item));
            }

            if(hoursFromNow == 0) {
                int minutesFromNow = p.getMinutes();
                return new TimeHolderModel(String.format("%02d:%02d ( in %2d minutes)", hours, minutes - (hours * 60), minutesFromNow),
                        resources.getColor(R.color.secondary_list_item));
            } else {
                return new TimeHolderModel(String.format("%02d:%02d ( in %2d hours)", hours, minutes - (hours * 60), hoursFromNow),
                        resources.getColor(R.color.secondary_list_item));
            }
        } else if(currentDateTime.isAfter(dateTimeOfEvent)) {
            if(hoursFromNow == 0) {
                int minutesFromNow = p.getMinutes();
                return new TimeHolderModel(String.format("%02d:%02d (%2d minutes ago)", hours, minutes - (hours * 60), Math.abs(minutesFromNow)),
                        resources.getColor(R.color.red));
            } else {
                return new TimeHolderModel(String.format("%02d:%02d (%2d hours ago)", hours, minutes - (hours * 60), Math.abs(hoursFromNow)),
                        resources.getColor(R.color.red));
            }
        }

        return null;
    }

    public String formatDate(DateTime dateTime) {
        return dateTime.toString(DateTimeFormat.forPattern(BuildConfig.DATE_FORMAT));
    }

    private boolean isWithinSevenDaysFromNow(DateTime dateTime) {
        return dateTime.isAfter(dateTime.plusDays(1)) && dateTime.isBefore(dateTime.plusDays(7));
    }
}
