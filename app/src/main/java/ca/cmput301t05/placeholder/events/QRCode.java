package ca.cmput301t05.placeholder.events;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Objects;
import java.util.UUID;

public class QRCode {
    UUID eventID;
    public Boolean type; // true if infoQR, false if checkInQR


    // constructs a QRCode object and encodes it with the eventID of the event that it's for
    public QRCode(Event event, String type){
        if (Objects.equals(type, "eventinfo")) {this.type = true;}
        else {this.type = false;}
        this.eventID = event.getEventID();
        MultiFormatWriter writer = new MultiFormatWriter();

        try
        {
            BitMatrix matrix = writer.encode((eventID.toString() + type), BarcodeFormat.QR_CODE,400,400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            //need to attach to a view or something with setImageBitmap(bitmap)




        } catch (WriterException e)
        {
            e.printStackTrace();
        }
    }



    public Boolean getType(){return this.type;}

    public void setType(Boolean type){this.type = type;}

}
