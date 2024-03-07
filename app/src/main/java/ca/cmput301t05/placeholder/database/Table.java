package ca.cmput301t05.placeholder.database;

import android.content.Context;
import com.google.firebase.firestore.CollectionReference;

public abstract class Table {
    protected CollectionReference collectionReference;

    public Table(){
    }
}
