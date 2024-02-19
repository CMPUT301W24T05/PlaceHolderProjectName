package ca.cmput301t05.placeholder.database;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageTable extends  DatabaseManager{

    //extends database manager which holds everything

    public ImageTable(){

        this.UploadFile("D:\\cmput301GIT\\PlaceHolderProjectName\\app\\src\\main\\java\\ca\\cmput301t05\\placeholder\\database\\yeet yah.jpg");

    }

    //https://firebase.google.com/docs/storage/android/upload-files#java_2

    public void UploadFile(String FilePath){

        StorageReference storageRef = storage.getReference();
        Uri file = Uri.fromFile(new File(FilePath));

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


}
