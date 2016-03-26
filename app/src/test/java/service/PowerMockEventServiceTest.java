package service;

import android.content.res.Resources;

import com.david.todo.BuildConfig;
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

        String dateText = _eventService.retreiveDateDisplayText(dateTime, _resources);

        Assert.assertEquals(true, dateText.equals("Feb - 26"));
    }
}
