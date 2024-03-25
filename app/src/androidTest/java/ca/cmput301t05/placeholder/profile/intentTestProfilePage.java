package ca.cmput301t05.placeholder.profile;
import android.os.SystemClock;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import androidx.test.espresso.action.ViewActions;
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


    @RunWith(MockitoJUnitRunner.class)
    public class intentTestProfilePage {
        // Intent test. Different from unit test above.

        @Rule
        // Launch the actual activity
        public ActivityScenarioRule<InitialSetupActivity> scenario = new
                ActivityScenarioRule<InitialSetupActivity>(InitialSetupActivity.class);

        @Test
        public void testProfilePageCorrectName(){ // Checks that when user's name matches on profile page
            // Will launch initial page where user prompted to enter name
            onView(withId(R.id.intro_name_edit)).perform(click());
            onView(withId(R.id.intro_name_edit)).perform(ViewActions.typeText("Dave"), closeSoftKeyboard()); // Entering name
            // Will go to the main activity homepage
            onView(withId(R.id.intro_submit_button)).perform(click());
            SystemClock.sleep(2000);
            onView(withId(R.id.btnProfile)).perform(click()); // Click on profile button
            onView(ViewMatchers.withId(R.id.edit_name))
                    // Check that the text matches the expected name "Dave"
                    .check(matches(withText("Dave")));
        }



    }

