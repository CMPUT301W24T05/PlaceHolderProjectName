package ca.cmput301t05.placeholder;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import ca.cmput301t05.placeholder.database.*;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;

/**
 * PlaceholderApp extends Application to provide a centralized location for managing global application state.
 * It initializes and holds references to various handlers and tables necessary for the app's operation,
 * including user profiles, event images, and device ID management.
 */
public class PlaceholderApp extends Application implements Serializable {

    private Profile userProfile;
    private EventPosterImageHandler posterImageHandler;
    private ProfileImageHandler profileImageHandler;
    private ProfileTable profileTable;
    private EventTable eventTable;
    private DeviceIDManager idManager;

    private HashMap<UUID, Event> joinedEvents;

    private HashMap<UUID, Event> hostedEvents;

    private Event cachedEvent;

    /**
     * Called when the application is starting, before any activity, service, or receiver objects (excluding content providers) have been created.
     * Initializes the tables and managers used throughout the application.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        profileTable = new ProfileTable();
        eventTable = new EventTable();
        idManager = new DeviceIDManager(this);

        posterImageHandler = new EventPosterImageHandler();
        profileImageHandler = new ProfileImageHandler();

        hostedEvents = new HashMap<>();
        joinedEvents = new HashMap<>();
    }

    /**
     * Returns the singleton instance of DeviceIDManager.
     * @return The DeviceIDManager instance.
     */
    public DeviceIDManager getIdManager(){
        return idManager;
    }

    /**
     * Returns the singleton instance of ProfileTable.
     * @return The ProfileTable instance.
     */
    public ProfileTable getProfileTable() {
        return profileTable;
    }

    /**
     * Returns the current user profile.
     * @return The current Profile instance if set; null otherwise.
     */
    public Profile getUserProfile() {
        return userProfile;
    }

    /**
     * Sets the current user profile.
     * @param profile The Profile to set as the current user profile.
     */
    public void setUserProfile(Profile profile) {
        this.userProfile = profile;
    }

    /**
     * Checks if a current user profile exists.
     * @return true if a user profile is set; false otherwise.
     */
    public boolean currentProfileExists(){
        return userProfile != null;
    }

    /**
     * Returns the singleton instance of EventPosterImageHandler.
     * @return The EventPosterImageHandler instance.
     */

    public EventPosterImageHandler getPosterImageHandler() {
        return posterImageHandler;
    }

    /**
     * Returns the singleton instance of ProfileImageHandler.
     * @return The ProfileImageHandler instance.
     */
    public ProfileImageHandler getProfileImageHandler() {
        return profileImageHandler;
    }

    /**
     * Returns the singleton instance of EventTable.
     * @return The EventTable instance.
     */
    public EventTable getEventTable() {
        return eventTable;
    }

    public HashMap<UUID,Event> getJoinedEvents(){
        return this.joinedEvents;
    }

    public HashMap<UUID,Event> getHostedEvents(){
        return this.hostedEvents;
    }

    public void setCachedEvent(Event cachedEvent) {
        this.cachedEvent = cachedEvent;
    }

    public Event getCachedEvent() {
        return cachedEvent;
    }
}
