package ca.cmput301t05.placeholder.ui.mainscreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import ca.cmput301t05.placeholder.InitialSetupActivity;
import ca.cmput301t05.placeholder.LoadingScreenActivity;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.notifications.Notification;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;
import ca.cmput301t05.placeholder.ui.events.creation.EnterEventDetailsActivity;

public class EventOrganizedFragment extends Fragment implements EventAdapter.OnItemClickListener{

    private PlaceholderApp app;
    private RecyclerView organizedEventsList;
    private EventAdapter organizedEventsAdapter;
    private ExtendedFloatingActionButton organize;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_organized, container, false);

        // Initialize PlaceholderApp instance
        app = (PlaceholderApp) getActivity().getApplicationContext();

        // Set up the list of created events
        ArrayList<Event> hostedEvents = new ArrayList<>(app.getHostedEvents().values());
        organizedEventsList = view.findViewById(R.id.listCreatedEvents);
        organizedEventsAdapter = new EventAdapter(getContext(), hostedEvents, EventAdapter.adapterType.HOSTED);
        organizedEventsList.setLayoutManager(new LinearLayoutManager(getContext()));
        organizedEventsList.setAdapter(organizedEventsAdapter);

        organizedEventsAdapter.setListener(this);
        organize = view.findViewById(R.id.OrganizerEventButton);
        organize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EnterEventDetailsActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do something when the user swipes down to refresh
                // For example, reload data from a data source
                loadData();
            }
        });

        return view;
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

        }

    }
    private void loadData() {
        UUID deviceId = app.getIdManager().getDeviceID();
        app.getProfileTable().fetchDocument(deviceId.toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                // The profile exists in firebase! We can continue to the Main activity
                app.setUserProfile(profile);
                app.getProfileImageHandler().getProfilePicture(profile, getActivity().getApplicationContext(), new BaseImageHandler.ImageCallback() {
                    @Override
                    public void onImageLoaded(Bitmap bitmap) {
                        profile.setProfilePictureBitmap(bitmap);
                        fetchEvents(profile);
                    }
                    @Override
                    public void onError(Exception e) {
                        profile.setProfilePictureToDefault();
                        fetchEvents(profile);
                    }
                });
            }
            @Override
            public void onFailure(Exception e) {
                // The profile does not exist in firebase, let's ask for the name of the user
                Log.e("App/ProfileFetch", e.toString());
            }
        });

    }

    private void fetchEvents(Profile profile) {
        AtomicInteger eventCounter = new AtomicInteger();

        if (profile.getHostedEvents() != null) {
            eventCounter.addAndGet(profile.getHostedEvents().size());
            fetchEvents(profile, "hostedEvents", eventCounter);
        }
        if (profile.getJoinedEvents() != null) {
            eventCounter.addAndGet(profile.getJoinedEvents().size());
            fetchEvents(profile, "joinedEvents", eventCounter);
        }
        if (profile.getInterestedEvents() != null) {
            eventCounter.addAndGet(profile.getInterestedEvents().size());
            fetchEvents(profile, "interestedEvents", eventCounter);
        }
        if (eventCounter.get() == 0) {
        }
    }

    private void fetchEvents(Profile profile, String event, AtomicInteger eventCounter) {
        List<String> events;

        switch (event) {
            case "hostedEvents":
                events = profile.getHostedEvents();
                break;
            case "joinedEvents":
                events = profile.getJoinedEvents();
                break;
            case "interestedEvents":
                events = profile.getInterestedEvents();
                break;
            default:
                return; // Method invoked with an invalid event string
        }

        if (events == null) {
            return;
        }
        for (String id : events) {
            app.getEventTable().fetchDocument(id.trim(), new Table.DocumentCallback<Event>() {
                @Override
                public void onSuccess(Event document) {

                    switch (event) {
                        case "hostedEvents":
                            app.getHostedEvents().put(UUID.fromString(id.trim()), document);
                            break;
                        case "joinedEvents":
                            app.getJoinedEvents().put(UUID.fromString(id.trim()), document);
                            break;
                        case "interestedEvents":
                            app.getInterestedEvents().put(UUID.fromString(id.trim()), document);
                            break;
                    }

                    // Decrease the counter once an event is fetched
                    eventCounter.decrementAndGet();
                    // If all events have been fetched, start MainActivity
                    if (eventCounter.get() == 0) {
                        refreshEventList();
                    }
                }
                @Override
                public void onFailure(Exception e) {
                    //TODO handle failure
                }
            });

        }
    }
    public void refreshEventList(){
        ArrayList<Event> hostedEvents = new ArrayList<>(app.getHostedEvents().values());
        organizedEventsAdapter = new EventAdapter(getContext(), hostedEvents, EventAdapter.adapterType.HOSTED);
        organizedEventsList.setLayoutManager(new LinearLayoutManager(getContext()));
        organizedEventsList.setAdapter(organizedEventsAdapter);
        organizedEventsAdapter.setListener(this);
        swipeRefreshLayout.setRefreshing(false); // indicate the refreshing has finished
    }
}
