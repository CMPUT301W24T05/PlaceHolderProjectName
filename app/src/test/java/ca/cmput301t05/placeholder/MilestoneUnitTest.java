package ca.cmput301t05.placeholder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static ca.cmput301t05.placeholder.milestones.MilestoneType.EVENTSTART;
import static ca.cmput301t05.placeholder.milestones.MilestoneType.FIRSTATTENDEE;
import static ca.cmput301t05.placeholder.milestones.MilestoneType.FIRSTSIGNUP;
import static ca.cmput301t05.placeholder.milestones.MilestoneType.FULLCAPACITY;
import static ca.cmput301t05.placeholder.milestones.MilestoneType.HALFWAY;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.milestones.Milestone;
import ca.cmput301t05.placeholder.milestones.MilestoneType;


/**
 * Unit test for the Milestone class.
 * @author Anthony
 */
public class MilestoneUnitTest {

    /**
     * Tests the constructor for the milestone class.
     */
    @Test
    public void testMilestoneConstructor() {
        UUID eventID = UUID.randomUUID();
        Event testEvent  = new Event(eventID);
        String eventName = "Test Event";
        ca.cmput301t05.placeholder.milestones.Milestone milestone = new ca.cmput301t05.placeholder.milestones.Milestone(FIRSTATTENDEE, testEvent);
        assertEquals(eventID, UUID.fromString(milestone.getEventID()));
        assertEquals(FIRSTATTENDEE, milestone.getmType());
        assertNotNull(milestone.getEventID());
        assertEquals("From " + eventName + ": YOU'VE GOT YOUR FIRST ATTENDEE!", milestone.generateMessage(FIRSTATTENDEE, eventName));
    }

    /**
     * Tests that each milestone type correctly generates the appropriate message.
     */
    @ParameterizedTest
    @MethodSource("mileStoneConstructorDataProvider")
    public void testMsgGeneration4DiffMilestoneTypes(MilestoneType milestoneType, String expectedMessage) {
        // Setting up the event object
        UUID eventID = UUID.randomUUID();
        String eventName = "Test Event";
        Event event = new Event(eventID);
        event.setEventName(eventName);
        // Initializing the milestone object to test
        ca.cmput301t05.placeholder.milestones.Milestone milestone = new ca.cmput301t05.placeholder.milestones.Milestone(milestoneType, event);
        Assertions.assertNotNull(milestone);
        Assertions.assertEquals("Test Event", milestone.getEventName());
        Assertions.assertEquals(eventID, UUID.fromString(milestone.getEventID()));
        Assertions.assertEquals(milestoneType, milestone.getmType());
        Assertions.assertEquals(expectedMessage, milestone.generateMessage(milestoneType, eventName)); // Verify that message is generated correctly
    }

    /**
     * Provides test data for {@code testMsgGeneration4DiffMilestoneTypes} method.
     * @return Stream of arguments to be used by the test.
     */
    private static Stream<Arguments> mileStoneConstructorDataProvider(){
        // Each argument is a different milestone type with a unique message.
        return Stream.of(
                Arguments.of(FIRSTATTENDEE, "From Test Event: YOU'VE GOT YOUR FIRST ATTENDEE!"),
                Arguments.of(FIRSTSIGNUP, "From: Test Event YOU'VE GOT YOUR FIRST SIGNUP!"),
                Arguments.of(HALFWAY, "From: Test Event YOU'RE HALFWAY TO YOUR EVENT CAPACITY!"),
                Arguments.of(FULLCAPACITY, "From: Test Event YOUR EVENT IS NOW AT FULL CAPACITY! CONGRATS!!"),
                Arguments.of(EVENTSTART, "From: Test Event YOUR EVENT HAS BEGUN")
        );
    }

    /**
     * Tests via comparison to ensure that each milestone object is recognized as a
     * distinct milestone regardless of its attributes. E.g. check that two different types of
     * milestones for the same event are recognized as separate milestone entities.
     */
    @Test
    public void testCompareMilestones(){
        UUID eventID = UUID.randomUUID();
        String eventName = "Test Event";
        Event event = new Event(eventID);
        event.setEventName(eventName);
        ca.cmput301t05.placeholder.milestones.Milestone milestoneFirstAttendee = new ca.cmput301t05.placeholder.milestones.Milestone(FIRSTATTENDEE, event);
        ca.cmput301t05.placeholder.milestones.Milestone milestoneFirstSignUp = new ca.cmput301t05.placeholder.milestones.Milestone(FIRSTSIGNUP, event);
        ca.cmput301t05.placeholder.milestones.Milestone milestoneFirstAttendeeDiffEvent = new ca.cmput301t05.placeholder.milestones.Milestone(FIRSTATTENDEE, new Event()); // Same type of milestone type but for different event.
        ca.cmput301t05.placeholder.milestones.Milestone milestoneFirstSignUpDiffEvent = new ca.cmput301t05.placeholder.milestones.Milestone(FIRSTSIGNUP, new Event());
        assertNotNull(milestoneFirstAttendee);
        assertNotNull(milestoneFirstSignUp);
        assertNotNull(milestoneFirstAttendeeDiffEvent);
        assertNotNull(milestoneFirstSignUpDiffEvent);
        assertNotEquals(milestoneFirstAttendee, milestoneFirstSignUp);
        assertNotEquals(milestoneFirstAttendee, milestoneFirstAttendeeDiffEvent);
        assertNotEquals(milestoneFirstSignUpDiffEvent, milestoneFirstSignUp);
        assertEquals(milestoneFirstAttendeeDiffEvent.getmType(), milestoneFirstAttendee.getmType()); // Different milestones, but are same type of milestone i.e. milestone for event's first attendee
        assertEquals(milestoneFirstSignUp.getmType(), milestoneFirstSignUpDiffEvent.getmType());
        Event duplicateEvent =new Event(eventID);
        duplicateEvent.setEventName(eventName);
        ca.cmput301t05.placeholder.milestones.Milestone newMileStoneSameFields =new Milestone(FIRSTATTENDEE, duplicateEvent);
        assertNotSame(milestoneFirstAttendee, newMileStoneSameFields);

    }



}
