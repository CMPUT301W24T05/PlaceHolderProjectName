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
    private String pictureID;

    private String type;

    public ImageDetails(Uri uploadedImageUri, String pictureID, ImageType imageType){
        this.id = UUID.randomUUID().toString();
        this.imageUri = uploadedImageUri;
        this.uploadTime = Calendar.getInstance();
        this.metadata = new ArrayList<>();
        this.pictureID = pictureID;

        if(imageType == ImageType.POSTER){
            this.type = "POSTER";
        }   else {
            this.type = "PROFILE";
        }
    }

    public ImageDetails(){
        this.id = UUID.randomUUID().toString();
        this.uploadTime = Calendar.getInstance();
        this.metadata = new ArrayList<>();
    }

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

    public ArrayList<String> getMetadata() {
        return metadata;
    }

    public String getId() {
        return id;
    }

    public void setType(ImageType type) {
        if (type == ImageType.POSTER){
            this.type = "POSTER";
        }   else {
            this.type = "PROFILE";
        }

    }

    public String getType() {
        return type;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public String getPictureID() {
        return pictureID;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void setPictureID(String id) {
        this.pictureID = id;
    }

    public Map<String, Object> toDocument(){
        Map<String, Object> document = new HashMap<>();

        document.put("id", this.id);
        document.put("imageUri", this.imageUri.toString());
        document.put("uploadTime", new Timestamp(this.uploadTime.getTime()));
        document.put("metadata", this.metadata);
        document.put("pictureID", this.pictureID);
        document.put("type", this.type);
        return document;
    }

    public void fromDocument(DocumentSnapshot document){

        this.id = (String) document.get("id");
        this.imageUri = Uri.parse((String) document.get("imageUri"));
        this.uploadTime.setTimeInMillis(document.getTimestamp("uploadTime").toDate().getTime());
        this.pictureID = (String) document.get("pictureID");
        this.type = (String) document.get("type");

        if (document.get("metadata") != null){
            this.metadata = (ArrayList<String>) document.get("metadata");
        }

    }
}
