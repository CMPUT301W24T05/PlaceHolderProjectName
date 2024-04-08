package ca.cmput301t05.placeholder;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.when;
import static java.util.regex.Pattern.matches;

import android.os.SystemClock;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import ca.cmput301t05.placeholder.database.DatabaseManager;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.utils.datafetchers.ProfileFetcher;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainUI {
//
    @Rule
    // Launch the actual activity
    public ActivityScenarioRule<InitialSetupActivity> scenario = new
            ActivityScenarioRule<InitialSetupActivity>(InitialSetupActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.POST_NOTIFICATIONS
    );
//    @Mock
//    PlaceholderApp mockApp;
//    @Mock
//    Profile mockProfile;

//    @Before
//    public void setUp(){
//        mockProfile = new Profile("Galvin Test", UUID.randomUUID());
//        mockApp.setUserProfile(mockProfile);
//    }
//    @Rule
//    // Launch the actual activity
//    public ActivityScenarioRule<MainActivity> scenario = new
//            ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void navigateBottomBar(){
        onView(withId(R.id.intro_name_edit)).perform(click());
        onView(withId(R.id.intro_name_edit)).perform(ViewActions.typeText("Galvin Test"), closeSoftKeyboard()); // Entering name
        // Will go to the main activity homepage
        onView(withId(R.id.intro_submit_button)).perform(click());
        SystemClock.sleep(2000);
        // check go to Explore Menu page
        onView(ViewMatchers.withId(R.id.explore_menu_item)).perform(click());
        //onView(ViewMatchers.withId(R.id.textViewOpenEvents)).check(withText("Dave"));

        onView(ViewMatchers.withId(R.id.futureEvents)).check(ViewAssertions.doesNotExist());
        //String eventExplore = "Open Events";
        //onView(ViewMatchers.withId(R.id.textViewOpenEvents)).check(matches());
    }
}