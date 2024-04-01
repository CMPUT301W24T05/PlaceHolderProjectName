package ca.cmput301t05.placeholder.ui;

import static ca.cmput301t05.placeholder.profile.ProfileImageGenerator.getCircularBitmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.MainActivity;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.ProfileEditActivity;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.ui.codescanner.QRCodeScannerActivity;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;
import ca.cmput301t05.placeholder.ui.events.ViewEventDetailsActivity;
import ca.cmput301t05.placeholder.ui.events.creation.EnterEventDetailsActivity;
import ca.cmput301t05.placeholder.ui.notifications.UserNotificationActivity;

public class HomeFragment extends Fragment implements EventAdapter.OnItemClickListener {

    private Button guideToEvent;
    private ImageButton profileButton;
    private ImageButton notificationButton;
    private Button startScannerButton;

    private RecyclerView joinedEventsList;
    private EventAdapter joinedEventsAdapter;
    private RecyclerView organizedEventsList;
    private EventAdapter organizedEventsAdapter;

    private TextView appNameView;

    private PlaceholderApp app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        app = (PlaceholderApp) getActivity().getApplicationContext();

        // Initialize your views here from the view instead of directly
        profileButton = view.findViewById(R.id.btnProfile);
        notificationButton = view.findViewById(R.id.btnNotifications);
        startScannerButton = view.findViewById(R.id.btnJoinEvent);
        guideToEvent = view.findViewById(R.id.btnCreateEvent);
        appNameView = view.findViewById(R.id.main_app_name);

        // Similar for RecyclerViews and adapters
        joinedEventsList = view.findViewById(R.id.listJoinedEvents);
        organizedEventsList = view.findViewById(R.id.listCreatedEvents);

        setButtonActions(view);
        setProfileIcon();

        ArrayList<Event> joinedEvents = new ArrayList<Event>(app.getJoinedEvents().values());
        joinedEventsList = view.findViewById(R.id.listJoinedEvents);
        joinedEventsAdapter = new EventAdapter(getActivity().getApplicationContext(), joinedEvents, EventAdapter.adapterType.ATTENDING);
        joinedEventsList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        joinedEventsList.setAdapter(joinedEventsAdapter);

        ArrayList<Event> hostedEvents = new ArrayList<>(app.getHostedEvents().values());
        organizedEventsList = view.findViewById(R.id.listCreatedEvents);
        organizedEventsAdapter = new EventAdapter(getActivity().getApplicationContext(), hostedEvents, EventAdapter.adapterType.HOSTED);
        organizedEventsList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        organizedEventsList.setAdapter(organizedEventsAdapter);

        joinedEventsAdapter.setListener(this);
        organizedEventsAdapter.setListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setProfileIcon();
    }

    // Adapt the existing methods from MainActivity to work in the context of a Fragment
    // Remember to adjust context and startActivity calls

    private void setProfileIcon() {
        if (app.getUserProfile().hasProfileBitmap()){
            profileButton.setImageBitmap(getCircularBitmap(app.getUserProfile().getProfilePictureBitmap()));
        } else {
            app.getProfileImageHandler().getProfilePicture(app.getUserProfile(), getActivity(), new BaseImageHandler.ImageCallback() {
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
        }    }

    private void setButtonActions(View fragmentView) {
        // Your existing logic for setting button actions
        profileButton = fragmentView.findViewById(R.id.btnProfile);
        profileButton.setOnClickListener(v -> {
            // Start ProfileEditActivity
            Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
            startActivity(intent);
        });

//        appNameView = fragmentView.findViewById(R.id.main_app_name);
//        appNameView.setOnClickListener(v -> {
//            // Restart MainActivity
//            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        });

        startScannerButton = fragmentView.findViewById(R.id.btnJoinEvent);
        startScannerButton.setOnClickListener(view -> {
            // Start QRCodeScannerActivity
            Intent intent = new Intent(getActivity(), QRCodeScannerActivity.class);
            startActivity(intent);
        });

        guideToEvent = fragmentView.findViewById(R.id.btnCreateEvent);

        guideToEvent.setOnClickListener(view -> {
            // Start EnterEventDetailsActivity
            Intent intent = new Intent(getActivity(), EnterEventDetailsActivity.class);
            startActivity(intent);
        });

        notificationButton = fragmentView.findViewById(R.id.btnNotifications);
        notificationButton.setOnClickListener(view -> {
            // Start NotificationsActivity
            Intent intent = new Intent(getActivity(), UserNotificationActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onItemClick(Event event, EventAdapter.adapterType type) {
        if (type == EventAdapter.adapterType.HOSTED){
            app.setCachedEvent(event);
            Intent i = new Intent(getActivity(), EventMenuActivity.class);
            startActivity(i);
        } else if (type == EventAdapter.adapterType.ATTENDING) {
            app.setCachedEvent(event);
            //TODO send to the event info page for attendees
            Intent i = new Intent(getActivity(), ViewEventDetailsActivity.class);
            startActivity(i);
        }
    }
}
