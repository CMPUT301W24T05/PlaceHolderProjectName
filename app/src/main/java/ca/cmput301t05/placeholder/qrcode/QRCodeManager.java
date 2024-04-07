package ca.cmput301t05.placeholder.qrcode;


import java.io.Serializable;
import java.util.UUID;

import ca.cmput301t05.placeholder.events.Event;


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
     * event information ("eventInfo") or for check-in purposes.
     *
     * @param event The event for which the QR code is generated.
     * @param type  The type of QR code to generate: "eventInfo" for an info QR code, any other value for a check-in QR code.
     * @return A QRCode object containing the event ID encoded within the QR code along with its type.
     */
    public QRCode generateQRCode(Event event, String type) {
        return new QRCode(event, type);
    }


    /**
     * Checks the type of the QR code based on its raw string representation. The type is determined by the string
     * following the event ID in the raw QR code string.
     *
     * @param rawQRcodeString The raw string representation of the QR code.
     * @return INFO if info, CHECK_IN if check_in, and ERROR otherwise
     */
    public QRCodeType checkQRcodeType(String rawQRcodeString) {

        //qrcode strings are split by semicolons
        String[] parts = rawQRcodeString.split(";");

        if (parts[1].equals("INFO")){
            return QRCodeType.INFO;
        }   else if (parts[1].equals("CHECK_IN")){
            return QRCodeType.CHECK_IN;
        }

        return QRCodeType.ERROR;
    }


    /**
     * Extracts and returns the event ID from a raw QR code string.
     *
     * @param rawQr The raw QR code string containing the event ID.
     * @return The UUID of the event extracted from the raw QR code string.
     */
    public UUID getEventID(String rawQr){

        String[] parts = rawQr.split(";");

        return UUID.fromString(parts[0]);
    }






}
