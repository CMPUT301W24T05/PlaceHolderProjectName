package ca.cmput301t05.placeholder.notifications;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ca.cmput301t05.placeholder.database.DocumentSerializable;

/**
 * Represents a notification associated with a user event. It contains information about
 * the notification message, the time it was generated, the creator of the notification,
 * and the event it is associated with. Users will store references to these notifications,
 * and the profile class contains a list of all notifications.
 */
public class Notification extends DocumentSerializable {

    /**
     * The unique identifier for this notification.
     */
    UUID notificationID;

    /**
     * The time at which the notification was created.
     */
    Calendar timeCreated;

    /**
     * The unique identifier of the user who created the notification.
     */
    UUID creatorID;

    /**
     * The unique identifier of the event from which the notification originated.
     */
    UUID fromEventID;

    /**
     * The message content of the notification.
     */
    String message;

    /**
     * Constructs a new Notification with nothing inside of it, please always use generic constructor, not this
     */
    public Notification(){

    }


    /**
     * Constructs a new Notification with the specified message, creator's UUID, and event's UUID.
     * It automatically sets the creation time to the current moment and generates a unique identifier for the notification.
     *
     * @param message   the notification message content
     * @param creatorID the UUID of the creator of the notification
     * @param eventID   the UUID of the event associated with the notification
     */
    public Notification(String message, UUID creatorID, UUID eventID){

        this.message = message;
        this.creatorID = creatorID;
        this.fromEventID = eventID;

        this.timeCreated = Calendar.getInstance();
        this.notificationID = UUID.randomUUID();

    }

    /**
     * Converts this Notification object into a Map representation for storing in the firebase.
     *
     * @return a Map containing key-value pairs representing the properties of this Notification.
     */
    public Map<String, Object> toDocument(){

        Map<String, Object> document = new HashMap<>();

        document.put("notificationID", this.notificationID.toString());
        document.put("creatorID", this.creatorID.toString());
        document.put("fromEventID", this.fromEventID.toString());

        document.put("message", message);

        //now convert cal to timestamp
        document.put("timeCreated", new Timestamp(timeCreated.getTime()));

        return document;
    }

    /**
     * Populates the properties of this Notification object based on a DocumentSnapshot retrieved from the database
     *
     * @param document the DocumentSnapshot containing the notification data.
     */
    public void fromDocument(DocumentSnapshot document){

        this.notificationID = UUID.fromString(document.getString("notificationID"));
        this.creatorID = UUID.fromString(document.getString("creatorID"));
        this.fromEventID = UUID.fromString(document.getString("fromEventID"));

        this.message = document.getString("message");

        //sets the calendar to this current one
        this.timeCreated.setTimeInMillis(document.getTimestamp("timeCreated").toDate().getTime());

    }

    //getters and setters

    public String getMessage() {
        return message;
    }

    public Calendar getTimeCreated() {
        return timeCreated;
    }

    public UUID getCreatorID() {
        return creatorID;
    }

    public UUID getFromEventID() {
        return fromEventID;
    }

    public UUID getNotificationID() {
        return notificationID;
    }

    public void setCreatorID(UUID creatorID) {
        this.creatorID = creatorID;
    }

    public void setFromEventID(UUID fromEventID) {
        this.fromEventID = fromEventID;
    }

    public void setNotificationID(UUID notificationID) {
        this.notificationID = notificationID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimeCreated(Calendar timeCreated) {
        this.timeCreated = timeCreated;
    }
}
