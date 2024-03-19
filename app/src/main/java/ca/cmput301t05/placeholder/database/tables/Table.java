package ca.cmput301t05.placeholder.database.tables;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import ca.cmput301t05.placeholder.database.DatabaseManager;
import ca.cmput301t05.placeholder.database.utils.Collections;
import ca.cmput301t05.placeholder.database.utils.DocumentSerializable;

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
     * enum which lets us get all the paths and such
     */
    final Collections COLLECTION;

    /**
     * Constructs a Table object with the given collection name.
     *
     * @param collection an enum which allows us to grab paths and such
     */
    public Table(Collections collection) {
        COLLECTION = collection;
        collectionReference = DatabaseManager.getInstance().getDb().collection(collection.getPath());
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

    public void fetchMultipleDocuments(ArrayList<String> documents, DocumentCallback<ArrayList<T>> callback){

        if (documents.isEmpty()){
            callback.onSuccess(new ArrayList<T>());
        }

        Log.d("Fetch", "in loop");
        //Idea is that we loop through all the tasks then add them to a arraylist need to be careful since the tasks may be on different threads
        ArrayList<T> fetchedDocs = new ArrayList<>();
        AtomicInteger docCounter = new AtomicInteger(documents.size()); //start at the size so we can deecrement to 0 so we dont have to check document size
        AtomicBoolean docFetchFail = new AtomicBoolean(false);

        for (String id : documents) {

            collectionReference.document(id).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();

                    T object = documentFromSnapshot(document);
                    fetchedDocs.add(object);

                    // Check if all documents have been fetched
                    if (docCounter.decrementAndGet() == 0 && !docFetchFail.get()) {
                        callback.onSuccess(fetchedDocs);
                    }
                } else {
                    docFetchFail.set(true);
                    callback.onFailure(task.getException());
                    // Prevent multiple callbacks
                    return;
                }
            });

        }

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
     * allows us to push multiple documents to the cloud. Does not work for array lists > 50
     *
     * @param documents document objects which we'll push
     * @param documentIds ids of the documents we'll push
     * @param callback callback to be called when push operation is complete
     */
    public void pushMultipleDocuments(ArrayList<T> documents, ArrayList<String> documentIds, DocumentCallback<ArrayList<T>> callback ){


        WriteBatch batch = DatabaseManager.getInstance().getDb().batch();

        if (documents.size() != documentIds.size()) {
            // Handle the error, perhaps by calling the callback with an error message
            callback.onFailure(new Exception("Documents arent the same sizes"));
            return;
        }

        // Loop through the documents
        for (int i = 0; i < documents.size(); i++) {
            // Get each document and its corresponding ID
            T document = documents.get(i);
            String documentId = documentIds.get(i);


            //get the document reference so we can get ready to batch send it
            DocumentReference docRef = collectionReference.document(documentId);

            batch.set(docRef, document.toDocument());
        }

        batch.commit().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onSuccess(documents); // Adjust as needed
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