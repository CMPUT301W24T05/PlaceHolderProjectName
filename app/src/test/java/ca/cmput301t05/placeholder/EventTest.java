package ca.cmput301t05.placeholder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import org.junit.Test;

import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.qrcode.QRCode;
import ca.cmput301t05.placeholder.qrcode.QRCodeManager;

public class EventTest {

    // test for toDocument
    //test event creation

    /*
    private String eventName;
    private UUID eventPosterID;
    private Bitmap eventPosterBitmap;
    private UUID eventCreator;
    private String eventInfo;
    private String infoQRCode, checkInQR; //qr codes are represented by their string
    private UUID eventID;
    private Calendar eventDate;
    private String eventLocation;
    private int maxAttendees;
     */

    public Event setUp(){
        String eventName = "eventTest";
        String eventInfo = "testing";
        int maxAttendees = 1;
        Event event = new Event(eventName, eventInfo, maxAttendees);


        event.setMaxAttendees(1);
        event.setEventDate(Calendar.getInstance());
        event.setEventName("eventTest");
        event.setEventInfo("testing");
        event.setEventLocation("Area51");
        event.setEventCreator(UUID.randomUUID());

        Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        event.setEventPosterBitmap(bitmap);

        QRCodeManager qrm = new QRCodeManager();
        QRCode check = qrm.generateQRCode(event, "checkIn");
        QRCode info = qrm.generateQRCode(event, "eventInfo");
        event.setInfoQRCode(info.getRawText()); event.setCheckInQR(check.getRawText());


        return event;
    }
    @Test
    public void testEventCreation(){
        Event eventEmpty = new Event();
        assertNotNull(eventEmpty.getEventID());
        assertTrue(eventEmpty.getAttendees().isEmpty());
        assertTrue(eventEmpty.getNotifications().isEmpty());
        assertTrue(eventEmpty.getRegisteredUsers().isEmpty());

        String eventName = "eventTest";
        String eventInfo = "testing";
        int maxAttendees = 1;
        Event event = new Event(eventName, eventInfo, maxAttendees);
        assertEquals(eventName, event.getEventName());
        assertEquals(eventInfo, event.getEventInfo());
        assertEquals(maxAttendees, event.getMaxAttendees());
    }

    @Test
    public void testFromDocument(){
        Event event = setUp();
        Map<String, Object> eventDoc = event.toDocument();
        
    }

}
