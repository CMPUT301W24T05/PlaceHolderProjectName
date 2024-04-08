package ca.cmput301t05.placeholder.admin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.ImageDetails.ImageDetails;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.ui.admin.AdminEventFragment;
import ca.cmput301t05.placeholder.ui.admin.AdminHomeActivity;
import ca.cmput301t05.placeholder.ui.admin.AdminHomeFragment;
import ca.cmput301t05.placeholder.ui.admin.AdminImagesFragment;
import ca.cmput301t05.placeholder.ui.admin.AdminProfilesFragment;
import androidx.fragment.app.testing.FragmentScenario;


public class AdminTests {


    @Mock
    AggregateQuery profilesCountQuery;

    @Mock
    AggregateQuery eventsCountQuery;

    @Mock
    Task<AggregateQuerySnapshot> profilesTask;

    @Mock
    Task<AggregateQuerySnapshot> eventsTask;

    @Mock
    AdminHomeFragment fragment;

    @Rule
    public ActivityScenarioRule<AdminHomeActivity> scenarioRule =
            new ActivityScenarioRule<>(AdminHomeActivity.class);

    @Before
    public void setUp() {
        Intents.init();
        Profile profile = new Profile("Tolu", UUID.randomUUID());
        profile.setAdmin(true);
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void AdminIntentTest() throws Exception {

        /*
        onView(withId(R.id.profile_menu_item)).perform(click());
        onView(withId(R.id.edit_event_buttoon)).perform(click());

        //should be at profile activity
        onView(withId(R.id.admin_button)).perform(click());

         */

    }

    /**
     *Checks if the current fragment displayed is the same as the expected fragment
     * @param fragmentClass
     */
    private void assertCurrentFragmentIsInstanceOf(Class<? extends Fragment> fragmentClass) {
        scenarioRule.getScenario().onActivity(activity -> {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.admin_fragment_container);
            assert currentFragment != null;
            assert fragmentClass.isInstance(currentFragment);
        });
    }

    @Test
    public void navigateAdminFragments(){
        onView(withId(R.id.admin_menu_images)).perform(click());
        assertCurrentFragmentIsInstanceOf(AdminImagesFragment.class);

        onView(withId(R.id.admin_menu_profiles)).perform(click());
        assertCurrentFragmentIsInstanceOf(AdminProfilesFragment.class);

        onView(withId(R.id.admin_menu_events)).perform(click());
        assertCurrentFragmentIsInstanceOf(AdminEventFragment.class);

        onView(withId(R.id.admin_menu_home)).perform(click());
        assertCurrentFragmentIsInstanceOf(AdminHomeFragment.class);
    }

    /*

    public void setMocks(){
        Query profilesQuery = Mockito.mock(Query.class);
        Query eventsQuery = Mockito.mock(Query.class);
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testAdminHome(){
        Query profilesQuery = Mockito.mock(Query.class);
        Query eventsQuery = Mockito.mock(Query.class);
        AggregateQuery profilesCountQuery = Mockito.mock(AggregateQuery.class);
        AggregateQuery eventsCountQuery = Mockito.mock(AggregateQuery.class);
        Task<AggregateQuerySnapshot> profilesTask = Mockito.mock(Task.class);
        Task<AggregateQuerySnapshot> eventsTask = Mockito.mock(Task.class);
        AdminHomeFragment fragment = Mockito.mock(AdminHomeFragment.class);
        AdminHomeActivity activity = Mockito.mock(AdminHomeActivity.class);



        View mockView = Mockito.mock(View.class);
        when(fragment.getView()).thenReturn(mockView);
        TextView profileCount = Mockito.mock(TextView.class);
        when(mockView.findViewById(anyInt())).thenReturn(profileCount);

        // Mock queries
        when(profilesQuery.count()).thenReturn(profilesCountQuery);
        when(profilesCountQuery.get(AggregateSource.SERVER)).thenReturn(profilesTask);
        when(eventsQuery.count()).thenReturn(eventsCountQuery);
        when(eventsCountQuery.get(AggregateSource.SERVER)).thenReturn(eventsTask);

        AggregateQuerySnapshot profileSnapshot = Mockito.mock(AggregateQuerySnapshot.class);
        when(profileSnapshot.getCount()).thenReturn(5L);
        when(profilesTask.isSuccessful()).thenReturn(true);
        when(profilesTask.getResult()).thenReturn(profileSnapshot);

        fragment.onViewCreated(mockView, null);
        // Verify that the TextView is updated with the correct count
        verify(profileCount).setText("5");
    }


    @Test
    public void testAdminImage(){
        FragmentScenario<AdminImagesFragment> scenario = FragmentScenario.launchInContainer(AdminImagesFragment.class);

        ArrayList<ImageDetails> imageDetailsList = new ArrayList<>();

        // Add dummy ImageDetails objects
        for (int i = 0; i < 10; i++) {
            ImageDetails imageDetails = new ImageDetails();
            // Set dummy data, you can customize this according to your needs
            imageDetails.setImageUri(Uri.parse("dummy_uri_" + i));
            imageDetails.setUploadTime(Calendar.getInstance());
            // Add the dummy ImageDetails object to the list
            imageDetailsList.add(imageDetails);
        }

        // You may have to implement a method in your adapter to set this data
        // For example, you could add a method in ViewAllImagesAdapter like setImages(ArrayList<ImageDetails> images)
        // Then you can call that method to set the dummy data

        // Now, populate the RecyclerView with this dummy data
        scenario.getScenario().onActivity(activity -> activity.setImages(imageDetailsList));
    }

     */

}



