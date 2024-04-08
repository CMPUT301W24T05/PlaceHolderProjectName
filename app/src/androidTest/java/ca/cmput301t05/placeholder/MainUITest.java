package ca.cmput301t05.placeholder;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
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

/**
 * Intent tests for app's main UI.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainUITest {
//
    @Rule
    // Launch the actual activity
    public ActivityScenarioRule<InitialSetupActivity> scenario = new
            ActivityScenarioRule<InitialSetupActivity>(InitialSetupActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.CAMERA
//            Manifest.permission.POST_NOTIFICATIONS
    );


    /**
     * Tests that the UI bottom navigation bar directs you different pages as expected.
     */
    @Test
    public void navigateBottomBar(){
        onView(withId(R.id.intro_name_edit)).perform(click());
        onView(withId(R.id.intro_name_edit)).perform(ViewActions.typeText("Galvin Test"), closeSoftKeyboard()); // Entering name
        // Will go to the main activity homepage
        onView(withId(R.id.intro_submit_button)).perform(click());
        SystemClock.sleep(2000);
        // check if land in home page
        onView(ViewMatchers.withId(R.id.main_page_toolbar)).check(ViewAssertions.matches(isDisplayed()));
        // check go to Explore Menu page
        onView(ViewMatchers.withId(R.id.explore_menu_item)).perform(click());
        onView(ViewMatchers.withId(R.id.listAllEvents)).check(ViewAssertions.matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.textViewOpenEvents)).check(ViewAssertions.matches(isDisplayed()));
        // check go to Event organized page
        onView(ViewMatchers.withId(R.id.organized_menu_item)).perform(click());
        onView(ViewMatchers.withText("Organized Events")).check(ViewAssertions.matches(isDisplayed()));
        // check go to Profile page
        onView(ViewMatchers.withId(R.id.profile_menu_item)).perform(click());
        onView(ViewMatchers.withId(R.id.edit_name)).check(ViewAssertions.matches(isDisplayed()));
        // check go to scanning page
        onView(ViewMatchers.withId(R.id.scan_menu_item)).perform(click());
        onView(ViewMatchers.withId(R.id.scanner_view)).check(ViewAssertions.matches(isDisplayed()));
    }
    @Test
    public void seeNotification(){
        onView(withId(R.id.intro_name_edit)).perform(click());
        onView(withId(R.id.intro_name_edit)).perform(ViewActions.typeText("Galvin Test"), closeSoftKeyboard()); // Entering name
        // Will go to the main activity homepage
        onView(withId(R.id.intro_submit_button)).perform(click());
        SystemClock.sleep(2000);
        //openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withId(R.id.home_notification_item)).perform(click());
        //onView(ViewMatchers.withId(R.id.recycler_page_name_text)).check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Tests the individual UI components behave properly
     * during the event creation process.
     */
    @Test
    public void createEventUI() {
        onView(withId(R.id.intro_name_edit)).perform(click());
        onView(withId(R.id.intro_name_edit)).perform(ViewActions.typeText("Galvin Test"), closeSoftKeyboard()); // Entering name
        // Will go to the main activity homepage
        onView(withId(R.id.intro_submit_button)).perform(click());
        SystemClock.sleep(2000);
        // navigate to organized menu
        onView(ViewMatchers.withId(R.id.organized_menu_item)).perform(click());
        onView(ViewMatchers.withText("Organized Events")).check(ViewAssertions.matches(isDisplayed()));
        // Choose to create a new event
        onView(ViewMatchers.withId(R.id.OrganizerEventButton)).perform(click());
        onView(ViewMatchers.withText("Select Poster")).check(ViewAssertions.matches(isDisplayed()));
        // choose to enter event details
        onView(ViewMatchers.withId(R.id.enterEventName)).perform(click());
        onView(ViewMatchers.withId(R.id.enterEventName)).perform(ViewActions.typeText("Galvin Test Event"), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.enterLocation)).perform(click());
        onView(ViewMatchers.withId(R.id.enterLocation)).perform(ViewActions.typeText("CCIS 1-140"), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.enterTime)).perform(click());
        onView(ViewMatchers.withId(R.id.enterTime)).perform(ViewActions.typeText("OK"), closeSoftKeyboard());
        // Confirm the selected time
        onView(ViewMatchers.withId(R.id.enterEventCapacity)).perform(ViewActions.typeText("20"), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.enterEventDescription)).perform(ViewActions.typeText("CMPUT 301 is so Hard!!"), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.eventDetailNextPage)).perform(click());

    }
}