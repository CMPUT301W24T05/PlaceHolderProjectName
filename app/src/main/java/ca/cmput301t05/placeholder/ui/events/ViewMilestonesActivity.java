package ca.cmput301t05.placeholder.ui.events;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.Milestone;
import ca.cmput301t05.placeholder.notifications.MilestoneType;
import ca.cmput301t05.placeholder.notifications.Notification;

public class ViewMilestonesActivity extends AppCompatActivity {


    private PlaceholderApp app;
    private Event curEvent;
    private ArrayList<Notification> notifications;
    private ArrayList<Milestone> milestones;
    private int capacity;
    private ArrayList<String> attendees;
    private int numAttendees;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.);
        app = (PlaceholderApp) getApplicationContext();
        curEvent = app.getCachedEvent();

        notifications = app.getUserNotifications();
        milestones = getMilestones(notifications);


        attendees = curEvent.getAttendees();
        numAttendees = attendees.size();
        capacity = curEvent.getMaxAttendees();

        if(capacity/numAttendees == 2 && !containsMilestoneType(MilestoneType.HALFWAY)){
            Milestone mHalfway = new Milestone(app.getUserProfile().getProfileID(), curEvent.getEventID(), MilestoneType.HALFWAY);
        }
        if(capacity == numAttendees && !containsMilestoneType(MilestoneType.FULLCAPACITY)){
            Milestone mFull = new Milestone(app.getUserProfile().getProfileID(), curEvent.getEventID(), MilestoneType.FULLCAPACITY);
        }
        if(numAttendees > 1 && !containsMilestoneType(MilestoneType.FIRSTATTENDEE)){
            Milestone mFull = new Milestone(app.getUserProfile().getProfileID(), curEvent.getEventID(), MilestoneType.FULLCAPACITY);
        }





    }

    public ArrayList<Milestone> getMilestones(ArrayList<Notification> notifications){
        ArrayList<Milestone> milestones = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification instanceof Milestone) {
                milestones.add((Milestone) notification);
            }
        }
        return milestones.isEmpty() ? null : milestones;
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
