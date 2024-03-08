package ca.cmput301t05.placeholder;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import java.io.Serializable;

import ca.cmput301t05.placeholder.database.*;
import ca.cmput301t05.placeholder.profile.Profile;

public class PlaceholderApp extends Application implements Serializable {

    private Profile userProfile;
    private EventPosterImageHandler posterImageHandler;
    private ProfileImageHandler profileImageHandler;
    private ProfileTable profileTable;
    private DeviceIDManager idManager;

    @Override
    public void onCreate() {
        super.onCreate();
        profileTable = new ProfileTable();
        idManager = new DeviceIDManager(this);

        posterImageHandler = new EventPosterImageHandler();
        profileImageHandler = new ProfileImageHandler();
    }

    public DeviceIDManager getIdManager(){
        return idManager;
    }

    public ProfileTable getProfileTable() {
        return profileTable;
    }

    public Profile getUserProfile() {
        return userProfile;
    }
    public void setUserProfile(Profile profile) {
        this.userProfile = profile;
    }
    public boolean currentProfileExists(){
        return userProfile != null;
    }

    public EventPosterImageHandler getPosterImageHandler() {
        return posterImageHandler;
    }

    public ProfileImageHandler getProfileImageHandler() {
        return profileImageHandler;
    }
}
