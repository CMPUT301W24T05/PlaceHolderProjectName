package ca.cmput301t05.placeholder.profile;

import java.util.List;
import java.util.UUID;

import ca.cmput301t05.placeholder.events.Event;

public class Profile {

    UUID profileID;
    String name, homePage, contactInfo;

    String profilePictureID; //this will point to the collection in firebase storage

    List<Event> hostedEvents;

    List<Event> joinedEvents;

    boolean isAdmin = false;






}
