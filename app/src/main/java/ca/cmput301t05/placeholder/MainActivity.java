package ca.cmput301t05.placeholder;

import static ca.cmput301t05.placeholder.profile.ProfileImageGenerator.getCircularBitmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.ui.HomeFragment;
import ca.cmput301t05.placeholder.ui.codescanner.QRCodeScannerActivity;
import ca.cmput301t05.placeholder.ui.events.EventDetailsDialogFragment;
import ca.cmput301t05.placeholder.ui.EventExplore;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;
import ca.cmput301t05.placeholder.ui.EventOrganized;
import ca.cmput301t05.placeholder.ui.events.ViewEventDetailsActivity;
import ca.cmput301t05.placeholder.ui.events.creation.EnterEventDetailsActivity;
import ca.cmput301t05.placeholder.ui.notifications.UserNotificationActivity;


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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        setButtonActions();
        setProfileIcon();

        Log.i("MainActivityProfileID", "Current profile ID:" + app.getUserProfile().getProfileID().toString());
        Log.i("MainActivityJoinedEvents", "Number of joined events: " + app.getJoinedEvents().size());
    }



    private void setProfileIcon() {


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

    public void openEventFrag(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EventDetailsDialogFragment infoViewAndSignup = new EventDetailsDialogFragment();

//        fragmentTransaction.replace(R.id.mainActivity_linearlayout, infoViewAndSignup);

        fragmentTransaction.commit();

    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home_menu_item) {
            // Navigate to MainActivity
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.explore_menu_item) {
            // Navigate to EventExplore
            startActivity(new Intent(this, EventExplore.class));
            return true;
        } else if (id == R.id.organized_menu_icon) {
            // Navigate to EventOrganized
            startActivity(new Intent(this, EventOrganized.class));
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onItemClick(Event event, EventAdapter.adapterType type) {



    }
}