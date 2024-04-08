package ca.cmput301t05.placeholder.utils;

import java.util.Comparator;

public class CompareByDate implements Comparator<HoldNotificationToEvent> {

    @Override
    public int compare(HoldNotificationToEvent o1, HoldNotificationToEvent o2) {
        return o2.getN().getTimeCreated().compareTo(o1.getN().getTimeCreated());
    }
}
