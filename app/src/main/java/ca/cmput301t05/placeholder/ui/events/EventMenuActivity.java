package ca.cmput301t05.placeholder.ui.events;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import ca.cmput301t05.placeholder.Location.MapDisplay_activity;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.EventTable;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.ui.events.creation.EnterEventDetailsActivity;
import ca.cmput301t05.placeholder.ui.events.organizer_info.ViewAttendeeCheckinActivity;
import ca.cmput301t05.placeholder.ui.events.organizer_info.ViewSignUpsActivity;
import ca.cmput301t05.placeholder.ui.notifications.EventNotificationPageActivity;


public class EventMenuActivity extends AppCompatActivity  {

    private TextView eventName;
    private ImageView eventPoster;

    private CardView signIns;
    private CardView editEvent;

    private CardView viewQRCodes;

    private CardView viewAnnouncments;

    private CardView mileStones;

    private CardView viewLocations;




    private TextView attendeeFraction;
    private TextView attendeeCount;
    private Button buttonBack;
    private TextView textViewEventDate;
    private TextView textViewEventLocation;
    private TextView textViewEventDetails;
    private TextView textViewEventAuthor;

    private Button viewAttendanceButton;

    PlaceholderApp app;
    Event curEvent;
    DrawerLayout drawerLayout;
    private EventTable eventTable;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    Intent fromIntent;

    private CircularProgressIndicator circularProgressBar;
    private int currentProgress; // Current progress value
    private int totalProgress; // Total progress value


    @SuppressLint({"MissingInflatedId", "NonConstantResourceId"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (PlaceholderApp) getApplicationContext();
        curEvent = app.getCachedEvent();
        setContentView(R.layout.event_menu_updated);
        eventTable = app.getEventTable();

        toolbar = findViewById(R.id.toolbarEventMenu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(EventMenuActivity.this, MainActivity.class);
//                startActivity(i);

                finish();
            }
        });

        circularProgressBar = findViewById(R.id.progress_bar_event);
        eventTable.fetchDocument(String.valueOf(curEvent.getEventID()),  new Table.DocumentCallback<Event>() {
                    @Override
                    public void onSuccess(Event document) {

                            totalProgress = document.getMaxAttendees();
                            currentProgress = document.getNumAttendees();
                            Bitmap posterPic = document.getEventPosterBitmap();
                            setUpButtons();
                            setUpText();
                            setUpPoster(document);
                            circularProgressBar = findViewById(R.id.progress_bar_event);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    animateProgress(currentProgress, totalProgress);
                                }
                            }, 350);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("amirza2", "FAILED TO GET EVENT DATA IN eventMenuActivity!");

                    }

        });
    }

    private void setUpPoster(Event event){
        Log.e("amirza2", "SET UP THIS METHOD");
        eventPoster = findViewById(R.id.imageView_event_menu);
        if (event.hasEventPosterBitmap()) {
            eventPoster.setImageBitmap(event.getEventPosterBitmap());
        } else {
            app.getPosterImageHandler().getPosterPicture(event, getApplicationContext(), new BaseImageHandler.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    eventPoster.setImageBitmap(bitmap);
                }

                @Override
                public void onError(Exception e) {
                    Log.e("Hoster_Event_card View", "Error loading image: " + e.getMessage());
                }
            });
        }
    }

    private void animateProgress(int current, int total) {
        // Calculate the progress percentage
        int progressPercentage = (int) (((float) current / total) * 100);

        // Set up a runnable to animate the progress change
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Animate the progress change
                circularProgressBar.setProgressCompat(progressPercentage, true);
            }
        };

        // Start the animation
        circularProgressBar.post(runnable);
    }



    /**
     * Sets up the textview to be displayed when this activity is called. Clicking on the text
     * will direct user to new activity to view a list of their event's attendees.
     *
     */

    private void setUpText(){

        attendeeFraction = findViewById(R.id.attendanceFraction);
        attendeeFraction.setText(currentProgress+"/"+totalProgress);

        Drawable image = EventMenuActivity.this.getDrawable( R.drawable.baseline_attendance_24 );
        int h = image.getIntrinsicHeight();
        int w = image.getIntrinsicWidth();
        image.setBounds( 0, 0, w, h );
        attendeeFraction.setCompoundDrawables( image , null, null, null );
        attendeeFraction.setCompoundDrawablePadding(5);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        attendeeFraction.setLayoutParams(params);

        attendeeFraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventMenuActivity.this, ViewAttendeeCheckinActivity.class);
                startActivity(intent);
            }
        });
        
    }


    private void setUpButtons(){
        signIns = findViewById(R.id.eventMCard1);

        signIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EventMenuActivity.this, ViewSignUpsActivity.class);
                startActivity(intent);
            }
        });

        editEvent = findViewById(R.id.eventMCard2);

        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentEdit = new Intent(EventMenuActivity.this, EnterEventDetailsActivity.class);
                intentEdit.putExtra("edit", true);
                startActivity(intentEdit);

            }
        });

        viewQRCodes = findViewById(R.id.eventMCard3);

// Set OnClickListener to launch ViewQRCodesActivity
        viewQRCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentQR = new Intent(EventMenuActivity.this, ViewQRCodesActivity.class);
                startActivity(intentQR);
            }
        });

// Set OnClickListener to launch ReuseQRActivity
        viewQRCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventMenuActivity.this, ReuseQRActivity.class);
                intent.putExtra("eventName", curEvent.getEventName());
                startActivity(intent);
            }
        });



        viewAnnouncments = findViewById(R.id.eventMCard4);

        viewAnnouncments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentAnnouncement = new Intent(EventMenuActivity.this, EventNotificationPageActivity.class);
                startActivity(intentAnnouncement);
            }
        });

        mileStones = findViewById(R.id.eventMCard5);

        mileStones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent intentMiles = new Intent(EventMenuActivity.this, ViewMilestonesActivity.class);
            startActivity(intentMiles);
            }
        });

       viewLocations = findViewById(R.id.eventMCard6);

        viewLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoc = new Intent(EventMenuActivity.this, MapDisplay_activity.class);
                startActivity(intentLoc);
            }
        });
    }

}