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

public class ImageTable extends  DatabaseManager{

    //extends database manager which holds everything

    private Context context;

    public ImageTable(Context context){
        this.context = context;

    }

    //https://firebase.google.com/docs/storage/android/upload-files#java_2

    public void uploadFile(Uri file){

        StorageReference rootStorageRef = storage.getReference();

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


}
