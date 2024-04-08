package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import ca.cmput301t05.placeholder.ui.codescanner.QRCodeScannerActivity;
import ca.cmput301t05.placeholder.ui.events.ViewEventDetailsFragment;
import ca.cmput301t05.placeholder.ui.events.checkin.SuccessfulCheckinActivity;
import ca.cmput301t05.placeholder.ui.mainscreen.EventExploreFragment;
import ca.cmput301t05.placeholder.ui.mainscreen.EventOrganizedFragment;
import ca.cmput301t05.placeholder.ui.mainscreen.NewHomeFragment;
import ca.cmput301t05.placeholder.ui.mainscreen.ProfileViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


/**
 * MainActivity serves as the primary entry point for the Placeholder application. It provides navigation to different
 * parts of the application, such as creating events, joining events via QR code scanning, editing user profiles,
 * and viewing notifications. This activity sets up the main user interface and initializes action listeners for
 * navigation buttons.
 */
public class MainActivity extends AppCompatActivity {

    private PlaceholderApp app;

    private ActivityResultLauncher<Intent> eventInfoSheetLauncher;

    /**
     * Called when the activity is starting. Initializes the application context, sets the content view,
     * and configures button listeners for navigating to various features of the app.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (PlaceholderApp) getApplicationContext();

        setContentView(R.layout.activity_main);
        setTheme(R.style.AppTheme);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new NewHomeFragment())
                    .commit();
        }
        setupBottomNavigationView();

        eventInfoSheetLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null && QRCodeScannerActivity.QRSCANNER_ACTIVITY_EVENTINFO.equals(data.getStringExtra(QRCodeScannerActivity.QR_SCANNER_ID_KEY))) {
                    // The Scanner activity has finished with an event_info code. Open the bottom sheet here.
                    ViewEventDetailsFragment bottomSheet = new ViewEventDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("interestedMode", true);
                    bottomSheet.setArguments(bundle);
                    bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
                } else if (data != null && QRCodeScannerActivity.QRSCANNER_ACTIVITY_CHECKIN.equals(data.getStringExtra(QRCodeScannerActivity.QR_SCANNER_ID_KEY))) {
                    Intent intent = new Intent(this, SuccessfulCheckinActivity.class);
                    eventInfoSheetLauncher.launch(intent);
                } else if (data != null && SuccessfulCheckinActivity.SUCCESSFUL_CHECKIN_VALUE.equals(data.getStringExtra(SuccessfulCheckinActivity.SUCCESSFUL_CHECKIN_KEY))) {
                    // The user has successfully checked into an event. Open the bottom sheet here.
                    ViewEventDetailsFragment bottomSheet = new ViewEventDetailsFragment();
                    bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
                }
            }
        });

        Log.i("MainActivityProfileID", "Current profile ID:" + app.getUserProfile().getProfileID().toString());
        Log.i("MainActivityJoinedEvents", "Number of joined events: " + app.getJoinedEvents().size());


    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment selectedFragment = null;

            if (id == R.id.home_menu_item) {
                selectedFragment = new NewHomeFragment();
            } else if (id == R.id.explore_menu_item) {
                selectedFragment = new EventExploreFragment();
            } else if (id == R.id.organized_menu_item) {
                selectedFragment = new EventOrganizedFragment();
            } else if (id == R.id.profile_menu_item) {
                selectedFragment = new ProfileViewFragment();
            } else if (id == R.id.scan_menu_item) {
                Intent intent = new Intent(this, QRCodeScannerActivity.class);
                eventInfoSheetLauncher.launch(intent);
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }

            return false;
        });
    }

}