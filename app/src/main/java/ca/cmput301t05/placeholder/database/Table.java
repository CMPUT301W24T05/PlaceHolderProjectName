package ca.cmput301t05.placeholder.database;

import android.content.Context;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

public abstract class Table<T extends DocumentSerializable> {
    protected CollectionReference collectionReference;

    public Table(String collectionName) {
        collectionReference = DatabaseManager.getInstance().getDb().collection(collectionName);
    }

    public interface DocumentCallback<T> {
        void onSuccess(T document);

        void onFailure(Exception e);
    }

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

    protected abstract T documentFromSnapshot(DocumentSnapshot snapshot);
}
