package view;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.david.todo.R;
import com.david.todo.view.TodoListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class TodoListActivityTest {

    @Rule
    public ActivityTestRule<TodoListActivity> mActivityRule =
            new ActivityTestRule<TodoListActivity>(TodoListActivity.class);

    @Test
    public void when_inputting_short_text() {
        onView(withId(R.id.short_note_text)).perform(typeText("NewText"),
                closeSoftKeyboard());

        onView(withId(R.id.short_note_text)).check(matches(withText("NewText")));
    }


}
