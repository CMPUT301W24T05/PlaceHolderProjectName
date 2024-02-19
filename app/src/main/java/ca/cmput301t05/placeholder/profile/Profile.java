package ca.cmput301t05.placeholder.profile;

import java.util.List;
import java.util.UUID;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.UserNotification;

public class Profile {

    private String profileID;
    private String name;
    private String homePage;
    private String contactInfo;
    private String profilePictureID;
//    private List<Event> hostedEvents;
//    private List<Event> joinedEvents;
//    private List<UserNotification> notifications;
    boolean isAdmin = false;

    public Profile(){

    }

    public Profile(String name, String profileID){
        this.name = name;
        this.profileID = profileID;
    }

/*    public void joinEvent(Event event){

        joinedEvents.add(event);

    }

    public void leaveEvent(Event event){

        joinedEvents.remove(event);
    }

    public void hostEvent(Event e){
        hostedEvents.add(e);
    }

    public void unHostEvent(Event e){
        hostedEvents.remove(e);
    }

    public void addNotification(UserNotification a){

        notifications.add(a);
    }

    public void removeNotification(UserNotification a){
        notifications.remove(a);
    }

    //getters / setters


    public List<Event> getHostedEvents() {
        return hostedEvents;
    }

    public List<Event> getJoinedEvents() {
        return joinedEvents;
    }

    public List<UserNotification> getNotifications() {
        return notifications;
    }*/

    public String getContactInfo() {
        return contactInfo;
    }

    public String getHomePage() {
        return homePage;
    }

    public String getName() {
        return name;
    }

    public String getProfilePictureID() {
        return profilePictureID;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

   /* public void setHostedEvents(List<Event> hostedEvents) {
        this.hostedEvents = hostedEvents;
    }

    public void setJoinedEvents(List<Event> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }
*/
    public void setName(String name) {
        this.name = name;
    }

/*    public void setNotifications(List<UserNotification> notifications) {
        this.notifications = notifications;
    }*/

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public void setProfilePictureID(String profilePictureID) {
        this.profilePictureID = profilePictureID;
    }

    
}
