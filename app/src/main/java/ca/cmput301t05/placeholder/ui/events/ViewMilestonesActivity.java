package ca.cmput301t05.placeholder.ui.events;

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

        notifications = app.getUserNotifications();
        milestones = getMilestones(notifications);


        numAttendees = curEvent.getAttendees().size();
        capacity = curEvent.getMaxAttendees();
        numRegistered = curEvent.getRegisteredUsers().size();

        Log.d("Test", "num of attendees is " + String.valueOf(numAttendees));


        setMilestones();
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
        if (numAttendees / capacity == 2 && !containsMilestoneType(MilestoneType.HALFWAY)) {
            Milestone mHalfway = new Milestone(app.getUserProfile().getProfileID(), curEvent.getEventID(), MilestoneType.HALFWAY);
            milestones.add(mHalfway); // Add the milestone to the milestones array
            notifications.add(mHalfway); // Add the milestone to the notifications array
        }

        if (capacity == numAttendees && !containsMilestoneType(MilestoneType.FULLCAPACITY)) {
            Milestone mFull = new Milestone(app.getUserProfile().getProfileID(), curEvent.getEventID(), MilestoneType.FULLCAPACITY);
            milestones.add(mFull); // Add the milestone to the milestones array
            notifications.add(mFull); // Add the milestone to the notifications array
        }

        if (numAttendees >= 1 && !containsMilestoneType(MilestoneType.FIRSTATTENDEE)) {
            Milestone mFirstAttendee = new Milestone(app.getUserProfile().getProfileID(), curEvent.getEventID(), MilestoneType.FIRSTATTENDEE);
            milestones.add(mFirstAttendee); // Add the milestone to the milestones array
            notifications.add(mFirstAttendee); // Add the milestone to the notifications array
        }

        if (numRegistered >= 1 && !containsMilestoneType(MilestoneType.FIRSTSIGNUP)) {
            Milestone mFirstSignup = new Milestone(app.getUserProfile().getProfileID(), curEvent.getEventID(), MilestoneType.FIRSTSIGNUP);
            milestones.add(mFirstSignup); // Add the milestone to the milestones array
            notifications.add(mFirstSignup); // Add the milestone to the notifications array
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
