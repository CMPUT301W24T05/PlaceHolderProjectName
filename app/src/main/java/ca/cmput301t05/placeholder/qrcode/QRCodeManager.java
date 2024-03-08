package ca.cmput301t05.placeholder.qrcode;


import java.io.Serializable;
import java.util.UUID;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.qrcode.QRCode;


/**
 * Manages QR code generation and processing for events, including generating QR codes with event information,
 * determining QR code types, and facilitating event joining through QR code scanning.
 */
public class QRCodeManager implements Serializable {

    /**
     * Constructs a QRCodeManager object. This constructor is empty and does not perform any operations.
     */
    public QRCodeManager() {

    }

    /**
     * Generates a QR code for a specified event and type. The type determines whether the QR code is for
     * event information ("eventinfo") or for check-in purposes.
     *
     * @param event The event for which the QR code is generated.
     * @param type  The type of QR code to generate: "eventinfo" for an info QR code, any other value for a check-in QR code.
     * @return A QRCode object containing the event ID encoded within the QR code along with its type.
     */
    public QRCode generateQRCode(Event event, String type) {
        return new QRCode(event, type);
    }


    /**
     * Attempts to join an event by decoding the provided raw QR code string. This method checks if the QR code
     * is of the correct type (check-in QR code) before proceeding to extract the event ID and attempt to join the event.
     *
     * @param rawQRcodeString The raw string representation of the QR code.
     * @return An Event object if the QR code is for a valid check-in and the event could be joined, otherwise returns null.
     */
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


    /**
     * Checks the type of the QR code based on its raw string representation. The type is determined by the string
     * following the event ID in the raw QR code string.
     *
     * @param rawQRcodeString The raw string representation of the QR code.
     * @return true if the QR code is an info QR code, false if it is a check-in QR code.
     */
    public boolean checkQRcodeType(String rawQRcodeString) {
        // Make sure you pass the raw text of QR code to this method
        // true if infoQR, false if checkInQR
        return  rawQRcodeString.substring(36).equals("True");
    }

    /**
     * Extracts and returns the event ID from a raw QR code string.
     *
     * @param eventIdStr The raw QR code string containing the event ID.
     * @return The UUID of the event extracted from the raw QR code string.
     */
    public UUID scanGetEventID(String eventIdStr){
            String eventID = eventIdStr.substring(0, 35); // parse the first 35 characters of the string to get UUID string
            return UUID.fromString(eventID); // Cast String to type UUID
        }






}
