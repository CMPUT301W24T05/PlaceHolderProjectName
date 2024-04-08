package ca.cmput301t05.placeholder.profile;
import android.os.SystemClock;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import static java.util.function.Predicate.not;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Nested;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import ca.cmput301t05.placeholder.InitialSetupActivity;
import ca.cmput301t05.placeholder.MainActivity;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;

/**
 * Intent test for the profile page.
 * @author Anthony
 */
@RunWith(MockitoJUnitRunner.class)
    public class ProfilePageTest {

    /**
     * Launches the activity.
     */
    @Rule
    public ActivityScenarioRule<InitialSetupActivity> scenario = new
            ActivityScenarioRule<InitialSetupActivity>(InitialSetupActivity.class);

    /**
     * Test that when a user initially enters their name, it is correctly
     * saved and previewed on their profile page. Also, tests that changes to user's
     * profile are saved.
     */
    @Test
    public void testProfilePageCorrectName(){
        onView(withId(R.id.intro_name_edit)).perform(click());
        onView(withId(R.id.intro_name_edit)).perform(ViewActions.typeText("Dave"), closeSoftKeyboard()); // Entering name
        onView(withId(R.id.intro_submit_button)).perform(click());
        SystemClock.sleep(2000);
        onView(withId(R.id.profile_menu_item)).perform(click());
        onView(ViewMatchers.withId(R.id.edit_name)).check(matches(withText("Dave")));
        onView(withId(R.id.profile_menu_item)).perform(click());
        onView(withId(R.id.edit_event_buttoon)).perform(click());
        onView(withId(R.id.edit_name)).perform(click());
        onView(withId(R.id.edit_name)).perform(ViewActions.clearText());
        onView(withId(R.id.edit_name)).perform(ViewActions.typeText("Not Dave"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(click());
        onView(withId(R.id.home_menu_item)).perform(click());
        SystemClock.sleep(1000);
        onView(withId(R.id.profile_menu_item)).perform(click());
        onView(ViewMatchers.withId(R.id.edit_name)).check(matches(withText("Not Dave")));
    }

    /**
     * Tests that making changes to profile information and canceling
     * those changes does not change any information (e.g. name or contact info) as expected.
     */
    @Test
    public void testProfilePageCancelChanges(){
        onView(withId(R.id.intro_name_edit)).perform(click());
        onView(withId(R.id.intro_name_edit)).perform(ViewActions.typeText("John"), closeSoftKeyboard()); // Entering name
        onView(withId(R.id.intro_submit_button)).perform(click());
        SystemClock.sleep(2000);
        onView(withId(R.id.profile_menu_item)).perform(click());
        onView(ViewMatchers.withId(R.id.edit_name)).check(matches(withText("John")));
        onView(withId(R.id.profile_menu_item)).perform(click());
        onView(withId(R.id.edit_event_buttoon)).perform(click());
        onView(withId(R.id.edit_name)).perform(click());
        onView(withId(R.id.edit_name)).perform(ViewActions.clearText());
        onView(withId(R.id.edit_name)).perform(ViewActions.typeText("Testy"), closeSoftKeyboard());
        onView(withId(R.id.edit_homepage)).perform(ViewActions.typeText("www.test.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_homepage)).perform(click());
        onView(withId(R.id.cancel_button)).perform(click());
        onView(withId(R.id.home_menu_item)).perform(click());
        SystemClock.sleep(1000);
        onView(withId(R.id.profile_menu_item)).perform(click());
        onView(ViewMatchers.withId(R.id.edit_name)).check(matches(withText("John")));
        onView(withId(R.id.edit_homepage)).check(matches(withText("")));
    }





    }

