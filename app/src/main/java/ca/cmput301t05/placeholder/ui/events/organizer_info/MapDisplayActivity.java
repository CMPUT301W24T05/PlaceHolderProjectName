package ca.cmput301t05.placeholder.ui.events.organizer_info;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import ca.cmput301t05.placeholder.Location.LocationManager;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
/**
 * An activity for displaying a map with attendee locations for an event.
 * This activity utilizes the osmdroid library to display a map and markers for attendees' locations.
 */
public class MapDisplayActivity extends AppCompatActivity implements LocationManager.LocationPermissionListener {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private PlaceholderApp app;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private Context context;
    private Event event;
    private FloatingActionButton closeMap;
    private static final long SPLASH_DELAY = 3000; // 3 seconds d
    /**
     * Called when the activity is starting. This is where most initialization should go.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Otherwise, it is null.
     */
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
        closeMap = findViewById(R.id.close_Map);

        closeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
    /**
     * Callback for the result from requesting permissions.
     * This method is invoked for every call on requestPermissions(android.app.Activity, String[], int).
     * @param requestCode The request code passed in requestPermissions(android.app.Activity, String[], int).
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward the permission request results to the LocationManager
        locationManager.onRequestPermissionsResult(requestCode, grantResults);
    }
    /**
     * Called when location permission is Granted.
     */
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
                    Toast.makeText(MapDisplayActivity.this, message, Toast.LENGTH_SHORT).show();

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
    /**
     * Called when location permission is denied.
     */
    public void onLocationPermissionDenied(){
        Toast.makeText(this, "onLocationPermissionDenied", Toast.LENGTH_SHORT).show();
        // when location is denied, set a default center point (UA area)
        IMapController mapController = map.getController();
        mapController.setZoom(14.5);
        double la = 53.519101618978745;
        double lo = -113.52437329734187;
        GeoPoint startPoint = new GeoPoint(la, lo);
        mapController.setCenter(startPoint);
        showAttendees();
        map.invalidate();
    }
    /**
     * Displays markers for attendees' locations on the map.
     */
    public void showAttendees() {
        HashMap<String, HashMap<String, Double>> attendees = event.getMap();
        ArrayList<Marker> markers = new ArrayList<>();
        if (attendees != null && !attendees.isEmpty()) {
            attendees.forEach((key, value) -> {
                if (value.get("latitude") != null && value.get("longitude") != null) {
                    double latitude = value.get("latitude");
                    double longitude = value.get("longitude");
                    String attendeeID = key;
                    // set the marker's title to be the name of the attendee
                    Toast.makeText(this, "showAttendees", Toast.LENGTH_SHORT).show();
                    app.getProfileTable().fetchDocument(key, new Table.DocumentCallback<Profile>() {
                        @Override
                        public void onSuccess(Profile document) {
                            String attendeeName = document.getName();
                            markers.add(createMarker(latitude, longitude, attendeeName));
                            Toast.makeText(MapDisplayActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
                            map.getOverlays().addAll(markers);
                            map.invalidate();
                        }
                        @Override
                        public void onFailure(Exception e) {
                            markers.add(createMarker(latitude, longitude, attendeeID));
                            Toast.makeText(MapDisplayActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
                            map.getOverlays().addAll(markers);
                            map.invalidate();
                        }
                    });
                }
            });
        }
    }
    /**
     * Creates a marker for the given latitude, longitude, and title.
     * @param latitude The latitude of the marker.
     * @param longitude The longitude of the marker.
     * @param title The title of the marker.
     * @return The marker object.
     */
    private Marker createMarker(double latitude, double longitude, String title) {
        Marker marker = new Marker(map);
        marker.setPosition(new GeoPoint(latitude, longitude));
        marker.setTitle(title);
        marker.setIcon(ContextCompat.getDrawable(this, R.drawable.baseline_attendee_map_icon));
        return marker;
    }
}
