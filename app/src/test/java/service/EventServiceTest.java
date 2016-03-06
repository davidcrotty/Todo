package service;


import com.david.todo.BuildConfig;
import com.david.todo.service.EventService;
import com.david.todo.service.TextEntryService;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.Date;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18)
public class EventServiceTest {

    private EventService _eventService;

    @Before
    public void setup_fixture() {
        ShadowLog.stream = System.out;
        _eventService = new EventService();
    }

    @Test
    public void when_getting_date_to_display() {
        //TODO mock resources
        System.out.println("Given a valid date of today");
        Date date = new DateTime().toDate();

        System.out.println("When getting text to display");
        String dateText = _eventService.retreiveDateDisplayText(date);

        System.out.println("Should display Today");
        Assert.assertEquals("Today", dateText);

        System.out.println("Given a valid date of tomorrow");
        date = new DateTime().plusDays(1).toDate();

        System.out.println("When getting text to display");
        dateText = _eventService.retreiveDateDisplayText(date);

        System.out.println("Should display Tomorrow");
        Assert.assertEquals("Tomorrow", dateText);

        System.out.println("Given a valid date of 4 days after 6/03/2016");
        DateTimeUtils.setCurrentMillisFixed(1457306452755L);
        DateTime dateTimeWeekday = new DateTime();
        date = dateTimeWeekday.toDate();

        System.out.println("When getting text to display");
        dateText = _eventService.retreiveDateDisplayText(date);

        System.out.println("Should display Mar - 10");
        Assert.assertEquals(dateTimeWeekday.toString(DateTimeFormat.forPattern(BuildConfig.DATE_FORMAT)), dateText);
    }

    @Test
    public void when_getting_date_to_display_invalid() {
        System.out.println("Given an invalid date, null");
        Date date = null;

        System.out.println("When getting text to display");
        String dateText = _eventService.retreiveDateDisplayText(date);

        System.out.println("Should display null");
        Assert.assertNull(dateText);
    }
}
