package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.milestones.MilestoneConditions;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.Milestone;
import ca.cmput301t05.placeholder.notifications.Notification;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.utils.datafetchers.DataFetchCallback;
import ca.cmput301t05.placeholder.utils.datafetchers.EventFetcher;
import ca.cmput301t05.placeholder.utils.datafetchers.ProfileFetcher;

import ca.cmput301t05.placeholder.utils.HoldNotificationToEvent;

import java.util.ArrayList;


/**
 * LoadingScreenActivity is an activity displayed during the startup of the application. It is responsible for
 * determining whether a user profile exists for the current device. If a profile exists, it transitions to the
 * MainActivity. Otherwise, it directs the user to the InitialSetupActivity to create a new profile.
 */
public class LoadingScreenActivity extends AppCompatActivity implements DataFetchCallback {

    private static final String TAG = "PlaceholderAppLoadingScreen";

    PlaceholderApp app;
    private ProfileFetcher profileFetcher;
    private EventFetcher eventFetcher;
    ArrayList<Notification> notifications;
    ArrayList<Milestone> milestones;
    int numAttendees;
    int capacity;
    int numRegistered;
    Calendar now;
    Calendar cal;

    /**
     * Called when the activity is starting. This method sets the content view to the loading screen layout
     * and initiates the process of fetching the user's profile based on the device ID.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied. Otherwise, it is null.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        app = (PlaceholderApp) getApplicationContext();
        milestones = app.getUserMilestones();

        profileFetcher = app.getProfileFetcher();
        eventFetcher = app.getEventFetcher();

        profileFetcher.addCallback(this);
        eventFetcher.addCallback(this);

        profileFetcher.fetchProfileIfDeviceIdExists();
    }

    @Override
    protected void onDestroy() {
        profileFetcher.removeCallback(this);
        eventFetcher.removeCallback(this);
        super.onDestroy();
    }

    private void startMainActivity() {
        Log.i("Placeholder App", String.format("Profile with id %s and name %s has been loaded!",
                app.getUserProfile().getProfileID(), app.getUserProfile().getName()));
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onProfileFetched(Profile profile) {
        // Profile fetched successfully. To fetch events, start the method of eventFetcher
        eventFetcher.fetchAllEvents(profile);
    }

    @Override
    public void onPictureLoaded(Bitmap bitmap) {

    }

    @Override
    public void onProfileFetchFailure(Exception exc) {
        // If profile fetch failed, navigate to setup activity
        Log.e(TAG, "Failed to fetch the profile", exc);
        Intent intent = new Intent(getApplicationContext(), InitialSetupActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNoIdFound() {
        // If no id found, navigate to setup activity
        Log.e(TAG, "No ID found");
        Intent intent = new Intent(getApplicationContext(), InitialSetupActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onEventFetched(Profile profile) {
        // Events fetched successfully. Navigate to MainActivity
        milestoneHandling();
        fetchNotifications(profile);
    }

    @Override
    public void onEventFetchError(Exception exception) {
        // Events fetched unsuccessfully, it's *probably* fine lol. Navigate to MainActivity
        Log.e(TAG, "Failed to fetch events", exception);
        fetchNotifications(app.getUserProfile());
    }

    private void fetchNotifications(Profile profile) {


        app.getNotificationTable().fetchMultipleDocuments(profile.getNotifications(), new Table.DocumentCallback<ArrayList<Notification>>() {
            @Override
            public void onSuccess(ArrayList<Notification> document) {
                app.getUserNotifications().addAll(document);

                //this is for loading notifications easily for user notifications
                app.setNotificationEventHolder(HoldNotificationToEvent.hashQuickList(document, app.getJoinedEvents()));

                startMainActivity();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Failed to fetch notifications", e);
                startMainActivity();
            }
        });
    }


    private void milestoneHandling(){

        HashMap<UUID, Event> hostedEvents = app.getHostedEvents(); //called after hosted events are updated

        for (Event e : hostedEvents.values()){

            MilestoneConditions.milestoneHandling(app, e, new MilestoneConditions.milestoneCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("MILESTONE_HANDLING", e.getMessage());
                }
            });

        }

    }

}