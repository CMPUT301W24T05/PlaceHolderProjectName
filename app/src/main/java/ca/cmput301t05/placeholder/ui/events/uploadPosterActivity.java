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

import ca.cmput301t05.placeholder.MainActivity;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;

public class uploadPosterActivity extends AppCompatActivity {

    private ImageView eventPoster;
    private Button back;
    private Button uploadPoster;
    private Button nextPage;
    private PlaceholderApp app;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_uploadposter);

        app = (PlaceholderApp) getApplicationContext();
        eventPoster = findViewById(R.id.eventPosterImage);
        back = findViewById(R.id.eventPoster_back);
        uploadPoster = findViewById(R.id.uploadPosterButton);
        nextPage = findViewById(R.id.event_posternext);

        UUID eventID = UUID.fromString(getIntent().getStringExtra("created_event_ID"));
        Event curEvent = new Event(eventID);

        curEvent.getEventFromDatabase();

        final Uri[] curPic = new Uri[1]; //this is so we can upload to database

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri == null){
                Log.d("PhotoPicker", "Selected URI: " + uri);
            }   else {

                Log.d("PhotoPicker", "No media selected");
                eventPoster.setImageURI(uri);
                nextPage.setVisibility(View.VISIBLE);

                curPic[0] = uri;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        uploadPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE) // For images only
                        .build();
                pickMedia.launch(request);
            }
        });

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.getPosterImageHandler().uploadPoster(curPic[0], curEvent); //updates the event

                Intent i = new Intent(uploadPosterActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });







    }
}
