package ca.cmput301t05.placeholder.database;

import ca.cmput301t05.placeholder.events.Event;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * The EventTable class represents a table in the database for storing events.
 * It extends the Table class and provides methods for converting Firestore document snapshots into Event objects.
 */
public class EventTable extends Table<Event> {

    private final static String COLLECTION_NAME = "events";
    public EventTable() {
        super(COLLECTION_NAME);
    }

    /**
     * Converts a Firestore document snapshot into an Event object.
     *
     * @param snapshot The Firestore document snapshot representing an event.
     * @return The Event object populated with data from the snapshot.
     */
    @Override
    protected Event documentFromSnapshot(DocumentSnapshot snapshot) {
        Event event = new Event();
        event.fromDocument(snapshot);
        return event;
    }
}
