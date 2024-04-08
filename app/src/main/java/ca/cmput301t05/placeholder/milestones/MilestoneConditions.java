package ca.cmput301t05.placeholder.milestones;

import android.util.Log;

import java.util.Calendar;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.database.firebaseMessaging.notificationHandler.HttpNotificationHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.Notification;
import ca.cmput301t05.placeholder.profile.Profile;

/**
 * Checks to make sure conditions have been met
 *
 */
public class MilestoneConditions {

    public interface milestoneCallback{

        void onSuccess();
        void onFailure(Exception e);

    }

    /**
     *  Makes a milestone according to the type, if the event satisfies the conditions
     * @param event event we're checking
     * @param type type of milestone
     * @return newly generated milestone according to the type, null if conditions not met
     */

    public static Milestone checkConditionsMet(Event event, MilestoneType type){
        Log.e("amirza2", "checking the milestone type: " +String.valueOf(type) );

        Log.e("amirza2",String.valueOf(event.getMilestones()));
        switch (type) {
            case FIRSTATTENDEE:
                if(!event.getAttendees().isEmpty()){
                    return new Milestone(type, event);
                }   else {
                    return null;
                }
            case FIRSTSIGNUP:
                if(!event.getRegisteredUsers().isEmpty()){
                    return new Milestone(type, event);
                }   else {
                    return null;
                }
            case HALFWAY:
                if((double) event.getAttendees().size() / event.getMaxAttendees() >= 0.5){
                    return new Milestone(type, event);
                }   else {
                    return null;
                }
            case FULLCAPACITY:
                if(event.getAttendees().size() == event.getMaxAttendees()){
                    return  new Milestone(type,event);
                } else {
                    return null;
                }
            case EVENTSTART:
                Log.e("amirza2","inside the event start milestone checker");
                Calendar c = Calendar.getInstance();

                if (c.after(event.getEventDate())){
                    return new Milestone(type ,event);
                }   else {
                    return null;
                }

            default:
                return null;
        }

    }

    /**
     * checks if we contain a type
     * @param event
     * @param type
     * @return boolean is it's already contained
     */
    public static boolean alreadyContainsMilestone(Event event, MilestoneType type){

        if (event.getMilestones().get(type.getIdString()) != null){

            return true;
        }

        return false;

    }

    /**
     * ASSUMES EVENT IS UPDATED TO THE DATABASE
     * HANDLES ALL MILESTONES AND SENDS TO DB
     * @param app
     * @param event
     */

    public static void milestoneHandling(PlaceholderApp app, Event event, milestoneCallback callback){


        for (MilestoneType type : MilestoneType.values()){

            if (!MilestoneConditions.alreadyContainsMilestone(event, type)){
                //generate the new milestones if the condition is met (NULL IF NOTHING GENERATED)
                Milestone milestone = MilestoneConditions.checkConditionsMet(event, type);

                if (milestone != null){

                    app.getMilestoneTable().pushDocument(milestone, milestone.getId(), new Table.DocumentCallback<Milestone>() {
                        @Override
                        public void onSuccess(Milestone document) {

                            event.getMilestones().put(type.getIdString(), milestone.getId());

                            //now update event with database then send the notification to the event organizer

                            app.getEventTable().pushDocument(event, event.getEventID().toString(), new Table.DocumentCallback<Event>() {
                                @Override
                                public void onSuccess(Event document) {

                                    //get ORGANIZER

                                    app.getProfileTable().fetchDocument(event.getEventCreator().toString(), new Table.DocumentCallback<Profile>() {
                                        @Override
                                        public void onSuccess(Profile document) {

                                            Notification n = new Notification(milestone.getMessage(), document.getProfileID(), event.getEventID());
                                            n.setPush(true);

                                            HttpNotificationHandler.sendNotificationToUser(n, document.getMessagingToken(), new HttpNotificationHandler.httpHandlercallback() {
                                                @Override
                                                public void onSuccess() {
                                                    Log.d("MILESTONE", "SENT MILESTONE TO ORGANIZER");
                                                    callback.onSuccess();
                                                }

                                                @Override
                                                public void onError(Exception e) {
                                                    callback.onFailure(e);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            callback.onFailure(e);
                                        }
                                    });


                                }

                                @Override
                                public void onFailure(Exception e) {
                                    callback.onFailure(e);
                                }
                            });

                        }

                        @Override
                        public void onFailure(Exception e) {
                            callback.onFailure(e);
                        }
                    });


                }

            }

        }


    }


}
