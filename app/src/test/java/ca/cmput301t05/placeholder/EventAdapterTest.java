package ca.cmput301t05.placeholder;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;

public class EventAdapterTest {

    private ArrayList<Event> mockArrayListSize2 (){
        ArrayList<Event> events = new ArrayList<Event>();
        events.add(new Event("Birthday Party", "Arrive 15 min early!", 15));
        events.add(new Event("Wedding", "Bring presents!", 200));
        return events;
    }


    @Test
    public void EventAdapterReturnsTwo(){
        // Simple test to see if ArrayAdapter returns correct number of events
        ArrayList<Event> events = mockArrayListSize2(); // Array list of size 2
        EventAdapter eventAdapter = new EventAdapter(null, events, EventAdapter.adapterType.HOSTED); // null context works in this scenario
        assertEquals(eventAdapter.getItemCount(), 2);
    }

    @Test
    public void EventAdapterReturnsZero(){
        // Simple test to see if ArrayAdapter returns zero when it contains no events
        ArrayList<Event> events = new ArrayList<>(); // Empty array list
        EventAdapter eventAdapter = new EventAdapter(null, events, EventAdapter.adapterType.HOSTED);
        assertEquals(eventAdapter.getItemCount(), 0);
    }


}