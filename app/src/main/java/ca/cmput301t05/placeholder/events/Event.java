package ca.cmput301t05.placeholder.events;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.utils.DocumentSerializable;
import ca.cmput301t05.placeholder.profile.Profile;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Represents an event, including information about the event name, organizer, date,
 * and attendees. Supports serialization for Firebase database storage and retrieval.
 *
 *
 */
public class Event extends DocumentSerializable implements Serializable {
    private String eventName;
    private UUID eventPosterID;
    private Bitmap eventPosterBitmap;
    private UUID eventCreator;
    private String eventInfo;
    private String infoQRCode, checkInQR; //qr codes are represented by their string
    private UUID eventID;
    private Calendar eventDate;
    private String eventLocation;
    private int maxAttendees;
    // Attendee is a dictionary (key is the profileID, value is another hashmap>
    // The inner hashmap (key is "longitude", "latitude", "Check_in_Times", all values are double)
    // if longitude and latitude are not shared, then it will be Null
    // when displaying checked in times, should convert to integer
    private HashMap<String, HashMap<String, Double>> attendees; //stored this way for the database
    private ArrayList<String> registeredUsers;

    private Long attendeesNum, registeredUsersNum; //used so we can show how many people are attending / registered

    private ArrayList<String> notifications; //notifications that are sent for this specific event

    private ArrayList<String> milestones; //This is for adding milestones




    /**
     * Default constructor that generates a new event with unique ID and empty attendee list.
     */
    public Event(){
        this.eventID = UUID.randomUUID();
        this.attendees = new HashMap<>();
        this.notifications = new ArrayList<>();
        this.registeredUsers = new ArrayList<>();
        this.milestones = new ArrayList<>();
    }

    public Event(DocumentSnapshot snapshot){
        this.fromDocument(snapshot);
    }

    /**
     * Constructor that initializes an event with a specific UUID.
     * Intended for use when retrieving an existing event from the database.
     * @param eventID The UUID of the event.
     */
    public Event(UUID eventID){
        this();
        this.eventID = eventID;
        this.attendees = new HashMap<>();
        this.notifications = new ArrayList<>();
        this.registeredUsers = new ArrayList<>();
        this.attendeesNum = 0L;
        this.registeredUsersNum = 0L;
        this.milestones = new ArrayList<>();
    }

    /**
     * Constructor that creates a new event with the specified name, information, and maximum number of attendees.
     * Generates unique ID for the event and QR codes for event information and check-in.
     * @param name The name of the event.
     * @param eventInfo The detailed information about the event.
     * @param maxAttendees The maximum number of attendees allowed for this event.
     */

    public Event(String name, String eventInfo, int maxAttendees){
        this();
        this.eventName = name;
        this.eventInfo = eventInfo;
        this.maxAttendees = maxAttendees;
        this.attendees = new HashMap<>();
        this.notifications = new ArrayList<>();
        this.registeredUsers = new ArrayList<>();
        this.attendeesNum = 0L;
        this.registeredUsersNum = 0L;
        this.milestones = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (eventCreator != null) if (eventCreator.equals(event.eventCreator))
            if (eventID != null) return eventID.equals(event.eventID);
        return false;
    }

    @Override
    public int hashCode() {
        int result = eventCreator.hashCode();
        result = 31 * result + eventID.hashCode();
        return result;
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
        document.put("infoQRCode", infoQRCode);
        document.put("checkInQR", checkInQR);
        document.put("eventID", eventID.toString());
        document.put("eventDate", eventDate != null ? new Timestamp(eventDate.getTime()) : null);
        document.put("maxAttendees", maxAttendees);
        document.put("attendees", attendees);
        document.put("eventLocation", eventLocation);
        document.put("eventCreator", eventCreator.toString());
        document.put("notifications", this.notifications);
        document.put("registeredUsers", this.registeredUsers);
        document.put("milestones", this.milestones);
        return document;
    }

