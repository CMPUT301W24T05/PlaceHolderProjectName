package ca.cmput301t05.placeholder.database.tables;

import ca.cmput301t05.placeholder.database.utils.Collections;
import ca.cmput301t05.placeholder.profile.Profile;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * The ProfileTable class represents a table in the database for storing profiles.
 * It extends the Table class and provides methods for converting Firestore document snapshots into Profile objects.
 */
public class ProfileTable extends Table<Profile> {
    public static final Collections COLLECTION = Collections.PROFILES;

    public ProfileTable() {
        super(COLLECTION);
    }

    /**
     * Converts a Firestore document snapshot into a Profile object.
     *
     * @param snapshot The Firestore document snapshot to convert.
     * @return The Profile object created from the snapshot.
     */
    @Override
    protected Profile documentFromSnapshot(DocumentSnapshot snapshot) {
        Profile profile = new Profile();
        profile.fromDocument(snapshot);
        return profile;
    }

    public CollectionReference getCollectionReference(){
        return collectionReference;
    }
}
