package ca.cmput301t05.placeholder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;

public class ProfileTest {

    /*
    private UUID profileID;
    private String name;
    private String homePage;
    private String contactInfo;
    private Bitmap profilePictureBitmap;
    private UUID profilePictureID;
    private List<String> hostedEvents;
    private List<String> joinedEvents;
    private List<String> interestedEvents;
    private ArrayList<String> notifications;
    boolean isAdmin = false;
    private String messagingToken; //for notifications

     */

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

        System.out.println("Actual Messaging Token: " + user.getMessagingToken());
        System.out.println("Expected Messaging Token: " + document.getString("messagingToken"));
        System.out.println("Actual Name: " + user.getName());

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
        //assertEquals(token, user.getMessagingToken());
    }

}

