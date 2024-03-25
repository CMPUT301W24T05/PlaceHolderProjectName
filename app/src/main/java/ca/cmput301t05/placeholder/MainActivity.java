package ca.cmput301t05.placeholder;

import static ca.cmput301t05.placeholder.profile.ProfileImageGenerator.getCircularBitmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import java.util.UUID;

import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.ui.codescanner.QRCodeScannerActivity;
import ca.cmput301t05.placeholder.ui.events.EventDetailsDialogFragment;
import ca.cmput301t05.placeholder.ui.events.EventExplore;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;
import ca.cmput301t05.placeholder.ui.events.EventOrganized;
import ca.cmput301t05.placeholder.ui.events.ViewQRCodesActivity;
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
    private Button guideToEvent;
    private ImageButton profileButton;
    private ImageButton notificationButton;
    private Button startScannerButton;

    private RecyclerView joinedEventsList;
    private EventAdapter joinedEventsAdapter;
    private RecyclerView organizedEventsList;
    private EventAdapter organizedEventsAdapter;

    private TextView appNameView;

    private Button testButton;


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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        setButtonActions();
        setProfileIcon();

        Log.i("MainActivityProfileID", "Current profile ID:" + app.getUserProfile().getProfileID().toString());
        Log.i("MainActivityJoinedEvents", "Number of joined events: " + app.getJoinedEvents().size());

        ArrayList<Event> joinedEvents = new ArrayList<Event>(app.getJoinedEvents().values());
        joinedEventsList = findViewById(R.id.listJoinedEvents);
        joinedEventsAdapter = new EventAdapter(getApplicationContext(), joinedEvents, EventAdapter.adapterType.ATTENDING);
        joinedEventsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        joinedEventsList.setAdapter(joinedEventsAdapter);

        ArrayList<Event> hostedEvents = new ArrayList<>(app.getHostedEvents().values());
        organizedEventsList = findViewById(R.id.listCreatedEvents);
        organizedEventsAdapter = new EventAdapter(getApplicationContext(), hostedEvents, EventAdapter.adapterType.HOSTED);
        organizedEventsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        organizedEventsList.setAdapter(organizedEventsAdapter);

        joinedEventsAdapter.setListener(this);
        organizedEventsAdapter.setListener(this);




    }

    @Override
    protected void onResume() {
        super.onResume();
        setProfileIcon();
    }

    private void setProfileIcon() {
        if (app.getUserProfile().hasProfileBitmap()){
            profileButton.setImageBitmap(getCircularBitmap(app.getUserProfile().getProfilePictureBitmap()));
        } else {
            app.getProfileImageHandler().getProfilePicture(app.getUserProfile(), this, new BaseImageHandler.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    profileButton.setImageBitmap(getCircularBitmap(bitmap));
                }

                @Override
                public void onError(Exception e) {
                    app.getUserProfile().setProfilePictureToDefault();
                    profileButton.setImageBitmap(getCircularBitmap(app.getUserProfile().getProfilePictureBitmap()));
                }
            });
        }
    }

    private void setButtonActions() {
        profileButton = findViewById(R.id.btnProfile);
        profileButton.setOnClickListener(v -> {
            // Start ProfileEditActivity
            Intent intent = new Intent(MainActivity.this, ProfileEditActivity.class);
            startActivity(intent);
        });

        appNameView = findViewById(R.id.main_app_name);
        appNameView.setOnClickListener(v -> {
            // Restart MainActivity
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });


//        testButton = findViewById(R.id.event_menu_test);
//
//        testButton.setOnClickListener(view -> {
//            // Start QRCodeScannerActivity
//            Intent intent = new Intent(MainActivity.this, EventMenuActivity.class);
//            startActivity(intent);
//        });


        startScannerButton = findViewById(R.id.btnJoinEvent);
        startScannerButton.setOnClickListener(view -> {
            // Start QRCodeScannerActivity
            Intent intent = new Intent(MainActivity.this, EventMenuActivity.class);
            startActivity(intent);
        });

        guideToEvent = findViewById(R.id.btnCreateEvent);

        guideToEvent.setOnClickListener(view -> {
            // Start EnterEventDetailsActivity
            Intent intent = new Intent(MainActivity.this, EnterEventDetailsActivity.class);
            startActivity(intent);
        });

        notificationButton = findViewById(R.id.btnNotifications);
        notificationButton.setOnClickListener(view -> {
            // Start NotificationsActivity
            Intent intent = new Intent(MainActivity.this, UserNotificationActivity.class);
            startActivity(intent);
        });


        //HANDLE FRAGMENT POP UP HERE
        Boolean openFrag = getIntent().getBooleanExtra("openFragment", false);

        if(openFrag){
            //open fragment
            openEventFrag();
            getIntent().putExtra("openFragment", false);
        }



    }

    public void openEventFrag(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EventDetailsDialogFragment infoViewAndSignup = new EventDetailsDialogFragment();

        fragmentTransaction.replace(R.id.mainActivity_linearlayout, infoViewAndSignup);

        fragmentTransaction.commit();

    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item1) {
            // Navigate to MainActivity
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.menu_item2) {
            // Navigate to EventExplore
            startActivity(new Intent(this, EventExplore.class));
            return true;
        } else if (id == R.id.menu_item3) {
            // Navigate to EventOrganized
            startActivity(new Intent(this, EventOrganized.class));
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onItemClick(Event event, EventAdapter.adapterType type) {

        if (type == EventAdapter.adapterType.HOSTED){
            app.setCachedEvent(event);
            Intent i = new Intent(MainActivity.this, EventMenuActivity.class);
            startActivity(i);
        } else if (type == EventAdapter.adapterType.ATTENDING) {
            app.setCachedEvent(event);
            //TODO send to the event info page for attendees

        }

    }
}