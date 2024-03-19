package ca.cmput301t05.placeholder.ui.events.creation;

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

import java.util.concurrent.atomic.AtomicReference;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;
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
    private Button nextPage;
    private PlaceholderApp app;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private Event currEvent;

    private Intent fromEdit;

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
        fromEdit = getIntent();

        // Fetches a specific event's ID from the intent passed to this activity
        currEvent = app.getCachedEvent();

        if(fromEdit.hasExtra("edit")){
            app.getPosterImageHandler().getPosterPicture(currEvent, eventPoster);
            nextPage.setText("Update Event");
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
                nextPage.setVisibility(View.VISIBLE);
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

        nextPage.setOnClickListener(view -> {

            //set this to the cache so on the final page we can do everything
            app.setPicCache(curPic.get());
            currEvent.setEventPosterFromUri(curPic.get(), this);

            if(nextPage.getText()== "Update Event"){
                Intent updated = new Intent(UploadPosterActivity.this, PreviewEventActivity.class);
                updated.putExtra("edit", true);
                startActivity(updated);
                finish();

            }else{
                Intent i = new Intent(UploadPosterActivity.this, GenerateInfoCheckinActivity.class);
                startActivity(i);
                finish();
            }









            if(nextPage.getText()== "Update Event"){


            }

        });
    }
}




