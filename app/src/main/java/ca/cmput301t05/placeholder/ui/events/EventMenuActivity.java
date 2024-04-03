package ca.cmput301t05.placeholder.ui.events;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import ca.cmput301t05.placeholder.MainActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import ca.cmput301t05.placeholder.Location.MapDisplay_activity;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.EventTable;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
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
                finish(); // Handle back button click event
            }
        });

        viewAttendanceButton = findViewById(R.id.editEventViewAttendance);
        viewAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventMenuActivity.this, ViewAttendeeCheckinActivity.class);
                startActivity(intent);
            }
        });


        circularProgressBar = findViewById(R.id.progress_bar_event);
//        circularProgressBar.setIndicatorInset(40);
        Log.e("amirza2", String.valueOf(circularProgressBar.getIndicatorInset()));

        eventTable.fetchDocument(String.valueOf(curEvent.getEventID()),  new Table.DocumentCallback<Event>() {
                    @Override
                    public void onSuccess(Event document) {
                            totalProgress = document.getMaxAttendees();
                            currentProgress = document.getNumAttendees();
                            setUpButtons();
                            setUpText();
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

                    }

        });

//        buttonBack = findViewById(R.id.event_menu_back);
//        fromIntent = getIntent();



        //SETTING UP THE POSTER PIC- may use later
//        if (curEvent.hasEventPosterBitmap()) {
//            eventPoster.setImageBitmap(curEvent.getEventPosterBitmap());
//        } else {
//            app.getPosterImageHandler().getPosterPicture(curEvent, this, new BaseImageHandler.ImageCallback() {
//                @Override
//                public void onImageLoaded(Bitmap bitmap) {
//                    eventPoster.setImageBitmap(bitmap);
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    // Handle error
//                    Log.e("EventDetailsDialogFragment", "Error loading image: " + e.getMessage());
//                }
//            });
//        }

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

    private void setUpText(){
        attendeeCount = findViewById(R.id.textViewNumOfAttendees);
        if (currentProgress != 1) {
            attendeeCount.setText("You have " + currentProgress + " attendees");
        }
        else {
            attendeeCount.setText("You have " + currentProgress + " attendee");
        }
        attendeeFraction = findViewById(R.id.attendanceFraction);
        attendeeFraction.setText(currentProgress+"/"+totalProgress);
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

        viewQRCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentQR = new Intent(EventMenuActivity.this, ViewQRCodesActivity.class);
                startActivity(intentQR);
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

//    private void setEventDetails() {
//        //curEvent = app.getCachedEvent();
//        eventName = findViewById(R.id.event_menu_name);
//        textViewEventDate = findViewById(R.id.event_menu_eventDate);
//        textViewEventLocation = findViewById(R.id.event_preview_eventlocation);
//        textViewEventDetails = findViewById(R.id.event_preview_eventinfo);
//        textViewEventAuthor = findViewById(R.id.event_preview_author);
//        eventPoster = findViewById(R.id.event_menu_poster);
//
//        String dateTime = generateEventDateTime();
//        eventName.setText(curEvent.getEventName());
//        textViewEventDate.setText(dateTime);
//        textViewEventLocation.setText(curEvent.getLocation());
//        textViewEventDetails.setText(curEvent.getEventInfo());
//
//        UUID profileId = curEvent.getEventCreator();
//        app.getProfileTable().fetchDocument(profileId.toString(), new Table.DocumentCallback<Profile>() {
//
//            @Override
//            public void onSuccess(Profile document) {
//                textViewEventAuthor.setText(document.getName());
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//            }
//        });
//
//        if (curEvent.hasEventPosterBitmap()) {
//            eventPoster.setImageBitmap(curEvent.getEventPosterBitmap());
//        } else {
//            // This should never be executed, since the poster has not been uploaded yet
//            app.getPosterImageHandler().getPosterPicture(curEvent, this, new BaseImageHandler.ImageCallback() {
//                @Override
//                public void onImageLoaded(Bitmap bitmap) {
//                    eventPoster.setImageBitmap(bitmap);
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    // Handle error
//                    Log.e("PreviewEventActivity", "Error loading image: " + e.getMessage());
//                }
//            });
//        }
//    }

//    private String generateEventDateTime() {
//        if (curEvent != null) {
//            Calendar eventCalendar = curEvent.getEventDate();
//            int year = eventCalendar.get(Calendar.YEAR);
//            int month = eventCalendar.get(Calendar.MONTH) + 1; //January is 0
//            int day = eventCalendar.get(Calendar.DAY_OF_MONTH);
//            // Or get the hour for 12-hour format
//            int hour12 = eventCalendar.get(Calendar.HOUR);
//            // Get AM or PM
//            int amOrPm = eventCalendar.get(Calendar.AM_PM);
//            String timePeriod = amOrPm == Calendar.AM ? " AM" : " PM";
//
//            String time = hour12 + timePeriod;
//            String date = day + ", " + month + ", " + year;
//            return time + " - " + date;
//        } else {
//            return "Event date is not available";
//        }
//
//    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        app.setCachedEvent(curEvent);
//        int itemId = item.getItemId();
//        if (itemId == R.id.edit) {
//            Intent intentEdit = new Intent(EventMenuActivity.this, EnterEventDetailsActivity.class);
//            intentEdit.putExtra("edit", true);
//            startActivity(intentEdit);
//        } else if (itemId == R.id.accessqr) {
//            Intent intentQR = new Intent(EventMenuActivity.this, ViewQRCodesActivity.class);
//            startActivity(intentQR);
//        } else if (itemId == R.id.announcements) {
//            Intent intentAnnouncement = new Intent(EventMenuActivity.this, EventNotificationPageActivity.class);
//            startActivity(intentAnnouncement);
//        } else if (itemId == R.id.attendance) {
//            Intent intentAttendance = new Intent(EventMenuActivity.this, ViewAttendeeCheckinActivity.class);
//            startActivity(intentAttendance);
//        }
//        else if (itemId == R.id.registry){
//            Intent intentAttendance = new Intent(EventMenuActivity.this,ViewSignUpsActivity.class);
//            startActivity(intentAttendance);
//        }
//        else if (itemId == R.id.check_in_locations) {
//            Intent intentLoc = new Intent(EventMenuActivity.this, MapDisplay_activity.class);
//            startActivity(intentLoc);
//        } else if (itemId == R.id.milestones) {
//            Intent intentMiles = new Intent(EventMenuActivity.this, ViewMilestonesActivity.class);
//            startActivity(intentMiles);
//        }
//
//
//
//        // Handle more items as needed
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected (MenuItem item){
//        // Pass the event to ActionBarDrawerToggle
//        if (toggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//
//        // Handle your other action bar items here if needed
//
//        return super.onOptionsItemSelected(item);
//    }




}