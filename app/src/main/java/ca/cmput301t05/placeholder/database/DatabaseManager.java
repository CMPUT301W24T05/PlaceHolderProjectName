package ca.cmput301t05.placeholder.database;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class DatabaseManager {

    //store db
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public DatabaseManager(){

    }


}
