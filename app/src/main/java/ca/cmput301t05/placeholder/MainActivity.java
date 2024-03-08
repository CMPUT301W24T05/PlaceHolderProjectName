package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.cmput301t05.placeholder.database.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.qrcode.QRCode;
import ca.cmput301t05.placeholder.qrcode.QRCodeManager;
import ca.cmput301t05.placeholder.ui.codescanner.QRCodeScannerActivity;
import ca.cmput301t05.placeholder.ui.events.EventDetailsDialogFragment;
import ca.cmput301t05.placeholder.ui.events.ViewEventDetailsActivity;
import ca.cmput301t05.placeholder.ui.events.creation.EnterEventDetailsActivity;
import ca.cmput301t05.placeholder.ui.notifications.NotificationsFragment;


/**
 * MainActivity serves as the primary entry point for the Placeholder application. It provides navigation to different
 * parts of the application, such as creating events, joining events via QR code scanning, editing user profiles,
 * and viewing notifications. This activity sets up the main user interface and initializes action listeners for
 * navigation buttons.
 */
public class MainActivity extends AppCompatActivity {

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

        setButtonActions();

        Log.i("MainActivityProfileID", "Current profile ID:" + app.getUserProfile().getProfileID().toString());
        Log.i("MainActivityJoinedEvents", "Number of joined events: " + app.getJoinedEvents().size());

        ArrayList<Event> joinedEvents = new ArrayList<Event>(app.getJoinedEvents().values());
        joinedEventsList = findViewById(R.id.listJoinedEvents);
        joinedEventsAdapter = new EventAdapter(getApplicationContext(), joinedEvents);
        joinedEventsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        joinedEventsList.setAdapter(joinedEventsAdapter);

        ArrayList<Event> hostedEvents = new ArrayList<>(app.getHostedEvents().values());
        organizedEventsList = findViewById(R.id.listCreatedEvents);
        organizedEventsAdapter = new EventAdapter(getApplicationContext(), hostedEvents);
        organizedEventsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        organizedEventsList.setAdapter(organizedEventsAdapter);
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

        startScannerButton = findViewById(R.id.btnJoinEvent);

        ImageView testQR = findViewById(R.id.qr_test_pic);
        QRCodeManager qrM = new QRCodeManager();



        app.getEventTable().fetchDocument("a950e92d-7b97-4d47-9d90-f6e421c42cd1", new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event document) {

                QRCode qr = qrM.generateQRCode(document, "eventInfo");

                Log.d("Event_info",document.getEventID().toString());

                testQR.setImageBitmap(qr.getBitmap());

            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        startScannerButton.setOnClickListener(view -> {
            // Start QRCodeScannerActivity
            Intent intent = new Intent(MainActivity.this, QRCodeScannerActivity.class);
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
            Intent intent = new Intent(MainActivity.this, NotificationsFragment.class);
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

}

