package ca.cmput301t05.placeholder.database;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;

public class ImageTable extends Table {

    StorageReference rootStorageRef = databaseManager.storage.getReference();

    public ImageTable(Context context){
        super(context);
    }

    //https://firebase.google.com/docs/storage/android/upload-files#java_2

    public void uploadFile(Uri file){


        String filename = "images/" + UUID.randomUUID().toString() + ".jpg";
        StorageReference storageRef = rootStorageRef.child(filename);

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();

        UploadTask uploadTask = storageRef.putFile(file, metadata);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

    }

    public void uploadResource(int resourceId){
        Uri tempFileUri = copyResourceToTempFile(resourceId, "image_", ".jpg");
        uploadFile(tempFileUri);
    }

    public Uri copyResourceToTempFile(int resourceId, String filePrefix, String fileSuffix) {
        InputStream in = context.getResources().openRawResource(resourceId);
        try {
            // Create a temporary file in the app's cache directory
            File tempFile = File.createTempFile(filePrefix, fileSuffix, context.getCacheDir());
            OutputStream out = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();

            // Return a Uri to the temporary file
            return Uri.fromFile(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void uploadProfilePicture(Uri file, Profile profile){

        UUID profileID = UUID.randomUUID();

        String filename = "profiles/" + profileID.toString() + ".jpg";
        StorageReference storageRef = rootStorageRef.child(filename);

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("Profile", profile.getProfileID().toString())
                .setContentType("image/jpg")
                .build();

        UploadTask uploadTask = storageRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

        profile.setProfilePictureID(profileID);

    }

    //allows us to upload a poster to the database
    public void uploadPoster(Uri file, Event event){
        //uploads a poster and sets the poster id of the event

        UUID posterID = UUID.randomUUID();

        String filename = "posters/" + posterID.toString() + ".jpg";
        StorageReference storageRef = rootStorageRef.child(filename);

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("Event", event.getEventID().toString())
                .setContentType("image/jpg")
                .build();

        UploadTask uploadTask = storageRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

        event.setEventPosterID(posterID);
    }

    //will save a picture to the storage
    public void getProfilePicture(Profile profile){


    }

    public void getPosterPicture(Event event){


    }


}
