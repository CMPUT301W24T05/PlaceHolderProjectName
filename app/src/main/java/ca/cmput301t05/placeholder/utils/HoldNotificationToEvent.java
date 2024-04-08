package ca.cmput301t05.placeholder.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.Notification;

/**
 * Just a way to hold both notifications and events for a user, so we can easily go to event
 * from user notifications
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
     * Used on start up so we can get cached notifications -> events so we can load them later instead of loading on fragment opening
     * @param ns
     * @param es
     * @return
     */
    public static ArrayList<HoldNotificationToEvent> getQuickList(ArrayList<Notification> ns, ArrayList<Event> es){

        ArrayList<Notification> notifications = new ArrayList<>();
        notifications.addAll(ns);

        ArrayList<HoldNotificationToEvent> quickList = new ArrayList<>();
        HashMap<String, Notification> quickCheck = new HashMap<>();

        for (Notification n : notifications){
            quickCheck.put(n.getFromEventID().toString(), n);
        }

        for (Event e : es){

            if (quickCheck.get(e.getEventID().toString()) != null){

                quickList.add(new HoldNotificationToEvent(quickCheck.get(e.getEventID().toString()),e));
                //remove notification from  list incase notis come from same event
                quickCheck.remove(e.getEventID().toString());

            }

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
