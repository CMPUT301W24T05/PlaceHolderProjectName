package ca.cmput301t05.placeholder.utils;

import java.util.Comparator;

public class CompareByDate implements Comparator<holdNotiEvent> {

    @Override
    public int compare(holdNotiEvent o1, holdNotiEvent o2) {
        return o2.getN().getTimeCreated().compareTo(o1.getN().getTimeCreated());
    }
}
