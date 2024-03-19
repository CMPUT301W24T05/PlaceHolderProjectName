package ca.cmput301t05.placeholder.ui.events.creation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
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
import java.util.List;
import java.util.UUID;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.ui.events.ViewQRCodesActivity;

public class PreviewEventActivity extends AppCompatActivity {
    private Button buttonCreateEvent;
    private Button buttonBack;
    private TextView textViewEventDate;
    private TextView textViewEventLocation;
    private TextView textViewEventDetails;
    private TextView textViewEventAuthor;
    private TextView textViewEventName;
    private ImageView imageViewEventPoster;

    private PlaceholderApp app;
    private Event currentEvent;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_preview);

        app = (PlaceholderApp) getApplicationContext();
        currentEvent = app.getCachedEvent();

        setUpViews();
        setEventDetails();

        buttonBack.setOnClickListener(view -> finish());
        buttonCreateEvent.setOnClickListener(view -> handleEventCreation(app, currentEvent));
    }

    private void setUpViews() {
        buttonCreateEvent = findViewById(R.id.preview_create);
        buttonBack = findViewById(R.id.event_preview_back);
        textViewEventName = findViewById(R.id.preview_name);
        textViewEventDate = findViewById(R.id.event_preview_eventDate);
        textViewEventLocation = findViewById(R.id.event_preview_eventlocation);
        textViewEventDetails = findViewById(R.id.event_preview_eventinfo);
        textViewEventAuthor = findViewById(R.id.event_preview_author);
        imageViewEventPoster = findViewById(R.id.event_preview_poster);
    }

    private void setEventDetails() {
        String dateTime = generateEventDateTime();
        textViewEventName.setText(currentEvent.getEventName());
        textViewEventDate.setText(dateTime);
        textViewEventLocation.setText(currentEvent.getLocation());
        textViewEventDetails.setText(currentEvent.getEventInfo());

        UUID profileId = currentEvent.getEventCreator();
        app.getProfileTable().fetchDocument(profileId.toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile document) {
                textViewEventAuthor.setText(document.getName());
            }

            @Override
            public void onFailure(Exception e) {
            }
        });

        if (currentEvent.hasEventPosterBitmap()) {
            imageViewEventPoster.setImageBitmap(currentEvent.getEventPosterBitmap());
        } else {
            // This should never be executed, since the poster has not been uploaded yet
            app.getPosterImageHandler().getPosterPicture(currentEvent, this, new BaseImageHandler.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    imageViewEventPoster.setImageBitmap(bitmap);
                }

                @Override
                public void onError(Exception e) {
                    // Handle error
                    Log.e("PreviewEventActivity", "Error loading image: " + e.getMessage());
                }
            });
        }
    }

    private String generateEventDateTime() {
        Calendar eventCalendar = currentEvent.getEventDate();
        int year = eventCalendar.get(Calendar.YEAR);
        int month = eventCalendar.get(Calendar.MONTH) + 1; //January is 0
        int day = eventCalendar.get(Calendar.DAY_OF_MONTH);
        // Or get the hour for 12-hour format
        int hour12 = eventCalendar.get(Calendar.HOUR);
        // Get AM or PM
        int amOrPm = eventCalendar.get(Calendar.AM_PM);
        String timePeriod = amOrPm == Calendar.AM ? " AM" : " PM";

        String time = hour12 + timePeriod;
        String date = day + ", " + month + ", " + year;
        return time + " - " + date;
    }

    //moved some lines from onCreate to a new method to reduce clutter
    private void handleEventCreation(PlaceholderApp app, Event currentEvent) {
        //push changes
        app.getPosterImageHandler().uploadPoster(app.getPicCache(), currentEvent, this); //updates the event

        // Pushes the current event (currEvent) to the event table in the database
        app.getEventTable().pushDocument(currentEvent, currentEvent.getEventID().toString(), new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event document) {

                List<String> hostedEvents = app.getUserProfile().getHostedEvents();
                hostedEvents.add(currentEvent.getEventID().toString());
                app.getUserProfile().setHostedEvents(hostedEvents);
                app.getProfileTable().pushDocument(app.getUserProfile(), app.getUserProfile().getProfileID().toString(), new Table.DocumentCallback<Profile>() {
                    @Override
                    public void onSuccess(Profile document) {
                        // If the document was successfully updated in the database, start the Main activity and finish this activity
                        String message = "Event, " + currentEvent.getEventName() + " , Successfully created";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        //change this to navigate to access qr code page
                        Intent intent = new Intent(PreviewEventActivity.this, ViewQRCodesActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                // TODO Handle the failure of updating the event in the database
            }
        });
    }
}
