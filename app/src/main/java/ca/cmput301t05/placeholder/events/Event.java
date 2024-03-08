package ca.cmput301t05.placeholder.events;

import ca.cmput301t05.placeholder.database.DocumentSerializable;
import ca.cmput301t05.placeholder.profile.Profile;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Event extends DocumentSerializable implements Serializable {

    String eventName;

    UUID eventPosterID;

    String eventInfo;

    public QRCode infoQRCode, checkInQR;

    QRCodeManager QRCM = new QRCodeManager();

    UUID eventID;

    Calendar eventDate;

    int maxAttendees;

    HashMap<String, Integer> attendees; //stored this way for the database
    //string = profileID, int = # of times checked in

    public Event(){
        this.eventID = UUID.randomUUID();
        this.attendees = new HashMap<>();
        infoQRCode = QRCM.generateQRCode(this, "eventInfo");
        checkInQR = QRCM.generateQRCode(this, "checkIn");
    }

    public Event(UUID eventID){
        //please call getEventDatabase to this otherwise will error
        this.eventID = eventID;
        this.attendees = new HashMap<>();
        infoQRCode = QRCM.generateQRCode(this, "eventInfo");
    }


    public Event(String name, String eventInfo, int maxAttendees){


        this.eventName = name;
        this.eventInfo = eventInfo;
        this.eventID = UUID.randomUUID();
        this.maxAttendees = maxAttendees;
        this.attendees = new HashMap<>();
        infoQRCode = QRCM.generateQRCode(this, "eventInfo");
    }

    @Exclude
    public Map<String, Object> toDocument() {
        Map<String, Object> document = new HashMap<>();

        document.put("eventName", eventName);
        document.put("eventPosterID", eventPosterID != null ? eventPosterID.toString() : null);
        document.put("eventInfo", eventInfo);
        //document.put("infoQRCode", infoQRCode);
        //document.put("checkInQR", checkInQR);
        document.put("eventID", eventID.toString());
        document.put("eventDate", eventDate != null ? new Timestamp(eventDate.getTime()) : null);
        document.put("maxAttendees", maxAttendees);
        document.put("attendees", attendees);

        return document;
    }

    public void fromDocument(DocumentSnapshot document) {
        String eventId = document.getString("eventID");
        if(eventId != null && !eventId.equals("null")) {
            eventID = UUID.fromString(eventId);
        }
        if(document.getString("eventName") != null) {
            eventName = document.getString("eventName");
        }
        if(document.getString("eventInfo") != null) {
            eventInfo = document.getString("eventInfo");
        }
        String eventPosterId = document.getString("eventPosterID");
        if(eventPosterId != null && !eventPosterId.equals("null")) {
            eventPosterID = UUID.fromString(eventPosterId);
        }
        //infoQRCode = ; // Convert back from string
        //checkInQR = ; // Convert back from string
        if(document.getTimestamp("eventDate") != null) {
            eventDate = Calendar.getInstance();
            eventDate.setTimeInMillis(document.getTimestamp("eventDate").toDate().getTime());
        }
        if(document.getLong("maxAttendees") != null) {
            maxAttendees = document.getLong("maxAttendees").intValue();
        }
        if(document.get("attendees") != null) {
            attendees = (HashMap<String, Integer>) document.get("attendees");
        }
    }

    //checks in a attendee
    public boolean checkIn(Profile profile){

        //returns false if attendee can't check in, true if can

        if(attendees.size() == maxAttendees){
            return false;
        }

        if (attendees.containsKey(profile.getProfileID().toString())) {

            Integer i = attendees.get(profile.getProfileID().toString());

            attendees.put(profile.getProfileID().toString(), i + 1);

        } else {

            attendees.put(profile.getProfileID().toString(), 1);
        }

        return true;

    }

    public void removeAttendee(Profile profile){

        attendees.remove(profile.getProfileID().toString());
    }

    //returns array of all attendees
    public String[] getAttendees(){


        return attendees.keySet().toArray(new String[0]);
    }

    //getters and setters


    public void setAttendees(HashMap<String, Integer> attendees) {
        this.attendees = attendees;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }


    public String getEventInfo() {
        return eventInfo;
    }

    public String getEventName() {
        return eventName;
    }

    public UUID getEventPosterID() {
        return eventPosterID;
    }

    public UUID getEventID(){
        return  eventID;
    }

    public void setEventPosterID(UUID id){
        this.eventPosterID = id;
    }

    public void setEventID(UUID eventID) {
        this.eventID = eventID;
    }

    public void setEventDate(Calendar eventDate) {
        this.eventDate = eventDate;
    }

    public void setMaxAttendees(int c){
        this.maxAttendees = c;
    }

    public int getMaxAttendees(){
        return this.maxAttendees;
    }

}



