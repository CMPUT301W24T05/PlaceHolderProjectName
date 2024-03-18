package ca.cmput301t05.placeholder.Location;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;

public class Successful_Checked_In_Activity extends AppCompatActivity implements LocationManager.LocationPermissionListener {
    private LocationManager locationManager;
    private PlaceholderApp app;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (PlaceholderApp) getApplicationContext();
        setContentView(R.layout.checked_in_page);

        locationManager = app.getLocationManager();
        locationManager.setLocationPermissionListener(this);
        // Request Location Permission by showing the pop up windows, this will automatically call OnRequestPermissionsResult()
        locationManager.requestLocationPermission(this);
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
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    String message = "Latitude: "+latitude + " Longtitude: " + longitude;
                    Toast.makeText(Successful_Checked_In_Activity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    };
    public void onLocationPermissionDenied(){
        Toast.makeText(this, "onLocationPermissionDenied", Toast.LENGTH_SHORT).show();
    };
}
