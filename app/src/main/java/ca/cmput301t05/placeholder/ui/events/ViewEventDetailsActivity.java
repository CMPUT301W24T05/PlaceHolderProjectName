package ca.cmput301t05.placeholder.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.ProfileEditActivity;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.ProfileTable;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;

public class ViewEventDetailsActivity extends AppCompatActivity {

    private Button back;

    private Button profile_button;

    private TextView event_date;

    private TextView event_location;

    private TextView event_details;

    private TextView event_author;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for testing
        Event displayEvent = new Event(UUID.fromString("e906a09f-a80b-4e2a-a598-ab12aec2b468"));
        displayEvent.setEventCreator(UUID.fromString("cb379e24-11a0-41dd-ad85-e86aaa1df731"));
        displayEvent.setEventLocation("EventLocation");

        Calendar calendarE = Calendar.getInstance();
        calendarE.setTimeZone(TimeZone.getTimeZone("GMT-7")); // Set to the equivalent of UTC-7
        calendarE.set(2024, Calendar.MARCH, 7, 15, 37, 34);
        calendarE.set(Calendar.MILLISECOND, 0);

        displayEvent.setEventDate(calendarE);
        displayEvent.setEventInfo("Simple event Description");
        displayEvent.setMaxAttendees(22);




        setContentView(R.layout.event_vieweventdetails);


        back = findViewById(R.id.vieweventdetails_back);
        profile_button = findViewById(R.id.vieweventdetails_profile);

        event_date = findViewById(R.id.vieweventdetails_date);
        event_location = findViewById(R.id.vieweventdetails_location);

        event_details = findViewById(R.id.vieweventdetails_eventinfo);
        event_author = findViewById(R.id.vieweventdetails_eventAuthor);


        PlaceholderApp app = (PlaceholderApp) getApplicationContext();





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); //go back to last page
            }
        });

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open profile
                Intent i = new Intent(ViewEventDetailsActivity.this, ProfileEditActivity.class);
                startActivity(i);
            }
        });


        Log.d("Event_Check", String.valueOf(displayEvent.getEventName()));
        //get date
        Log.e("Event_Check",String.valueOf(displayEvent.getEventDate()));

        Calendar calendar = displayEvent.getEventDate();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; //January is 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Or get the hour for 12-hour format
        int hour12 = calendar.get(Calendar.HOUR);
        // Get AM or PM
        int amPm = calendar.get(Calendar.AM_PM);

        String amOrPm;
        if (amPm == Calendar.AM){
            amOrPm = " AM";
        }   else {
            amOrPm = " PM";
        }


        String time = String.valueOf(hour12) + amOrPm;
        String date = String.valueOf(day) + ", " + String.valueOf(month) + ", " + String.valueOf(year);

        String dateTime = time + " - " + date;

        event_date.setText(dateTime);

        event_location.setText(displayEvent.getLocation());

        event_details.setText(displayEvent.getEventInfo());

        UUID profile_id = displayEvent.getEventCreator();

        app.getProfileTable().fetchProfile(profile_id.toString(), new ProfileTable.ProfileCallback() {
            @Override
            public void onSuccess(Profile profile) {
                event_author.setText(profile.getName());
            }

            @Override
            public void onFailure(Exception e) {

            }
        });














    }
}
