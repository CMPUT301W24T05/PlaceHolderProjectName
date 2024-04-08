package ca.cmput301t05.placeholder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Test;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;

/**
 * Unit tests for the profile class.
 */
public class ProfileUnitTest {


    public Profile mockProfile(){
        return new Profile("Bob", UUID.randomUUID());
    }

    public Event mockEvent(){
        return new Event("Soccer Game", "Bring water bottle", 40);
    }

    /**
     * Tests the {@code joinEvent} and {@code leaveEvent} methods for Profile class.
     * Tests joining and leaving events.
     * @author Anthony
     */
    @Test
    public void testProfileJoinThenLeaveEvent() {
        Profile userProfile = new Profile("Bob", UUID.randomUUID());
        assertEquals(userProfile.getJoinedEvents().size(), 0);
        Event event1  = new Event("Test event", "Test event description", 56);
        Event event2  = new Event("Test event 2", "Test event description 2", 23);
        userProfile.joinEvent(event1);
        assertEquals(userProfile.getJoinedEvents().size(), 1); // Joined one event should should return one event
        userProfile.joinEvent(event2);
        assertEquals(userProfile.getJoinedEvents().size(), 2);
        userProfile.leaveEvent(event1);
        assertEquals(userProfile.getJoinedEvents().size(), 1);
        userProfile.leaveEvent(event2);
        assertEquals(userProfile.getJoinedEvents().size(), 0);
    }

    /**
     * Tests the constructor for the Profile class.
     * @author Anthony
     */
    @Test
    public void testProfileConstructor(){
        UUID testID = UUID.randomUUID();
        Profile profile = new Profile("Testy Name", testID);
        assertEquals(profile.getName(),"Testy Name");
        assertEquals(profile.getProfileID(), testID);
        // None of the following fields should be null when profile is initialized.
        assertNotNull(profile.getJoinedEvents());
        assertNotNull(profile.getHostedEvents());
        assertNotNull(profile.getInterestedEvents());
        assertNotNull(profile.getNotifications());
    }

    /**
     * Tests that a Profile object is correctly converted to a document
     * in the form of a Map &lt;String, Object&gt;
     * After conversion the object is stored in the database.
     * @author Anthony
     */
    @Test
        public void testProfileSerialization() {
        UUID testID = UUID.randomUUID();
        Profile profile = new Profile("Testy Name", testID);
        assertTrue(profile.toDocument() instanceof HashMap);
    }

    /**
     * Tests that a DocumentSnapshot object is correctly converted back to an instance of Profile.
     * Test checks that fields of the Profile object are correctly set when it does not have hosted events,
     * interested events, or joined events.
     * @author Anthony
     */
    @Test
    public void testProfileDeSerialization(){
        UUID testID = UUID.randomUUID();
        UUID profilePicID = UUID.randomUUID();
        DocumentSnapshot document = mock(DocumentSnapshot.class);
        when(document.getString("profileID")).thenReturn(String.valueOf(testID));
        when(document.getString("name")).thenReturn("John");
        when(document.getString("homePage")).thenReturn("http://testexample.com");
        when(document.getString("contactInfo")).thenReturn("john@example.com");
        when(document.getString("profilePictureID")).thenReturn(String.valueOf(profilePicID));
        when(document.get("hostedEvents")).thenReturn(null);
        when(document.get("joinedEvents")).thenReturn(null);
        when(document.get("interestedEvents")).thenReturn(null);
        when(document.get("notifications")).thenReturn(new ArrayList<>());
        when(document.get("messagingToken")).thenReturn("12345");
        when(document.getString("messagingToken")).thenReturn("12345");
        when(document.getBoolean("isAdmin")).thenReturn(false);
        Profile profile = new Profile();
        profile.fromDocument(document);
        assertEquals("John", profile.getName());
        assertEquals("http://testexample.com", profile.getHomePage());
        assertEquals("john@example.com", profile.getContactInfo());
        assertEquals(profilePicID, profile.getProfilePictureID());
        assertNotNull(profile.getHostedEvents());
        assertNotNull(profile.getJoinedEvents());
        assertNotNull(profile.getInterestedEvents());
        assertNotNull(profile.getNotifications());
        assertEquals("12345", profile.getMessagingToken());
        assertFalse(profile.isAdmin());
    }

