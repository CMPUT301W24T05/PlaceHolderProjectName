package ca.cmput301t05.placeholder.ui.events.creation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.ui.events.ViewQRCodesActivity;

public class PreviewEventActivity extends AppCompatActivity {

    Button createEvent;

    private Button back;

    private TextView event_date;

    private TextView event_location;

    private TextView event_details;

    private TextView event_author;

    private TextView event_name;

    private ImageView event_poster;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_preview);

        PlaceholderApp app = (PlaceholderApp) getApplicationContext();
        Event curEvent = app.getCachedEvent();

        createEvent = findViewById(R.id.preview_create);
        back = findViewById(R.id.event_preview_back);
        event_name = findViewById(R.id.preview_name);
        event_date = findViewById(R.id.event_preview_eventDate);
        event_location = findViewById(R.id.event_preview_eventlocation);
        event_details = findViewById(R.id.event_preview_eventinfo);
        event_author = findViewById(R.id.event_preview_author);
        event_poster = findViewById(R.id.event_preview_poster);

        Calendar calendar = curEvent.getEventDate();

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

        event_name.setText(curEvent.getEventName());

        event_date.setText(dateTime);

        event_location.setText(curEvent.getLocation());

        event_details.setText(curEvent.getEventInfo());

        UUID profile_id = curEvent.getEventCreator();

        app.getProfileTable().fetchDocument(profile_id.toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile document) {
                event_author.setText(document.getName());
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        app.getPosterImageHandler().getPosterPicture(curEvent, event_poster);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); //go back to last page
            }
        });


        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //basically we want to upload the qr strings
                curEvent.toDocument();

                //push changes
                app.getPosterImageHandler().uploadPoster(app.getPicCache(), curEvent); //updates the event

                // Pushes the current event (currEvent) to the event table in the database
                // This push is also asynchronous, we go back to the Main activity in the onSuccess callback
                app.getEventTable().pushDocument(curEvent, curEvent.getEventID().toString(), new Table.DocumentCallback<Event>() {
                    @Override
                    public void onSuccess(Event document) {
                        // If the document was successfully updated in the database, start the Main activity and finish this activity
                        String message = "Event, " + curEvent.getEventName() +  " , Successfully created";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        //change this to navigate to access qr code page

                        Intent i = new Intent(PreviewEventActivity.this, ViewQRCodesActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // TODO Handle the failure of updating the event in the database
                    }
                });

            }
        });
    }
}
