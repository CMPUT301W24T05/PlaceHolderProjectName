package ca.cmput301t05.placeholder.ui.events;

import android.annotation.SuppressLint;
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

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.ui.events.creation.EnterEventDetailsActivity;

public class EventMenuActivity extends AppCompatActivity {

    private TextView eventName;
    private ImageView eventPoster;
    private Button editEvent;
    private Button checkRegister;
    private Button accessQRCodes;
    private Button menuAnnouncements;
    private Button menuAttendance;
    private Button menuMilestones;
    private Button checkInLocations;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlaceholderApp app = (PlaceholderApp) getApplicationContext();
        Event curEvent = app.getCachedEvent();

        setContentView(R.layout.event_menu);

        eventName = findViewById(R.id.event_menu_name);
        eventPoster = findViewById(R.id.event_menu_poster);
        editEvent = findViewById(R.id.edit_event);
        checkRegister = findViewById(R.id.check_register);
        accessQRCodes = findViewById(R.id.access_qr_codes);
        menuAnnouncements = findViewById(R.id.menu_announcements);
        menuAttendance = findViewById(R.id.menu_attendance);
        menuMilestones = findViewById(R.id.menu_milestones);
        checkInLocations = findViewById(R.id.check_in_locations);


        eventName.setText(curEvent.getEventName());
        if (curEvent.hasEventPosterBitmap()) {
            eventPoster.setImageBitmap(curEvent.getEventPosterBitmap());
        } else {
            app.getPosterImageHandler().getPosterPicture(curEvent, this, new BaseImageHandler.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    eventPoster.setImageBitmap(bitmap);
                }

                @Override
                public void onError(Exception e) {
                    // Handle error
                    Log.e("EventDetailsDialogFragment", "Error loading image: " + e.getMessage());
                }
            });
        }

        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventMenuActivity.this, EnterEventDetailsActivity.class);
                intent.putExtra("edit", true);
                startActivity(intent);
            }
        });

        accessQRCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventMenuActivity.this, ViewQRCodesActivity.class);
                startActivity(intent);
            }
        });

    }
}
