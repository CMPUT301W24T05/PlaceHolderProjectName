package ca.cmput301t05.placeholder.milestones;

import java.io.Serializable;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.MilestoneType;

/**
 * Milestone class which we can call to the database and allow us to grab events milestones
 *
 */
public class Milestone implements Serializable {

    private MilestoneType mType;

    private String message;

    private String eventID;
    private String eventName;


    Milestone(MilestoneType type, Event event){

        this.mType = type;
        this.eventID = event.getEventID().toString();
        this.eventName = event.getEventName();

        this.message = generateMessage(this.mType, this.eventName);

    }

    private String generateMessage(MilestoneType mType, String eventName) {
        // Generate message based on the milestone type
        switch (mType) {
            case FIRSTATTENDEE:
                return "From "+eventName+": YOU'VE GOT YOUR FIRST ATTENDEE!";
            case FIRSTSIGNUP:
                return "From: "+eventName+" YOU'VE GOT YOUR FIRST SIGNUP!";
            case HALFWAY:
                return "From: "+eventName+" YOU'RE HALFWAY TO YOUR EVENT CAPACITY!";
            case FULLCAPACITY:
                return "From: "+eventName+" YOUR EVENT IS NOW AT FULL CAPACITY! CONGRATS!!";
            case EVENTSTART:
                return "From: "+eventName+" YOUR EVENT HAS BEGUN";
            default:
                return "Default message";
        }
    }




}
