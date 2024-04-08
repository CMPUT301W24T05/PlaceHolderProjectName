package ca.cmput301t05.placeholder.database.ImageDetails;

import android.net.Uri;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ca.cmput301t05.placeholder.database.utils.DocumentSerializable;

/**
 * Essentially the idea is that every image will have an associate image details
 * so we can store what number the image generated was
 */
public class ImageDetails extends DocumentSerializable {

    private String id;
    private Uri imageUri;
    private Calendar uploadTime;
    private ArrayList<String> metadata;
    private String ImagePath;
    private String objectID; //EVENT:ID or PROFILE:ID

    /**
     * Constructs an ImageDetails object with the specified image URI and picture path.
     * @param uploadedImageUri The URI of the uploaded image.
     * @param picturePath The path of the image.
     */
    public ImageDetails(Uri uploadedImageUri, String picturePath){
        this.id = UUID.randomUUID().toString();
        this.imageUri = uploadedImageUri;
        this.uploadTime = Calendar.getInstance();
        this.metadata = new ArrayList<>();
        this.ImagePath = picturePath;

    }
    /**
     * Constructs an empty ImageDetails object.
     */
    public ImageDetails(){
        this.id = UUID.randomUUID().toString();
        this.uploadTime = Calendar.getInstance();
        this.metadata = new ArrayList<>();
    }
    /**
     * Constructs an ImageDetails object from a DocumentSnapshot.
     * @param documentSnapshot The DocumentSnapshot containing image details.
     */
    public ImageDetails(DocumentSnapshot documentSnapshot){
        this.uploadTime = Calendar.getInstance();
        fromDocument(documentSnapshot);
    }

    public void addMetadata(String value){
        this.metadata.add(value);
    }

    public Calendar getUploadTime() {
        return uploadTime;
    }
    public void setUploadTime(Calendar uploadTime) {
        this.uploadTime = uploadTime;
    }

    public ArrayList<String> getMetadata() {
        return metadata;
    }

    public String getId() {
        return id;
    }


    public Uri getImageUri() {
        return imageUri;
    }

    public String getImagePath() {
        return this.ImagePath;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void setImagePath(String path) {
        this.ImagePath = path;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getObjectID() {
        return objectID;
    }

    public Map<String, Object> toDocument(){
        Map<String, Object> document = new HashMap<>();

        document.put("id", this.id);
        document.put("imageUri", this.imageUri.toString());
        document.put("uploadTime", new Timestamp(this.uploadTime.getTime()));
        document.put("metadata", this.metadata);
        document.put("ImagePath", this.ImagePath);
        document.put("objectID", this.objectID);
        return document;
    }

    public void fromDocument(DocumentSnapshot document){

        this.id = (String) document.get("id");
        this.imageUri = Uri.parse((String) document.get("imageUri"));
        this.uploadTime.setTimeInMillis(document.getTimestamp("uploadTime").toDate().getTime());
        this.ImagePath = (String) document.get("ImagePath");
        this.objectID = (String) document.get("objectID");

        if (document.get("metadata") != null){
            this.metadata = (ArrayList<String>) document.get("metadata");
        }

    }
}
