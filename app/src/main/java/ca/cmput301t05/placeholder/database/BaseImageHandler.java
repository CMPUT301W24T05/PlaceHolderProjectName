package ca.cmput301t05.placeholder.database;

import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.UUID;

public abstract class BaseImageHandler {

    protected StorageReference rootStorageRef = DatabaseManager.getInstance().getStorage().getReference();

    protected String getFileMimeType(Uri file) {
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(file.toString());
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
    }

    protected void uploadImage(Uri file, String imageID, String folder, String customMetadataKey, String customMetadataValue) {
        String filename = folder + "/" + imageID;
        StorageReference storageRef = rootStorageRef.child(filename);

        String mimeType = getFileMimeType(file);
        if (mimeType == null) {
            // Default to "image/jpeg" if MIME type cannot be determined
            mimeType = "image/jpeg";
        }

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata(customMetadataKey, customMetadataValue)
                .setContentType(mimeType)
                .build();

        UploadTask uploadTask = storageRef.putFile(file, metadata);
    }

    protected void getImage(String imageID, String folder, ImageView imageView) {
        String filename = folder + "/" + imageID;
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

    protected void removeImage(String imageID, String folder) {
        String filename = folder + "/" + imageID;
        StorageReference storageReference = rootStorageRef.child(filename);

        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Image Database", "Image deleted");
            }
        });
    }
}

