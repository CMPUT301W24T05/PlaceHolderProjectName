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
import java.util.UUID;

import ca.cmput301t05.placeholder.database.DatabaseManager;

public class QRCodeManager {


    public QRCodeManager() {

    }

    public QRCode generateQRCode(Event event, String type) {
        return new QRCode(event, type);
    }
    public Event getEventInfo(String qrcode) {
        //extract only the part with the event id
        String eventID = qrcode.substring(0, 35);


        String type = qrcode.substring(36);

        if (type.equals("true")) {

            Event e = new Event(UUID.fromString(eventID));
            e.getEventFromDatabase();

            return e;

        } else {
            return null;
        }


    }


}
