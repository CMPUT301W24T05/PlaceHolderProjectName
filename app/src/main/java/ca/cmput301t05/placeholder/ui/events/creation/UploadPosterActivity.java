package ca.cmput301t05.placeholder.ui.events.creation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;
import ca.cmput301t05.placeholder.ui.events.GenerateInfoCheckinActivity;
import ca.cmput301t05.placeholder.ui.events.ViewQRCodesActivity;

/**
 * UploadPosterActivity allows users to upload a poster image for an event. This activity is part of the event
 * creation process where users can select an image from their device to represent the event. The activity handles
 * selecting and uploading the image to the database and links the image with the specified event.
 */
public class UploadPosterActivity extends AppCompatActivity {

    private ImageView eventPoster;
    private Button back;
    private Button uploadPoster;
    private Button createEvent;
    private PlaceholderApp app;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private Event currentEvent;

    private Intent fromEdit;

    /**
     * Called when the activity is starting. This method initializes the UI components, sets up the action listeners,
     * and retrieves the event object from the database based on the event ID passed through an intent.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied. Otherwise, it is null.
     */
    @SuppressLint("MissingInflatedId")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_uploadposter);

        app = (PlaceholderApp) getApplicationContext();
        eventPoster = findViewById(R.id.eventPosterImage);
        back = findViewById(R.id.eventPoster_back);
        uploadPoster = findViewById(R.id.uploadPosterButton);
        createEvent = findViewById(R.id.event_create);
        fromEdit = getIntent();

        // Fetches a specific event's ID from the intent passed to this activity
        currentEvent = app.getCachedEvent();

        if(fromEdit.hasExtra("edit")){
            eventPoster.setImageBitmap(currentEvent.getEventPosterBitmap());
            createEvent.setText("Update Event");
            createEvent.setVisibility(View.VISIBLE);
        }


        // I'm using atomic reference, as it's thread-safe, meaning it can be updated while being accessed by multiple threads
        AtomicReference<Uri> curPic = new AtomicReference<>();

        // Fetches an Event document by its ID from the events table in the database
        // This fetch is asynchronous, we set the current event (currEvent) in the onSuccess callback
        setupActions(curPic);

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri == null){
                Log.d("PhotoPicker", "No media selected");
            }   else {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                eventPoster.setImageURI(uri);
                if(!fromEdit.hasExtra("edit")){createEvent.setVisibility(View.VISIBLE);}
                curPic.set(uri);
            }
        });
    }

    /**
     * Sets up the actions for UI components. This includes setting up the media picker for selecting an image,
     * configuring the button to trigger the media picker, and setting up the navigation for the next button after
     * uploading the poster. The uploaded poster is attached to the current event object and updated in the database.
     */
    private void setupActions(AtomicReference<Uri> curPic) {
        back.setOnClickListener(view -> finish());

        uploadPoster.setOnClickListener(view -> {
            PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE) // For images only
                    .build();
            pickMedia.launch(request);
        });

        createEvent.setOnClickListener(view -> {

            //set this to the cache so on the final page we can do everything
            app.setPicCache(curPic.get());
            currentEvent.setEventPosterFromUri(curPic.get(), this);
            handleEventCreation(app, currentEvent);



        });
    }

    private void handleEventCreation(PlaceholderApp app, Event currentEvent) {

        if(fromEdit.hasExtra("edit")){
            //push changes
            app.getPosterImageHandler().uploadPoster(app.getPicCache(), currentEvent, this); //updates the event

            // Pushes the current event (currEvent) to the event table in the database
            app.getEventTable().updateDocument(currentEvent, currentEvent.getEventID().toString(), new Table.DocumentCallback<Event>() {
                @Override
                public void onSuccess(Event document) {


                    app.getProfileTable().updateDocument(app.getUserProfile(), app.getUserProfile().getProfileID().toString(), new Table.DocumentCallback<Profile>() {
                        @Override
                        public void onSuccess(Profile document) {
                            // If the document was successfully updated in the database, start the Main activity and finish this activity
                            String message = "Event, " + currentEvent.getEventName() + " , Successfully created";
                            Toast.makeText(app.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            //change this to navigate to access qr code page
                            Intent intent = new Intent(UploadPosterActivity.this, EventMenuActivity.class);
                            startActivity(intent);

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
        }else {

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
                            Toast.makeText(app.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            //change this to navigate to access qr code page
                            Intent intent = new Intent(UploadPosterActivity.this, ViewQRCodesActivity.class);
                            startActivity(intent);
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
}




