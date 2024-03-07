package ca.cmput301t05.placeholder.events;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ca.cmput301t05.placeholder.database.DatabaseManager;
import ca.cmput301t05.placeholder.profile.Profile;

public class Event {

    String eventName;

    UUID eventPosterID;

    String eventInfo;

    QRCode infoQRCode, checkInQR;

    QRCodeManager QRCM = new QRCodeManager();

    UUID eventID;

    Calendar eventDate;

    int maxAttendees;

    HashMap<String, Integer> attendees; //stored this way for the database
    //string = profileID, int = # of times checked in

    public Event(){
        this.eventID = UUID.randomUUID();
        this.attendees = new HashMap<>();
        infoQRCode = QRCM.generateQRCode(this, "eventInfo");
        checkInQR = QRCM.generateQRCode(this, "checkIn");
    }

    public Event(UUID eventID){
        //please call getEventDatabase to this otherwise will error
        this.eventID = eventID;
        this.attendees = new HashMap<>();
        infoQRCode = QRCM.generateQRCode(this, "eventInfo");
        checkInQR = QRCM.generateQRCode(this, "checkIn");
    }


    public Event(String name, String eventInfo, int maxAttendees){


        this.eventName = name;
        this.eventInfo = eventInfo;
        this.eventID = UUID.randomUUID();
        this.maxAttendees = maxAttendees;
        this.attendees = new HashMap<>();
        infoQRCode = QRCM.generateQRCode(this, "eventInfo");
        checkInQR = QRCM.generateQRCode(this, "checkIn");
    }

    public boolean getEventFromDatabase(UUID eventID){
      
        DatabaseManager databaseManager = DatabaseManager.getInstance();

        final boolean[] found = new boolean[1];

        databaseManager.db.collection("events").document(eventID.toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    updateFromDocScreenshot(documentSnapshot);
                }
                else {
                    found[0] = false;
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        found[0] = false;
                    }
                });

        return found[0];
    }


    public boolean getEventFromDatabase(){
        //use current uuid
        return getEventFromDatabase(this.eventID);
    }

    public boolean sendEventToDatabase(){
        //returns true if event successfully is sent to db
        //false otherwise


        final boolean[] found = new boolean[1]; //essentially is an area in memory so we can use it outside of method

        DatabaseManager databaseManager = DatabaseManager.getInstance();

        databaseManager.db.collection("events").document(eventID.toString()).set(toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Event successfully uploaded!");
                        found[0] = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("Error uploading event: " + e.getMessage());
                        found[0] = false;
                    }
                });

        return found[0];

    }

    //checks in a attendee
    public boolean checkIn(Profile profile){

        //returns false if attendee can't check in, true if can

        if(attendees.size() == maxAttendees){
            return false;
        }

        if (attendees.containsKey(profile.getProfileID().toString())) {

            Integer i = attendees.get(profile.getProfileID().toString());

            attendees.put(profile.getProfileID().toString(), i + 1);

        } else {

            attendees.put(profile.getProfileID().toString(), 1);
        }

        return true;

    }

    private void updateFromDocScreenshot(DocumentSnapshot documentSnapshot){

        if (documentSnapshot != null && documentSnapshot.exists()) {

            this.eventName = documentSnapshot.getString("eventName");

            Timestamp eventTime = documentSnapshot.getTimestamp("eventDate");


            Calendar c = Calendar.getInstance();
            c.setTime(eventTime.toDate());

            this.eventDate = c;


            this.maxAttendees = Math.toIntExact(documentSnapshot.getLong("maxAttendees"));

            this.eventInfo = documentSnapshot.getString("eventInfo");

            String posterID = documentSnapshot.getString("eventPosterID");

            if (posterID != null){
                this.eventPosterID = UUID.fromString(posterID);
            }


            if (documentSnapshot.get("attendees") != null){

                this.attendees = (HashMap<String, Integer>) documentSnapshot.get("attendees");
            }

            // Set other fields similarly
        } else {
            // Handle the case where the document does not exist
            System.out.println("Document not found"); //this shouldnt happen though
        }

    }

    private Map<String, Object> toMap(){

        HashMap<String, Object> result = new HashMap<>();

        result.put("eventName", eventName);
        result.put("eventPosterID", eventPosterID != null ? eventPosterID.toString() : null);
        result.put("eventInfo", eventInfo);

        if (eventDate != null){
            //convert to a firebase Timestamp
            result.put("eventDate", new Timestamp(eventDate.getTime()));
        }
        else {
            result.put("eventDate", null);
        }

        result.put("maxAttendees", maxAttendees);

        if (attendees.isEmpty()) {
            result.put("attendees", null);

        } else {

            result.put("attendees", this.attendees);

        }


        return result;

    }

    public void removeAttendee(Profile profile){

        attendees.remove(profile.getProfileID().toString());
    }

    //returns array of all attendees
    public String[] getAttendees(){


        return attendees.keySet().toArray(new String[0]);
    }

    //getters and setters


    public void setAttendees(HashMap<String, Integer> attendees) {
        this.attendees = attendees;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }


    public String getEventInfo() {
        return eventInfo;
    }

    public String getEventName() {
        return eventName;
    }

    public UUID getEventPosterID() {
        return eventPosterID;
    }

    public UUID getEventID(){
        return  eventID;
    }

    public void setEventPosterID(UUID id){
        this.eventPosterID = id;
    }

    public void setEventID(UUID eventID) {
        this.eventID = eventID;
    }

    public void setEventDate(Calendar eventDate) {
        this.eventDate = eventDate;
    }

    public void setMaxAttendees(int c){
        this.maxAttendees = c;
    }

    public int getMaxAttendees(){
        return this.maxAttendees;
    }

}



