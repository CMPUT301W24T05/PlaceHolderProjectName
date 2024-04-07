package ca.cmput301t05.placeholder;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

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
     */
    @Test
    public void testMaxAttendeeCount(){
        // Should've used parameterized test for this method but I already typed it out so oh well.
        assertEquals(new Event("Test 1", "Test 1", 5).getMaxAttendees(), 5);
        assertEquals(new Event("Test 2", "Test 2", 100000).getMaxAttendees(), 100000);
        assertEquals(new Event("Test 3", "Test 3", 700).getMaxAttendees(), 700);
        assertEquals(new Event("Test 4", "Test 4", 0).getMaxAttendees(), 0);
        assertEquals(new Event("Test 5", "Test 5", 45).getMaxAttendees(), 45);
        assertEquals(new Event("Test 6", "Test 6", 1).getMaxAttendees(), 1);
    }


    /**
     * Tests the checkIn and reachMaxCapacity methods for the Event class.
     * @param profiles ArrayList of profiles.
     * @param expectedCount The expected count of attendees for the event.
     * @param maxCap The expected boolean for whether max capacity has been reached.
     */
    @ParameterizedTest
    @MethodSource("attendeeDataProvider")
    public void testCheckInAttendees(ArrayList<Profile> profiles, int expectedCount, boolean maxCap){
        Event event = new Event("Test event", "Test event description", 5);
        int attendeeCount = profiles.size();
        for (int i = 0; i < attendeeCount; i++ ){
            event.checkIn(profiles.get(i), 0d, 0d); // Check in every profile from profiles.
        }
        assertEquals( event.getNumAttendees(), expectedCount); // Assert correct count returned by method.
        assertEquals(event.reachMaxCapacity(), maxCap);

    }

    /**
     * Provides data for parameterized test method {@code testCheckInAttendees}.
     * @return Stream of arguments to be used.
     */
    private static Stream<Arguments> attendeeDataProvider(){
        return Stream.of(
                Arguments.of(generateProfilesForTesting(1), 1, false),
                Arguments.of(generateProfilesForTesting(2), 2, false),
                Arguments.of(generateProfilesForTesting(3), 3, false),
                Arguments.of(generateProfilesForTesting(4), 4, false),
                Arguments.of(generateProfilesForTesting(5), 5, true),
                Arguments.of(generateProfilesForTesting(0), 0, false)
        );
    }

    /**
     * Generates the data to be be used for testing.
     * @param count The size of the ArrayList returned; the number of profiles to be generated.
     * @return ArrayList<Profile>
     */
    private static ArrayList<Profile> generateProfilesForTesting(int count){
        ArrayList<Profile> arrayList = new ArrayList<>();
        for (int i = 0; i < count; i++){
            arrayList.add(new Profile(String.valueOf(i), UUID.randomUUID()));
        }
        return arrayList;
    }


    /**
     * Tests the checkIn method for the Event class when the same profile
     * but with different geolocation checks in multiple times.
     */
    @Test
    public void testDuplicateAttendeeCheckIn(){
        Profile profile1 = new Profile("Jack",  UUID.randomUUID());
        Profile profile2 = new Profile("Bob",  UUID.randomUUID());
        Event event  = new Event("Test event", "Test event description", 46);
        assertEquals(event.getNumAttendees(), 0);
        event.checkIn(profile1, 123213d, 2213213d);
        assertEquals(event.getNumAttendees(), 1);
        event.checkIn(profile2, 8884553d, 00213d);
        assertEquals(event.getNumAttendees(), 2);
        event.checkIn(profile2, 823123d, 42222213d); // Same profile checking in just different geolocation.
        assertEquals(event.getNumAttendees(), 2); // Assert that attendee count has remained the same.

    }

}
