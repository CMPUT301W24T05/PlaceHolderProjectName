package ca.cmput301t05.placeholder.database;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;

import java.util.Map;

public abstract class DocumentSerializable {

    @Exclude
    public abstract Map<String, Object> toDocument();

    public abstract void fromDocument(DocumentSnapshot document);
}