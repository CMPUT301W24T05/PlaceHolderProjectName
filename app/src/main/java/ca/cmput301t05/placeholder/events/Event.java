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

/**
 * Represents an event, including information about the event name, organizer, date,
 * and attendees. Supports serialization for Firebase database storage and retrieval.
 */
public class Event extends DocumentSerializable implements Serializable {

    String eventName;

    UUID eventPosterID;

    String eventInfo;

    QRCode infoQRCode, checkInQR;

    QRCodeManager QRCM = new QRCodeManager();

    UUID eventID;

    Calendar eventDate;

    int maxAttendees;

    HashMap<String, Integer> attendees; //stored this way for the database
    //string = profileID, int = # of times checked in

    /**
     * Default constructor that generates a new event with unique ID and empty attendee list.
     * Also generates QR codes for event information and check-in.
     */
    public Event(){
        this.eventID = UUID.randomUUID();
        this.attendees = new HashMap<>();
        infoQRCode = QRCM.generateQRCode(this, "eventInfo");
        checkInQR = QRCM.generateQRCode(this, "checkIn");
    }

    /**
     * Constructor that initializes an event with a specific UUID.
     * Intended for use when retrieving an existing event from the database.
     * @param eventID The UUID of the event.
     */
    public Event(UUID eventID){
        this.eventID = eventID;
        this.attendees = new HashMap<>();
        infoQRCode = QRCM.generateQRCode(this, "eventInfo");
        checkInQR = QRCM.generateQRCode(this, "checkIn");
    }

    /**
     * Constructor that creates a new event with the specified name, information, and maximum number of attendees.
     * Generates unique ID for the event and QR codes for event information and check-in.
     * @param name The name of the event.
     * @param eventInfo The detailed information about the event.
     * @param maxAttendees The maximum number of attendees allowed for this event.
     */

    public Event(String name, String eventInfo, int maxAttendees){


        this.eventName = name;
        this.eventInfo = eventInfo;
        this.eventID = UUID.randomUUID();
        this.maxAttendees = maxAttendees;
        this.attendees = new HashMap<>();
        infoQRCode = QRCM.generateQRCode(this, "eventInfo");
        checkInQR = QRCM.generateQRCode(this, "checkIn");
    }

    /**
     * Converts this event object into a Map that can be used for document storage in Firebase Firestore.
     * @return A Map representing the event's data.
     */
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
    /**
     * Populates the event object's fields based on a Firestore document snapshot.
     * @param document The Firestore document snapshot representing an event.
     */
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


    /**
     * Attempts to check in an attendee to the event.
     * If the maximum number of attendees has been reached, no more attendees can be checked in.
     * @param profile The profile of the attendee trying to check in.
     * @return True if the attendee was successfully checked in, false otherwise.
     */
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

    /**
     * Removes an attendee from the event.
     * @param profile The profile of the attendee to remove.
     */
    public void removeAttendee(Profile profile){

        attendees.remove(profile.getProfileID().toString());
    }

    /**
     * Retrieves an array of all attendee IDs.
     * @return An array of Strings, each representing a unique attendee ID.
     */
    public String[] getAttendees(){


        return attendees.keySet().toArray(new String[0]);
    }

    //getters and setters


    /**
     * Sets the attendees for the event.
     * @param attendees A HashMap where the key is the attendee's profile ID as a String, and the value is the number of times they've checked in.
     */
    public void setAttendees(HashMap<String, Integer> attendees) {
        this.attendees = attendees;
    }


    /**
     * Sets the detailed information about the event.
     * @param eventInfo A string containing the event's information.
     */
    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }

    /**
     * Sets the name of the event.
     * @param eventName A string representing the name of the event.
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Retrieves the detailed information about the event.
     * @return A string containing the event's information.
     */
    public String getEventInfo() {
        return eventInfo;
    }

    /**
     * Retrieves the name of the event.
     * @return A string representing the name of the event.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Retrieves the UUID of the event poster (the organizer).
     * @return The UUID of the event poster.
     */
    public UUID getEventPosterID() {
        return eventPosterID;
    }

    /**
     * Retrieves the UUID of the event.
     * @return The UUID of the event.
     */
    public UUID getEventID(){
        return  eventID;
    }

    /**
     * Sets the UUID of the event poster
     * @param id The UUID to be set as the event poster's ID.
     */
    public void setEventPosterID(UUID id){
        this.eventPosterID = id;
    }

    /**
     * Sets the UUID of the event.
     * @param eventID The UUID to be set for the event.
     */
    public void setEventID(UUID eventID) {
        this.eventID = eventID;
    }

    /**
     * Sets the date and time for the event.
     * @param eventDate A Calendar instance representing the date and time of the event.
     */
    public void setEventDate(Calendar eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Sets the maximum number of attendees allowed for the event.
     * @param c The maximum number of attendees as an integer.
     */
    public void setMaxAttendees(int c){
        this.maxAttendees = c;
    }

    /**
     * Retrieves the maximum number of attendees allowed for the event.
     * @return The maximum number of attendees as an integer.
     */
    public int getMaxAttendees(){
        return this.maxAttendees;
    }

}



