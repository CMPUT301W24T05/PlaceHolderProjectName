package ca.cmput301t05.placeholder.notifications;

import java.util.Calendar;
import java.util.UUID;

/**
 * Represents a milestone event in the context of an event notification.
 * A milestone marks a significant achievement or progress point in an event.
 * It inherits properties and behavior from the Notification class.
 */
public class Milestone extends Notification {

    /**
     * The date when the milestone is expected to be achieved.
     */
    private Calendar expectedDate;
    private MilestoneType mType;

    /**
     * Constructs a new Milestone with the specified creator's UUID, event's UUID, and milestone type.
     *
     * @param creatorID the UUID of the creator of the milestone
     * @param eventID the UUID of the event associated with the milestone
     * @param mType the type of milestone achieved
     */
    public Milestone(UUID creatorID, UUID eventID, MilestoneType mType) {
        super(generateMessage(mType), creatorID, eventID);
        this.mType = mType;
    }
    /**
     * Generates a message based on the milestone type.
     *
     * @param mType the type of milestone achieved
     * @return a message corresponding to the milestone type
     */
    private static String generateMessage(MilestoneType mType) {
        // Generate message based on the milestone type
        switch (mType) {
            case FIRSTATTENDEE:
                return "YOU'VE GOT YOUR FIRST ATTENDEE!";
            case FIRSTSIGNUP:
                return "YOU'VE GOT YOUR FIRST PERSON THAT IS INTERESTED IN YOUR EVENT!";
            case HALFWAY:
                return "YOU'RE HALFWAY TO YOUR EVENT CAPACITY!";
            // Add more cases for other milestone types as needed
            case FULLCAPACITY:
                return "YOUR EVENT IS NOW AT FULL CAPACITY! CONGRATS!!";
            default:
                return "Default message";
        }
    }

    // Getters and setters for mType
    public MilestoneType getMType() {
        return mType;
    }

    public void setMType(MilestoneType mType) {
        this.mType = mType;
    }


}