package ca.cmput301t05.placeholder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.MainActivity;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;

public class EventOrganized extends AppCompatActivity implements EventAdapter.OnItemClickListener{

    private PlaceholderApp app;
    private RecyclerView organizedEventsList;
    private EventAdapter organizedEventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_organized);

        // Initialize PlaceholderApp instance
        app = (PlaceholderApp) getApplicationContext();

        // Set up the list of created events
        ArrayList<Event> hostedEvents = new ArrayList<>(app.getHostedEvents().values());
        organizedEventsList = findViewById(R.id.listCreatedEvents);
        organizedEventsAdapter = new EventAdapter(getApplicationContext(), hostedEvents, EventAdapter.adapterType.HOSTED);
        organizedEventsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        organizedEventsList.setAdapter(organizedEventsAdapter);

        organizedEventsAdapter.setListener(this);

        // Initialize bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
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

        if (type == EventAdapter.adapterType.HOSTED){
            app.setCachedEvent(event);
            Intent i = new Intent(EventOrganized.this, EventMenuActivity.class);
            startActivity(i);
        } else if (type == EventAdapter.adapterType.ATTENDING) {
            app.setCachedEvent(event);
            //TODO send to the event info page for attendees

        }

    }
}
