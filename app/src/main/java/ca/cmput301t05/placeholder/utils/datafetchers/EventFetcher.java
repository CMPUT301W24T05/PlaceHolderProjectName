package ca.cmput301t05.placeholder.utils.datafetchers;

import android.content.Context;
import androidx.annotation.NonNull;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class EventFetcher extends AbstractFetcher {

    // Declaration of Enum
    public enum EventType {HOSTED_EVENTS, JOINED_EVENTS, INTERESTED_EVENTS}

    public EventFetcher(PlaceholderApp app, @NonNull Context context) {
        super(app, context);
    }

    /**
     * Fetch events and call back.
     */
    public void fetchAllEvents(Profile profile) {
        AtomicInteger eventCounter = new AtomicInteger();
        fetchEventsOfType(profile, profile.getHostedEvents(), EventType.HOSTED_EVENTS, eventCounter);
        fetchEventsOfType(profile, profile.getJoinedEvents(), EventType.JOINED_EVENTS, eventCounter);
        fetchEventsOfType(profile, profile.getInterestedEvents(), EventType.INTERESTED_EVENTS, eventCounter);
    }

    public void fetchEventType(Profile profile, EventType eventType){
        AtomicInteger eventCounter = new AtomicInteger();
        List<String> eventIds;
        switch (eventType) {
            case HOSTED_EVENTS:
                eventIds = profile.getHostedEvents();
                break;
            case JOINED_EVENTS:
                eventIds = profile.getJoinedEvents();
                break;
            case INTERESTED_EVENTS:
                eventIds = profile.getInterestedEvents();
                break;
            default:
                return;
        }
        fetchEventsOfType(profile, eventIds, eventType, eventCounter);
    }


    private void fetchEventsOfType(Profile profile, List<String> eventIds, EventType eventType, AtomicInteger eventCounter) {
        if (eventIds != null) {
            eventCounter.addAndGet(eventIds.size());
            fetchEventData(profile, eventIds, eventType, eventCounter);
        }
    }

    private void fetchEventData(Profile profile, List<String> eventIds, EventType eventType, AtomicInteger eventCounter) {
        if (eventIds == null) {
            return;
        }

        for (String id : eventIds) {
            app.getEventTable().fetchDocument(id.trim(), new Table.DocumentCallback<Event>() {
                @Override
                public void onSuccess(Event event) {
                    switch (eventType) {
                        case HOSTED_EVENTS:
                            app.getHostedEvents().put(UUID.fromString(id.trim()), event);
                            break;
                        case JOINED_EVENTS:
                            app.getJoinedEvents().put(UUID.fromString(id.trim()), event);
                            break;
                        case INTERESTED_EVENTS:
                            app.getInterestedEvents().put(UUID.fromString(id.trim()), event);
                            break;
                    }
                    eventCounter.decrementAndGet();
                    if (eventCounter.get() == 0) {
                        callbacks.forEach(c -> c.onEventFetched(profile));
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    callbacks.forEach(c -> c.onEventFetchError(e));
                }
            });
        }
    }
}
