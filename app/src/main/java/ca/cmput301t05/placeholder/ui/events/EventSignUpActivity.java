package ca.cmput301t05.placeholder.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.UUID;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.ProfileEditActivity;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;

public class EventSignUpActivity extends AppCompatActivity {

    private Button back;


    private TextView event_date;

    private TextView event_location;

    private TextView event_details;

    private TextView event_author;

    private ImageView event_poster;

    private Button interested;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //ASSUMING THAT WE HAVE CACHE THE EVENT WE JUST LOADED
        PlaceholderApp app = (PlaceholderApp) getApplicationContext();
        Event displayEvent = app.getCachedEvent();


        setContentView(R.layout.event_signupevent);


        back = findViewById(R.id.event_signup_back);

        event_date = findViewById(R.id.event_signup_eventDate);
        event_location = findViewById(R.id.event_signup_eventlocation);

        event_details = findViewById(R.id.event_signup_eventinfo);
        event_author = findViewById(R.id.event_signup_author);

        event_poster = findViewById(R.id.event_signup_poster);

        interested = findViewById(R.id.event_signup_interested);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); //go back to last page
            }
        });


        Log.d("Event_Check", String.valueOf(displayEvent.getEventName()));
        //get date
        Log.e("Event_Check", String.valueOf(displayEvent.getEventDate()));

        Calendar calendar = displayEvent.getEventDate();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; //January is 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Or get the hour for 12-hour format
        int hour12 = calendar.get(Calendar.HOUR);
        // Get AM or PM
        int amPm = calendar.get(Calendar.AM_PM);

        String amOrPm;
        if (amPm == Calendar.AM) {
            amOrPm = " AM";
        } else {
            amOrPm = " PM";
        }


        String time = String.valueOf(hour12) + amOrPm;
        String date = String.valueOf(day) + ", " + String.valueOf(month) + ", " + String.valueOf(year);

        String dateTime = time + " - " + date;

        event_date.setText(dateTime);

        event_location.setText(displayEvent.getLocation());

        event_details.setText(displayEvent.getEventInfo());

        UUID profile_id = displayEvent.getEventCreator();

        app.getProfileTable().fetchDocument(profile_id.toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile document) {
                event_author.setText(document.getName());
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        app.getPosterImageHandler().getPosterPicture(displayEvent, event_poster);

        interested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //basically when we click we join the event

                app.getUserProfile().joinEvent(displayEvent);
                app.getJoinedEvents().put(displayEvent.getEventID(),displayEvent);

                app.getUserProfile().toDocument();

                app.getProfileTable().pushDocument(app.getUserProfile(), app.getProfileTable().toString(), new Table.DocumentCallback<Profile>() {
                    @Override
                    public void onSuccess(Profile document) {
                        
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

                Toast toast = Toast.makeText(getApplicationContext(), "Joined Event", Toast.LENGTH_SHORT);
                toast.show();

                finish();

            }
        });
    }
}
