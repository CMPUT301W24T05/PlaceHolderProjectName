package ca.cmput301t05.placeholder.database.tables;

import com.google.firebase.firestore.DocumentSnapshot;

import ca.cmput301t05.placeholder.database.utils.Collections;
import ca.cmput301t05.placeholder.notifications.Milestone;

public class MilestoneTable extends Table<Milestone> {

    private final static Collections COLLECTION = Collections.IMAGEDETAILS;

    /**
     * Constructs a Table object with the given collection name.
     *
     * @param collection an enum which allows us to grab paths and such
     */
    public MilestoneTable(Collections collection) {
        super(collection);
    }

    @Override
    protected Milestone documentFromSnapshot(DocumentSnapshot snapshot) {
        return null;
    }
}
