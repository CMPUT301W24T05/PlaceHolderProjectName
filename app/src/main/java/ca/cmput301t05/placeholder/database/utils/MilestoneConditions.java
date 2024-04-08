package ca.cmput301t05.placeholder.database.utils;

import java.util.Calendar;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.milestones.Milestone;
import ca.cmput301t05.placeholder.milestones.MilestoneType;

/**
 * Checks to make sure conditions have been met
 *
 */
public class MilestoneConditions {

    /**
     *  Makes a milestone according to the type, if the event satisfies the conditions
     * @param event event we're checking
     * @param type type of milestone
     * @return newly generated milestone according to the type, null if conditions not met
     */

    public static Milestone checkConditionsMet(Event event, MilestoneType type){

        switch (type) {
            case FIRSTATTENDEE:
                if(!event.getAttendees().isEmpty()){
                    return new Milestone(type, event);
                }   else {
                    return null;
                }
            case FIRSTSIGNUP:
                if(!event.getRegisteredUsers().isEmpty()){
                    return new Milestone(type, event);
                }   else {
                    return null;
                }
            case HALFWAY:
                if((double) event.getAttendees().size() / event.getMaxAttendees() >= 0.5){
                    return new Milestone(type, event);
                }   else {
                    return null;
                }
            case FULLCAPACITY:
                if(event.getAttendees().size() == event.getMaxAttendees()){
                    return  new Milestone(type,event);
                } else {
                    return null;
                }
            case EVENTSTART:
                Calendar c = Calendar.getInstance();

                if (c.after(event.getEventDate())){
                    return new Milestone(type ,event);
                }   else {
                    return null;
                }

            default:
                return null;
        }

    }

    /**
     * checks if we contain a type
     * @param event
     * @param type
     * @return boolean is it's already contained
     */
    public static boolean alreadyContainsMilestone(Event event, MilestoneType type){

        if (event.getMilestones().get(type.getIdString()) != null){

            return true;
        }

        return false;

    }


}
