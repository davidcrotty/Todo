package service;

import com.david.todo.BuildConfig;
import com.david.todo.service.ITextEntryService;
import com.david.todo.service.TextEntryService;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18)
public class TextEntryServiceTest {

    private ITextEntryService _textEntryService;

    @Before
    public void setup_fixture() {
        ShadowLog.stream = System.out;
        _textEntryService = new TextEntryService();
    }

    @Test
    public void when_checking_if_text_entered() {
        System.out.println("Given 'foo' String");
        String text = "foo";

        System.out.println("When checking is text has been entered");
        boolean result = _textEntryService.textHasBeenEntered(text);

        System.out.println("Text Should be entered");
        Assert.assertEquals(true, result);
    }

    @Test
    public void when_checking_if_text_entered_invalid() {
        System.out.println("Given a null String");
        String text = null;

        System.out.println("When checking is text has been entered");
        boolean result = _textEntryService.textHasBeenEntered(text);

        System.out.println("Text Should not be be entered");
        Assert.assertEquals(false, result);

        System.out.println("Given an empty String");
        text = "";

        System.out.println("When checking is text has been entered");
        result = _textEntryService.textHasBeenEntered(text);

        System.out.println("Text Should not be be entered");
        Assert.assertEquals(false, result);
    }
}