    /**
     * Tests that a DocumentSnapshot object is correctly converted back to an instance of Profile.
     * Test checks that fields of the Profile object are correctly set when it does have a hosted event,
     * interested event, and a joined event.
     * @author Anthony
     */
    @Test
    public void testProfileDeSerializationWithEventFields(){
        UUID testID = UUID.randomUUID();
        UUID profilePicID = UUID.randomUUID();
        String hostedEventID = UUID.randomUUID().toString();
        String joinedEventID = UUID.randomUUID().toString();
        String interestedEventID = UUID.randomUUID().toString();
        DocumentSnapshot document = mock(DocumentSnapshot.class);
        when(document.getString("profileID")).thenReturn(String.valueOf(testID));
        when(document.getString("name")).thenReturn("John");
        when(document.getString("homePage")).thenReturn("http://testexample.com");
        when(document.getString("contactInfo")).thenReturn("john@example.com");
        when(document.getString("profilePictureID")).thenReturn(String.valueOf(profilePicID));
        when(document.get("hostedEvents")).thenReturn(Arrays.asList(hostedEventID));
        when(document.get("joinedEvents")).thenReturn(Arrays.asList(joinedEventID));
        when(document.get("interestedEvents")).thenReturn(Arrays.asList(interestedEventID));
        when(document.get("notifications")).thenReturn(new ArrayList<>());
        when(document.get("messagingToken")).thenReturn("12345");
        when(document.getString("messagingToken")).thenReturn("12345");
        when(document.getBoolean("isAdmin")).thenReturn(false);
        Profile profile = new Profile();
        profile.fromDocument(document);
        assertEquals("John", profile.getName());
        assertEquals("http://testexample.com", profile.getHomePage());
        assertEquals("john@example.com", profile.getContactInfo());
        assertEquals(profilePicID, profile.getProfilePictureID());
        assertEquals(profile.getHostedEvents().get(0),hostedEventID );
        assertEquals(profile.getInterestedEvents().get(0), interestedEventID);
        assertEquals(profile.getJoinedEvents().get(0), joinedEventID);
        assertNotNull(profile.getNotifications());
        assertEquals("12345", profile.getMessagingToken());
        assertFalse(profile.isAdmin());
    }


    /**
     * Tests that a DocumentSnapshot object is correctly converted back to an instance of Profile.
     * Test checks that fields of the Profile object are correctly set when it does have a hosted event,
     * and a joined event, but does not have any interested events.
     * @author Anthony
     */
    @Test
    public void testProfileDeSerializationWithAEventFieldMissing(){
        UUID testID = UUID.randomUUID();
        UUID profilePicID = UUID.randomUUID();
        String hostedEventID = UUID.randomUUID().toString();
        String joinedEventID = UUID.randomUUID().toString();
        DocumentSnapshot document = mock(DocumentSnapshot.class);
        when(document.getString("profileID")).thenReturn(String.valueOf(testID));
        when(document.getString("name")).thenReturn("John");
        when(document.getString("homePage")).thenReturn("http://testexample.com");
        when(document.getString("contactInfo")).thenReturn("john@example.com");
        when(document.getString("profilePictureID")).thenReturn(String.valueOf(profilePicID));
        when(document.get("hostedEvents")).thenReturn(Arrays.asList(hostedEventID));
        when(document.get("joinedEvents")).thenReturn(Arrays.asList(joinedEventID));
        when(document.get("interestedEvents")).thenReturn(null);
        when(document.get("notifications")).thenReturn(new ArrayList<>());
        when(document.get("messagingToken")).thenReturn("12345");
        when(document.getString("messagingToken")).thenReturn("12345");
        when(document.getBoolean("isAdmin")).thenReturn(false);
        Profile profile = new Profile();
        profile.fromDocument(document);
        assertEquals("John", profile.getName());
        assertEquals("http://testexample.com", profile.getHomePage());
        assertEquals("john@example.com", profile.getContactInfo());
        assertEquals(profilePicID, profile.getProfilePictureID());
        assertEquals(profile.getHostedEvents().get(0),hostedEventID );
        assertNotNull(profile.getInterestedEvents());
        assertEquals(profile.getJoinedEvents().get(0), joinedEventID);
        assertNotNull(profile.getNotifications());
        assertEquals("12345", profile.getMessagingToken());
        assertFalse(profile.isAdmin());
    }

    /**
     * Tests the {@code joinEvent} method for the profile class.
     * @author Tolu
     */
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

