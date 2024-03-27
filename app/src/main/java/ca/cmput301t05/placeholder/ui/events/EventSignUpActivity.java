package ca.cmput301t05.placeholder.ui.events;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.images.EventPosterImageHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;

//FIXME Do we mean to join an event through this activity or 'mark event as interested'?

public class EventSignUpActivity extends AppCompatActivity {

    private Button backButton;

    private TextView eventDateView;
    private TextView eventLocationView;
    private TextView eventDetailsView;
    private TextView eventAuthorView;

    private ImageView eventPosterView;

    private Button interestedButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ASSUMING THAT WE HAVE CACHE THE EVENT WE JUST LOADED
        PlaceholderApp app = (PlaceholderApp) getApplicationContext();
        Event displayEvent = app.getCachedEvent();

        setContentView(R.layout.event_signupevent);

        initialiseEventDetailsUI();

        backButton.setOnClickListener(view -> finish());

        updateEventDetails(displayEvent);

        handleEventButton(app, displayEvent);
    }

    private void initialiseEventDetailsUI() {
        backButton = findViewById(R.id.event_signup_back);
        eventDateView = findViewById(R.id.event_signup_eventDate);
        eventLocationView = findViewById(R.id.event_signup_eventlocation);
        eventDetailsView = findViewById(R.id.event_signup_eventinfo);
        eventAuthorView = findViewById(R.id.event_signup_author);
        eventPosterView = findViewById(R.id.event_signup_poster);
        interestedButton = findViewById(R.id.event_signup_interested);
    }

    private void updateEventDetails(Event displayEvent) {
        PlaceholderApp app = (PlaceholderApp) getApplicationContext();

        eventDateView.setText(formatEventDate(displayEvent.getEventDate()));
        eventLocationView.setText(displayEvent.getLocation());
        eventDetailsView.setText(displayEvent.getEventInfo());

        app.getProfileTable().fetchDocument(displayEvent.getEventCreator().toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile document) {
                eventAuthorView.setText(document.getName());
            }

            @Override
            public void onFailure(Exception e) {
            }
        });

        updateEventPoster(displayEvent, app.getPosterImageHandler());
    }

    private String formatEventDate(Calendar eventDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh a - dd, MM, yyyy", Locale.getDefault());
        return dateFormat.format(eventDate.getTime());
    }

    private void updateEventPoster(Event displayEvent, EventPosterImageHandler posterHandler) {
        if (displayEvent.hasEventPosterBitmap()) {
            eventPosterView.setImageBitmap(displayEvent.getEventPosterBitmap());
        } else {
            posterHandler.getPosterPicture(displayEvent, this, new BaseImageHandler.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    eventPosterView.setImageBitmap(bitmap);
                }

                @Override
                public void onError(Exception e) {
                    Log.e("EventDetailsDialogFragment", "Error loading image: " + e.getMessage());
                }
            });
        }
    }

    private void handleEventButton(PlaceholderApp app, Event displayEvent) {
        interestedButton.setOnClickListener(view -> joinEvent(app, displayEvent));
    }

    private void joinEvent(PlaceholderApp app, Event displayEvent) {
        app.getUserProfile().joinEvent(displayEvent);
        app.getJoinedEvents().put(displayEvent.getEventID(), displayEvent);
        app.getUserProfile().toDocument();
        app.getProfileTable().pushDocument(app.getUserProfile(), app.getUserProfile().getProfileID().toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile document) {
            }

            @Override
            public void onFailure(Exception e) {
            }
        });
        showMessageAndFinish("Joined Event: " + displayEvent.getEventName());
    }

    private void showMessageAndFinish(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        finish();
    }
}
