package ca.cmput301t05.placeholder.utils;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.Notification;

/**
 * Just a way to hold both notifications and events for a user, so we can easily go to event
 * from user notifications
 */
public class holdNotiEvent {

    private Event e;
    private Notification n;

    public holdNotiEvent(Notification n, Event e){
        this.e = e;
        this.n = n;
    }

    public Event getE() {
        return e;
    }

    public Notification getN() {
        return n;
    }
}
