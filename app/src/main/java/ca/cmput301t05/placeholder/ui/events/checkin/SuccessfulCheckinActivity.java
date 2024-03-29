package ca.cmput301t05.placeholder.ui.events.checkin;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.Location.LocationManager;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.EventTable;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.ui.events.ViewEventDetailsActivity;

public class SuccessfulCheckinActivity extends AppCompatActivity implements LocationManager.LocationPermissionListener {
    private LocationManager locationManager;
    private PlaceholderApp app;
    private Button next_button;
    private CheckBox shareLocation;
    private Event event;
    private Profile profile;
    private boolean isShared;
    private double latitude;
    private double longitude;
    private EventTable eventTable;
    // Used to make a page stay there a 1.5 second if choose not to share location
    private static final long SPLASH_DELAY = 3000; // 3 seconds d
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (PlaceholderApp) getApplicationContext();
        setContentView(R.layout.checked_in_page);

        locationManager = app.getLocationManager();
        locationManager.setLocationPermissionListener(this);
        // Request Location Permission by showing the pop up windows, this will automatically call OnRequestPermissionsResult()
        locationManager.requestLocationPermission(this);
        event = app.getCachedEvent();
        profile = app.getUserProfile();
        eventTable = app.getEventTable();

        next_button = findViewById(R.id.go_to_event_button);
        shareLocation = findViewById(R.id.checkbox_share_location);

        // when click on next button, check the status of share location, update the event object appropriately
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShared = shareLocation.isChecked();
                if (isShared == true){
                    // if choose to share location, update database
                    event.checkIn(profile, longitude, latitude);
                    Toast.makeText(SuccessfulCheckinActivity.this, "Location Shared", Toast.LENGTH_SHORT).show();
                }else{
                    event.checkIn(profile, null, null);
                    Toast.makeText(SuccessfulCheckinActivity.this, "Location Not Shared", Toast.LENGTH_SHORT).show();
                }
                // update the database!
                eventTable.pushDocument(event, event.getEventID().toString(), new Table.DocumentCallback<Event>() {
                    @Override
                    public void onSuccess(Event document){
                        // Do something after the event is successfully uploaded
                    }
                    @Override
                    public void onFailure(Exception e){
                        // Event upload failed, handle failure
                    }
                });
                Intent intent = new Intent(SuccessfulCheckinActivity.this, ViewEventDetailsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // check if maximum capacity is reached, if reached, return false
        if (!event.reachMaxCapacity()){
            TextView message = findViewById(R.id.textView4);
            message.setText("Maximum Capacity has been reached!");
            next_button.setVisibility(View.INVISIBLE);
            shareLocation.setVisibility(View.INVISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, SPLASH_DELAY);
        }
    }

    // Overriden method from the activity class, will call the onRequestPermissionsResult in LocationManagerClass
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward the permission request results to the LocationManager
        locationManager.onRequestPermissionsResult(requestCode, grantResults);
    }

    public void onLocationPermissionGranted(){
        Toast.makeText(this, "onLocationPermissionGranted", Toast.LENGTH_SHORT).show();
        locationManager.getLastLocation(new LocationManager.LocationCallback() {
            @Override
            public void onLocationReceived(Location location) {
                if (location!=null){
                    // testing to see my location on screen
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    String message = "Latitude: "+latitude + " Longitude: " + longitude;
                    Toast.makeText(SuccessfulCheckinActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    };

    public void onLocationPermissionDenied(){
        Toast.makeText(this, "onLocationPermissionDenied", Toast.LENGTH_SHORT).show();
        // when location is denied, wait for 3 seconds and directly go to the event details page
        shareLocation.setVisibility(View.INVISIBLE);
        next_button.setVisibility(View.INVISIBLE);
        event.checkIn(profile, null, null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                eventTable.pushDocument(event, event.getEventID().toString(), new Table.DocumentCallback<Event>() {
                    @Override
                    public void onSuccess(Event document){
                        // Do something after the event is successfully uploaded
                    }

                    @Override
                    public void onFailure(Exception e){
                        // Event upload failed, handle failure
                    }
                });
                Intent intent = new Intent(SuccessfulCheckinActivity.this, ViewEventDetailsActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);
    }
}
