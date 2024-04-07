package ca.cmput301t05.placeholder.utils.datafetchers;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.profile.Profile;

import java.util.UUID;

public class ProfileFetcher extends AbstractFetcher {

    public ProfileFetcher(PlaceholderApp app, @NonNull Context context) {
        super(app, context);
    }

    public void fetchProfileIfDeviceIdExists() {
        if (!app.getIdManager().deviceHasIDStored()) {
            callbacks.forEach(DataFetchCallback::onNoIdFound);
            return;
        }

        UUID deviceId = app.getIdManager().getDeviceID();
        fetchProfile(deviceId);
    }

    public void fetchProfile(UUID deviceId) {
        app.getProfileTable().fetchDocument(deviceId.toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                // The profile exists in firebase! We can continue to the Main activity
                app.setUserProfile(profile);
                callbacks.forEach(c -> c.onProfileFetched(profile));
                fetchProfileImage(profile);
            }

            @Override
            public void onFailure(Exception e) {
                callbacks.forEach(c -> c.onProfileFetchFailure(e));
            }
        });
    }

    public void fetchProfileImage(Profile profile) {
        app.getProfileImageHandler().getProfilePicture(profile, context, new BaseImageHandler.ImageCallback() {
            @Override
            public void onImageLoaded(Bitmap bitmap) {
                profile.setProfilePictureBitmap(bitmap);
                callbacks.forEach(c -> c.onPictureLoaded(bitmap));
            }

            @Override
            public void onError(Exception e) {
                profile.setProfilePictureToDefault();
                callbacks.forEach(c -> c.onPictureLoaded(profile.getProfilePictureBitmap()));
            }
        });
    }
}