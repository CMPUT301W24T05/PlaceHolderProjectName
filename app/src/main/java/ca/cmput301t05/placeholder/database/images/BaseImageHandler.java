package ca.cmput301t05.placeholder.database.images;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.database.DatabaseManager;
import ca.cmput301t05.placeholder.database.ImageDetails.ImageDetails;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.*;

import java.io.InputStream;
import java.io.IOException;

/**
 * The abstract base class for handling image-related operations such as uploading, retrieving,
 * and removing images from firebase storage.
 */
public abstract class BaseImageHandler {

    // Callback interface for image retrieval
    public interface ImageCallback {
        void onImageLoaded(Bitmap bitmap);
        void onError(Exception e);
    }

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
    protected void uploadImage(Uri file, String imageID, String folder, String customMetadataKey, String customMetadataValue, Context context) throws IOException {
        String filename = folder + "/" + imageID;
        StorageReference storageRef = rootStorageRef.child(filename);

        //this will be an object which we store relevant image details
        ImageDetails details = new ImageDetails();

        // Use ContentResolver for content URIs, otherwise fall back to getFileMimeType
        String mimeType;

        if (file.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            mimeType = context.getContentResolver().getType(file);
        } else {
            mimeType = getFileMimeType(file);
        }

        if (mimeType == null || !(mimeType.startsWith("image/"))) {
            throw new IOException("Invalid file. This is not an image file.");
        }

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata(customMetadataKey, customMetadataValue)
                .setCustomMetadata("ImageDetailsKey", details.getId())
                .setContentType(mimeType)
                .build();
        UploadTask uploadTask = storageRef.putFile(file, metadata);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Handle successful uploads on complete

            PlaceholderApp app = (PlaceholderApp) context.getApplicationContext();

            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d("Image Upload", "Image upload successful");
                    details.setImageUri(uri);
                    details.setImagePath(filename);
                    String objectIDMeta = customMetadataKey + ";" + customMetadataValue;
                    details.setObjectID(objectIDMeta);

                    app.getImageDetailTable().pushDocument(details, details.getId(), new Table.DocumentCallback<ImageDetails>() {
                        @Override
                        public void onSuccess(ImageDetails document) {
                            Log.d("ImageDetails", "Image Details Uploaded");

                            //now set event or profile imageDetails to this object


                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.d("ImageDetails", "Error");
                        }
                    });
                }
            });





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
     */
// Updated getImage method
    protected void getImage(String imageID, String folder, Context context, ImageCallback callback) {
        String filename = folder + "/" + imageID;
        StorageReference storageReference = rootStorageRef.child(filename);

        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(context) // Context needs to be passed or obtained differently
                    .asBitmap()
                    .load(uri)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            callback.onImageLoaded(resource);
                        }
                    });
        }).addOnFailureListener(callback::onError);
    }


    /**
     * Removes an image from Firebase Storage.
     *
     * @param imageID The ID of the image to be removed.
     * @param folder  The folder in which the image is located.
     */
    protected void removeImage(String imageID, String folder, Context context) {
        String filename = folder + "/" + imageID;
        StorageReference storageReference = rootStorageRef.child(filename);

        //delete reference to the object
        storageReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                String id = storageMetadata.getCustomMetadata("ImageDetailsKey");
                PlaceholderApp app = (PlaceholderApp) context;
                app.getImageDetailTable().deleteDocument(id, new Table.DocumentCallback() {
                    @Override
                    public void onSuccess(Object document) {
                        Log.d("ImageDetails", "Image Details Deleted");

                        storageReference.delete().addOnSuccessListener(unused -> Log.d("Image Database", "Image deleted"))
                                .addOnFailureListener(e -> {
                                    // If the image ID is invalid or the image does not exist
                                    Log.d("Image Database", "Error: " + e.getMessage());
                                })
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("ImageDetails", "Error");
                    }
                });

            }
        });



    }

    /**
     * Converts a Uri into a Bitmap.
     *
     * @param context the application context.
     * @param uri the Uri of the image.
     * @return the converted Bitmap, or null if the Uri could not be converted.
     */
    public static Bitmap uriToBitmap(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            Log.d("Uri to Bitmap", "Conversion failed: " + e.getMessage());
            return null;
        }
    }
}

