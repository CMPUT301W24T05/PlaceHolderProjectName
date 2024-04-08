package ca.cmput301t05.placeholder;

import static com.google.firebase.firestore.model.FieldIndex.IndexOffset.fromDocument;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ca.cmput301t05.placeholder.notifications.Notification;

/**
 * Unit tests for the Notification class.
 * @author Anthony
 */
@RunWith(RobolectricTestRunner.class)
public class NotificationUnitTest {


    private Notification notification;
    private final UUID creatorID = UUID.randomUUID();
    private final UUID eventID = UUID.randomUUID();
    private final String message = "Test Notification";

    /**
     * Sets up a Notification object to perform tests on.
     */
    @Before
    public void setUp() {
        notification = new Notification(message, creatorID, eventID);
    }

    /**
     * Test the notification constructor with message, creatorID, and
     * eventID provided as parameters.
     */
    @Test
    public void testConstructorWithParams(){
        assertEquals(message, notification.getMessage());
        assertEquals(creatorID, notification.getCreatorID());
        assertEquals(eventID, notification.getFromEventID());
        assertNotNull(notification.getTimeCreated());
        assertEquals(notification.getNotificationID().getClass(), UUID.class);
        assertFalse(notification.isPinned());
        assertFalse(notification.isRead());
        assertFalse(notification.isPush()); // Java will set a variable to false by default if boolean value not specified.
    }


    /**
     * Tests the setters for the Notification class.
     */
    @Test
    public void testSetters() {
        UUID newCreatorID = UUID.randomUUID(); // Set up new fields for the notification object.
        UUID newEventID = UUID.randomUUID();
        String newMessage = "New Test Notification";
        Calendar newTimeCreated = Calendar.getInstance();
        notification.setCreatorID(newCreatorID);
        notification.setFromEventID(newEventID);
        notification.setMessage(newMessage);
        notification.setTimeCreated(newTimeCreated);
        notification.setPinned(true);
        notification.setRead(true);
        notification.setPush(true);
        assertEquals(newCreatorID, notification.getCreatorID());
        assertEquals(newEventID, notification.getFromEventID());
        assertEquals(newMessage, notification.getMessage());
        assertEquals(newTimeCreated, notification.getTimeCreated());
        assertTrue(notification.isPinned());
        assertTrue(notification.isRead());
        assertTrue(notification.isPush());
    }

    /**
     * Tests that a notification is correctly converted to a document
     * in the form of a Map object to be stored in the database.
     */
    @Test
    public void testToDocument(){
        assertNotNull(notification.toDocument());
        assertTrue(notification.toDocument().containsKey("notificationID"));
        assertTrue(notification.toDocument().containsKey("creatorID"));
        assertTrue(notification.toDocument().containsKey("fromEventID"));
        assertTrue(notification.toDocument().containsKey("message"));
        assertTrue(notification.toDocument().containsKey("timeCreated"));
        assertTrue(notification.toDocument().containsKey("isPinned"));
        assertTrue(notification.toDocument().containsKey("isRead"));
        assertTrue(notification.toDocument().containsKey("isPush"));
    }

    /**
     * Tests that a notification is correctly initialized from a document
     * fetched from the database.
     */
    @Test
    public void testFromDocument() {
        DocumentSnapshot mockDocument  = mock(DocumentSnapshot.class); // Mock the DocumentSnapshot object
        Timestamp mockTimestamp = mock(Timestamp.class); // Mock Timestamp instance used by method
        Date mockDate = mock(Date.class); // Mock Date instance
        UUID notifID = UUID.randomUUID();
        when(mockDocument.getString("notificationID")).thenReturn(String.valueOf(notifID));
        when(mockDocument.getString("creatorID")).thenReturn(String.valueOf(creatorID));
        when(mockDocument.getString("fromEventID")).thenReturn(String.valueOf(eventID));
        when(mockDocument.getString("message")).thenReturn("Testy message");
        when(mockDocument.getTimestamp("timeCreated")).thenReturn(mockTimestamp);
        when(mockTimestamp.toDate()).thenReturn(mockDate);
        when(mockDate.getTime()).thenReturn( 1618831200000L); // Return arbitrary time value in milliseconds
        when(mockDocument.getBoolean("isPinned")).thenReturn(true);
        when(mockDocument.get("isRead")).thenReturn(null); // If null, field "isRead" should be set to false.
        when(mockDocument.get("isPush")).thenReturn(null); // Same as above
        Notification notification = new Notification(mockDocument); // Initialize a notification object from the mock document snapshot.
        assertEquals("Testy message", notification.getMessage());
        assertEquals(creatorID, notification.getCreatorID());
        assertEquals(eventID, notification.getFromEventID());
        assertTrue(notification.isPinned());
        assertFalse(notification.isRead());
        assertFalse(notification.isPush());
        assertEquals(1618831200000L, notification.getTimeCreated().getTimeInMillis());
    }


    /**
     * Tests that the notification constructor throws a necessary JSONException
     * when the data provided for its {@code timeCreated} field is an invalid JSON string.
     * @throws JSONException The expected exception to be thrown by the method.
     */
    @Test(expected = JSONException.class)
    public void testConstructorWithInvalidJson() throws JSONException {
        // Create a map with valid data, but an invalid JSON string for the time_created field
        Map<String, String> invalidData = new HashMap<>();
        invalidData.put("notification_uuid", String.valueOf(UUID.randomUUID()));
        invalidData.put("message", "Test message");
        invalidData.put("creator_id", String.valueOf(UUID.randomUUID()));
        invalidData.put("event_uuid", String.valueOf(UUID.randomUUID()));
        invalidData.put("is_push", "false");
        invalidData.put("is_pinned", "true");
        invalidData.put("is_read", "false");
        // Provide an invalid JSON string for the time_created field
        invalidData.put("time_created", "invalid_json_string");
        // This constructor call should throw a JSONException
        Notification notification = new Notification(invalidData);
    }

}
