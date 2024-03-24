package ca.cmput301t05.placeholder.ui.events.organizer_info;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import ca.cmput301t05.placeholder.Location.Successful_Checked_In_Activity;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.EventTable;
import ca.cmput301t05.placeholder.database.tables.ProfileTable;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;


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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (PlaceholderApp) getApplicationContext();
        setContentView(R.layout.view_attendee_checkin);

        profileTable = app.getProfileTable();

        event = app.getCachedEvent();
        eventID = event.getEventID();
        attendee_list = event.getAttendees();
        attendee_map = event.getMap(); // from event object gets the hashmap to get checkin count for each attendee

        attendDisplayed = new HashMap<String, Double>();
        RecyclerView checkInRecyclerView  = findViewById(R.id.attendee_checkin_listview);
        TextView emptyTextView = findViewById(R.id.emptyTextView);


        if (attendee_list.size() == 0){
            Toast.makeText(ViewAttendeeCheckinActivity.this, "You have no attendees!", Toast.LENGTH_SHORT).show();
            emptyTextView.setVisibility(View.VISIBLE);
            checkInRecyclerView.setVisibility(View.GONE);

        }
        else {
            for (int i = 0; i < attendee_list.size(); i++) { // they have attendees
                String uuid = attendee_list.get(i); // get their uuid to get their name

                profileTable.fetchDocument(uuid,  new Table.DocumentCallback<Profile>() {
                    @Override
                    public void onSuccess(Profile document){
                        // Do something after the event is successfully uploaded
                        attendDisplayed.put(document.getName(), attendee_map.get(uuid).get("Check_in_Times"));
                    }
                    @Override
                    public void onFailure(Exception e){
                        // Event upload failed, handle failure
                    }
                });
            }

            emptyTextView.setVisibility(View.GONE);
            checkInRecyclerView.setVisibility(View.VISIBLE);
            attendanceViewAdapter = new AttendanceViewAdapter(this, attendDisplayed);
            checkInRecyclerView.setAdapter(attendanceViewAdapter);


                // next get their checkin count from the event



        }







    }
}
