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
import ca.cmput301t05.placeholder.ui.mainscreen.EventExploreFragment;
import ca.cmput301t05.placeholder.ui.mainscreen.EventOrganizedFragment;
import ca.cmput301t05.placeholder.ui.mainscreen.ProfileUpdatedFragment;
import ca.cmput301t05.placeholder.ui.mainscreen.HomeFragment;
import ca.cmput301t05.placeholder.ui.mainscreen.ProfileFragment;
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
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
        setupBottomNavigationView();

        eventInfoSheetLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null && QRCodeScannerActivity.QR_SCANNER_ID_VALUE.equals(data.getStringExtra(QRCodeScannerActivity.QR_SCANNER_ID_KEY))) {
                    // The Scanner activity has finished with an event_info code. Open the bottom sheet here.
                    ViewEventDetailsFragment bottomSheet = new ViewEventDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("interestedMode", true);
                    bottomSheet.setArguments(bundle);
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
                selectedFragment = new HomeFragment();
            } else if (id == R.id.explore_menu_item) {
                selectedFragment = new EventExploreFragment();
            } else if (id == R.id.organized_menu_item) {
                selectedFragment = new EventOrganizedFragment();
            } else if (id == R.id.profile_menu_item){
                selectedFragment = new ProfileUpdatedFragment();
            } else if (id == R.id.scan_menu_item){
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
//<<<<<<< HEAD
//
//    private void setButtonActions() {
//
//
////        //HANDLE FRAGMENT POP UP HERE
////        Boolean openFrag = getIntent().getBooleanExtra("openFragment", false);
////
////        if(openFrag){
////            //open fragment
////            openEventFrag();
////            getIntent().putExtra("openFragment", false);
////        }
//
//
//    }
//
//    public void openEventFrag() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        EventDetailsDialogFragment infoViewAndSignup = new EventDetailsDialogFragment();
//
////        fragmentTransaction.replace(R.id.mainActivity_linearlayout, infoViewAndSignup);
//
//        fragmentTransaction.commit();
//
//    }
//
//    @Override
//    public void onItemClick(Event event, EventAdapter.adapterType type) {
//
//        if (type == EventAdapter.adapterType.HOSTED) {
//            app.setCachedEvent(event);
//            Intent i = new Intent(MainActivity.this, EventMenuActivity.class);
//            i.putExtra("myEvent", event);
////            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            startActivity(i);
//        } else if (type == EventAdapter.adapterType.ATTENDING) {
//            app.setCachedEvent(event);
//            //TODO send to the event info page for attendees
//            Intent i = new Intent(MainActivity.this, ViewEventDetailsActivity.class);
//            startActivity(i);
//
//        }
//
//    }
//=======
//>>>>>>> 28e881358aec56b4784e14b2df392a2e804ac0bd
}