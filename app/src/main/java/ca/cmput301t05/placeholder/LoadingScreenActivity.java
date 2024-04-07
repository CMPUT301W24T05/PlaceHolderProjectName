package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import ca.cmput301t05.placeholder.database.firebaseMessaging.notificationHandler.HttpNotificationHandler;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.Milestone;
import ca.cmput301t05.placeholder.notifications.MilestoneType;
import ca.cmput301t05.placeholder.notifications.Notification;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.profile.ProfileImageGenerator;

import ca.cmput301t05.placeholder.utils.holdNotiEvent;


/**
 * LoadingScreenActivity is an activity displayed during the startup of the application. It is responsible for
 * determining whether a user profile exists for the current device. If a profile exists, it transitions to the
 * MainActivity. Otherwise, it directs the user to the InitialSetupActivity to create a new profile.
 */
public class LoadingScreenActivity extends AppCompatActivity {

    PlaceholderApp app;
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

        fetchProfileAndContinue();
    }

    /**
     * Attempts to fetch the user profile associated with the device's ID. If the device does not have a stored
     * ID or the profile cannot be found in the database, the user is redirected to InitialSetupActivity to
     * allow profile creation. If a profile is found, the application proceeds to the MainActivity.
     */
    private void fetchProfileAndContinue() {
        if (!app.getIdManager().deviceHasIDStored()) {
            Intent intent = new Intent(getApplicationContext(), InitialSetupActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        UUID deviceId = app.getIdManager().getDeviceID();
        app.getProfileTable().fetchDocument(deviceId.toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                // The profile exists in firebase! We can continue to the Main activity
                app.setUserProfile(profile);

                app.getProfileImageHandler().getProfilePicture(profile, LoadingScreenActivity.this, new BaseImageHandler.ImageCallback() {
                    @Override
                    public void onImageLoaded(Bitmap bitmap) {
                        profile.setProfilePictureBitmap(bitmap);
                        fetchEvents(profile);
                    }

                    @Override
                    public void onError(Exception e) {
                        profile.setProfilePictureToDefault();
                        fetchEvents(profile);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                // The profile does not exist in firebase, let's ask for the name of the user
                Log.e("App/ProfileFetch", e.toString());
                Intent intent = new Intent(getApplicationContext(), InitialSetupActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void fetchEvents(Profile profile) {
        AtomicInteger eventCounter = new AtomicInteger();

        if (profile.getHostedEvents() != null) {
            eventCounter.addAndGet(profile.getHostedEvents().size());
            fetchEvents(profile, "hostedEvents", eventCounter);
        }
        if (profile.getJoinedEvents() != null) {
            eventCounter.addAndGet(profile.getJoinedEvents().size());
            fetchEvents(profile, "joinedEvents", eventCounter);
        }
        if (profile.getInterestedEvents() != null) {
            eventCounter.addAndGet(profile.getInterestedEvents().size());
            fetchEvents(profile, "interestedEvents", eventCounter);
        }

        // Check if there are no events to fetch in the first place, start MainActivity immediately
        if (eventCounter.get() == 0) {
            fetchNotifications(profile);
        }
    }

    private void fetchEvents(Profile profile, String event, AtomicInteger eventCounter) {
        List<String> events;

        switch (event) {
            case "hostedEvents":
                events = profile.getHostedEvents();
                break;
            case "joinedEvents":
                events = profile.getJoinedEvents();
                break;
            case "interestedEvents":
                events = profile.getInterestedEvents();
                break;
            default:
                return; // Method invoked with an invalid event string
        }

        if (events == null) {
            return;
        }

        //now load all the events into their respective container

        for (String id : events) {
            app.getEventTable().fetchDocument(id.trim(), new Table.DocumentCallback<Event>() {
                @Override
                public void onSuccess(Event document) {

                    switch (event) {
                        case "hostedEvents":
                            app.getHostedEvents().put(UUID.fromString(id.trim()), document);
                            break;
                        case "joinedEvents":
                            app.getJoinedEvents().put(UUID.fromString(id.trim()), document);
                            break;
                        case "interestedEvents":
                            app.getInterestedEvents().put(UUID.fromString(id.trim()), document);
                            break;
                    }

                    // Decrease the counter once an event is fetched
                    eventCounter.decrementAndGet();
                    // If all events have been fetched, start MainActivity
                    if (eventCounter.get() == 0) {
                        startMainActivity();
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    //TODO handle failure
                }
            });

        }
    }

    private void fetchNotifications(Profile profile) {
        app.getNotificationTable().fetchMultipleDocuments(profile.getNotifications(), new Table.DocumentCallback<ArrayList<Notification>>() {
            @Override
            public void onSuccess(ArrayList<Notification> document) {
                app.getUserNotifications().addAll(document);

                //this is for loading notifications easily for user notifications
                app.setNotificationEventHolder(holdNotiEvent.hashQuickList(document, app.getJoinedEvents()));

                startMainActivity();
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }

    private void startMainActivity() {
        // sets milestones for each event
        setMilestones();
        Log.i("Placeholder App", String.format("Profile with id %s and name %s has been loaded!",
                app.getUserProfile().getProfileID(), app.getUserProfile().getName()));
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Sets the milestones for all the events in the application.
     */
    private void setMilestones() {
        ArrayList<Event> myEvents = new ArrayList<>(app.getHostedEvents().values());

        for (Event e : myEvents) {
            checkMilestones(e);
        }
    }

    /**
     * Checks various milestones for a given event and adds them to the application if they are present.
     *
     * @param curEvent the event to check milestones for
     */
    private void checkMilestones(Event curEvent) {
        numAttendees = curEvent.getAttendees().size();
        capacity = curEvent.getMaxAttendees();
        numRegistered = curEvent.getRegisteredUsers().size();
        now = Calendar.getInstance();
        cal = curEvent.getEventDate();

        addMilestoneIfPresent(getMilestoneByCondition((double) numAttendees / capacity >= 0.5, MilestoneType.HALFWAY, curEvent));
        addMilestoneIfPresent(getMilestoneByCondition(capacity == numAttendees, MilestoneType.FULLCAPACITY, curEvent));
        addMilestoneIfPresent(getMilestoneByCondition(numAttendees >= 1, MilestoneType.FIRSTATTENDEE, curEvent));
        addMilestoneIfPresent(getMilestoneByCondition(now.compareTo(cal) > 0, MilestoneType.EVENTSTART, curEvent));
        addMilestoneIfPresent(getMilestoneByCondition(numRegistered >= 1, MilestoneType.FIRSTSIGNUP, curEvent));
    }

    /**
     * Private method to get a milestone based on a condition, milestone type, and event.
     *
     * @param condition a boolean value representing the condition for the milestone
     * @param type the milestone type
     * @param curEvent the event object
     * @return an Optional object containing the milestone if the condition is met and milestone type is not already present, otherwise an empty Optional object
     */
    private Optional<Milestone> getMilestoneByCondition(boolean condition, MilestoneType type, Event curEvent) {
        return !containsMilestoneType(type) && condition ?
                Optional.of(new Milestone(app.getUserProfile().getProfileID(), curEvent.getEventID(), type, curEvent.getEventName())) :
                Optional.empty();
    }

    /**
     * Adds a milestone to the application if it is present.
     *
     * @param milestone an Optional object that represents a milestone event
     */
    private void addMilestoneIfPresent(Optional<Milestone> milestone) {
        milestone.ifPresent(this::addMilestone);
    }


    /**
     * Checks if the given MilestoneType is present in the milestones list.
     *
     * @param type the MilestoneType to check
     * @return true if the MilestoneType is present in the milestones list, false otherwise
     */
    public boolean containsMilestoneType(MilestoneType type) {
        return milestones != null && milestones.stream().anyMatch(milestone -> milestone.getMType() == type);
    }

    public void addMilestone(Milestone milestone) {
        //push to notification database
        Profile profile = app.getUserProfile();
        String token = profile.getMessagingToken();
        HttpNotificationHandler.sendNotificationToUser(milestone, token, new HttpNotificationHandler.httpHandlercallback() {
            @Override
            public void onSuccess() {
                app.getUserMilestones().add(milestone);
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }
}