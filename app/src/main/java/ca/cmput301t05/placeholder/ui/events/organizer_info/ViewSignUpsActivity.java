package ca.cmput301t05.placeholder.ui.events.organizer_info;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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
 * This activity displays the list of users who have signed up for an event.
 */
public class ViewSignUpsActivity extends AppCompatActivity {

    private PlaceholderApp app;
    private Event event;

    private ArrayList<String> signUps; // each element is an array of UUID string
    private RecyclerView signUpRecyclerView;

    private ProfileTable profileTable;

    private EventTable eventTable;

    private ArrayList<String> signupNames;

    private SignUpViewAdapter signUpViewAdapter;
    private Toolbar toolbar;



    private TextView emptyTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (PlaceholderApp) getApplicationContext();
        setContentView(R.layout.view_signups_activity);
        event = app.getCachedEvent();
        UUID eventID = event.getEventID();

        profileTable = app.getProfileTable();
        eventTable = app.getEventTable();

        toolbar = findViewById(R.id.toolbarSignups);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Handle back button click event
            }
        });


        eventTable.fetchDocument(String.valueOf(eventID), new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event document) {

                signUps = document.getRegisteredUsers();
                signupNames = new ArrayList<String>();
                int numOfSignUps  = signUps.size();
                signUpRecyclerView  = findViewById(R.id.signups_recyclerview);
                emptyTextView = findViewById(R.id.emptyTextView4Signup);
                Log.e("amirza2", String.valueOf(numOfSignUps));

                if (numOfSignUps <= 0){
                    signUpRecyclerView.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(ViewSignUpsActivity.this, "You have no signups!", Toast.LENGTH_SHORT).show();
                }
                else {
                    final AtomicInteger counter = new AtomicInteger(0); // Counter to track successful fetches
                    for (int i = 0; i < numOfSignUps; i++) {
                        String uuid = signUps.get(i);
                        profileTable.fetchDocument(uuid, new Table.DocumentCallback<Profile>() {
                            @Override
                            public void onSuccess(Profile document) {
                                signupNames.add(document.getName()); // Add the profile's name to the array list to display
                                if (counter.incrementAndGet() == numOfSignUps) { // Check if all documents have been fetched
                                    signUpViewAdapter = new SignUpViewAdapter(ViewSignUpsActivity.this, signupNames);
                                    signUpRecyclerView.setVisibility(View.VISIBLE);
                                    emptyTextView.setVisibility(View.GONE);
                                    signUpRecyclerView.setAdapter(signUpViewAdapter);
                                    signUpRecyclerView.setLayoutManager(new LinearLayoutManager(ViewSignUpsActivity.this));
                                }
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.e("amirza2", "Profile table access failed inside ViewSignUpsActivity");
                                if (counter.incrementAndGet() == numOfSignUps) { // Check if all documents have been fetched
                                    // If there's a failure, still try to set up the adapter
                                    signUpViewAdapter = new SignUpViewAdapter(ViewSignUpsActivity.this, signupNames);
                                    signUpRecyclerView.setVisibility(View.VISIBLE);
                                    emptyTextView.setVisibility(View.GONE);
                                    signUpRecyclerView.setAdapter(signUpViewAdapter);
                                    signUpRecyclerView.setLayoutManager(new LinearLayoutManager(ViewSignUpsActivity.this));
                                }
                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(Exception e) {
                Log.e("amirza2", "Failed in ViewSignUpsActivity");
            }
        });

    }
}
