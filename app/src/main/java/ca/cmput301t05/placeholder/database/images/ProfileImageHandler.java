package ca.cmput301t05.placeholder.database.images;

import android.net.Uri;
import android.widget.ImageView;
import ca.cmput301t05.placeholder.profile.Profile;

import java.util.UUID;

/**
 * The ProfileImageHandler class is responsible for handling profile image-related operations such as uploading,
 * retrieving, and removing profile images.
 */
public class ProfileImageHandler extends BaseImageHandler {

    /**
     * Uploads the profile picture to the Firebase Storage and updates the profile with the new picture ID.
     *
     * @param file    The Uri of the image file to upload.
     * @param profile The profile to assign the profile picture to.
     */
    public void uploadProfilePicture(Uri file, Profile profile) {

        UUID profileID = profile.getProfilePictureID() == null ? UUID.randomUUID() : profile.getProfilePictureID();
        uploadImage(file, profileID.toString(), "profiles", "Profile", profile.getProfileID().toString());
        // This has potential for bugs. Even if Uri is invalid it will set the profile Id
        profile.setProfilePictureID(profileID);
        // What will happen if we subsequently call getProfilePicture?
        profile.toDocument();
    }

    /**
     * Retrieves and sets the profile picture of a profile into an ImageView.
     *
     * @param profile   The profile whose profile picture is to be retrieved.
     * @param imageView The ImageView to set the profile picture into.
     */
    public void getProfilePicture(Profile profile, ImageView imageView) {
        if (profile.getProfilePictureID() == null) {
            return;
        }

        getImage(profile.getProfilePictureID().toString(), "profiles", imageView);
    }

    /**
     * Removes the profile picture of a given profile.
     *
     * @param profile The profile whose profile picture will be removed.
     */
    public void removeProfilePic(Profile profile) {
        if (profile.getProfilePictureID() == null) {
            return;
        }

        removeImage(profile.getProfilePictureID().toString(), "profiles");

        profile.setProfilePictureID(null);
    }
}
