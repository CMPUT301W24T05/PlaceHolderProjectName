package ca.cmput301t05.placeholder.milestones;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ca.cmput301t05.placeholder.database.utils.DocumentSerializable;
import ca.cmput301t05.placeholder.events.Event;

/**
 * Milestone class which we can call to the database and allow us to grab events milestones
 *
 */
public class Milestone extends DocumentSerializable{

    private MilestoneType mType;

    private String message;

    private String eventID;
    private String eventName;

    private String id;
    /**
     * Constructs a milestone object from a document snapshot retrieved from the database.
     *
     * @param snapshot The document snapshot containing milestone data.
     */

    public Milestone(DocumentSnapshot snapshot){
        this.fromDocument(snapshot);
    }

    /**
     * Constructs a milestone object with the given type and event.
     *
     * @param type  The type of milestone.
     * @param event The event associated with the milestone.
     */
    public Milestone(MilestoneType type, Event event){

        this.mType = type;
        this.eventID = event.getEventID().toString();
        this.eventName = event.getEventName();
        this.id = type.getId() + ";" + eventID; //essentially milestones will be TYPE;EVENT-ID for ID

        this.message = generateMessage(this.mType, this.eventName);

    }
    /**
     * Generates a message for the milestone based on its type and associated event name.
     *
     * @param mType     The type of milestone.
     * @param eventName The name of the associated event.
     * @return The generated milestone message.
     */
    public String generateMessage(MilestoneType mType, String eventName) {
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

    /**
     * Converts the milestone object to a map suitable for storing in the database.
     *
     * @return A map representing the milestone data.
     */
    public Map<String, Object> toDocument(){

        Map<String, Object> document = new HashMap<>();

        document.put("id", id);
        document.put("type", mType.getId());
        document.put("eventID", eventID);
        document.put("message", message);
        document.put("eventName", eventName);

        return document;
    }
    /**
     * Populates the milestone object's fields from the given document snapshot retrieved from the database.
     *
     * @param snapshot The document snapshot containing milestone data.
     */
    public void fromDocument(DocumentSnapshot snapshot){

        this.id = snapshot.getString("id");
        this.mType = getType(snapshot.getDouble("type"));
        this.eventID = snapshot.getString("eventID");
        this.message = snapshot.getString("message");
        this.eventName = snapshot.getString("eventName");

    }
    /**
     * Retrieves the milestone type from the database representation.
     *
     * @param d The database representation of the milestone type.
     * @return The corresponding milestone type.
     */
    private MilestoneType getType(double d){

        switch ((int) d) {
            case 0:
                return MilestoneType.FIRSTATTENDEE;
            case 1:
                return MilestoneType.FIRSTSIGNUP;
            case 2:
                return MilestoneType.EVENTSTART;
            case 3:
                return MilestoneType.EVENTEND;
            case 4:
                return MilestoneType.HALFWAY;
            case 5:
                return MilestoneType.FULLCAPACITY;
        }

        return null;
    }
    /**
     * Gets the message associated with the milestone.
     *
     * @return The milestone message.
     */
    public String getMessage() {
        return message;
    }
    /**
     * Gets the type of the milestone.
     *
     * @return The milestone type.
     */
    public MilestoneType getmType() {
        return mType;
    }
    /**
     * Gets the ID of the event associated with the milestone.
     *
     * @return The event ID.
     */
    public String getEventID() {
        return eventID;
    }
    /**
     * Gets the ID of the milestone.
     *
     * @return The milestone ID.
     */
    public String getId() {
        return id;
    }
    /**
     * Gets the name of the event associated with the milestone.
     *
     * @return The event name.
     */
    public String getEventName() {
        return eventName;
    }
}
