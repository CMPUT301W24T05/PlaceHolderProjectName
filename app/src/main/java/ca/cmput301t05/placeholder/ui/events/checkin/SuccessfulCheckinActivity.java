package ca.cmput301t05.placeholder.ui.events.checkin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import ca.cmput301t05.placeholder.Location.LocationManager;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.EventTable;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.ui.events.ViewEventDetailsFragment;

import java.util.List;

/**
 * This class represents an activity that is displayed after a successful check-in process.
 * It extends the AppCompatActivity class and implements the LocationManager.LocationPermissionListener interface.
 * The SuccessfulCheckinActivity class is responsible for handling the check-in process,
 * updating the database, and navigating to the event details activity.
 *
 * @see LocationManager.LocationPermissionListener
 */
public class SuccessfulCheckinActivity extends AppCompatActivity implements LocationManager.LocationPermissionListener {
    public static final String SUCCESSFUL_CHECKIN_KEY = "SuccessfulCheckinKey";
    public static final String SUCCESSFUL_CHECKIN_VALUE = "SUCCESS_CHECKIN";
    private static final int SPLASH_DELAY = 3000;
    private LocationManager locationManager;
    private PlaceholderApp app;
    private Button next_button;
    private CheckBox shareLocation;
    private Event event;
    private Profile profile;
    private double latitude;
    private double longitude;
    private EventTable eventTable;
    private ImageView animated_checkmark;
    private static final String LOCATION_SHARED_TOAST = "Location Shared";
    private static final String LOCATION_NOT_SHARED_TOAST = "Location Not Shared";

