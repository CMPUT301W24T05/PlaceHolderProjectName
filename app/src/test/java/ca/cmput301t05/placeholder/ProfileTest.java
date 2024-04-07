package ca.cmput301t05.placeholder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.UUID;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;

public class ProfileTest {

        public Profile mockProfile(){
            return new Profile("Bob", UUID.randomUUID());
        }

        public Event mockEvent(){
            return new Event("Soccer Game", "Bring water bottle", 40);
        }
        @Test
        public void testJoinEvent() {
            // Tests joining and leaving event functionality in profile
            Profile userProfile = mockProfile();
            Event event  = mockEvent();
            userProfile.joinEvent(event);
            assertEquals(userProfile.getJoinedEvents().size(), 1); // Joined one event should should return one event
            userProfile.leaveEvent(event);
            assertEquals(userProfile.getJoinedEvents().size(), 0);
        }


}
