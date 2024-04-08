package ca.cmput301t05.placeholder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import android.content.Context;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;

/**
 * Unit tests for EventAdapter class.
 * @author Anthony
 */
public class EventAdapterTest {

    /**
     * Provides test data for {@code testEventAdapterItemCount} method.
     * @return Stream of arguments for the test.
     */
    private static Stream<Arguments> eventArrayListProvider(){
        return Stream.of(
                Arguments.of(new ArrayList<>(Arrays.asList(new Event(), new Event(), new Event())), 3),
                Arguments.of(new ArrayList<>(Arrays.asList(new Event())), 1),
                Arguments.of(new ArrayList<>(), 0),
                Arguments.of(new ArrayList<>(Arrays.asList(new Event(), new Event(), new Event(),new Event(),new Event(),new Event(),new Event())), 7),
                Arguments.of(new ArrayList<>(Arrays.asList(new Event(), new Event())), 2),
                Arguments.of(new ArrayList<>(Arrays.asList(new Event(), new Event(),new Event(),new Event(),new Event())), 5)
        );
    }

    /**
     * Tests the {@code getItemCount} method for Event Adapter class.
     * @param events An array list of event objects.
     * @param expectedCount An integer; representing the expected count that should be returned by method call.
     */
    @ParameterizedTest
    @MethodSource("eventArrayListProvider")
    public void testEventAdapterItemCount(ArrayList<Event> events, int expectedCount){
        Context mockContext = mock(Context.class);
        EventAdapter eventAdapter = new EventAdapter(mockContext, events, EventAdapter.adapterType.HOSTED);
        Assertions.assertEquals(eventAdapter.getItemCount(), expectedCount);
    }


}