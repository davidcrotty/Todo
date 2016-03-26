package service;


import android.app.Application;
import android.content.res.Resources;

import com.david.todo.BuildConfig;
import com.david.todo.R;
import com.david.todo.model.DateHolderModel;
import com.david.todo.service.EventService;
import com.david.todo.service.TextEntryService;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18)
public class EventServiceTest {

    private EventService _eventService;
    private Resources _resourceMock;

    @Before
    public void setup_fixture() {
        ShadowLog.stream = System.out;
        _eventService = new EventService();

        Application app = spy(RuntimeEnvironment.application);

        _resourceMock = spy(app.getResources());
        when(app.getResources())
                .thenReturn(_resourceMock);

        when(_resourceMock.getString(R.string.today_text))
                .thenReturn("Today");

        when(_resourceMock.getString(R.string.tomorrow_text))
                .thenReturn("Tomorrow");
    }

    @Test
    public void when_getting_date_to_display() {
        System.out.println("Given a valid date of today");
        Date date = new DateTime().toDate();

        System.out.println("When getting text to display");
        DateHolderModel dateText = _eventService.retreiveDateDisplayText(new DateTime(date), _resourceMock);

        System.out.println("Should display Today");
        Assert.assertEquals("Today", dateText.getDateText());

        System.out.println("Given a valid date of tomorrow");
        date = new DateTime().plusDays(1).toDate();

        System.out.println("When getting text to display");
        dateText = _eventService.retreiveDateDisplayText(new DateTime(date), _resourceMock);

        System.out.println("Should display Tomorrow");
        Assert.assertEquals("Tomorrow", dateText.getDateText());

        System.out.println("Given a valid date of 4 days after 6/03/2016");
        long feb_10_2016 = 1455141162000L;
        DateTimeUtils.setCurrentMillisFixed(feb_10_2016);
        DateTime dateTimeWeekday = new DateTime();
        date = dateTimeWeekday.toDate();

        //reset date so all dates from here on are read normally
        DateTimeUtils.setCurrentMillisFixed(System.currentTimeMillis());

        System.out.println("When getting text to display");
        dateText = _eventService.retreiveDateDisplayText(new DateTime(date), _resourceMock);

        System.out.println("Should display Feb - 10");
        Assert.assertEquals(dateTimeWeekday.toString(DateTimeFormat.forPattern(BuildConfig.DATE_FORMAT)), dateText.getDateText());
    }

    @Test
    public void when_getting_date_to_display_invalid() {
        System.out.println("Given an invalid date, null");

        System.out.println("When getting text to display");
        DateHolderModel dateText = _eventService.retreiveDateDisplayText(null, _resourceMock);

        System.out.println("Should display null");
        Assert.assertNull(dateText);
    }
}
