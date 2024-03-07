package ca.cmput301t05.placeholder.database;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
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

import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;

public class ImageTable extends Table {

    StorageReference rootStorageRef = DatabaseManager.getInstance().getStorage().getReference();

    public ImageTable(){
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

//    public void uploadResource(int resourceId){
//        Uri tempFileUri = copyResourceToTempFile(resourceId, "image_", ".jpg");
//        uploadFile(tempFileUri);
//    }
//
//    public Uri copyResourceToTempFile(int resourceId, String filePrefix, String fileSuffix) {
//        InputStream in = context.getResources().openRawResource(resourceId);
//        try {
//            // Create a temporary file in the app's cache directory
//            File tempFile = File.createTempFile(filePrefix, fileSuffix, context.getCacheDir());
//            OutputStream out = new FileOutputStream(tempFile);
//            byte[] buffer = new byte[1024];
//            int read;
//
//            while ((read = in.read(buffer)) != -1) {
//                out.write(buffer, 0, read);
//            }
//            in.close();
//            out.flush();
//            out.close();
//
//            // Return a Uri to the temporary file
//            return Uri.fromFile(tempFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    public void uploadProfilePicture(Uri file, Profile profile){
        UUID profileID;
        if (profile.getProfilePictureID() == null){
            profileID = UUID.randomUUID();
        }   else {
            profileID = profile.getProfilePictureID();
        }


        String profilepicID = profileID.toString();

        String filename = "profiles/" + profilepicID;
        StorageReference storageRef = rootStorageRef.child(filename);

        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(file.toString());
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);

        if (mimeType == null) {
            // Default to "image/jpeg" if MIME type cannot be determined
            mimeType = "image/jpeg";
        }

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("Profile", profile.getProfileID().toString())
                .setContentType(mimeType)
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
        profile.toDocument();

    }

    //allows us to upload a poster to the database
    public void uploadPoster(Uri file, Event event){
        //uploads a poster and sets the poster id of the event



        UUID posterID;

        if (event.getEventPosterID() == null){
            posterID = UUID.randomUUID();
        }   else {
            posterID = event.getEventPosterID();
        }

        String posterS = posterID.toString();

        String filename = "posters/" + posterS;
        StorageReference storageRef = rootStorageRef.child(filename);

        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(file.toString());
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);

        if (mimeType == null) {
            // Default to "image/jpeg" if MIME type cannot be determined
            mimeType = "image/jpeg";
        }

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("Event", event.getEventID().toString())
                .setContentType(mimeType)
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
        event.sendEventToDatabase();
    }

    //will save a picture to the storage
    public void getProfilePicture(Profile profile, ImageView imageView){

        String filename = "profiles/" + profile.getProfilePictureID().toString();

        StorageReference storageReference = rootStorageRef.child(filename);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Load your image here
                Glide.with(imageView.getContext())
                        .load(uri)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //implement some error checking
            }
        });

    }

    public void getPosterPicture(Event event, ImageView imageView){

        if (event.getEventPosterID() == null){
            return;
        }

        String filename = "posters/" + event.getEventPosterID().toString();
        StorageReference storageReference = rootStorageRef.child(filename);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Load your image here need to have error images and such

                Glide.with(imageView.getContext())
                        .load(uri)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //implement some error checking
            }
        });

    }

    public void removeProfilePic(Profile p){

        String profilePicID = p.getProfilePictureID().toString();

        if (profilePicID == null){
            return;
        }

        String filename = "profiles/" + profilePicID;
        StorageReference storageReference = rootStorageRef.child(filename);

        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Image Database", "Image deleted");
            }
        });

        p.setProfilePictureID(null);


    }

    public void removeEventPoster(Event e){

        String eventPosterID = e.getEventPosterID().toString();

        if (eventPosterID == null){
            return;
        }

        String filename = "posters/" + eventPosterID;
        StorageReference storageReference = rootStorageRef.child(filename);

        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Image Database", "Image deleted");
            }
        });

        e.setEventPosterID(null);
    }

    public void testImage(String filename, ImageView imageView){

        String f = "images/" + filename + ".jpg";
        StorageReference storageReference = rootStorageRef.child(f);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Load your image here need to have error images and such

                Glide.with(imageView.getContext())
                        .load(uri)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //implement some error checking
            }
        });
    }




}
