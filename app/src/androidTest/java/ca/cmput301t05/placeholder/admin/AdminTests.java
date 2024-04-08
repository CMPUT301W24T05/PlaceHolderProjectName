package ca.cmput301t05.placeholder.admin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import org.junit.Test;

import ca.cmput301t05.placeholder.R;

public class AdminTests {


    @Test
    public void AdminIntentTest() throws Exception {


        onView(withId(R.id.profile_menu_item)).perform(click());
        onView(withId(R.id.edit_event_buttoon)).perform(click());

        //should be at profile activity
        onView(withId(R.id.admin_button)).perform(click());

    }

}
