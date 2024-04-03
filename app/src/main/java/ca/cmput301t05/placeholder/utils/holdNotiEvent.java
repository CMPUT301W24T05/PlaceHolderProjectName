package ca.cmput301t05.placeholder.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

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

    /**
     * Used on start up so we can get cached notifications -> events so we can load them later instead of loading on fragment opening
     * @param ns
     * @param es
     * @return
     */
    public static ArrayList<holdNotiEvent> getQuickList(ArrayList<Notification> ns, ArrayList<Event> es){

        ArrayList<holdNotiEvent> quickList = new ArrayList<>();
        HashMap<String, Notification> quickCheck = new HashMap<>();

        for (Notification n : ns){
            quickCheck.put(n.getFromEventID().toString(), n);
        }

        for (Event e : es){

            if (quickCheck.get(e.getEventID().toString()) != null){

                quickList.add(new holdNotiEvent(quickCheck.get(e.getEventID().toString()),e));

            }

        }


        return quickList;
    }

    public static ArrayList<holdNotiEvent> hashQuickList(ArrayList<Notification> n, HashMap<UUID, Event> e){
        ArrayList<holdNotiEvent> combination = new ArrayList<>();

        for (Notification noti : n){

            if (e.get(noti.getFromEventID()) != null){

                combination.add(new holdNotiEvent(noti, e.get(noti.getFromEventID())));
            }
        }

        return combination;
    }
}
