package ca.cmput301t05.placeholder.database.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;

import ca.cmput301t05.placeholder.database.DatabaseManager;
import ca.cmput301t05.placeholder.events.Event;

import java.io.IOException;
import java.util.UUID;

/**
 * The EventPosterImageHandler class extends the BaseImageHandler class and provides
 * methods to upload, retrieve, and remove event poster images from Firebase Storage.
 */
public class EventPosterImageHandler extends BaseImageHandler {

    /**
     * Uploads a poster image for an event.
     *
     * @param file  The URI of the image file to be uploaded.
     * @param event The event object for which the poster image is being uploaded.
     */
    public void uploadPoster(Uri file, Event event, Context context) {
        UUID posterID = event.getEventPosterID() == null ? UUID.randomUUID() : event.getEventPosterID();

        try {
            uploadImage(file, posterID.toString(), "posters", "Event", event.getEventID().toString(), context);
        } catch (IOException e) {
            Log.e("EventPosterImageHandler", "The provided uri is invalid: " + file.toString());
            // Return and don't associate the event poster picture id to the event
            return;
        }

        event.setEventPosterID(posterID);
    }

    public void getPosterPicture(Event event, Context context, ImageCallback callback) {
        if (event.getEventPosterID() == null) {
            callback.onError(new Exception("Poster ID is null"));
        } else {
            getImage(event.getEventPosterID().toString(), "posters", context, new BaseImageHandler.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    // Successfully retrieved the image
                    event.setEventPosterBitmap(bitmap);
                    callback.onImageLoaded(bitmap);
                }

                @Override
                public void onError(Exception e) {
                    callback.onError(e);
                }
            });
        }
    }


    /**
     * Removes the event poster image for the given event.
     * If the event does not have a poster image, nothing is done.
     *
     * @param event The event object for which the poster image is being removed.
     */
    public void removeEventPoster(Event event, Context context) {
        if (event.getEventPosterID() == null) {
            return;
        }

        DatabaseManager.getInstance().getDb().collection("events").document(event.getEventID().toString()).update("eventPosterID", null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                removeImage(event.getEventPosterID().toString(), "posters", context);

                event.setEventPosterID(null);
            }
        });




    }
}
