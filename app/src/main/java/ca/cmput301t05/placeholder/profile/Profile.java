package ca.cmput301t05.placeholder.profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.util.Log;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.UserNotification;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;

public class Profile {
    private UUID profileID;
    private String name;
    private String homePage;
    private String contactInfo;
    private UUID profilePictureID;
    private List<Event> hostedEvents;
    private List<Event> joinedEvents;
    private List<UserNotification> notifications;
    boolean isAdmin = false;

    public Profile(){

    }

    public Profile(String name, UUID profileID){
        this.name = name;
        this.profileID = profileID;
    }

    public void joinEvent(Event event){
        joinedEvents.add(event);
    }

    public boolean isAdmin() {
        return isAdmin;
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
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getHomePage() {
        return homePage;
    }

    public String getName() {
        return name;
    }
    public UUID getProfilePictureID() {
        return profilePictureID;
    }
    public UUID getProfileID() {
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

    public void setHostedEvents(List<Event> hostedEvents) {
        this.hostedEvents = hostedEvents;
    }

    public void setJoinedEvents(List<Event> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNotifications(List<UserNotification> notifications) {
        this.notifications = notifications;
    }

    public void setProfileID(UUID profileID) {
        this.profileID = profileID;
    }

    public void setProfilePictureID(UUID profilePictureID) {
        this.profilePictureID = profilePictureID;
    }

    @Exclude
    public Map<String, Object> toDocument() {
        Map<String, Object> document = new HashMap<>();
        document.put("profileID", String.valueOf(profileID));
        document.put("name", name);
        document.put("homePage", homePage);
        document.put("contactInfo", contactInfo);

        if (profilePictureID == null){
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
            hostedEvents = (List<Event>) document.get("hostedEvents");
        }
        if(document.get("joinedEvents") != null) {
            joinedEvents = (List<Event>)document.get("joinedEvents");
        }
        if(document.get("notifications") != null) {
            notifications = (List<UserNotification>)document.get("notifications");
        }
        if(document.getBoolean("isAdmin") != null) {
            isAdmin = Boolean.TRUE.equals(document.getBoolean("isAdmin"));
        }
    }

    
}
