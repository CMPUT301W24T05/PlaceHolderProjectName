package ca.cmput301t05.placeholder.ui.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;
import ca.cmput301t05.placeholder.ui.events.ViewEventDetailsFragment;

import java.util.ArrayList;

/**
 * A fragment for exploring and displaying events.
 */
public class EventExploreFragment extends Fragment implements EventAdapter.OnItemClickListener{

    private PlaceholderApp app;
    private RecyclerView allEventsList;
    private EventAdapter allEventsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    /**
     * Initializes the fragment's view.
     * @param inflater The layout inflater object.
     * @param container The view container.
     * @param savedInstanceState The saved instance state bundle.
     * @return The inflated view for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_explore, container, false);
        initializeApp();
        setupSwipeRefreshLayout(view);
        setupEventList(view);
        fetchAllEvents(); // Fetch events after initializing the adapter
        return view;
    }
    /**
     * Initializes the PlaceholderApp instance.
     */
    private void initializeApp() {
        app = (PlaceholderApp) requireActivity().getApplicationContext();
    }
    /**
     * Sets up the swipe refresh layout for updating events.
     * @param view The view associated with the fragment.
     */
    private void setupSwipeRefreshLayout(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchAllEvents();
            swipeRefreshLayout.setRefreshing(false);
        });
    }
    /**
     * Sets up the RecyclerView for displaying events.
     * @param view The view associated with the fragment.
     */
    private void setupEventList(View view) {
        allEventsList = view.findViewById(R.id.listAllEvents);
        allEventsAdapter = new EventAdapter(getContext(), new ArrayList<>(), EventAdapter.adapterType.ATTENDING); // Use getContext() here
        allEventsList.setLayoutManager(new LinearLayoutManager(getContext()));
        allEventsList.setAdapter(allEventsAdapter);
        allEventsAdapter.setListener(this);
    }
    /**
     * Fetches all events from the database.
     */
    private void fetchAllEvents() {
        // Fetch all events from Firebase
        app.getEventTable().fetchAllDocuments(new Table.DocumentCallback<ArrayList<Event>>() {
            @Override
            public void onSuccess(ArrayList<Event> events) {
                // Display the fetched events
                displayEvents(events);
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure
            }
        });
    }

    /**
     * Displays the fetched events in the RecyclerView.
     * @param events The list of events to display.
     */
    private void displayEvents(ArrayList<Event> events) {
        // Update the RecyclerView with fetched events
        allEventsAdapter.addOrUpdateEvents(events);
    }
    /**
     * Handles item click events on the RecyclerView.
     * @param event The event object clicked.
     * @param type The type of adapter clicked.
     */
    @Override
    public void onItemClick(Event event, EventAdapter.adapterType type) {
        app.setCachedEvent(event);
        if (type == EventAdapter.adapterType.HOSTED){
            Intent intent= new Intent(getActivity(), EventMenuActivity.class);
            startActivity(intent);
        } else if (type == EventAdapter.adapterType.ATTENDING) {
            ViewEventDetailsFragment bottomSheet = new ViewEventDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("interestedMode", true);
            bottomSheet.setArguments(bundle);
            bottomSheet.show(getChildFragmentManager(), getTag());
        }
    }
}

