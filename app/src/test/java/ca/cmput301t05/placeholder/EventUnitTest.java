package ca.cmput301t05.placeholder;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import java.util.UUID;
import ca.cmput301t05.placeholder.events.Event;




/**
 * Unit tests for the Event class.
 * @author Anthony
 */

public class EventUnitTest {


    /**
     * Test the event constructor with no parameters provided.
     */
    @Test
    public void testEventConstructorNoParam(){
        Event event = new Event();
        assertEquals(event.getAttendees().size(), 0); // Should be empty array
        assertEquals(event.getRegisteredUsers().size(),0);
        assertEquals(event.getNotifications().size(),0);
        assertEquals(event.getEventID().getClass(), UUID.class); // Event ID should be a UUID
    }

    /**
     * Test the event constructor with only the event ID provided.
     */
    @Test
    public void testEventConstructorUuidProvided(){
        UUID uuid = UUID.randomUUID(); // Generate a random uuid
        Event event = new Event(uuid);
        // None of the following fields for an event object should be null when initialized
        assertEquals(event.getEventID(), uuid);
        assertNotNull(event.getAttendees());
        assertNotNull(event.getNotifications());
        assertNotNull(event.getRegisteredUsers());
        assertNotNull(event.getRegisteredUsersNum());
        assertNotNull(event.getAttendeesNum());
    }

    /**
     * Test the event constructor with the event name, event description, and event capacity limit provided.
     */
    @Test
    public void testEventConstructorMultipleParamProvided(){
        Event event = new Event("Party","",0);
        assertEquals(event.getEventName(), "Party");
        assertEquals(event.getEventInfo(), "");
        assertEquals(event.getMaxAttendees(), 0);
        assertNotNull(event.getAttendees());
        assertNotNull(event.getNotifications());
        assertNotNull(event.getRegisteredUsers());
        assertNotNull(event.getAttendeesNum());
        assertNotNull(event.getRegisteredUsersNum());
    }

    /**
     * Tests the getMaxAttendee method for the Event class.
     *
     */
    @Test
    public void testMaxAttendeeCount(){
        assertEquals(new Event("Test 1", "Test 1", 5).getMaxAttendees(), 5);
        assertEquals(new Event("Test 2", "Test 2", 100000).getMaxAttendees(), 100000);
        assertEquals(new Event("Test 3", "Test 3", 700).getMaxAttendees(), 700);
        assertEquals(new Event("Test 4", "Test 4", 0).getMaxAttendees(), 0);
        assertEquals(new Event("Test 5", "Test 5", 45).getMaxAttendees(), 45);
        assertEquals(new Event("Test 6", "Test 6", 1).getMaxAttendees(), 1);
    }


}