    /**
     * Called when the activity is starting. This method initializes the app, sets up the button click handling,
     * and checks and handles the event max capacity.
     *
     * @param savedInstanceState The last saved instance state of the activity, or null if this is the first creation.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeApp();
        setupButtonClickHandling();
        checkAndHandleEventMaxCapacity();
        imageViewAnimation(); // Check mark animation
    }

    /**
     * Overrides the {@code onStart} method of the parent class. This method is called when the activity is starting.
     * This method checks the permission for accessing fine location. If the permission is granted,
     * it calls the {@code getLastLocation} method of the {@code locationManager} object to retrieve the last known location.
     * If the location is not null, it calls the {@code handleLocationReceived} method with the location as a parameter.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.getLastLocation(new LocationManager.LocationCallback() {
                @Override
                public void onLocationReceived(Location location) {
                    if (location != null) {
                        handleLocationReceived(location);
                    }
                }
            });
        }
    }

    /**
     * Function handles the check mark animation.
     */
    private void imageViewAnimation(){
        animated_checkmark = findViewById(R.id.imageViewSuccessCheckin);
        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) animated_checkmark.getDrawable();
        animatedVectorDrawable.start();
    }

    /**
     * Initializes the app by setting up various fields and views.
     * <p>
     * This method initializes the {@code app} field by getting the application context. It also sets the content view to
     * the checked_in_page layout. Additionally, it initializes the {@code locationManager} field, {@code event} field,
     * {@code profile} field, {@code eventTable} field, {@code next_button} field, and {@code shareLocation} view by
     * getting them from the app object and finding them by id in the layout.
     * </p>
     */
    private void initializeApp() {
        app = (PlaceholderApp) getApplicationContext();
        setContentView(R.layout.checked_in_page);
        locationManager = app.getLocationManager();
        event = app.getCachedEvent();
        profile = app.getUserProfile();
        eventTable = app.getEventTable();
        next_button = findViewById(R.id.go_to_event_button);
        shareLocation = findViewById(R.id.checkbox_share_location);
    }

    /**
     * Sets up the click handling for the button.
     * <p>
     * This method sets an OnClickListener for the next_button view. When the button is clicked,
     * it calls the handleButtonClick() method.
     */
    private void setupButtonClickHandling() {
        next_button.setOnClickListener(v -> handleButtonClick());
    }

    /**
     * Checks if the maximum capacity for an event has been reached and handles the scenario accordingly.
     * <p>
     * This method checks if the maximum capacity for the event has been reached by calling the reachMaxCapacity()
     * method on the event object. If the maximum capacity has not been reached, it calls the handleMaxCapacityReached() method.
     */
    private void checkAndHandleEventMaxCapacity() {
        if (event.reachMaxCapacity()) {
            handleMaxCapacityReached();
        }
    }

    /**
     * Handles the button click event.
     * <p>
     * This method checks if the shareLocation checkbox is checked.
     * If it is, it updates the database by calling the checkIn method on the event object with the profile, longitude, and latitude parameters.
     * It displays a toast message indicating that the location has been shared.
     * If the shareLocation checkbox is not checked, it calls the checkIn method on the event object with the profile, null, and null parameters.
     * It displays a toast message indicating that the location has not been shared.
     * Finally, it calls the updateProfile method to update the profile and then returns.
     * </p>
     */
    private void handleButtonClick() {
        boolean isShared = shareLocation.isChecked();
        if (isShared) {
            // if choose to share location, update database
            event.checkIn(profile, longitude, latitude);
            Toast.makeText(SuccessfulCheckinActivity.this, LOCATION_SHARED_TOAST, Toast.LENGTH_SHORT).show();
        } else {
            event.checkIn(profile, null, null);
            Toast.makeText(SuccessfulCheckinActivity.this, LOCATION_NOT_SHARED_TOAST, Toast.LENGTH_SHORT).show();
        }
        updateProfile();
    }

    /**
     * Updates the profile document in the profile table.
     * <p>
     * This method calls the updateDocument method on the profile table instance retrieved from the app object.
     * It updates the specified document with the profile object and its unique ID.
     * If the update operation is successful, it calls navigateToEventDetails().
     * If the update operation fails, it does nothing.
     * </p>
     */
    private void updateProfile(){
        //MAKE SURE NO DUPLICATES
        if (!profile.getJoinedEvents().contains(event.getEventID().toString())){
            List<String> joinedEvents = profile.getJoinedEvents();
            joinedEvents.add(event.getEventID().toString());
            app.getJoinedEvents().put(event.getEventID(), event);
            app.getProfileTable().updateDocument(profile, profile.getProfileID().toString(), new Table.DocumentCallback<Profile>() {
                @Override
                public void onSuccess(Profile document) {
                    Log.e("amirza2","Will call navigate to eventDetails");
                    navigateToEventDetails();
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    // Profile update failure, handle failure
                }
            });

        }   else {

            navigateToEventDetails();
            finish();
        }



    }

    /**
     * Navigates to the event details activity.
     * <p>
     * This method pushes the document to the Firestore collection using the event table,
     * calls the onSuccess callback method if the push operation is successful,
     * and calls the onFailure callback method if the push operation fails.
     * <p>
     * After that, it creates and starts an intent to navigate to the ViewEventDetailsActivity,
     * finishes the current activity, and takes the user to the event details activity.
     */
    private void navigateToEventDetails() {
        eventTable.pushDocument(event, event.getEventID().toString(), new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event document) {
                app.setCachedEvent(event);
                Intent data = new Intent();
                data.putExtra(SUCCESSFUL_CHECKIN_KEY, SUCCESSFUL_CHECKIN_VALUE);
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                // Event upload failed, handle failure
            }
        });
    }

    /**
     * Handles the scenario when the maximum capacity for an event has been reached.
     * This method sets the text of a TextView to indicate that the maximum capacity has been reached,
     * and hides two buttons: next_button and shareLocation.
     * It then schedules a delay using Handler's postDelayed() method to finish the current activity
     * after a specified delay defined by the SPLASH_DELAY constant.
     */
    private void handleMaxCapacityReached() {
        TextView message = findViewById(R.id.textView4);
        message.setText("Maximum Capacity has been reached!");
        next_button.setVisibility(View.INVISIBLE);
        shareLocation.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(this::finish, SPLASH_DELAY);
    }

    /**
     * Handles the result of a permission request for location.
     *
     * @param requestCode The request code passed to requestPermissions().
     * @param permissions The requested permissions. This array is not null.
     * @param grantResults The grant results for the corresponding permissions, which is either PERMISSION_GRANTED or PERMISSION_DENIED. This array is not null.
     */
    // Override method from the activity class, will call the onRequestPermissionsResult in LocationManagerClass
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationManager.onRequestPermissionsResult(requestCode, grantResults);
    }

    /**
     * Handles the scenario when the location permission is granted.
     * This method displays a toast message and calls the getLastLocation method on the locationManager object.
     * The getLastLocation method takes a LocationCallback as a parameter and will asynchronously call the onLocationReceived method
     * with the received location object if it is not null.
     */
    @Override
    public void onLocationPermissionGranted() {
        Toast.makeText(this, "onLocationPermissionGranted", Toast.LENGTH_SHORT).show();
        locationManager.getLastLocation(new LocationManager.LocationCallback() {
            @Override
            public void onLocationReceived(Location location) {
                if (location != null) {
                    handleLocationReceived(location);
                }
            }
        });
    }

    /**
     * Handles the received location.
     * This method displays a toast message with the latitude and longitude values.
     *
     * @param location The received location object
     */
    private void handleLocationReceived(Location location) {
        // testing to see my location on screen
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        String message = "Latitude: " + latitude + " Longitude: " + longitude;
        Toast.makeText(SuccessfulCheckinActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Handles the scenario when the location permission is denied.
     * This method displays a toast message, hides the shareLocation and next_button views,
     * calls the checkIn method on the event object with profile as a parameter and setting the longitude and latitude as null,
     * and then navigates to the event details activity after a delay.
     */
    @Override
    public void onLocationPermissionDenied() {
        Toast.makeText(this, "onLocationPermissionDenied", Toast.LENGTH_SHORT).show();
        shareLocation.setVisibility(View.INVISIBLE);
        next_button.setVisibility(View.INVISIBLE);
        event.checkIn(profile, null, null);
        navigateToEventDetailsAfterDelay();
    }

    /**
     * Navigates to the event details activity after a specified delay.
     * This method uses a Handler to post a runnable that calls the navigateToEventDetails method
     * after a delay defined by the SPLASH_DELAY constant.
     */
    private void navigateToEventDetailsAfterDelay() {
        new Handler().postDelayed(this::navigateToEventDetails, SPLASH_DELAY);
    }
}
