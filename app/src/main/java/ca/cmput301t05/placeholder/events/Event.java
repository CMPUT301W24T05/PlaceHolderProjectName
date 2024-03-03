package ca.cmput301t05.placeholder.events;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
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




    HashMap<Profile,Integer> attendees; //stores all attendees and how many times they have checked in

    public Event(String name, String eventInfo, int maxAttendees){

        //need to make creation of event automatically go to database

        this.eventName = name;
        this.eventInfo = eventInfo;
        this.eventID = UUID.randomUUID();
        this.maxAttendees = maxAttendees;
        this.attendees = new HashMap<>();
        //create QR codes
        this.infoQRCode = QRCM.generateQRCode(this, "eventInfo");
        this.checkInQR = QRCM.generateQRCode(this, "checkIn");

        DatabaseManager databaseManager = DatabaseManager.getInstance();

        databaseManager.db.collection("events").document(eventID.toString()).set(toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Event successfully uploaded!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("Error uploading event: " + e.getMessage());
                    }
                });




    }

    //checks in a attendee
    public boolean checkIn(Profile profile){

        //returns false if attendee can't check in, true if can

        if(attendees.size() == maxAttendees){
            return false;
        }

        if (attendees.containsKey(profile)) {

            Integer i = attendees.get(profile);

            attendees.put(profile, i + 1);

        } else {

            attendees.put(profile, 1);
        }

        return true;

    }

    public Map<String, Object> toMap(){

        HashMap<String, Object> result = new HashMap<>();

        result.put("eventName", eventName);
        result.put("eventPosterID", eventPosterID != null ? eventPosterID.toString() : null);
        result.put("eventInfo", eventInfo);
        result.put("eventDate", eventDate != null ? eventDate.getTimeInMillis() : null);
        result.put("maxAttendees", maxAttendees);

        if (attendees.isEmpty()) {
            result.put("attendees", null);

        } else {

            HashMap<String, Integer> attendeesMap = new HashMap<>();
            for (Map.Entry<Profile, Integer> entry : attendees.entrySet()) {
                // Assuming Profile has a unique identifier we can use as a key
                attendeesMap.put(entry.getKey().getProfileID().toString(), entry.getValue());
            }

            result.put("attendees", attendeesMap);

        }


        return result;

    }

    public void removeAttendee(Profile profile){

        attendees.remove(profile);
    }

    //returns array of all attendees
    public Profile[] getAttendees(){


        return attendees.keySet().toArray(new Profile[0]);
    }

    //getters and setters


    public void setAttendees(HashMap<Profile, Integer> attendees) {
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

}
