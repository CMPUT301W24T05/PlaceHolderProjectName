package ca.cmput301t05.placeholder.ui.events;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import ca.cmput301t05.placeholder.MainActivity;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.Table;
import ca.cmput301t05.placeholder.events.Event;

/**
 * UploadPosterActivity allows users to upload a poster image for an event. This activity is part of the event
 * creation process where users can select an image from their device to represent the event. The activity handles
 * selecting and uploading the image to the database and links the image with the specified event.
 */
public class UploadPosterActivity extends AppCompatActivity {

    private ImageView eventPoster;
    private Button back;
    private Button uploadPoster;
    private Button nextPage;
    private PlaceholderApp app;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private Event currEvent;

    /**
     * Called when the activity is starting. This method initializes the UI components, sets up the action listeners,
     * and retrieves the event object from the database based on the event ID passed through an intent.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied. Otherwise, it is null.
     */
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_uploadposter);

        app = (PlaceholderApp) getApplicationContext();
        eventPoster = findViewById(R.id.eventPosterImage);
        back = findViewById(R.id.eventPoster_back);
        uploadPoster = findViewById(R.id.uploadPosterButton);
        nextPage = findViewById(R.id.event_posternext);

        // Fetches a specific event's ID from the intent passed to this activity
        UUID eventID = UUID.fromString(getIntent().getStringExtra("created_event_ID"));

        // Fetches an Event document by its ID from the events table in the database
        // This fetch is asynchronous, we set the current event (currEvent) in the onSuccess callback
        app.getEventTable().fetchDocument(eventID.toString(), new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event document) {
                // If the document successfully fetched from the database
                // set the returned event document (document) as the current event (currEvent)
                currEvent = document;
                setupActions();
            }

            @Override
            public void onFailure(Exception e) {
                // TODO Handle there being no event with 'eventID' in the database
            }
        });
    }

    /**
     * Sets up the actions for UI components. This includes setting up the media picker for selecting an image,
     * configuring the button to trigger the media picker, and setting up the navigation for the next button after
     * uploading the poster. The uploaded poster is attached to the current event object and updated in the database.
     */
    private void setupActions() {
        // I'm using atomic reference, as it's thread-safe, meaning it can be updated while being accessed by multiple threads
        AtomicReference<Uri> curPic = new AtomicReference<>();

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri == null){
                Log.d("PhotoPicker", "No media selected");
            }   else {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                eventPoster.setImageURI(uri);
                nextPage.setVisibility(View.VISIBLE);
                curPic.set(uri);
            }
        });

        back.setOnClickListener(view -> finish());

        uploadPoster.setOnClickListener(view -> {
            PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE) // For images only
                    .build();
            pickMedia.launch(request);
        });

        nextPage.setOnClickListener(view -> {

            app.getPosterImageHandler().uploadPoster(curPic.get(), currEvent); //updates the event

            // Pushes the current event (currEvent) to the event table in the database
            // This push is also asynchronous, we go back to the Main activity in the onSuccess callback
            app.getEventTable().pushDocument(currEvent, currEvent.getEventID().toString(), new Table.DocumentCallback<Event>() {
                @Override
                public void onSuccess(Event document) {
                    // If the document was successfully updated in the database, start the Main activity and finish this activity
                    Intent i = new Intent(UploadPosterActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    // TODO Handle the failure of updating the event in the database
                }
            });
        });
    }
}
