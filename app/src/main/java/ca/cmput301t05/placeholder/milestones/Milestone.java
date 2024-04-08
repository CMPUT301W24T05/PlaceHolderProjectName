package ca.cmput301t05.placeholder.milestones;

import java.io.Serializable;
import java.util.UUID;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.database.firebaseMessaging.notificationHandler.HttpNotificationHandler;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.MilestoneType;
import ca.cmput301t05.placeholder.notifications.Notification;
import ca.cmput301t05.placeholder.profile.Profile;

/**
 * Milestone class which we can call to the database and allow us to grab events milestones
 *
 */
public class Milestone implements Serializable {

    private MilestoneType mType;

    private String message;

    private String eventID;
    private String eventName;
    UUID creatorID;
    String token;
    private Notification notification;

    PlaceholderApp app;


    Milestone(MilestoneType type, Event event, Profile profile){

        this.mType = type;
        this.eventID = event.getEventID().toString();
        this.eventName = event.getEventName();
        this.creatorID = profile.getProfileID();
        this.token = profile.getMessagingToken();;
        this.message = generateMessage(this.mType, this.eventName);
        notification = new Notification(new Notification(message,creatorID, UUID.fromString(eventID)));

        HttpNotificationHandler.sendNotificationToUser(notification, token, new HttpNotificationHandler.httpHandlercallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    private String generateMessage(MilestoneType mType, String eventName) {
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






}
