package ca.cmput301t05.placeholder.events;

/*
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

 */

public class QRCodeManager {


    public QRCodeManager(){

    }

    public QRCode generateQRCode (Event event){
        return new QRCode(event);
    }

    /* Still working on it ig
    
    public String getEventInfo (String qrcode){
        String doc = qrcode.substring(0, 35);

        Firestore firestore = FirestoreOptions.getDefaultInstance().getService();
        DocumentReference docRef = firestore.collection("events").document(doc);

        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            // Get the path of the document
            String path = document.getReference().getPath();
            System.out.println("Document path: " + path);
        } else {
            System.out.println("Document not found!");
        }
    }
    */

    /*
    public Boolean checkType(QRCode qr){
        if (qr.getType()){
            return true;
        }
        else{
            return false;
        }
    }
     */


}
