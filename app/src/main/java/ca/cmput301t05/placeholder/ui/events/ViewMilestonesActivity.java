package ca.cmput301t05.placeholder.ui.events;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.Milestone;
import ca.cmput301t05.placeholder.notifications.MilestoneType;
import ca.cmput301t05.placeholder.notifications.Notification;


import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.UUID;

public class ViewMilestonesActivity extends AppCompatActivity {


    private PlaceholderApp app;
    private Event curEvent;
    private ArrayList<Notification> notifications;
    private ArrayList<Milestone> milestones;
    private int capacity;
    private int numAttendees;
    private Button back;

    private TextView signee1_text, attendee1_text, halfway_text, full_text, start_text;
    private ImageView signee1_im, attendee1_im, halfway_im, full_im, start_im;
    private int numRegistered;

    private Calendar cal, now;
    private int progress;
    private ProgressBar milestoneBar;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_milestones);
        app = (PlaceholderApp) getApplicationContext();
        curEvent = app.getCachedEvent();

        signee1_text = findViewById(R.id.mile_signup);
        signee1_im  = findViewById(R.id.sign_up_icon);
        attendee1_text = findViewById(R.id.mile_attendee);
        attendee1_im = findViewById(R.id.attendee_icon);
        halfway_text = findViewById(R.id.mile_halfway);
        halfway_im = findViewById(R.id.halfway_icon);
        full_text = findViewById(R.id.mile_full);
        full_im  = findViewById(R.id.full_icon);
        start_text = findViewById(R.id.mile_start);
        start_im = findViewById(R.id.start_icon);
        milestones = getMilestones(curEvent);
        milestoneBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        progress = 0;



        numAttendees = curEvent.getAttendees().size();
        capacity = curEvent.getMaxAttendees();
        numRegistered = curEvent.getRegisteredUsers().size();
        now = Calendar.getInstance();
        cal = curEvent.getEventDate();

        int result = numAttendees / capacity;
        Log.d("isHalfway?: ", String.valueOf(result));

        Log.d("Test", "num of attendees is " + String.valueOf(numAttendees));



        //setMilestones();
        //setCheckBoxes();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





    }

    public ArrayList<Milestone> getMilestones(Event event){
        ArrayList<Milestone> Miles = new ArrayList<Milestone>();
        UUID eventID = event.getEventID();
        for(Milestone mile : app.getUserMilestones()){
            if (mile.getFromEventID().compareTo(eventID) == 0){
                Miles.add(mile);
            }
        }
        return Miles;

    }

    public void setMilestonesBar(){
        if (milestones != null) {
            for (Milestone milestone : milestones) {
                switch (milestone.getMType()) {
                    case FIRSTATTENDEE:
                        progress = progress +10;
                        break;
                    case FIRSTSIGNUP:
                        progress += 10;
                        break;
                    case HALFWAY:
                        progress += 10;
                        break;
                    case FULLCAPACITY:
                        progress += 10;
                        break;
                        //add checkbox for event start
                }
            }
            milestoneBar.setProgress(progress);

        }
    }
}
