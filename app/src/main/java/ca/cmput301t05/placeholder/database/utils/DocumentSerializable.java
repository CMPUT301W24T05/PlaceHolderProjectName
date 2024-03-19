package ca.cmput301t05.placeholder.database.utils;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;

import java.util.Map;

/**
 * This class represents a DocumentSerializable, which is an abstract class that provides methods for converting
 * objects to and from Firestore documents.
 */
public abstract class DocumentSerializable {

    /**
     * Converts an object to a Firestore document by returning a map representation of the object.
     *
     * @return A map representation of the object.
     */
    @Exclude
    public abstract Map<String, Object> toDocument();

    /**
     * Populates the object's fields based on a Firestore document snapshot.
     *
     * @param document The Firestore document snapshot representing the object.
     */
    public abstract void fromDocument(DocumentSnapshot document);
}