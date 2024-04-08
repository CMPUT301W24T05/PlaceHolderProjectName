package ca.cmput301t05.placeholder.Location;

import android.Manifest;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
/**
 * Utility class for managing location-related operations such as requesting location permissions,
 * retrieving last known location, and handling location permission callbacks.
 */
public class LocationManager {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Context context;
    private LocationPermissionListener permissionListener;

    /**
     * Interface for receiving location permission callbacks.
     */
    // LocationPermissionListener is implemented by the Successful_Checked_In_Activity class
    // so that I can call the onRequestPermissionsResult in the activity and pass the result back
    // to the LocationManager
    public interface LocationPermissionListener {
        void onLocationPermissionGranted();
        void onLocationPermissionDenied();
    }
    /**
     * Sets the location permission listener.
     *
     * @param listener The location permission listener.
     */
    public void setLocationPermissionListener(LocationPermissionListener listener) {
        permissionListener = listener;
    }

    /**
     * Constructs a new LocationManager.
     *
     * @param context The context in which the LocationManager operates.
     */
    public LocationManager(Context context){
        this.context = context;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }
    /**
     * Checks if the ACCESS_FINE_LOCATION permission has been granted.
     *
     * @return True if the permission is granted, false otherwise.
     */
    public boolean checkLocationPermission() {
        // Check if the ACCESS_FINE_LOCATION permission has been granted
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    /**
     * Requests the ACCESS_FINE_LOCATION permission from the user.
     *
     * @param activity The activity requesting the permission.
     */
    // Activity is the screen that you want the pop up window to appear
    public void requestLocationPermission(Activity activity) {
        // Request the ACCESS_FINE_LOCATION permission from the user
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }
    /**
     * Handles the result of the location permission request.
     *
     * @param requestCode The request code for the permission request.
     * @param grantResults The grant results for the permission request.
     */

    // Delegate the taks to the activity class by using the permissionListen interface
    public void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (permissionListener != null) {
                    permissionListener.onLocationPermissionGranted();
                }
            } else {
                if (permissionListener != null) {
                    permissionListener.onLocationPermissionDenied();
                }
            }
        }
    }
    /**
     * Interface for receiving location callbacks.
     */
    public interface LocationCallback {
        void onLocationReceived(Location location);
    }

    // the location returned can be Null (not granted access), since location might not be available immediately
    // Instead of directly returning the last location from the method,
    // you should use a callback mechanism or a listener to handle the location when it becomes available.
    // the location is a location object if (successfully get the location)
    public void getLastLocation(final LocationCallback callback) {
        if (checkLocationPermission()) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                callback.onLocationReceived(location);
                                // onSuccess will takes some time, and may not happen instantly
                                // have the callback interface, where we implement the method in the activity.class
                                // so all the code related to the location will only be executed when it is onSuccess
                                Log.d("Location", "lastLocation is not null!");
                            } else {
                                // If location is null, handle the case accordingly
                                Log.d("Location", "lastLocation is null!");
                            }
                        }
                    });
        } else {
            permissionListener.onLocationPermissionDenied();
        }
    }

    // return the location


}
