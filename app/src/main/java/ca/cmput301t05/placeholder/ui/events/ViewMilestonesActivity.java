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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_milestones_updated);
        app = (PlaceholderApp) getApplicationContext();
        event = app.getCachedEvent();
        eventID = event.getEventID();
        eventTable = app.getEventTable();
        toolbar = findViewById(R.id.toolbar4Milestone);
        Log.e("amirza2", "GOT HERE");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Handle back button click event
            }
        });

        Log.e("amirza2", "GOT HERE too");
        eventTable.fetchDocument(String.valueOf(eventID),  new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event document) {
                Log.e("amirza2", "inside on success");
                mileStones = new ArrayList<>(document.getMilestones().keySet());
                Log.e("amirza2", String.valueOf(mileStones.size()));
                RecyclerView recyclerView = findViewById(R.id.recycler4Milestone);
                TextView textView = findViewById(R.id.emptyTextView4MileStone);
                if (mileStones.size() <= 0){ // No mile stones achieved. Nothing to display.
                    textView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else { // our event achieved some milestones.
                    ArrayList<String> data = new ArrayList<>();
                    for (int i = 0; i < mileStones.size(); i++){
                        Log.e("amirza2", mileStones.get(i));
                        data.add(generateMilestoneString(mileStones.get(i)));
                    }

                    recyclerView.setVisibility(View.VISIBLE);
                    Log.e("amirza2","recylcer view set to visible");
                    textView.setVisibility(View.GONE);
                    Log.e("amirza2","textview view set to gone");
                    milestoneListAdapter = new MilestoneListAdapter(ViewMilestonesActivity.this, data); // Setting the adapter
                    Log.e("amirza2","Created the the adapter");
                    recyclerView.setAdapter(milestoneListAdapter);
                    Log.e("amirza2","set the adapter");
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
            Log.e("amirza", String.valueOf((milestone.contains("EVENTSTART"))));
            return ("Event has reached max capacity.");
        }
    }


}
