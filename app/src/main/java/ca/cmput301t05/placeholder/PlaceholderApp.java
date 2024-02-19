package ca.cmput301t05.placeholder;

import android.app.Application;

import ca.cmput301t05.placeholder.database.ImageTable;
import ca.cmput301t05.placeholder.database.ProfileTable;
import ca.cmput301t05.placeholder.profile.Profile;

public class PlaceholderApp extends Application {

    private Profile userProfile;
    private ImageTable imageTable;
    private ProfileTable profileTable;

    @Override
    public void onCreate() {
        super.onCreate();
        imageTable = new ImageTable(this);
        profileTable = new ProfileTable(this);
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

    public void setUserProfile(Profile userProfile) {
        this.userProfile = userProfile;
    }
}
