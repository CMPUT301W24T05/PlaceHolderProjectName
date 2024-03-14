package ca.cmput301t05.placeholder.database;

import com.google.firebase.firestore.DocumentSnapshot;

import ca.cmput301t05.placeholder.notifications.Notification;

public class NotificationTable extends Table<Notification>{

    private final static String COLLECTION_NAME = "notifications";

    public NotificationTable() {
        super(COLLECTION_NAME);
    }

    @Override
    protected Notification documentFromSnapshot(DocumentSnapshot snapshot) {

        Notification notification = new Notification();
        notification.fromDocument(snapshot);

        return notification;
    }
}
