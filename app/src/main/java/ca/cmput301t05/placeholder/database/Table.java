package ca.cmput301t05.placeholder.database;

import android.content.Context;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * The Table class is an abstract class that represents a table in the database.
 * It provides methods for fetching and pushing documents to the table.
 *
 * @param <T> The type of document stored in the table, which should extend DocumentSerializable.
 */
public abstract class Table<T extends DocumentSerializable> {
    /**
     * A reference to a collection in the Firestore database.
     */
    protected CollectionReference collectionReference;

    /**
     * Constructs a Table object with the given collection name.
     *
     * @param collectionName The name of the collection in the database.
     */
    public Table(String collectionName) {
        collectionReference = DatabaseManager.getInstance().getDb().collection(collectionName);
    }

    /**
     * Callback interface for document operations in the Table class.
     *
     * @param <T> The type of the document.
     */
    public interface DocumentCallback<T> {
        /**
         * This method is called when the document operation is successful.
         *
         * @param document The document object that was operated upon.
         */
        void onSuccess(T document);

        /**
         * This method is called when the document operation fails.
         *
         * @param e The exception that occurred during the document operation.
         */
        void onFailure(Exception e);
    }

    /**
     * Fetches a document from the collection using the given document ID and calls the callback methods based on the result of the operation.
     *
     * @param documentId The ID of the document to fetch.
     * @param callback   The callback to be called when the fetch operation is complete.
     */
    public void fetchDocument(String documentId, DocumentCallback<T> callback) {
        collectionReference.document(documentId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    T object = documentFromSnapshot(document);
                    callback.onSuccess(object);
                } else {
                    callback.onFailure(new Exception("Document does not exist"));
                }
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    /**
     * Pushes a document to the Firestore collection with the specified document ID and calls the callback methods based on the result of the operation.
     *
     * @param document    The document object to be pushed.
     * @param documentId  The ID of the document.
     * @param callback    The callback to be called when the push operation is complete.
     */
    public void pushDocument(T document, String documentId, DocumentCallback<T> callback) {
        collectionReference.document(documentId).set(document.toDocument())
                .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onSuccess(document);
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    /**
     * Converts a Firestore DocumentSnapshot into an object of type T.
     *
     * @param snapshot The DocumentSnapshot to convert.
     * @return An object of type T representing the DocumentSnapshot.
     */
    protected abstract T documentFromSnapshot(DocumentSnapshot snapshot);
}
