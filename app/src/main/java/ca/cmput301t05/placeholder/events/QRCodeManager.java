package ca.cmput301t05.placeholder.events;


import java.util.UUID;


public class QRCodeManager {




    public QRCodeManager() {

    }

    public QRCode generateQRCode(Event event, String type) {
        return new QRCode(event, type);
    }


    public Event joinEvent(String rawQRcodeString){ // Join the event
        if (!checkQRcodeType(rawQRcodeString)){ // its a valid QR code for check-in
            UUID uuid = scanGetEventID(rawQRcodeString);
            // I'm commenting out getting the event from DB for now, will do it outside this activity
            Event event = new Event(uuid);
//            boolean success = event.getEventFromDatabase();
//            Log.e("amirza2", "Join event state" + success);
            return event;
        }
        return null;

    }

    
    // public void
    public boolean checkQRcodeType(String rawQRcodeString) {
        // Make sure you pass the raw text of QR code to this method
        // true if infoQR, false if checkInQR
        return  rawQRcodeString.substring(36).equals("True");
    }

    public UUID scanGetEventID(String eventIdStr){
            String eventID = eventIdStr.substring(0, 35); // parse the first 35 characters of the string to get UUID string
            return UUID.fromString(eventID); // Cast String to type UUID
        }






}
