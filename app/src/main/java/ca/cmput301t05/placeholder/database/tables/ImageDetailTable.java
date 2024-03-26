package ca.cmput301t05.placeholder.database.tables;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import ca.cmput301t05.placeholder.database.ImageDetails.ImageDetails;
import ca.cmput301t05.placeholder.database.utils.Collections;

public class ImageDetailTable extends Table<ImageDetails>{

    private final static Collections COLLECTION = Collections.IMAGEDETAILS;
    private final String PATHTODETAILS = "details";
    private final String HIGHESTIDNAME = "highestID";


    /**
     * Constructs a Table object with the given collection name.
     */
    public ImageDetailTable() {
        super(COLLECTION);

    }

    @Override
    protected ImageDetails documentFromSnapshot(DocumentSnapshot snapshot) {
        return new ImageDetails(snapshot);
    }

    public CollectionReference getCollectionReference(){
        return collectionReference;
    }


}
