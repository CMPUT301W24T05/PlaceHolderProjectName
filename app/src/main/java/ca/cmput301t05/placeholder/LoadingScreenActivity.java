package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.utils.datafetchers.DataFetchCallback;
import ca.cmput301t05.placeholder.utils.datafetchers.EventFetcher;
import ca.cmput301t05.placeholder.utils.datafetchers.ProfileFetcher;

/**
 * LoadingScreenActivity is an activity displayed during the startup of the application. It is responsible for
 * determining whether a user profile exists for the current device. If a profile exists, it transitions to the
 * MainActivity. Otherwise, it directs the user to the InitialSetupActivity to create a new profile.
 */
public class LoadingScreenActivity extends AppCompatActivity implements DataFetchCallback {

    PlaceholderApp app;
    private ProfileFetcher profileFetcher;
    private EventFetcher eventFetcher;

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
        Intent intent = new Intent(getApplicationContext(), InitialSetupActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNoIdFound() {
        // If no id found, navigate to setup activity
        Intent intent = new Intent(getApplicationContext(), InitialSetupActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onEventFetched(Profile profile) {
        // Events fetched successfully. Navigate to MainActivity
        startMainActivity();
    }

    @Override
    public void onEventFetchError(Exception exception) {
        // Events fetched unsuccessfully, it's *probably* fine lol. Navigate to MainActivity
        startMainActivity();
    }
}
