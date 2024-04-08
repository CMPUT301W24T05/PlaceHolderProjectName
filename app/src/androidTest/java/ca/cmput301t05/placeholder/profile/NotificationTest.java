package ca.cmput301t05.placeholder.profile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.assertion.ViewAssertions;

import static org.mockito.AdditionalMatchers.not;

import android.Manifest;
import android.os.SystemClock;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.os.SystemClock;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.runner.RunWith;

import ca.cmput301t05.placeholder.InitialSetupActivity;
import ca.cmput301t05.placeholder.R;

/**
 * Intent testing for the app's notifications UI component.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NotificationTest {

    @Rule
    // Launch the actual activity
    public ActivityScenarioRule<InitialSetupActivity> scenario = new
            ActivityScenarioRule<InitialSetupActivity>(InitialSetupActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.CAMERA
    );

    /**
     * Validates that clicking on the notification icon on the app's main page
     * directs you to a new page where event notification can be displayed and seen.
     */
    @Test
    public void seeNotification(){
        onView(withId(R.id.intro_name_edit)).perform(click());
        onView(withId(R.id.intro_name_edit)).perform(ViewActions.typeText("Galvin Test"), closeSoftKeyboard()); // Entering name
        // Will go to the main activity homepage
        onView(withId(R.id.intro_submit_button)).perform(click());
        SystemClock.sleep(2000);
        onView(withId(R.id.home_notification_item)).perform(click());
        onView(ViewMatchers.withId(R.id.recycler_page_name_text)).check(ViewAssertions.matches(isDisplayed()));
    }
}
