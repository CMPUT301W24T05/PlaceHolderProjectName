package ca.cmput301t05.placeholder.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import ca.cmput301t05.placeholder.ProfileEditActivity;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;

public class ViewEventDetailsActivity extends AppCompatActivity {

    private Button back;

    private ImageButton profile_button;

    private TextView event_date;

    private TextView event_location;

    private TextView event_details;

    private TextView event_author;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.event_vieweventdetails);


        back = findViewById(R.id.vieweventdetails_back);
        profile_button = findViewById(R.id.vieweventdetails_profile);

        event_date = findViewById(R.id.vieweventdetails_date);
        event_location = findViewById(R.id.vieweventdetails_location);

        event_details = findViewById(R.id.vieweventdetails_eventinfo);
        event_author = findViewById(R.id.vieweventdetails_eventAuthor);

        //assume we grab the event id from a bundle and use that to display
        //so just grab from the database for testing
        Event displayEvent = new Event(UUID.fromString("201a67cf-1aee-4ae7-95e9-6808a1a6fb85"));
        displayEvent.getEventFromDatabase();

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

        //get date
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

        //event_author.setText();











    }
}
