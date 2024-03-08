package ca.cmput301t05.placeholder.database;

import android.net.Uri;
import android.widget.ImageView;
import ca.cmput301t05.placeholder.events.Event;

import java.util.UUID;

public class EventPosterImageHandler extends BaseImageHandler {

    public void uploadPoster(Uri file, Event event) {
        UUID posterID = event.getEventPosterID() == null ? UUID.randomUUID() : event.getEventPosterID();

        uploadImage(file, posterID.toString(), "posters", "Event", event.getEventID().toString());

        event.setEventPosterID(posterID);
        event.sendEventToDatabase();
    }

    public void getPosterPicture(Event event, ImageView imageView) {
        if (event.getEventPosterID() == null) {
            return;
        }

        getImage(event.getEventPosterID().toString(), "posters", imageView);
    }

    public void removeEventPoster(Event event) {
        if (event.getEventPosterID() == null) {
            return;
        }

        removeImage(event.getEventPosterID().toString(), "posters");

        event.setEventPosterID(null);
    }
}
