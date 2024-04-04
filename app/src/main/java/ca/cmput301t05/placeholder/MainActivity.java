package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ca.cmput301t05.placeholder.ui.codescanner.QRCodeScannerActivity;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;
import ca.cmput301t05.placeholder.ui.events.ViewEventDetailsActivity;
import ca.cmput301t05.placeholder.ui.mainscreen.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.ui.mainscreen.HomeFragment;
import ca.cmput301t05.placeholder.ui.events.EventDetailsDialogFragment;
import ca.cmput301t05.placeholder.ui.mainscreen.EventExploreFragment;
import ca.cmput301t05.placeholder.ui.mainscreen.EventOrganizedFragment;


/**
 * MainActivity serves as the primary entry point for the Placeholder application. It provides navigation to different
 * parts of the application, such as creating events, joining events via QR code scanning, editing user profiles,
 * and viewing notifications. This activity sets up the main user interface and initializes action listeners for
 * navigation buttons.
 */
public class MainActivity extends AppCompatActivity implements EventAdapter.OnItemClickListener {

    private PlaceholderApp app;

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
        setButtonActions();

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
                selectedFragment = new ProfileFragment();
            } else if (id == R.id.scan_menu_item){
                Intent intent = new Intent(MainActivity.this, QRCodeScannerActivity.class);
                startActivity(intent);
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }

            return false;
        });
    }

    private void setButtonActions() {


//        //HANDLE FRAGMENT POP UP HERE
//        Boolean openFrag = getIntent().getBooleanExtra("openFragment", false);
//
//        if(openFrag){
//            //open fragment
//            openEventFrag();
//            getIntent().putExtra("openFragment", false);
//        }


    }

    public void openEventFrag() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EventDetailsDialogFragment infoViewAndSignup = new EventDetailsDialogFragment();

//        fragmentTransaction.replace(R.id.mainActivity_linearlayout, infoViewAndSignup);

        fragmentTransaction.commit();

    }

    @Override
    public void onItemClick(Event event, EventAdapter.adapterType type) {

        if (type == EventAdapter.adapterType.HOSTED) {
            app.setCachedEvent(event);
            Intent i = new Intent(MainActivity.this, EventMenuActivity.class);
            i.putExtra("myEvent", event);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
        } else if (type == EventAdapter.adapterType.ATTENDING) {
            app.setCachedEvent(event);
            //TODO send to the event info page for attendees
            Intent i = new Intent(MainActivity.this, ViewEventDetailsActivity.class);
            startActivity(i);

        }

    }
}