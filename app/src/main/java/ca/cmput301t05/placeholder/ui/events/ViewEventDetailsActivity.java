package ca.cmput301t05.placeholder.ui.events;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.UUID;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.ProfileEditActivity;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;

public class ViewEventDetailsActivity extends AppCompatActivity {
    private TextView eventDateTextView;
    private TextView eventLocationTextView;
    private TextView eventDetailsTextView;
    private TextView eventAuthorTextView;
    private ImageView eventPosterImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Getting event from the application context
        PlaceholderApp app = (PlaceholderApp) getApplicationContext();
        Event displayEvent = app.getCachedEvent(); // Initialize UI
        setContentView(R.layout.event_vieweventdetails);
        initTextViews();
        initImageView(); // Set button click listeners
        setEventDetails(displayEvent); // Show event poster
        setEventPoster(app, displayEvent);
    }

    private void initTextViews() {
        eventDateTextView = findViewById(R.id.event_signup_eventDate);
        eventLocationTextView = findViewById(R.id.event_signup_eventlocation);
        eventDetailsTextView = findViewById(R.id.event_signup_eventinfo);
        eventAuthorTextView = findViewById(R.id.event_signup_author);
    }

    private void initImageView() {
        eventPosterImageView = findViewById(R.id.event_signup_poster);
    }

    private void setEventDetails(Event displayEvent) {
        Log.d("Event_Check", String.valueOf(displayEvent.getEventName())); //get date
        Log.e("Event_Check", String.valueOf(displayEvent.getEventDate()));
        Calendar calendar = displayEvent.getEventDate();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; //January is 0
        int day = calendar.get(Calendar.DAY_OF_MONTH); // Or get the hour for 12-hour format
        int hour12 = calendar.get(Calendar.HOUR); // Get AM or PM
        int amPm = calendar.get(Calendar.AM_PM);
        String amOrPm;
        if (amPm == Calendar.AM) {
            amOrPm = " AM";
        } else {
            amOrPm = " PM";
        }
        String time = hour12 + amOrPm;
        String date = day + ", " + month + ", " + year;
        String dateTime = time + " - " + date;
        eventDateTextView.setText(dateTime);
        eventLocationTextView.setText(displayEvent.getLocation());
        eventDetailsTextView.setText(displayEvent.getEventInfo());
        PlaceholderApp app = (PlaceholderApp) getApplicationContext();
        UUID profileID = displayEvent.getEventCreator();
        app.getProfileTable().fetchDocument(profileID.toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile document) {
                eventAuthorTextView.setText(document.getName());
            }

            @Override
            public void onFailure(Exception e) {
            }
        });
    }

    private void setEventPoster(PlaceholderApp app, Event displayEvent) {
        if (displayEvent.hasEventPosterBitmap()) {
            eventPosterImageView.setImageBitmap(displayEvent.getEventPosterBitmap());
        } else {
            app.getPosterImageHandler().getPosterPicture(displayEvent, this, new BaseImageHandler.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    eventPosterImageView.setImageBitmap(bitmap);
                }

                @Override
                public void onError(Exception e) { // Handle error
                    Log.e("EventDetailsDialogFragment", "Error loading image: " + e.getMessage());
                }
            });
        }
    }
}
