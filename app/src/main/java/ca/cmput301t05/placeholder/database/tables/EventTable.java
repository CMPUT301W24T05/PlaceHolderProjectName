package ca.cmput301t05.placeholder.database.tables;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.database.utils.Collections;
import ca.cmput301t05.placeholder.events.Event;

/**
 * The EventTable class represents a table in the database for storing events.
 * It extends the Table class and provides methods for converting Firestore document snapshots into Event objects.
 */
public class EventTable extends Table<Event> {

    private final static Collections COLLECTION = Collections.EVENTS;
    public EventTable() {
        super(COLLECTION);
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

    // Fetch all documents from the collection
    public void fetchAllDocuments(DocumentCallback<ArrayList<Event>> callback) {
        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Event> events = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    events.add(documentFromSnapshot(document));
                }
                callback.onSuccess(events);
            } else {
                callback.onFailure(task.getException());
            }
        });
    }
}

