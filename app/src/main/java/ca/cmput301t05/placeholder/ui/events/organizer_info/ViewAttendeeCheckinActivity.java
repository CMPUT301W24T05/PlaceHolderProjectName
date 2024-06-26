package ca.cmput301t05.placeholder.ui.events.organizer_info;

import static java.nio.file.Paths.get;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.EventTable;
import ca.cmput301t05.placeholder.database.tables.ProfileTable;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;

/**
 * Activity for page containing a list of people who have checked into the event.
 * @author Anthony
 */
public class ViewAttendeeCheckinActivity extends AppCompatActivity { // Organizer can view who has checked into their event and how many times they have.

    private PlaceholderApp app;
    private EventTable eventTable;
    private ProfileTable profileTable;

    private  AttendanceViewAdapter attendanceViewAdapter;
    private Event event;
    private UUID eventID;
    private HashMap<String, HashMap<String, Double>> attendee_map; // Contains the profile ids of attendees

    private ArrayList<String> attendee_list;

    private HashMap<String, Double> attendDisplayed; // Hashmap displayed

    private Toolbar toolbar;


    /**
     * Creates the activity; sets up the UI, and retrieves event information from the database.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (PlaceholderApp) getApplicationContext();
        Log.e("amirza2", "about to set content view");
        setContentView(R.layout.view_attendee_checkin);
        profileTable = app.getProfileTable();
        eventTable = app.getEventTable();

        event = app.getCachedEvent();
        eventID = event.getEventID();

        toolbar = findViewById(R.id.toolbarCheckIn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Handle back button click event
            }
        });



        eventTable.fetchDocument(String.valueOf(eventID),  new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event document){
                attendee_list = document.getAttendees();
                attendee_map = document.getMap(); // from event object gets the hashmap to get checkin count for each attendee
                attendDisplayed = new HashMap<String, Double>();
                RecyclerView checkInRecyclerView  = findViewById(R.id.attendee_checkin_);
                TextView emptyTextView = findViewById(R.id.emptyTextView);

                if (attendee_list.size() == 0){
                    Toast.makeText(ViewAttendeeCheckinActivity.this, "You have no attendees!", Toast.LENGTH_SHORT).show();
                    emptyTextView.setVisibility(View.VISIBLE);
                    checkInRecyclerView.setVisibility(View.GONE);

                }
                else {
                    // Define a counter to keep track of the number of fetch operations completed
                    AtomicInteger fetchCount = new AtomicInteger(0);
                    // Define a variable to store the total number of fetch operations
                    int totalFetches = attendee_list.size();
                    Log.e("amirza2", "Number of attendees is:");

                    Log.e("amirza2", String.valueOf(attendee_list.size()));

                    for (int i = 0; i < attendee_list.size(); i++) { // they have attendees
                        String uuid = attendee_list.get(i); // get their uuid to get their name

                        profileTable.fetchDocument(uuid,  new Table.DocumentCallback<Profile>() {
                            @Override
                            public void onSuccess(Profile document){
                                // Do something after the event is successfully uploaded
                                Log.e("amirza2", String.valueOf(uuid));
                                Log.e("amirza2", String.valueOf(attendee_map.get(uuid).get("Check_in_Times")));
                                Log.e("amirza2", document.getName());
                                attendDisplayed.put(document.getName(), attendee_map.get(uuid).get("Check_in_Times"));
                                int count = fetchCount.incrementAndGet();
                                if (count == totalFetches) {
                                    // All fetch operations are completed, proceed with the code
                                    Log.e("amirza2","I am in the if statement for on success");
                                    emptyTextView.setVisibility(View.GONE);
                                    checkInRecyclerView.setVisibility(View.VISIBLE);
                                    Log.e("amirza2", "size is  "+attendDisplayed.size());

                                    attendanceViewAdapter = new AttendanceViewAdapter(ViewAttendeeCheckinActivity.this, attendDisplayed);
                                    Log.e("amirza2","about to set the adapter in on success");

                                    checkInRecyclerView.setAdapter(attendanceViewAdapter);
                                    checkInRecyclerView.setLayoutManager(new LinearLayoutManager(ViewAttendeeCheckinActivity.this));
                                    Log.e("amirza2","Should display by now!");
                                }

                            }
                            @Override
                            public void onFailure(Exception e){
                                int count = fetchCount.incrementAndGet();

                                // Check if all fetch operations are completed
                                if (count == totalFetches) {
                                    // All fetch operations are completed, proceed with the code
                                    Log.e("amirza2","I am in the if statement for on failure");

                                    emptyTextView.setVisibility(View.GONE);
                                    checkInRecyclerView.setVisibility(View.VISIBLE);
                                    Log.e("amirza2", "size is  "+attendDisplayed.size());
                                    attendanceViewAdapter = new AttendanceViewAdapter(ViewAttendeeCheckinActivity.this, attendDisplayed);
                                    Log.e("amirza2","about to set the adapter in on failure");

                                    checkInRecyclerView.setAdapter(attendanceViewAdapter);
                                    checkInRecyclerView.setLayoutManager(new LinearLayoutManager(ViewAttendeeCheckinActivity.this));
                                    Log.e("amirza2","Should display by now!");
                                }

                            }
                        });



                    }

                }

            }
            @Override
            public void onFailure(Exception e){
                Log.e("amirza2","Fail");


            }
        });

    }



}
