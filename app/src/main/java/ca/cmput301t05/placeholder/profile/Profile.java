package ca.cmput301t05.placeholder.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import ca.cmput301t05.placeholder.database.DocumentSerializable;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.Notification;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;

/**
 * This class represents a user profile. Include information about the name, homepage, contact info, notifications
 *  and any events hosted or joined.
 */
public class Profile extends DocumentSerializable {
    private UUID profileID;
    private String name;
    private String homePage;
    private String contactInfo;
    private UUID profilePictureID;
    private List<String> hostedEvents;
    private List<String> joinedEvents;
    private List<Notification> notifications;
    boolean isAdmin = false;

    /**
     * Default constructor creating an empty profile.
     */
    public Profile(){

    }

    /**
     * This constructor creates a new Profile with the given name and profileID.
     *
     * @param name      The name of the profile.
     * @param profileID The unique ID of the profile.
     */
    public Profile(String name, UUID profileID){
        this.name = name;
        this.profileID = profileID;
        this.joinedEvents = new ArrayList<>();
        this.hostedEvents = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    /**
     * Adds the given event to the list of joined events for this profile.
     *
     * @param event The event to join.
     */
    public void joinEvent(Event event){
        joinedEvents.add(event.getEventID().toString());
    }

    /**
     * Checks if the user is an admin.
     *
     * @return true if the user is an admin, false otherwise.
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Removes the given event from the list of joined events for this profile.
     *
     * @param event The event to be removed.
     */
    public void leaveEvent(Event event){
        joinedEvents.remove(event.getEventID().toString());
    }

    /**
     * Adds the given event to the list of hosted events for this profile.
     *
     * @param e The event to be hosted.
     */
    public void hostEvent(Event e){
        hostedEvents.add(e.getEventID().toString());
    }

    /**
     * Removes the given event from the list of hosted events for this profile.
     *
     * @param e The event to be de-hosted.
     */
    public void unHostEvent(Event e){
        hostedEvents.remove(e.getEventID().toString());
    }

    /**
     * Adds a UserNotification to the list of notifications.
     *
     * @param a The UserNotification to be added.
     */
    public void addNotification(Notification a){
        notifications.add(a);
    }

    /**
     * This method removes the given UserNotification from the list of notifications in the Profile class.
     *
     * @param a The UserNotification to be removed.
     */
    public void removeNotification(Notification a){
        notifications.remove(a);
    }

    /**
     * Retrieves the list of hosted events for this profile.
     *
     * @return The list of hosted events.
     */
    //getters / setters
    public List<String> getHostedEvents() {
        return hostedEvents;
    }

    /**
     * Retrieves the list of joined events for this profile.
     *
     * @return The list of joined events.
     */
    public List<String> getJoinedEvents() {
        return joinedEvents;
    }

    /**
     * Retrieves the list of notifications for this profile.
     *
     * @return The list of notifications.
     */
    public List<Notification> getNotifications() {
        return notifications;
    }

    /**
     * Retrieves the contact information for this profile.
     *
     * @return The contact information as a String.
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Retrieves the home page URL for this profile.
     *
     * @return The home page URL as a string.
     */
    public String getHomePage() {
        return homePage;
    }

    /**
     * Retrieves the name of the profile.
     *
     * @return The name of the profile as a String.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the profile picture ID of the profile.
     *
     * @return The profile picture ID as a UUID.
     */
    public UUID getProfilePictureID() {
        return profilePictureID;
    }

    /**
     * Retrieves the unique ID of the profile.
     *
     * @return The profile ID as a UUID.
     */
    public UUID getProfileID() {
        return profileID;
    }

    /**
     * Sets the home page URL for this profile.
     *
     * @param homePage The home page URL as a String.
     */
    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    /**
     * Sets the admin status of the profile.
     *
     * @param admin true if the profile is an admin, false otherwise.
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    /**
     * Sets the contact information for this profile.
     *
     * @param contactInfo The contact information as a String.
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Sets the list of hosted events for this profile.
     *
     * @param hostedEvents The list of hosted events to be set.
     */
    public void setHostedEvents(List<String> hostedEvents) {
        this.hostedEvents = hostedEvents;
    }

    /**
     * Sets the list of joined events for this profile.
     *
     * @param joinedEvents The list of joined events to be set.
     */
    public void setJoinedEvents(List<String> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }

    /**
     * Sets the name of the profile.
     *
     * @param name The name to be set for the profile.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the list of UserNotifications for this profile.
     *
     * @param notifications The list of UserNotifications to be set.
     */
    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    /**
     * Sets the profile ID for this profile.
     *
     * @param profileID The profile ID to be set.
     */
    public void setProfileID(UUID profileID) {
        this.profileID = profileID;
    }

    /**
     * Sets the profile picture ID of the profile.
     *
     * @param profilePictureID The profile picture ID to be set.
     */
    public void setProfilePictureID(UUID profilePictureID) {
        this.profilePictureID = profilePictureID;
    }

    /**
     * Converts the Profile object to a document in the form of a Map &lt;String, Object&gt;.
     * This method is used to store the Profile object in a database.
     *
     * @return The Profile object converted to a document.
     */
    @Override
    @Exclude
    public Map<String, Object> toDocument() {
        Map<String, Object> document = new HashMap<>();


        document.put("profileID", profileID.toString());
        document.put("name", name);
        document.put("homePage", homePage);
        document.put("contactInfo", contactInfo);

        if (this.profilePictureID == null){
            document.put("profilePictureID", null);
        }   else {
            document.put("profilePictureID", profilePictureID.toString());
        }
        document.put("hostedEvents", hostedEvents); // Assumes Event class can be serialized
        document.put("joinedEvents", joinedEvents); // Assumes Event class can be serialized
        document.put("notifications", notifications); // Assumes UserNotification can be serialized
        document.put("isAdmin", isAdmin);
        return document;
    }

    /**
     * Converts a DocumentSnapshot to a Profile object.
     *
     * @param document The DocumentSnapshot to convert to a Profile object.
     */
    @Override
    public void fromDocument(DocumentSnapshot document) {
        String profileDocID = document.getString("profileID");
        if(profileDocID != null && !profileDocID.equals("null")) {
            profileID = UUID.fromString(profileDocID);
        }
        if(document.getString("name") != null) {
            name = document.getString("name");
        }
        if(document.getString("homePage") != null) {
            homePage = document.getString("homePage");
        }
        if(document.getString("contactInfo") != null) {
            contactInfo = document.getString("contactInfo");
        }
        String profileImageID = document.getString("profilePictureID");
        if(profileImageID != null && !profileImageID.equals("null")) {
            profilePictureID = UUID.fromString(profileImageID);
        }

        if(document.get("hostedEvents") != null) {
            hostedEvents = (List<String>) document.get("hostedEvents");
        }
        if(document.get("joinedEvents") != null) {
            joinedEvents = (List<String>)document.get("joinedEvents");
        }
        if(document.get("notifications") != null) {
            notifications = (List<Notification>)document.get("notifications");
        }
        if(document.getBoolean("isAdmin") != null) {
            isAdmin = Boolean.TRUE.equals(document.getBoolean("isAdmin"));
        }
    }

    private ArrayList<String> turnNotifToString(){

        ArrayList<String> notifID = new ArrayList<>();

        for (Notification n : this.notifications){

            notifID.add(n.getNotificationID().toString());
        }

        return notifID;
    }


    
}
