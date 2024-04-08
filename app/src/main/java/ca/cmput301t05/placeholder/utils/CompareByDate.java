package ca.cmput301t05.placeholder.utils;

import java.util.Comparator;

import ca.cmput301t05.placeholder.notifications.Notification;

public class CompareByDate implements Comparator<Notification> {

    @Override
    public int compare(Notification o1, Notification o2) {
        return o2.getTimeCreated().compareTo(o1.getTimeCreated());
    }
}
