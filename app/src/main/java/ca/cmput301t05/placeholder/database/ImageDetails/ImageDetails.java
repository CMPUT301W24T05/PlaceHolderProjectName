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

    public ImageDetails(Uri uploadedImageUri){
        this.id = UUID.randomUUID().toString();
        this.imageUri = uploadedImageUri;
        this.uploadTime = Calendar.getInstance();
        this.metadata = new ArrayList<>();

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

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Map<String, Object> toDocument(){
        Map<String, Object> document = new HashMap<>();

        document.put("id", this.id);
        document.put("imageUri", this.imageUri.toString());
        document.put("uploadTime", new Timestamp(this.uploadTime.getTime()));
        document.put("metadata", this.metadata);

        return document;
    }

    public void fromDocument(DocumentSnapshot document){

        this.id = (String) document.get("id");
        this.imageUri = Uri.parse((String) document.get("imageUri"));
        this.uploadTime.setTimeInMillis(document.getTimestamp("uploadTime").toDate().getTime());

        if (document.get("metadata") != null){
            this.metadata = (ArrayList<String>) document.get("metadata");
        }

    }
}
