package ca.cmput301t05.placeholder.database.images;

import android.net.Uri;
import android.widget.ImageView;
import ca.cmput301t05.placeholder.events.Event;

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
    public void uploadPoster(Uri file, Event event) {
        UUID posterID = event.getEventPosterID() == null ? UUID.randomUUID() : event.getEventPosterID();

        uploadImage(file, posterID.toString(), "posters", "Event", event.getEventID().toString());

        event.setEventPosterID(posterID);
    }

    /**
     * Retrieves the poster picture for an event and sets it to the provided ImageView.
     *
     * @param event      The event object for which the poster picture is being retrieved.
     * @param imageView  The ImageView where the poster picture will be set.
     */
    public void getPosterPicture(Event event, ImageView imageView) {
        if (event.getEventPosterID() == null) {
            return;
        }

        getImage(event.getEventPosterID().toString(), "posters", imageView);
    }

    /**
     * Removes the event poster image for the given event.
     * If the event does not have a poster image, nothing is done.
     *
     * @param event The event object for which the poster image is being removed.
     */
    public void removeEventPoster(Event event) {
        if (event.getEventPosterID() == null) {
            return;
        }

        removeImage(event.getEventPosterID().toString(), "posters");

        event.setEventPosterID(null);
    }
}
