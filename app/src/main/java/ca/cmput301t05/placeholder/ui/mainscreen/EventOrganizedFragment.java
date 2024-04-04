package ca.cmput301t05.placeholder.ui.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;
import ca.cmput301t05.placeholder.ui.events.creation.EnterEventDetailsActivity;

public class EventOrganizedFragment extends Fragment implements EventAdapter.OnItemClickListener{

    private PlaceholderApp app;
    private RecyclerView organizedEventsList;
    private EventAdapter organizedEventsAdapter;
    private ExtendedFloatingActionButton organize;

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
}
