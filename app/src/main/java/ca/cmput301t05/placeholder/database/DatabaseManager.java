package ca.cmput301t05.placeholder.database;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import ca.cmput301t05.placeholder.events.Event;

/**
 * The DatabaseManager class is responsible for managing the database connection and providing database-related functionality.
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    //store db
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private DatabaseManager() {}

    /**
     * Retrieves the instance of FirebaseFirestore.
     *
     * @return The instance of FirebaseFirestore.
     */
    public FirebaseFirestore getDb() { return this.db; }

    /**
     * Retrieves the FirebaseStorage instance used for managing storage.
     *
     * @return The FirebaseStorage instance for managing storage.
     */
    public FirebaseStorage getStorage() { return this.storage; }

    /**
     * Retrieves the instance of DatabaseManager while ensuring that only one instance is created.
     *
     * @return The instance of DatabaseManager.
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }


}
