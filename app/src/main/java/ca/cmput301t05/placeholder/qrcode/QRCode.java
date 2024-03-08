package ca.cmput301t05.placeholder.qrcode;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Objects;
import java.util.UUID;

import ca.cmput301t05.placeholder.events.Event;

/**
 * Represents a QR Code for an event, encapsulating the event ID and the type of QR code.
 * The QR code can be of two types: infoQR (true) for event information or checkInQR (false) for event check-in.
 * This class utilizes the ZXing ("Zebra Crossing") library for encoding the event's ID and type into a QR code.
 */
public class QRCode {
    UUID eventID;
    public Boolean type; // true if infoQR, false if checkInQR

    public Bitmap bitmap;


    /**
     * Constructs a QRCode object and encodes it with the eventID of the event that it's for,
     * along with the specified type ("eventinfo" for an info QR code, otherwise a check-in QR code).
     *
     * @param event The event for which the QR code is generated.
     * @param type The type of QR code: "eventinfo" for an info QR code, any other value for a check-in QR code.
     */
    public QRCode(Event event, String type){
        if (Objects.equals(type, "eventinfo")) {this.type = true;}
        else {this.type = false;}
        this.eventID = event.getEventID();
        MultiFormatWriter writer = new MultiFormatWriter();

        try
        {
            BitMatrix matrix = writer.encode((eventID.toString() + type), BarcodeFormat.QR_CODE,400,400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            this.bitmap = encoder.createBitmap(matrix);
            //need to attach to a view or something with setImageBitmap(bitmap)




        } catch (WriterException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Gets the type of the QR code.
     *
     * @return Boolean indicating the type of QR code: true for infoQR, false for checkInQR.
     */
    public Boolean getType(){return this.type;}

    /**
     * Sets the type of the QR code.
     *
     * @param type Boolean indicating the type of QR code: true for infoQR, false for checkInQR.
     */
    public void setType(Boolean type){this.type = type;}

}