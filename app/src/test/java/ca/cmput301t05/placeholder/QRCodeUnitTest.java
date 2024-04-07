package ca.cmput301t05.placeholder;


import static junit.framework.TestCase.assertEquals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import static ca.cmput301t05.placeholder.qrcode.QRCodeType.CHECK_IN;
import static ca.cmput301t05.placeholder.qrcode.QRCodeType.INFO;

import android.graphics.Bitmap;
import android.util.Log;

import org.junit.*;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.UUID;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.qrcode.QRCode;
import ca.cmput301t05.placeholder.qrcode.QRCodeManager;
import ca.cmput301t05.placeholder.qrcode.QRCodeType;

/**
 * Unit tests for QRCode class.
 * @author Anthony
 */
@RunWith(RobolectricTestRunner.class)
public class QRCodeUnitTest {

    /**
     * Tests that a info QR code is encoded with the correct UUID as the event ID
     * and correct type of QR code; info QR code.
     */
    @Test
    public void testGenerateInfoQRCode() {
        QRCodeManager qrCodeManager = new QRCodeManager();
        Event event = new Event("Testy Event", "this is my event info", 100);
        UUID eventID = event.getEventID();
        QRCode qrCode = qrCodeManager.generateQRCode(event, "eventInfo");
        assertNotNull(qrCode.getType());
        Assert.assertEquals(qrCode.getType(), INFO); // Assert that the QR code is "info" QR code type
        String rawText = qrCode.getRawText();
        assertEquals(qrCodeManager.getEventID(rawText).getClass(), UUID.class);// Check that event ID is of type UUID
        assertEquals(eventID,qrCodeManager.getEventID(rawText)); // Assert that the QR code's UUID and the event ID match
    }

    /**
     * Tests that a check-in QR code is encoded with the correct UUID as the event ID
     * and correct type of QR code; check-in QR code.
     */
    @Test
    public void testGenerateCheckInQRCode() {
        QRCodeManager qrCodeManager = new QRCodeManager();
        Event event = new Event("Cool Event", "Even description", 90);
        UUID eventID = event.getEventID();
        QRCode qrCode = qrCodeManager.generateQRCode(event, "eventCheckIn");
        assertNotNull(qrCode.getType());
        Assert.assertEquals(qrCode.getType(), CHECK_IN); // Assert that the QR code is a "check-in" QR code type
        String rawText = qrCode.getRawText();
        assertEquals(qrCodeManager.getEventID(rawText).getClass(), UUID.class);// Check that event ID is of type UUID
        assertEquals(eventID,qrCodeManager.getEventID(rawText)); // Assert that the QR code's UUID and the event ID match
    }


}
