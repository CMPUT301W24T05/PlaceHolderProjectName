package ca.cmput301t05.placeholder.database.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;

import ca.cmput301t05.placeholder.database.DatabaseManager;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.profile.ProfileImageGenerator;

import java.io.IOException;
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
    public void uploadProfilePicture(Uri file, Profile profile, Context context) {

        UUID profileID = profile.getProfilePictureID() == null ? UUID.randomUUID() : profile.getProfilePictureID();
        try {
            uploadImage(file, profileID.toString(), "profiles", "Profile", profile.getProfileID().toString(), context);
        } catch (IOException e) {
            Log.e("ProfileImageHandler", "The provided uri is invalid: " + file.toString());
            // Return and don't associate the profile picture id to the profile
            return;
        }

        profile.setProfilePictureID(profileID);
    }


    /**
     * Retrieves the profile picture for the given profile.
     *
     * @param profile       The profile to retrieve the profile picture for.
     * @param context       The context in which the method is called.
     * @param imageCallback The callback to be invoked when the image is loaded or retrieval fails.
     */
    public void getProfilePicture(Profile profile, Context context, ImageCallback imageCallback) {
        if (profile.getProfilePictureID() == null) {
            // If there's no profile image ID, generate a default profile image
            Bitmap defaultImage = ProfileImageGenerator.defaultProfileImage(profile.getName());
            // Directly invoke onImageLoaded with the default image
            imageCallback.onImageLoaded(defaultImage);
        } else {
            // Attempt to retrieve the profile image from Firebase Storage
            String imageID = profile.getProfilePictureID().toString();
            String folder = "profiles"; // Assuming the images are stored in a 'profiles' folder in Firebase Storage

            getImage(imageID, folder, context, new ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    // Successfully loaded the image, now set it to the profile and invoke the callback
                    profile.setProfilePictureBitmap(bitmap);
                    imageCallback.onImageLoaded(bitmap);
                }

                @Override
                public void onError(Exception e) {
                    // Failed to load the image, generate a default image as a fallback
                    Bitmap defaultImage = ProfileImageGenerator.defaultProfileImage(profile.getName());
                    profile.setProfilePictureBitmap(defaultImage);
                    imageCallback.onImageLoaded(defaultImage); // Alternatively, you could call onError to handle the failure explicitly
                }
            });
        }
    }


    /**
     * Removes the profile picture of a given profile.
     *
     * @param profile The profile whose profile picture will be removed.
     */
    public void removeProfilePic(Profile profile, Context context, ImageDeletionCallback imageDeletionCallback) {
        if (profile.getProfilePictureID() == null) {
            return;
        }

        Log.d("profile_image_deletion", profile.getProfileID().toString());

        DatabaseManager.getInstance().getDb().collection("profiles").document(profile.getProfileID().toString()).update("profilePictureID", null).addOnCompleteListener(task -> {

            if (task.isSuccessful()){

                removeImage(profile.getProfileID().toString(), "profiles", context, new ImageDeletionCallback() {
                    @Override
                    public void onImageDeleted() {
                        Log.d("Profile_Image_deletion", "Image Deleted");
                        imageDeletionCallback.onImageDeleted();
                    }

                    @Override
                    public void onError(Exception e) {
                        imageDeletionCallback.onError(e);
                    }
                });


            }   else {

                Log.d("Profile_Image_deletion", task.getException().getMessage());
            }

        });





    }
}
