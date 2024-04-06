package ca.cmput301t05.placeholder;


import static junit.framework.TestCase.assertEquals;

import static org.junit.Assert.assertTrue;

import static ca.cmput301t05.placeholder.qrcode.QRCodeType.CHECK_IN;
import static ca.cmput301t05.placeholder.qrcode.QRCodeType.INFO;

import android.graphics.Bitmap;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.qrcode.QRCode;
import ca.cmput301t05.placeholder.qrcode.QRCodeManager;
import ca.cmput301t05.placeholder.qrcode.QRCodeType;

public class QRCodeTest {

    public BinaryBitmap convertToBinaryBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        // Convert ARGB pixels to grayscale luminance values
        int[] luminances = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;
                luminances[y * width + x] = (byte) ((r + g + b) / 3);
            }
        }

        // Create a LuminanceSource from the grayscale luminance values
        LuminanceSource source = new RGBLuminanceSource(width, height, luminances);

        // Create a BinaryBitmap from the LuminanceSource using a HybridBinarizer
        return new BinaryBitmap(new HybridBinarizer(source));
    }

    @Rule
    QRCodeManager QRCM = new QRCodeManager();

    @Test
    public void testGenerateQRCode(){

        //still receiving a tests not received error. Needs debugging

        Event event = new Event("exampleEvent", "this is my event info", 100);
        QRCode infoqr = QRCM.generateQRCode(event, "eventinfo");
        QRCode checkInQr = QRCM.generateQRCode(event, "checkin");

        BinaryBitmap infobitmap = convertToBinaryBitmap(infoqr.getBitmap());
        MultiFormatReader inforeader = new MultiFormatReader();

        BinaryBitmap checkbitmap = convertToBinaryBitmap(checkInQr.getBitmap());
        MultiFormatReader checreader = new MultiFormatReader();
        try {
            Result iresult = inforeader.decode(infobitmap);
            String infoqrText = iresult.getText();

            Result cresult = inforeader.decode(infobitmap);
            String checkqrText = cresult.getText();

            //test that the QR is of the right types
            QRCodeType infoqrType = infoqr.getType();
            QRCodeType checkInQrType = checkInQr.getType();

            assertEquals(INFO, infoqrType);
            assertEquals(CHECK_IN, checkInQrType);


            //Test that the qrcode includes a UUID
            String iEventId = QRCM.getEventID(infoqrText).toString();
            UUID iUUID = UUID.fromString(iEventId);
            assertTrue("Value is of type UUID", iUUID instanceof UUID);

            String cEventId = QRCM.getEventID(checkqrText).toString();
            UUID cUUID = UUID.fromString(cEventId);
            assertTrue("Value is of type UUID", cUUID instanceof UUID);



        } catch (NotFoundException e) {
            throw new RuntimeException("Failed to decode QR code", e);
        }


    }
}
