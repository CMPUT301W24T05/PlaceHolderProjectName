package ca.cmput301t05.placeholder.database.utils;

import ca.cmput301t05.placeholder.database.ImageDetails.ImageDetails;

/**
 * Enum to easily allow us to grab the collection name
 */
public enum Collections{

    EVENTS("events", "eventID"),
    NOTIFICATIONS("notifications", "notificationsID"),
    PROFILES("profiles", "profileID"),

    IMAGEDETAILS("imageDetails", "id");

    private final String path;
    private final String id;

    Collections(String path, String idPath) {
        this.path = path;
        this.id = idPath;
    }

    public String getPath() {
        return path;
    }

    public String getId(){
        return id;
    }

}