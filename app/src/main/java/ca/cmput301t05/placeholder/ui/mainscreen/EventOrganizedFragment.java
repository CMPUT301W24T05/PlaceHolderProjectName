package ca.cmput301t05.placeholder.ui.mainscreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import ca.cmput301t05.placeholder.utils.datafetchers.DataFetchCallback;
import ca.cmput301t05.placeholder.utils.datafetchers.EventFetcher;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;
import ca.cmput301t05.placeholder.ui.events.creation.EnterEventDetailsActivity;

public class EventOrganizedFragment extends Fragment implements EventAdapter.OnItemClickListener, DataFetchCallback {

    private PlaceholderApp app;
    private EventAdapter organizedEventsAdapter;
    private ExtendedFloatingActionButton organizeEventButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    private EventFetcher eventFetcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_organized, container, false);

        // Initialize PlaceholderApp instance
        app = (PlaceholderApp) requireActivity().getApplicationContext();

        eventFetcher = app.getEventFetcher();
        eventFetcher.addCallback(this);

        // Set up the list of created events
        ArrayList<Event> hostedEvents = new ArrayList<>(app.getHostedEvents().values());
        RecyclerView organizedEventsList = view.findViewById(R.id.listCreatedEvents);
        organizedEventsAdapter = new EventAdapter(getContext(), hostedEvents, EventAdapter.adapterType.HOSTED);
        organizedEventsList.setLayoutManager(new LinearLayoutManager(getContext()));
        organizedEventsList.setAdapter(organizedEventsAdapter);

        organizedEventsAdapter.setListener(this);
        organizeEventButton = view.findViewById(R.id.OrganizerEventButton);
        organizeEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EnterEventDetailsActivity.class);
            startActivity(intent);
        });

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() ->
                eventFetcher.fetchEventType(app.getUserProfile(), EventFetcher.EventType.HOSTED_EVENTS));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshEventList();
    }

    @Override
    public void onDestroy() {
        eventFetcher.removeCallback(this);
        super.onDestroy();
    }

    @Override
    public void onItemClick(Event event, EventAdapter.adapterType type) {
        if (type == EventAdapter.adapterType.HOSTED){
            app.setCachedEvent(event);
            Intent i = new Intent(getActivity(), EventMenuActivity.class);
            startActivity(i);
        }
    }

    public void refreshEventList(){
        ArrayList<Event> hostedEvents = new ArrayList<>(app.getHostedEvents().values());
        organizedEventsAdapter.addOrUpdateEvents(hostedEvents);
    }

    @Override
    public void onProfileFetched(Profile profile) {
        // Not applicable to this fragment
    }

    @Override
    public void onPictureLoaded(Bitmap bitmap) {
        // Not applicable to this fragment
    }

    @Override
    public void onProfileFetchFailure(Exception exc) {
        // Not applicable to this fragment
    }

    @Override
    public void onNoIdFound() {
        // Not applicable to this fragment
    }

    @Override
    public void onEventFetched(Profile profile) {
        refreshEventList();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false); // indicate that refreshing has finished
        }
    }

    @Override
    public void onEventFetchError(Exception exception) {
        refreshEventList();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false); // indicate that refreshing has finished
        }
    }
}
