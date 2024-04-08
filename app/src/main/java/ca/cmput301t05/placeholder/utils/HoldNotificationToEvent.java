package ca.cmput301t05.placeholder.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.Notification;

/**
 * Just a way to hold both notifications and events for a user, so we can easily go to event
 * from user notifications
 *
 * @deprecated
 * Didn't have enough time to get functionality to be working for this
 */
public class HoldNotificationToEvent {

    private Event e;
    private Notification n;

    public HoldNotificationToEvent(Notification n, Event e){
        this.e = e;
        this.n = n;
    }

    public Event getE() {
        return e;
    }

    public Notification getN() {
        return n;
    }

    /**
     * Turns notification and Event array to an object whihc allows us to sync the two
     * @param ns
     * @param es
     * @return
     */
    public static ArrayList<HoldNotificationToEvent> getQuickList(ArrayList<Notification> ns, ArrayList<Event> es){

        ArrayList<Notification> notifications = new ArrayList<>();
        notifications.addAll(ns);

        ArrayList<HoldNotificationToEvent> quickList = new ArrayList<>();

        //so the idea here is that we map the notifications to their respective events
        for (Notification n : notifications){


        }

        return quickList;
    }

    public static ArrayList<HoldNotificationToEvent> hashQuickList(ArrayList<Notification> n, HashMap<UUID, Event> e){
        ArrayList<HoldNotificationToEvent> combination = new ArrayList<>();

        ArrayList<Notification> notifications = new ArrayList<>();
        notifications.addAll(n);

        HashMap<UUID, Event> eventHashMap = new HashMap<>();
        eventHashMap.putAll(e);


        Iterator<Notification> iterator = notifications.iterator();
        while (iterator.hasNext()) {
            Notification noti = iterator.next();
            if (eventHashMap.get(noti.getFromEventID()) != null) {
                combination.add(new HoldNotificationToEvent(noti, e.get(noti.getFromEventID())));
                // Remove using iterator to avoid ConcurrentModificationException
                iterator.remove();
                eventHashMap.remove(noti.getFromEventID());
            }
        }


        return combination;
    }
}
