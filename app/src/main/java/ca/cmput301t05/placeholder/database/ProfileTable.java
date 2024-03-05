package ca.cmput301t05.placeholder.database;

import android.content.Context;

import com.google.firebase.firestore.DocumentSnapshot;

import ca.cmput301t05.placeholder.profile.Profile;

public class ProfileTable extends Table {

    public interface ProfileCallback {
        void onSuccess(Profile profile);

        void onFailure(Exception e);
    }

    private DeviceIDManager idManager;
    public static final String COLLECTION_NAME = "profiles";

    public ProfileTable(Context context) {
        super(context);
        idManager = new DeviceIDManager(context);
    }

    public boolean deviceHasProfile() {
        // Check if the device id has been stored
        return idManager.deviceHasIDStored();
    }

    public void fetchProfile(String profileID, ProfileCallback callback) {
        databaseManager.db.collection(COLLECTION_NAME).document(profileID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {
                    Profile profile = new Profile();
                    profile.fromDocument(doc);
                    callback.onSuccess(profile);
                } else {
                    callback.onFailure(new Exception("Profile was not found"));
                }
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    public void pushProfile(Profile profile, ProfileCallback callback) {
        databaseManager.db.collection(COLLECTION_NAME).document(String.valueOf(profile.getProfileID())).set(profile.toDocument())
                .addOnSuccessListener(aVoid -> callback.onSuccess(profile))
                .addOnFailureListener(callback::onFailure);
    }
}
