package service;

import android.content.res.Resources;

import com.david.todo.BuildConfig;
import com.david.todo.R;
import com.david.todo.model.DateHolderModel;
import com.david.todo.model.TimeHolderModel;
import com.david.todo.service.EventService;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.annotation.Config;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DateTime.class)
@Config(constants = BuildConfig.class, sdk = 18)
public class PowerMockEventServiceTest {

    @Mock
    EventService _eventService;

    @Mock
    Resources _resources;

    @Before
    public void setup_fixture() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void when_validating_overdue_date() {
        DateTime dateTime = PowerMockito.mock(DateTime.class);
        PowerMockito.mockStatic(DateTimeFormat.class);

        given(_eventService.formatDate(any(DateTime.class))).willReturn("Feb - 26");
        given(dateTime.toLocalDate()).willReturn(new LocalDate(1456513911000L));
        given(_resources.getString(any(int.class))).willReturn("Feb - 26");
        given(dateTime.toString()).willReturn("Feb - 26");
        given(_eventService.retreiveDateDisplayText(any(DateTime.class), any(Resources.class))).willCallRealMethod();

        DateHolderModel dateText = _eventService.retreiveDateDisplayText(dateTime, _resources);

        Assert.assertEquals(true, dateText.getDateText().equals("Feb - 26"));
    }

    @Test
    public void when_greater_than_24_hours() {
        //Given
        DateTime currentDateTime = new DateTime(1459104780000L); // Sun, 27 Mar 2016 19:53:00 GMT
        DateTime eventTime = new DateTime(1459277400000L); //Tue, 29 Mar 2016 19:50:00 GMT
        given(_eventService.retreiveTimeDisplaytext(any(DateTime.class), any(DateTime.class), any(Resources.class))).willCallRealMethod();
        given(_resources.getColor(any(int.class))).willReturn(0x000);

        //When
        TimeHolderModel timeHolderModel = _eventService.retreiveTimeDisplaytext(eventTime, currentDateTime, _resources);

        //Should
        Assert.assertEquals(true, timeHolderModel.getTextToDisplay().equals("19:50"));
    }

    @Test
    public void when_validating_time_ahead_of_now() {
        //Given
        DateTime currentDateTime = new DateTime(1459104780000L); // Sun, 27 Mar 2016 19:53:00 GMT
        DateTime eventTime = new DateTime(1459115580000L); //Su
        // n, 27 Mar 2016 21:53:00 GMT
        given(_eventService.retreiveTimeDisplaytext(any(DateTime.class), any(DateTime.class), any(Resources.class))).willCallRealMethod();
        given(_resources.getColor(any(int.class))).willReturn(0x000);

        //When
        TimeHolderModel timeHolderModel = _eventService.retreiveTimeDisplaytext(eventTime, currentDateTime, _resources);

        //Should
        Assert.assertEquals(true, timeHolderModel.getTextToDisplay().replaceAll("\\s+", " ").equals("22:53 ( in 3 hours)"));

        //Given
        eventTime = new DateTime(1459104960000L); //Sun, 27 Mar 2016 19:56:00 GMT

        //When
        timeHolderModel = _eventService.retreiveTimeDisplaytext(eventTime, currentDateTime, _resources);

        //Should
        Assert.assertEquals(true, timeHolderModel.getTextToDisplay().replaceAll("\\s+", " ").equals("19:56 ( in 3 minutes)"));
    }

    @Test
    public void when_validating_time_before_now() {
        //Given
        DateTime currentDateTime = new DateTime(1459104780000L); // Sun, 27 Mar 2016 19:53:00 GMT
        DateTime eventTime = new DateTime(1459094160000L); //Sun, 27 Mar 2016 16:56:00 GMT
        given(_eventService.retreiveTimeDisplaytext(any(DateTime.class), any(DateTime.class), any(Resources.class))).willCallRealMethod();
        given(_resources.getColor(any(int.class))).willReturn(0x000);

        //When
        TimeHolderModel timeHolderModel = _eventService.retreiveTimeDisplaytext(eventTime, currentDateTime, _resources);

        //Should
        Assert.assertEquals(true, timeHolderModel.getTextToDisplay().equals("16:56 ( 2 hours ago)"));

        //Given
        eventTime = new DateTime(1459104600000L); //Sun, 27 Mar 2016 18:50:00 GMT

        //When
        timeHolderModel = _eventService.retreiveTimeDisplaytext(eventTime, currentDateTime, _resources);

        //Should
        Assert.assertEquals(true, timeHolderModel.getTextToDisplay().equals("19:50 ( 3 minutes ago)"));
    }
}
