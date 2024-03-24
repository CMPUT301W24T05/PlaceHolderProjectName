package ca.cmput301t05.placeholder.ui.events;

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
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;

public class EventExplore extends AppCompatActivity {

    private PlaceholderApp app;
    private RecyclerView allEventsList;
    private EventAdapter allEventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_explore);

        app = (PlaceholderApp) getApplicationContext();

        // Initialize bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        fetchAllEvents();

        allEventsList = findViewById(R.id.listAllEvents);
        allEventsAdapter = new EventAdapter(getApplicationContext(), new ArrayList<Event>());
        allEventsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        allEventsList.setAdapter(allEventsAdapter);
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
}
