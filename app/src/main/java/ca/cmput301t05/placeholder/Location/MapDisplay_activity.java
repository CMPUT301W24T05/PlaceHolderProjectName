package ca.cmput301t05.placeholder.Location;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

import android.Manifest;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.ui.events.ViewEventDetailsActivity;

public class MapDisplay_activity extends AppCompatActivity implements LocationManager.LocationPermissionListener {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private PlaceholderApp app;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private static final long SPLASH_DELAY = 3000; // 3 seconds d
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_display);
        app = (PlaceholderApp) getApplicationContext();

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);


        // create locationManager object to know the location of the organizer
        locationManager = app.getLocationManager();
        locationManager.setLocationPermissionListener(this);
        locationManager.requestLocationPermission(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

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
                    Toast.makeText(MapDisplay_activity.this, message, Toast.LENGTH_SHORT).show();

                    //move the map on a default view point (where the organizer are)
//                    IMapController mapController = map.getController();
//                    mapController.setZoom(9.5);
//                    GeoPoint startPoint = new GeoPoint(latitude, longitude);
//                    mapController.setCenter(startPoint);
                }
            }
        });
    };

    public void onLocationPermissionDenied(){
        Toast.makeText(this, "onLocationPermissionDenied, cannot see the map", Toast.LENGTH_SHORT).show();
        // when location is denied, wait for 3 seconds and directly go to the event details page

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, SPLASH_DELAY);
    }

}
