package ca.cmput301t05.placeholder.events;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ca.cmput301t05.placeholder.profile.Profile;

public class Event {

    String eventName;

    String eventPosterID;

    String eventInfo;


    HashMap<Profile,Integer> attendees; //stores all attendees and how many times they have checked in

    public Event(String name, String eventInfo){

        this.eventName = name;
        this.eventInfo = eventInfo;

    }

    //checks in a attendee
    public void checkIn(Profile profile){

        if (attendees.containsKey(profile)) {

            Integer i = attendees.get(profile);

            attendees.put(profile, i + 1);

        } else {

            attendees.put(profile, 1);
        }

    }

    public void removeAttendee(Profile profile){

        attendees.remove(profile);
    }

    //returns array of all attendees
    public Profile[] getAttendees(){


        return attendees.keySet().toArray(new Profile[0]);
    }

    //getters and setters


    public void setAttendees(HashMap<Profile, Integer> attendees) {
        this.attendees = attendees;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventPosterID(String eventPosterID) {
        this.eventPosterID = eventPosterID;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventPosterID() {
        return eventPosterID;
    }
}
