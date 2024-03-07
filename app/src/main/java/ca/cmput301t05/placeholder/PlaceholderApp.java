package ca.cmput301t05.placeholder;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import java.io.Serializable;

import ca.cmput301t05.placeholder.database.DeviceIDManager;
import ca.cmput301t05.placeholder.database.ImageTable;
import ca.cmput301t05.placeholder.database.ProfileTable;
import ca.cmput301t05.placeholder.profile.Profile;

public class PlaceholderApp extends Application implements Serializable {

    private Profile userProfile;
    private ImageTable imageTable;
    private ProfileTable profileTable;
    private DeviceIDManager idManager;

    @Override
    public void onCreate() {
        super.onCreate();
        imageTable = new ImageTable(this);
        profileTable = new ProfileTable(this);
        idManager = new DeviceIDManager(this);
    }

    public DeviceIDManager getIdManager(){
        return idManager;
    }

    public ImageTable getImageTable() {
        return imageTable;
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
}
