package ca.cmput301t05.placeholder.database;

import ca.cmput301t05.placeholder.profile.Profile;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * The ProfileTable class represents a table in the database that stores profile information.
 * It extends the Table class.
 */
public class ProfileTable extends Table<Profile> {
    public static final String COLLECTION_NAME = "profiles";

    public ProfileTable() {
        super(COLLECTION_NAME);
    }

    @Override
    protected Profile documentFromSnapshot(DocumentSnapshot snapshot) {
        Profile profile = new Profile();
        profile.fromDocument(snapshot);
        return profile;
    }
}
