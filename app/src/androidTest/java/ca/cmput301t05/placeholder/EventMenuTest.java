package ca.cmput301t05.placeholder;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.UUID;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;
import ca.cmput301t05.placeholder.ui.events.creation.EnterEventDetailsActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventMenuTest {
    PlaceholderApp app;
    Event event;
    @Before
    public void setUp(){
        app = new PlaceholderApp();
        event = new Event();
        event.setEventName("Galvin Test Event");
        event.setEventID(UUID.randomUUID());
        app.setCachedEvent(event);
        //EventMenuActivity.setApp(app);
        //EventMenuActivity.setEvent(event);
        // Launch the EventMenuActivity with the created Event object
        ActivityScenario.launch(EventMenuActivity.class)
                .onActivity(activity -> {
                    activity.setApp(app); // Set PlaceholderApp if needed
                    activity.setEvent(event);
                });
    }
    @Test
    public void testButton(){
        // check if the event Menu is displayed or not
        onView(ViewMatchers.withId(R.id.attendanceFraction)).check(ViewAssertions.matches(isDisplayed()));
    }
}
