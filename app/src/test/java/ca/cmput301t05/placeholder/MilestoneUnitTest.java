package ca.cmput301t05.placeholder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static ca.cmput301t05.placeholder.notifications.MilestoneType.EVENTSTART;
import static ca.cmput301t05.placeholder.notifications.MilestoneType.FIRSTATTENDEE;
import static ca.cmput301t05.placeholder.notifications.MilestoneType.FIRSTSIGNUP;
import static ca.cmput301t05.placeholder.notifications.MilestoneType.FULLCAPACITY;
import static ca.cmput301t05.placeholder.notifications.MilestoneType.HALFWAY;

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

import ca.cmput301t05.placeholder.notifications.Milestone;
import ca.cmput301t05.placeholder.notifications.MilestoneType;

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
        UUID creatorID = UUID.randomUUID();
        UUID eventID = UUID.randomUUID();
        MilestoneType milestoneType = MilestoneType.FIRSTATTENDEE;
        String eventName = "Test Event";
        Milestone milestone = new Milestone(creatorID, eventID, milestoneType, eventName);
        assertEquals(creatorID, milestone.getCreatorID());
        assertEquals(eventID, milestone.getFromEventID());
        assertEquals(milestoneType, milestone.getMType());
        assertNotNull(milestone.getTimeCreated());
        assertNotNull(milestone.getNotificationID());
        assertEquals("From " + eventName + ": YOU'VE GOT YOUR FIRST ATTENDEE!", milestone.getMessage());
    }

    /**
     * Tests that each milestone type correctly generates the appropriate message.
     */
    @ParameterizedTest
    @MethodSource("mileStoneConstructorDataProvider")
    public void testMsgGeneration4DiffMilestoneTypes(MilestoneType milestoneType, String expectedMessage) {
        UUID creatorID = UUID.randomUUID();
        UUID eventID = UUID.randomUUID();
        String eventName = "Test Event";
        Milestone milestone = new Milestone(creatorID, eventID, milestoneType, eventName);
        Assertions.assertNotNull(milestone);
        Assertions.assertEquals(creatorID, milestone.getCreatorID());
        Assertions.assertEquals(eventID, milestone.getFromEventID());
        Assertions.assertEquals(milestoneType, milestone.getMType());
        Assertions.assertEquals(expectedMessage, milestone.getMessage()); // Verify that message is generated correctly
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


}
