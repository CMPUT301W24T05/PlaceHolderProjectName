package ca.cmput301t05.placeholder.database.images;

import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import ca.cmput301t05.placeholder.database.DatabaseManager;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.*;

import java.util.UUID;
import java.io.IOException;

/**
 * The abstract base class for handling image-related operations such as uploading, retrieving,
 * and removing images from firebase storage.
 */
public abstract class BaseImageHandler {

    protected StorageReference rootStorageRef = DatabaseManager.getInstance().getStorage().getReference();

    /**
     * Retrieves the MIME type of file from its Uri.
     *
     * @param file The Uri of the file.
     * @return The MIME type of the file, or null if the MIME type cannot be determined.
     */
    protected String getFileMimeType(Uri file) {
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(file.toString());
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
    }

    /**
     * Uploads an image to the Firebase Storage with custom metadata.
     *
     * @param file                The Uri of the image file to upload.
     * @param imageID             The ID of the image.
     * @param folder              The folder in which to upload the image.
     * @param customMetadataKey   The key for the custom metadata.
     * @param customMetadataValue The value for the custom metadata.
     */
    protected void uploadImage(Uri file, String imageID, String folder, String customMetadataKey, String customMetadataValue) throws IOException {

        String filename = folder + "/" + imageID;
        StorageReference storageRef = rootStorageRef.child(filename);

        String mimeType = getFileMimeType(file);
        if (mimeType == null || !(mimeType.startsWith("image/"))) {
            throw new IOException("Invalid file. This is not an image file.");
        }

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata(customMetadataKey, customMetadataValue)
                .setContentType(mimeType)
                .build();
        UploadTask uploadTask = storageRef.putFile(file, metadata);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Handle successful uploads on complete
            Log.d("Image Upload", "Image upload successful");
        }).addOnFailureListener(e -> {
            // Handle unsuccessful uploads
            Log.d("Image Upload", "Image upload failed: " + e.getMessage());
        }).addOnProgressListener(taskSnapshot -> {
            // Progress percentage can be calculated as (bytesTransferred / totalByteCount) * 100
            double progressPercentage = (100.0 * taskSnapshot.getBytesTransferred()) /
                    taskSnapshot.getTotalByteCount();
            Log.d("Image Upload", "Upload is " + progressPercentage + "% done");
        }).addOnPausedListener(taskSnapshot -> Log.d("Image Upload", "Upload is paused"));
    }

    /**
     * Retrieves and sets an image from Firebase Storage into an ImageView using the Glide library.
     *
     * @param imageID   The ID of the image to retrieve.
     * @param folder    The folder in which the image is located.
     * @param imageView The ImageView to set the image into.
     */
    protected void getImage(String imageID, String folder, ImageView imageView) {
        String filename = folder + "/" + imageID;
        StorageReference storageReference = rootStorageRef.child(filename);

        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            //Load your image here
            Glide.with(imageView.getContext())
                    .load(uri)
                    .into(imageView);
        }).addOnFailureListener(e -> {
            // If the image ID is invalid or the image does not exist, then the download will fail.
            Log.d("Image Database", "Error: " + e.getMessage());
        });
    }

    /**
     * Removes an image from Firebase Storage.
     *
     * @param imageID The ID of the image to be removed.
     * @param folder  The folder in which the image is located.
     */
    protected void removeImage(String imageID, String folder) {
        String filename = folder + "/" + imageID;
        StorageReference storageReference = rootStorageRef.child(filename);

        storageReference.delete().addOnSuccessListener(unused -> Log.d("Image Database", "Image deleted"))
                .addOnFailureListener(e -> {
                    // If the image ID is invalid or the image does not exist
                    Log.d("Image Database", "Error: " + e.getMessage());
                });
    }
}

