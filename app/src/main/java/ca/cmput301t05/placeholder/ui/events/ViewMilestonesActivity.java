package ca.cmput301t05.placeholder.ui.events;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.EventTable;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.milestones.MilestoneConditions;
import ca.cmput301t05.placeholder.notifications.Milestone;
import ca.cmput301t05.placeholder.notifications.Notification;
import ca.cmput301t05.placeholder.ui.events.organizer_info.ViewAttendeeCheckinActivity;


import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ViewMilestonesActivity extends AppCompatActivity {


    private PlaceholderApp app;
    private Event event;
    private UUID eventID;
    private Toolbar toolbar;
    private List<String> mileStones;

    private EventTable eventTable;
    private MilestoneListAdapter milestoneListAdapter;

    RecyclerView recyclerView;
    ArrayList<String> data;
    TextView textView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_milestones_updated);
        app = (PlaceholderApp) getApplicationContext();
        event = app.getCachedEvent();
        eventID = event.getEventID();
        eventTable = app.getEventTable();
        toolbar = findViewById(R.id.toolbar4Milestone);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Handle back button click event
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler4Milestone);
        TextView textView = findViewById(R.id.emptyTextView4MileStone);
        eventTable.fetchDocument(String.valueOf(eventID),  new Table.DocumentCallback<Event>() {

            @Override
            public void onSuccess(Event document) {

                MilestoneConditions.milestoneHandling(app, document, new MilestoneConditions.milestoneCallback() { // Before setting up UI update the event in db to retrieve new milestones.
                    @Override
                    public void onSuccess() { // Get the list of milestones
                        // updates the event in the db
                        Log.e("amirza2", "GOT TO ON SUCCESS IN MILESTONE HANDELING");
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("MILESTONE_HANDLING", String.valueOf(e));
                    }
                });

                // Handle UI here

                if (document.getMilestones() != null){
                    mileStones = new ArrayList<>(document.getMilestones().keySet());
                    data = new ArrayList<>(); // Not necessary but I'll refactor it later if I have time.
                    for (int i = 0; i < mileStones.size(); i++){
                        data.add(generateMilestoneString(mileStones.get(i)));
                    }
                }

                if (data == null || data.size() == 0){ // No mile stones achieved. Nothing to display.
                    textView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else { // our event achieved some milestones.

                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    milestoneListAdapter = new MilestoneListAdapter(ViewMilestonesActivity.this, data); // Setting the adapter
                    recyclerView.setAdapter(milestoneListAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ViewMilestonesActivity.this));
                }

            }

            @Override
            public void onFailure(Exception e) {
                Log.e("FAIL", "Failure to fetch event from firebase in ViewMileStonesActivity class.");
            }

        });

    }

    /**
     * Returns the corresponding message for the milestone provided.
     * @param milestone A string representing the milestone the event achieved.
     * @return A message representing the milestone achieved.
     */
    private String generateMilestoneString(String milestone){
        milestone = milestone.replaceAll("\\s+$", ""); // Remove all trailing white spaces
        milestone = milestone.replaceAll("^\\s+", ""); // Remove preceding white spaces
        if (milestone.contains("EVENTSTART")){ // I have no clue why but doing milestone == "EVENTSTART" did not work. The two strings were the same, but they were off by some white space character I did not care enough find.
            return ("Your event has started.");
        } else if (milestone.contains("FIRSTATTENDEE")) {
            return ("Your first attendee checked in.");
        } else if (milestone.contains("FIRSTSIGNUP")) {
            return ("Someone has signed up to your event.");
        } else if (milestone.contains("EVENTEND")) {
            return ("Your event has ended.");
        } else if (milestone.contains("HALFWAY")) {
            return ("Your event is halfway done.");
        } else{ // milestone == "FULLCAPACITY"
            return ("Event has reached max capacity.");
        }
    }





}
