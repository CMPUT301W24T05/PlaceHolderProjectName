package ca.cmput301t05.placeholder.events;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;


import java.util.HashMap;

import ca.cmput301t05.placeholder.database.DatabaseManager;

public class QRCodeManager {

    protected DatabaseManager databaseManager;

    public QRCodeManager() {

    }

    public QRCode generateQRCode(Event event) {
        return new QRCode(event);
    }


    public HashMap<String, Object> getEventInfo(String qrcode) {
        String doc = qrcode.substring(0, 35);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        DocumentReference eventRef = db.collection("events").document(doc);

        Task<DocumentSnapshot> task = eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        DocumentSnapshot document = task.getResult();
        HashMap<String, Object> eventInfo = (HashMap<String, Object>) document.getData();

        return eventInfo;



    }
}