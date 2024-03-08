package ca.cmput301t05.placeholder.database;

import ca.cmput301t05.placeholder.events.Event;
import com.google.firebase.firestore.DocumentSnapshot;

public class EventTable extends Table<Event> {

    private final static String COLLECTION_NAME = "events";
    public EventTable() {
        super(COLLECTION_NAME);
    }

    @Override
    protected Event documentFromSnapshot(DocumentSnapshot snapshot) {
        Event event = new Event();
        event.fromDocument(snapshot);
        return event;
    }
}
