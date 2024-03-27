package ca.cmput301t05.placeholder.ui.events;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.UUID;

import ca.cmput301t05.placeholder.MainActivity;

import ca.cmput301t05.placeholder.Location.MapDisplay_activity;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.ui.events.creation.EnterEventDetailsActivity;
import ca.cmput301t05.placeholder.ui.events.organizer_info.ViewAttendeeCheckinActivity;
import ca.cmput301t05.placeholder.ui.events.organizer_info.ViewSignUpsActivity;
import ca.cmput301t05.placeholder.ui.notifications.EventNotificationPageActivity;


public class EventMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView eventName;
    private ImageView eventPoster;
<<<<<<< HEAD
    private Button editEvent;
    private Button checkSignUps;
    private Button accessQRCodes;
    private Button menuAnnouncements;
    private Button menuAttendance;
    private Button menuMilestones;
    private Button checkInLocations;
    private Button backButton;
=======
>>>>>>> fcfa63012ec86b4d6d775ced33c062ff85f2d6b3


    private Button buttonBack;
    private TextView textViewEventDate;
    private TextView textViewEventLocation;
    private TextView textViewEventDetails;
    private TextView textViewEventAuthor;

    PlaceholderApp app;
    Event curEvent;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    Intent fromIntent;


    @SuppressLint({"MissingInflatedId", "NonConstantResourceId"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (PlaceholderApp) getApplicationContext();
        curEvent = app.getCachedEvent();
        setContentView(R.layout.event_menu);
        buttonBack = findViewById(R.id.event_menu_back);
        fromIntent = getIntent();

<<<<<<< HEAD
        eventName = findViewById(R.id.event_menu_name);
        eventPoster = findViewById(R.id.event_menu_poster);
        editEvent = findViewById(R.id.edit_event);
        checkSignUps = findViewById(R.id.check_signUps);
        accessQRCodes = findViewById(R.id.access_qr_codes);
        menuAnnouncements = findViewById(R.id.menu_announcements);
        menuAttendance = findViewById(R.id.menu_attendance);
        menuMilestones = findViewById(R.id.menu_milestones);
        checkInLocations = findViewById(R.id.check_in_locations);
        backButton = findViewById(R.id.event_menu_back);
=======
        drawerLayout = findViewById(R.id.event_menu_drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.menu_nav);
        setSupportActionBar(toolbar);

        // Set up hamburger icon
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //navigation actions
        navigationView.setNavigationItemSelectedListener(this);



        setEventDetails();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventMenuActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
>>>>>>> fcfa63012ec86b4d6d775ced33c062ff85f2d6b3

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

<<<<<<< HEAD
        menuAnnouncements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventMenuActivity.this, EventNotificationPageActivity.class);
                startActivity(i);
            }
        });

        accessQRCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventMenuActivity.this, ViewQRCodesActivity.class);
                startActivity(intent);
            }
        });

        menuAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventMenuActivity.this, ViewAttendeeCheckinActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        checkInLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventMenuActivity.this, MapDisplay_activity.class);
                startActivity(intent);
            }
        });

        checkSignUps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventMenuActivity.this, ViewSignUpsActivity.class);
                startActivity(intent);
            }
        });



=======
>>>>>>> fcfa63012ec86b4d6d775ced33c062ff85f2d6b3

    }

    private void setEventDetails() {
        //curEvent = app.getCachedEvent();
        eventName = findViewById(R.id.event_menu_name);
        textViewEventDate = findViewById(R.id.event_menu_eventDate);
        textViewEventLocation = findViewById(R.id.event_preview_eventlocation);
        textViewEventDetails = findViewById(R.id.event_preview_eventinfo);
        textViewEventAuthor = findViewById(R.id.event_preview_author);
        eventPoster = findViewById(R.id.event_menu_poster);

        String dateTime = generateEventDateTime();
        eventName.setText(curEvent.getEventName());
        textViewEventDate.setText(dateTime);
        textViewEventLocation.setText(curEvent.getLocation());
        textViewEventDetails.setText(curEvent.getEventInfo());

        UUID profileId = curEvent.getEventCreator();
        app.getProfileTable().fetchDocument(profileId.toString(), new Table.DocumentCallback<Profile>() {

            @Override
            public void onSuccess(Profile document) {
                textViewEventAuthor.setText(document.getName());
            }

            @Override
            public void onFailure(Exception e) {
            }
        });
      
        if (curEvent.hasEventPosterBitmap()) {
            eventPoster.setImageBitmap(curEvent.getEventPosterBitmap());
        } else {
            // This should never be executed, since the poster has not been uploaded yet
            app.getPosterImageHandler().getPosterPicture(curEvent, this, new BaseImageHandler.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    eventPoster.setImageBitmap(bitmap);
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
        if (curEvent != null) {
            Calendar eventCalendar = curEvent.getEventDate();
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
        } else {
            return "Event date is not available";
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        app.setCachedEvent(curEvent);
        int itemId = item.getItemId();
        if (itemId == R.id.edit) {
            Intent intentEdit = new Intent(EventMenuActivity.this, EnterEventDetailsActivity.class);
            intentEdit.putExtra("edit", true);
            startActivity(intentEdit);
        } else if (itemId == R.id.accessqr) {
            Intent intentQR = new Intent(EventMenuActivity.this, ViewQRCodesActivity.class);
            startActivity(intentQR);
        } else if (itemId == R.id.announcements) {
            Intent intentAnnouncement = new Intent(EventMenuActivity.this, EventNotificationPageActivity.class);
            startActivity(intentAnnouncement);
        } else if (itemId == R.id.attendance) {
            Intent intentAttendance = new Intent(EventMenuActivity.this, ViewAttendeeCheckinActivity.class);
            startActivity(intentAttendance);
        } else if (itemId == R.id.check_in_locations) {
            Intent intentLoc = new Intent(EventMenuActivity.this, MapDisplay_activity.class);
            startActivity(intentLoc);
        } else if (itemId == R.id.milestones) {
            Intent intentMiles = new Intent(EventMenuActivity.this, ViewMilestonesActivity.class);
            startActivity(intentMiles);
        }



            // Handle more items as needed
            return true;

    }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Pass the event to ActionBarDrawerToggle
            if (toggle.onOptionsItemSelected(item)) {
                return true;
            }

            // Handle your other action bar items here if needed

            return super.onOptionsItemSelected(item);
        }




}


