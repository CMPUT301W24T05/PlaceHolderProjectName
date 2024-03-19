package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.profile.ProfileImageGenerator;

/**
 * LoadingScreenActivity is an activity displayed during the startup of the application. It is responsible for
 * determining whether a user profile exists for the current device. If a profile exists, it transitions to the
 * MainActivity. Otherwise, it directs the user to the InitialSetupActivity to create a new profile.
 */
public class LoadingScreenActivity extends AppCompatActivity {

    PlaceholderApp app;

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

        // Check if there are no events to fetch in the first place, start MainActivity immediately
        if (eventCounter.get() == 0) {
            startMainActivity();
        }
    }

    private void fetchProfilePicture(Profile profile){

    }

    private void fetchEvents(Profile profile, String event, AtomicInteger eventCounter) {
        List<String> events;

        boolean hosted = event.equals("hostedEvents");

        if (hosted) {
            events = profile.getHostedEvents();
        } else {

            events = profile.getJoinedEvents();
        }

        if (events == null) {
            return;
        }

        //now load all the events into their respective container

        for (String id : events) {
            app.getEventTable().fetchDocument(id.trim(), new Table.DocumentCallback<Event>() {
                @Override
                public void onSuccess(Event document) {

                    if (hosted) {
                        app.getHostedEvents().put(UUID.fromString(id.trim()), document);
                    } else {
                        app.getJoinedEvents().put(UUID.fromString(id.trim()), document);
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

    private void startMainActivity() {
        Log.i("Placeholder App", String.format("Profile with id %s and name %s has been loaded!",
                  app.getUserProfile().getProfileID(), app.getUserProfile().getName()));
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
