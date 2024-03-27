package ca.cmput301t05.placeholder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;
import ca.cmput301t05.placeholder.ui.events.EventSignUpActivity;


public class EventExploreFragment extends Fragment implements EventAdapter.OnItemClickListener{

    private PlaceholderApp app;
    private RecyclerView allEventsList;
    private EventAdapter allEventsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_explore, container, false);

        app = (PlaceholderApp) getActivity().getApplicationContext();

        allEventsList = view.findViewById(R.id.listAllEvents);
        allEventsAdapter = new EventAdapter(getContext(), new ArrayList<>(), EventAdapter.adapterType.ATTENDING); // Use getContext() here
        allEventsList.setLayoutManager(new LinearLayoutManager(getContext()));
        allEventsList.setAdapter(allEventsAdapter);

        allEventsAdapter.setListener(this);

        fetchAllEvents(); // Fetch events after initializing the adapter
        return view;
    }

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

    private void displayEvents(ArrayList<Event> events) {
        // Update the RecyclerView with fetched events
        allEventsAdapter.setEvents(events);
        allEventsAdapter.notifyDataSetChanged();
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
            Intent i = new Intent(getActivity(), EventSignUpActivity.class);
            startActivity(i);
        }
    }
}

