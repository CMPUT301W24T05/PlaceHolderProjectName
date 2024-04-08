package ca.cmput301t05.placeholder.notifications;

import java.util.Calendar;
import java.util.UUID;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.milestones.MilestoneType;

/**
 *
 *
 * Represents a milestone event in the context of an event notification.
 * A milestone marks a significant achievement or progress point in an event.
 * It inherits properties and behavior from the Notification class.
 *
 * @Deprecated
 * Please use Milestones in the milestone folder instead, this class causes lots of bugs
 */
@Deprecated
public class Milestone extends Notification {

    /**
     * The date when the milestone is expected to be achieved.
     */
    private Calendar expectedDate;
    private MilestoneType mType;
    private PlaceholderApp app;
    private String eventName;
    /**
     * Constructs a new Milestone with the specified creator's UUID, event's UUID, and milestone type.
     *
     * @param creatorID the UUID of the creator of the milestone
     * @param eventID the UUID of the event associated with the milestone
     * @param mType the type of milestone achieved
     */
    public Milestone(UUID creatorID, UUID eventID, MilestoneType mType, String eventName) {
        super(generateMessage(mType, eventName), creatorID, eventID);
        this.mType = mType;
        this.eventName = eventName;

    }
    /**
     * Generates a message based on the milestone type.
     *
     * @param mType the type of milestone achieved
     * @return a message corresponding to the milestone type
     */
    private static String generateMessage(MilestoneType mType, String eventName) {
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

    // Getters and setters for mType
    public MilestoneType getMType() {
        return mType;
    }

    public void setMType(MilestoneType mType) {
        this.mType = mType;
    }


}