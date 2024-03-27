package ca.cmput301t05.placeholder.Location;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.HashMap;

import android.Manifest;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.qrcode.QRCodeType;
import ca.cmput301t05.placeholder.ui.codescanner.QRCodeScannerActivity;
import ca.cmput301t05.placeholder.ui.events.EventSignUpActivity;
import ca.cmput301t05.placeholder.ui.events.ViewEventDetailsActivity;

public class MapDisplay_activity extends AppCompatActivity implements LocationManager.LocationPermissionListener {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private PlaceholderApp app;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private Context context;
    private Event event;
    private static final long SPLASH_DELAY = 3000; // 3 seconds d
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (PlaceholderApp) getApplicationContext();
        context = getApplicationContext();

        // set your user agent to prevent getting banned from the osm servers
        // do this before setContentView(R.layout.map_display)
        SharedPreferences sharedPrefs = context.getSharedPreferences("PlaceholderAppPrefs", Context.MODE_PRIVATE);
        Configuration.getInstance().load(context, sharedPrefs);
        Configuration.getInstance().setUserAgentValue(BuildConfig.BUILD_TYPE);

        setContentView(R.layout.map_display);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        // create locationManager object to know the location of the organizer
        locationManager = app.getLocationManager();
        locationManager.setLocationPermissionListener(this);
        locationManager.requestLocationPermission(this);

        event = app.getCachedEvent();

        // for testing:
//        app.getEventTable().fetchDocument("917812f0-4400-420e-8260-f566943ac624", new Table.DocumentCallback<Event>() {
//            @Override
//            public void onSuccess(Event event1){
//                app.setCachedEvent(event1); //sets the cached event so we can use it on the next pag
//                event = app.getCachedEvent();
//            }
//            @Override
//            public void onFailure(Exception e){
//                // Failed to get fetch the event for eventId with exception e
//            }
//        });

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
                    IMapController mapController = map.getController();
                    mapController.setZoom(14.5);
                    GeoPoint startPoint = new GeoPoint(latitude, longitude);
                    mapController.setCenter(startPoint);
                    Marker marker = new Marker(map);
                    marker.setPosition(startPoint);
                    marker.setTitle("You");
                    map.getOverlays().add(marker);
                    showAttendees();
                    map.invalidate();
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

    public void showAttendees() {
        HashMap<String, HashMap<String, Double>> attendees = event.getMap();
        ArrayList<Marker> markers = new ArrayList<>();
        if (attendees != null && !attendees.isEmpty()) {
            attendees.forEach((key, value) -> {
                if (value.get("latitude") != null && value.get("longitude") != null) {
                    double latitude = value.get("latitude");
                    double longitude = value.get("longitude");
                    String attendeeID = key;
                    markers.add(createMarker(latitude, longitude, attendeeID));
                }
            });
            map.getOverlays().addAll(markers);
            map.invalidate();
        }
    }
    private Marker createMarker(double latitude, double longitude, String title) {
        Marker marker = new Marker(map);
        marker.setPosition(new GeoPoint(latitude, longitude));
        marker.setTitle(title);
        marker.setIcon(ContextCompat.getDrawable(this, R.drawable.baseline_attendee_map_icon));
        return marker;
    }
}