    // Method to get String value from a document
    private String getStringValueFromDocument(DocumentSnapshot document, String key) {
        return document.getString(key);
    }

    // Method to get UUID from a document
    private UUID getUUIDFromDocument(DocumentSnapshot document, String key) {
        String uuidAsString = getStringValueFromDocument(document, key);
        return (uuidAsString != null && !uuidAsString.equals("null"))? UUID.fromString(uuidAsString) : null;
    }

    /**
     * Populates the event object's fields based on a Firestore document snapshot.
     * @param document The Firestore document snapshot representing an event.
     */
    public void fromDocument(DocumentSnapshot document) {
        eventID = getUUIDFromDocument(document, "eventID");
        eventCreator = getUUIDFromDocument(document, "eventCreator");
        eventName = getStringValueFromDocument(document, "eventName");
        eventInfo = getStringValueFromDocument(document, "eventInfo");
        eventPosterID = getUUIDFromDocument(document, "eventPosterID");
        infoQRCode = getStringValueFromDocument(document, "infoQRCode");
        checkInQR = getStringValueFromDocument(document, "checkInQR");
        eventLocation = getStringValueFromDocument(document, "eventLocation");

        if(document.getTimestamp("eventDate") != null) {
            eventDate = Calendar.getInstance();
            eventDate.setTimeInMillis(Objects.requireNonNull(document.getTimestamp("eventDate")).toDate().getTime());
        }
        if(document.getLong("maxAttendees") != null) {
            maxAttendees = Objects.requireNonNull(document.getLong("maxAttendees")).intValue();
        }
        if(document.get("attendees") != null) {
            attendees = (HashMap<String, HashMap<String, Double>>) document.get("attendees");
        }

        if(document.get("notifications") != null){
            notifications = (ArrayList<String>) document.get("notifications");
        }
        if(document.get("registeredUsers") != null){
            registeredUsers = (ArrayList<String>) document.get("registeredUsers");
        }

        if (registeredUsers != null){
            this.registeredUsersNum = (long) registeredUsers.size();
        }   else {
            this.registeredUsersNum = 0L;
        }

        if (attendees != null){
            this.attendeesNum = (long) attendees.size();
        }   else {
            this.attendeesNum = 0L;
        }

        if (document.get("milestones") != null){

            this.milestones = (ArrayList<String>) document.get("milestones");
        }



    }


    /**
     * Attempts to check in an attendee to the event.
     * If the maximum number of attendees has been reached, no more attendees can be checked in.
     * @param profile The profile of the attendee trying to check in.
     * @param longitude The longitude of user when he checked in
     * @param latitude The latitude of user when he checked in
     */
    public void checkIn(Profile profile, Double longitude, Double latitude){

        if (attendees.containsKey(profile.getProfileID().toString())) {
            // Since it contains the key (this means this is user's second time checked in)
            // We will Retrieve the inner HashMap corresponding to the profile ID
            HashMap<String, Double> attendeeInfo = attendees.get(profile.getProfileID().toString());
            Double i = attendeeInfo.get("Check_in_Times");
            attendeeInfo.put("Check_in_Times", i + 1);
            attendeeInfo.put("longitude", longitude);
            attendeeInfo.put("latitude", latitude);

            attendees.replace(profile.getProfileID().toString(), attendeeInfo);

        } else {
            // first time checking in
            HashMap<String, Double> attendeeInfo = new HashMap<>();
            attendeeInfo.put("Check_in_Times", 1.0);
            attendeeInfo.put("longitude", longitude);
            attendeeInfo.put("latitude", latitude);
            attendees.put(profile.getProfileID().toString(), attendeeInfo);
            this.attendeesNum += 1;
        }
    }
    // Check if maximum capacity is reached
    public boolean reachMaxCapacity(){
        return (attendees.size() == maxAttendees);
    }

