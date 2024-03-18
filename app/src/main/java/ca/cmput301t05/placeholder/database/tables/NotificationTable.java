package ca.cmput301t05.placeholder.database.tables;

import ca.cmput301t05.placeholder.database.utils.Collections;
import com.google.firebase.firestore.DocumentSnapshot;

import ca.cmput301t05.placeholder.notifications.Notification;


/**
 * The NotificationTable class represents a table in the database for storing notifications.
 * It extends the Table class and provides methods for converting Firestore document snapshots into Notification objects.
 */
public class NotificationTable extends Table<Notification>{

    private final static Collections COLLECTION = Collections.NOTIFICATIONS;

    public NotificationTable() {
        super(COLLECTION);
    }

    @Override
    protected Notification documentFromSnapshot(DocumentSnapshot snapshot) {

        return new Notification(snapshot);
    }

}
