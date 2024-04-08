package ca.cmput301t05.placeholder.database.tables;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import ca.cmput301t05.placeholder.database.utils.Collections;
import ca.cmput301t05.placeholder.milestones.Milestone;

public class MilestoneTable extends Table<Milestone> {

    private final static Collections COLLECTION = Collections.MILESTONES;

    /**
     * Constructs a Table object with the given collection name.
     *
     * @param collection an enum which allows us to grab paths and such
     */
    public MilestoneTable() {
        super(COLLECTION);
    }

    @Override
    protected Milestone documentFromSnapshot(DocumentSnapshot snapshot) {
        return new Milestone(snapshot);
    }

    public CollectionReference getCollectionReference(){
        return collectionReference;
    }
}
