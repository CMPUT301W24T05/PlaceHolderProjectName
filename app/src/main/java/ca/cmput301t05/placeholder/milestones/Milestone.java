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

    public Milestone(DocumentSnapshot snapshot){
        this.fromDocument(snapshot);
    }


    public Milestone(MilestoneType type, Event event){

        this.mType = type;
        this.eventID = event.getEventID().toString();
        this.eventName = event.getEventName();
        this.id = type.getId() + ";" + eventID; //essentially milestones will be TYPE;EVENT-ID for ID

        this.message = generateMessage(this.mType, this.eventName);

    }

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

    public Map<String, Object> toDocument(){

        Map<String, Object> document = new HashMap<>();

        document.put("id", id);
        document.put("type", mType.getId());
        document.put("eventID", eventID);
        document.put("message", message);
        document.put("eventName", eventName);

        return document;
    }

    public void fromDocument(DocumentSnapshot snapshot){

        this.id = snapshot.getString("id");
        this.mType = getType(snapshot.getDouble("type"));
        this.eventID = snapshot.getString("eventID");
        this.message = snapshot.getString("message");
        this.eventName = snapshot.getString("eventName");

    }

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

    public String getMessage() {
        return message;
    }

    public MilestoneType getmType() {
        return mType;
    }

    public String getEventID() {
        return eventID;
    }

    public String getId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }
}