    /**
     * Tests the retrieving the profile's hosted, interested, and joined events.
     * @author Tolu
     */
    @Test
    public void testGetEvents(){
        Profile userProfile = mockProfile();
        Event event = mockEvent();
        userProfile.hostEvent(event);
        userProfile.addInterestedEvent(event);
        userProfile.joinEvent(event);
        List<String> eventsHost = userProfile.getHostedEvents();
        List<String> eventsInterest = userProfile.getInterestedEvents();
        List<String> eventsJoin = userProfile.getJoinedEvents();
        assertEquals(event.getEventID().toString(), eventsHost.get(0));
        assertEquals(event.getEventID().toString(), eventsInterest.get(0));
        assertEquals(event.getEventID().toString(), eventsJoin.get(0));
    }

    /**
     * Test the {@code toDocument} method for the profile class.
     * @author Tolu
     */
    @Test
    public void testToFromDocument(){
        Profile user = mockProfile();
        Event event = mockEvent();
        String pid = user.getProfileID().toString();
        String name = user.getName();
        String homePage = "Home";
        String contactInfo = "contact";
        UUID profilepic = UUID.randomUUID();
        String notification = "notif";
        boolean admin = true;
        String token = "token";
        user.hostEvent(event);
        user.addInterestedEvent(event);
        user.joinEvent(event);
        user.setHomePage(homePage);
        user.setContactInfo(contactInfo);
        user.setProfilePictureID(profilepic);
        user.addNotification(notification);
        user.setAdmin(admin);
        user.setMessagingToken(token);
        Map<String, Object> doc = user.toDocument();
        assertEquals(pid, (String) doc.get("profileID"));
        assertEquals(name, doc.get("name"));
        assertEquals(homePage, doc.get("homePage"));
        assertEquals(contactInfo, doc.get("contactInfo"));
        assertEquals(profilepic.toString(), (String) doc.get("profilePictureID"));
        assertEquals(user.getHostedEvents(), doc.get("hostedEvents"));
        assertEquals(user.getJoinedEvents(), doc.get("joinedEvents"));
        assertEquals(user.getInterestedEvents(), doc.get("interestedEvents"));
        assertEquals(user.getNotifications(), doc.get("notifications"));
        assertEquals(admin, doc.get("isAdmin"));
        assertEquals(token, doc.get("messagingToken"));
    }

    /**
     * Test the {@code fromDocument} method for the profile class.
     * @author Tolu
     */

    @Test
    public void testFromDocument() {
        Profile user = new Profile();
        Event event = mockEvent();
        UUID pid = UUID.fromString("bafdb5cf-ee0b-43c3-aff7-27bb2d2a676f");
        String name = "Bob";
        String homePage = "Home";
        String contactInfo = "contact";
        UUID profilepic = UUID.fromString("bafdb5cf-ee0b-43c3-aff7-27bb2d2a676f");
        String notification = "notif";
        boolean admin = true;
        String token = "token";
        String eventName = "event1";
        List<String> events = new ArrayList<>();
        events.add(eventName);
        ArrayList<String> notifications = new ArrayList<>();
        notifications.add(notification);
        DocumentSnapshot document = mock(DocumentSnapshot.class);
        when(document.getString("profileID")).thenReturn(pid.toString());
        when(document.getString("name")).thenReturn(name);
        when(document.getString("homePage")).thenReturn(homePage);
        when(document.getString("contactInfo")).thenReturn(contactInfo);
        when(document.getString("profilePictureID")).thenReturn(profilepic.toString());
        when(document.get("hostedEvents")).thenReturn(events);
        when(document.get("joinedEvents")).thenReturn(events);
        when(document.get("interestedEvents")).thenReturn(events);
        when(document.get("notifications")).thenReturn(notifications); // Assuming notifications is initialized with an empty list
        when(document.getBoolean("isAdmin")).thenReturn(admin);
        when(document.getString("messagingToken")).thenReturn(token);
        user.fromDocument(document);
        assertEquals(pid, user.getProfileID());
        assertEquals(name, user.getName());
        assertEquals(homePage, user.getHomePage());
        assertEquals(contactInfo, user.getContactInfo());
        assertEquals(profilepic, user.getProfilePictureID());
        assertEquals(events, user.getHostedEvents());
        assertEquals(events, user.getJoinedEvents());
        assertEquals(events, user.getInterestedEvents());
        assertEquals(notifications, user.getNotifications());
        assertTrue(user.isAdmin());

    }




}