    /**
     * Removes an attendee from the event.
     * @param profile The profile of the attendee to remove.
     */
    public void removeAttendee(Profile profile){

        attendees.remove(profile.getProfileID().toString());
        this.attendeesNum -= 1;
    }

    /**
     * Retrieves an array of all attendee IDs.
     *
     * @return an arrayList of strings pertaining to the attendee's ids
     */
    public ArrayList<String> getAttendees(){

        return new ArrayList<>(attendees.keySet());
    }

    public ArrayList<String> getRegisteredUsers() {
        return registeredUsers;
    }

    /**
     * put user ID to a list of registered users of an event in eventTable
     *
     * @return boolean (true if succesful sign up, false if the user have already signed up
     */
    public void userSignup(Profile user){
        this.registeredUsers.add(user.getProfileID().toString());
        this.registeredUsersNum += 1;
    }

    public boolean userHasSignedUp(Profile user){
        return this.registeredUsers.contains(user.getProfileID().toString());
    }

    public void userUnsignup(Profile user){
        this.registeredUsers.remove(user.getProfileID().toString());
        this.registeredUsersNum -= 1;
    }

    public HashMap<String, HashMap<String, Double>> getMap(){ // Need to access check in count for each attendee
        return this.attendees;
    }

    public int getNumAttendees(){
        return new ArrayList<>(attendees.keySet()).size();
    }

    //getters and setters


    /**
     * Sets the attendees for the event.
     * @param attendees A HashMap where the key is the attendee's profile ID as a String, and the value is the number of times they've checked in.
     */
    public void setAttendees(HashMap<String, HashMap<String, Double>> attendees) {
        this.attendees = attendees;
        this.attendeesNum = (long) attendees.size();
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
        return this.eventInfo;
    }

    /**
     * Retrieves the name of the event.
     * @return A string representing the name of the event.
     */
    public String getEventName() {

        return this.eventName;
    }

    /**
     * Retrieves the UUID of the event poster (the organizer).
     * @return The UUID of the event poster.
     */
    public UUID getEventPosterID() {
        return this.eventPosterID;
    }

    /**
     * Retrieves the UUID of the event.
     * @return The UUID of the event.
     */
    public UUID getEventID(){
        return  this.eventID;
    }

    public String getLocation(){
        return this.eventLocation;
    }

    public void setEventLocation(String location){
        this.eventLocation = location;
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
     *
     * @return returns a calendar corresponding to the date
     */
    public Calendar getEventDate() {
        return eventDate;
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

    public void addNotification(String notificationID){
        this.notifications.add(notificationID);
    }

    public void removeNotification(String notificationID){
        this.notifications.remove(notificationID);
    }

    public void setEventCreator(UUID eventCreator){ this.eventCreator = eventCreator;}

    public UUID getEventCreator(){return this.eventCreator;}

    public String getCheckInQR() {
        return checkInQR;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public String getInfoQRCode() {
        return infoQRCode;
    }

    public void setCheckInQR(String checkInQR) {
        this.checkInQR = checkInQR;
    }

    public void setInfoQRCode(String infoQRCode) {
        this.infoQRCode = infoQRCode;
    }

    public Bitmap getEventPosterBitmap() {
        return eventPosterBitmap;
    }

    public void setEventPosterBitmap(Bitmap eventPosterBitmap) {
        this.eventPosterBitmap = eventPosterBitmap;
    }

    public boolean hasEventPosterBitmap(){
        return this.eventPosterBitmap != null;
    }

    public void setEventPosterFromUri(Uri imageFromUri, Context context){
        Bitmap bmp = BaseImageHandler.uriToBitmap(context, imageFromUri);
        if(bmp != null) {
            setEventPosterBitmap(bmp);
        }
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public Long getAttendeesNum() {
        return attendeesNum;
    }

    public Long getRegisteredUsersNum() {
        return registeredUsersNum;
    }

    public ArrayList<String> getMilestones() {
        return milestones;
    }
}



