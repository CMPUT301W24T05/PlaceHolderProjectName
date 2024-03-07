package ca.cmput301t05.placeholder.events;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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


    public QRCodeManager() {

    }

    public QRCode generateQRCode(Event event, String type) {
        return new QRCode(event, type);
    }


    public HashMap<String, Object> getEventInfo(String qrcode) {
        //extract only the part with t
        String doc = qrcode.substring(0, 35);

        String type = qrcode.substring(36);

        if (type.equals("true")) {
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

            return (HashMap<String, Object>) document.getData();
        } else {
            return null;
        }


    }
}
