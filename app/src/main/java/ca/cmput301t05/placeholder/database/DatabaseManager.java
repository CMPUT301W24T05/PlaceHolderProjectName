package ca.cmput301t05.placeholder.database;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import ca.cmput301t05.placeholder.events.Event;

public class DatabaseManager {

    private static DatabaseManager instance;
    //store db
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private DatabaseManager(){

    }

    public FirebaseFirestore getDb(){
        return this.db;
    }

    public FirebaseStorage getStorage(){
        return this.storage;
    }

    public static synchronized DatabaseManager getInstance(){
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }


}
