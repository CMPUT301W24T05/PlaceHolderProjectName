package ca.cmput301t05.placeholder.notifications;


import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.type.DateTime;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ca.cmput301t05.placeholder.database.utils.DocumentSerializable;
import ca.cmput301t05.placeholder.utils.StringManip;

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
    @SerializedName("notification_uuid")
    private UUID notificationID;

    /**
     * The time at which the notification was created.
     */
    @SerializedName("time_created")
    private Calendar timeCreated;

    /**
     * The unique identifier of the user who created the notification.
     */
    @SerializedName("creator_id")
    private UUID creatorID;

    /**
     * The unique identifier of the event from which the notification originated.
     */
    @SerializedName("event_uuid")
    private UUID fromEventID;

    /**
     * The message content of the notification.
     */
    @SerializedName("message")
    private String message;

    /**
     * Used for sorting the top by pinned
     */
    @SerializedName("is_pinned")
    private boolean isPinned;

    @SerializedName("is_read")
    private boolean isRead;

    @SerializedName("is_push")
    private boolean isPush;

    /**
     * Constructs a new Notification with nothing inside of it, please always use generic constructor, not this
     */
    public Notification(DocumentSnapshot snapshot){
        this.timeCreated = Calendar.getInstance();
        fromDocument(snapshot);
    }

    public Notification(Map<String,String> fromFCM) throws JSONException {

        this.notificationID = UUID.fromString(fromFCM.get("notification_uuid"));
        this.message = fromFCM.get("message");
        this.creatorID = UUID.fromString(fromFCM.get("creator_id"));
        this.fromEventID = UUID.fromString(fromFCM.get("event_uuid"));
        this.isPush = StringManip.getTrueOrFalse(fromFCM.get("is_push"));
        this.isPinned = StringManip.getTrueOrFalse(fromFCM.get("is_pinned"));
        this.isRead = StringManip.getTrueOrFalse(fromFCM.get("is_read"));


        String date = fromFCM.get("time_created");

        JSONObject jsonObject = new JSONObject(date);

        int month = jsonObject.getInt("month");
        int year = jsonObject.getInt("year");
        int dayOfMonth = jsonObject.getInt("dayOfMonth");
        int hourOfDay = jsonObject.getInt("hourOfDay");
        int minute = jsonObject.getInt("minute");
        int second = jsonObject.getInt("second");

        this.timeCreated = Calendar.getInstance();

        this.timeCreated.set(Calendar.MONTH, month);
        this.timeCreated.set(Calendar.YEAR, year);
        this.timeCreated.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        this.timeCreated.set(Calendar.HOUR_OF_DAY, hourOfDay);
        this.timeCreated.set(Calendar.MINUTE, minute);
        this.timeCreated.set(Calendar.SECOND, second);

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

        this.isPinned = false;
        this.isRead = false;

    }

    public Notification(Notification n) {
        this.timeCreated = n.timeCreated;
        this.isPush = n.isPush;
        this.isRead = n.isRead;
        this.notificationID = n.notificationID;
        this.fromEventID = n.fromEventID;
        this.isPinned = n.isPinned;
        this.creatorID = n.creatorID;
        this.message = n.message;

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

        document.put("isPinned", this.isPinned);

        document.put("isRead", this.isRead);
        document.put("isPush", this.isPush);

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

        this.isPinned = document.getBoolean("isPinned");

        if (document.get("isRead") == null){
            this.isRead = false; //for data migration
        }   else {
            this.isRead = document.getBoolean("isRead");
        }

        if (document.get("isPush") == null){
            this.isPush = false;
        }   else {
            this.isPush = document.getBoolean("isPush");
        }

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

    public Boolean isPinned(){
        return this.isPinned;
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

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setPush(boolean push) {
        isPush = push;
    }

    public boolean isPush() {
        return isPush;
    }
}
