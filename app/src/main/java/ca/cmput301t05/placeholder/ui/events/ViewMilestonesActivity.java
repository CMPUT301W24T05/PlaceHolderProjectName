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
import android.widget.TextView;

import java.util.Calendar;

public class ViewMilestonesActivity extends AppCompatActivity {


    private PlaceholderApp app;
    private Event curEvent;
    private ArrayList<Notification> notifications;
    private ArrayList<Milestone> milestones;
    private int capacity;
    private int numAttendees;
    private Button back;

    private CheckBox checkBoxFirstAttendee;
    private CheckBox checkBoxFirstSignup;
    private CheckBox checkBoxHalfway;
    private CheckBox checkBoxFullCapacity;

    private int numRegistered;

    private Calendar cal, now;
    private TextView check;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_milestones);
        app = (PlaceholderApp) getApplicationContext();
        curEvent = app.getCachedEvent();

        checkBoxFirstAttendee = findViewById(R.id.checkBox_first_attendee);
        checkBoxFirstSignup = findViewById(R.id.checkBox_first_signup);
        checkBoxHalfway = findViewById(R.id.checkBox_halfway);
        checkBoxFullCapacity = findViewById(R.id.checkBox_full_capacity);
        back = findViewById(R.id.back_milestones);
        check = findViewById(R.id.check_test);
        notifications = app.getUserNotifications();
        milestones = getMilestones(notifications);




        numAttendees = curEvent.getAttendees().size();
        capacity = curEvent.getMaxAttendees();
        numRegistered = curEvent.getRegisteredUsers().size();
        now = Calendar.getInstance();
        cal = curEvent.getEventDate();

        int result = numAttendees / capacity;
        Log.d("isHalfway?: ", String.valueOf(result));

        Log.d("Test", "num of attendees is " + String.valueOf(numAttendees));

        check.setText(numAttendees +" / "+ capacity);


        //setMilestones();
        setCheckBoxes();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





    }

    public void setMilestones(){
        //needs to auto push notifications
        if ((double) numAttendees / capacity >= 0.5 && !containsMilestoneType(MilestoneType.HALFWAY)) {
            int result = numAttendees / capacity;
            Milestone mHalfway = new Milestone(app.getUserProfile().getProfileID(), curEvent.getEventID(), MilestoneType.HALFWAY, curEvent.getEventName());
            milestones.add(mHalfway); // Add the milestone to the milestones array
            notifications.add(mHalfway); // Add the milestone to the notifications array
            Log.d("isHalfway?: ", String.valueOf(result));
        }

        if (capacity == numAttendees && !containsMilestoneType(MilestoneType.FULLCAPACITY)) {
            Milestone mFull = new Milestone(app.getUserProfile().getProfileID(), curEvent.getEventID(), MilestoneType.FULLCAPACITY, curEvent.getEventName());
            milestones.add(mFull); // Add the milestone to the milestones array
            notifications.add(mFull); // Add the milestone to the notifications array
        }

        if (numAttendees >= 1 && !containsMilestoneType(MilestoneType.FIRSTATTENDEE)) {
            Milestone mFirstAttendee = new Milestone(app.getUserProfile().getProfileID(), curEvent.getEventID(), MilestoneType.FIRSTATTENDEE, curEvent.getEventName());
            milestones.add(mFirstAttendee); // Add the milestone to the milestones array
            notifications.add(mFirstAttendee); // Add the milestone to the notifications array
        }
        // change to cal validation
        if (now.compareTo(cal) > 0 && !containsMilestoneType(MilestoneType.EVENTSTART)) {
            Milestone mEventStart = new Milestone(app.getUserProfile().getProfileID(), curEvent.getEventID(), MilestoneType.FIRSTSIGNUP, curEvent.getEventName());
            milestones.add(mEventStart); // Add the milestone to the milestones array
            notifications.add(mEventStart); // Add the milestone to the notifications array
        }

        if (numRegistered >= 1 && !containsMilestoneType(MilestoneType.EVENTEND)) {
            Milestone mEventEnd = new Milestone(app.getUserProfile().getProfileID(), curEvent.getEventID(), MilestoneType.FIRSTSIGNUP, curEvent.getEventName());
            milestones.add(mEventEnd); // Add the milestone to the milestones array
            notifications.add(mEventEnd); // Add the milestone to the notifications array
        }
    }

    public void setCheckBoxes(){
        if (milestones != null) {
            for (Milestone milestone : milestones) {
                switch (milestone.getMType()) {
                    case FIRSTATTENDEE:
                        checkBoxFirstAttendee.setChecked(true);
                        break;
                    case FIRSTSIGNUP:
                        checkBoxFirstSignup.setChecked(true);
                        break;
                    case HALFWAY:
                        checkBoxHalfway.setChecked(true);
                        break;
                    case FULLCAPACITY:
                        checkBoxFullCapacity.setChecked(true);
                        break;
                        //add checkbox for event start
                }
            }
        }
    }

    public ArrayList<Milestone> getMilestones(ArrayList<Notification> notifications){
        ArrayList<Milestone> milestones = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification instanceof Milestone) {
                milestones.add((Milestone) notification);
            }
        }
        return milestones;
    }

    public boolean containsMilestoneType(MilestoneType type) {
        if (milestones == null) {
            return false; // If milestones array is null, return false
        }

        for (Milestone milestone : milestones) {
            if (milestone.getMType() == type) {
                return true; // If milestone of specified type found, return true
            }
        }

        return false; // If no milestone of specified type found, return false
    }


}
