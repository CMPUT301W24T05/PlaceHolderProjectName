package ca.cmput301t05.placeholder.database.tables;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import ca.cmput301t05.placeholder.database.DatabaseManager;
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

    /**
     * Deletes specific image from the databases
     * @param imageDetails
     */
    public void deleteImage(ImageDetails imageDetails, DocumentCallback callback){

        StorageReference storageRef = DatabaseManager.getInstance().getStorage().getReference().child(imageDetails.getImagePath());

        storageRef.delete().addOnSuccessListener(unused -> {

            //now delete imagedetails
            this.deleteDocument(imageDetails.getId(), new DocumentCallback() {
                @Override
                public void onSuccess(Object document) {
                    Log.d("Image Database", "Deleted Image");
                    callback.onSuccess(document);
                }

                @Override
                public void onFailure(Exception e) {
                    callback.onFailure(e);

                }
            });


        }).addOnFailureListener(e -> {
            // If the image ID is invalid or the image does not exist
            Log.d("Image Database", "Error: " + e.getMessage());
        });


    }


}
