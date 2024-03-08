package ca.cmput301t05.placeholder.database;

import android.net.Uri;
import android.widget.ImageView;
import ca.cmput301t05.placeholder.profile.Profile;

import java.util.UUID;

public class ProfileImageHandler extends BaseImageHandler {

    public void uploadProfilePicture(Uri file, Profile profile) {
        UUID profileID = profile.getProfilePictureID() == null ? UUID.randomUUID() : profile.getProfilePictureID();

        uploadImage(file, profileID.toString(), "profiles", "Profile", profile.getProfileID().toString());

        profile.setProfilePictureID(profileID);
        profile.toDocument();
    }

    public void getProfilePicture(Profile profile, ImageView imageView) {
        if (profile.getProfilePictureID() == null) {
            return;
        }

        getImage(profile.getProfilePictureID().toString(), "profiles", imageView);
    }

    public void removeProfilePic(Profile profile) {
        if (profile.getProfilePictureID() == null) {
            return;
        }

        removeImage(profile.getProfilePictureID().toString(), "profiles");

        profile.setProfilePictureID(null);
    }
}
