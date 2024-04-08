package ca.cmput301t05.placeholder.database;

import androidx.test.core.app.ApplicationProvider;
import ca.cmput301t05.placeholder.database.tables.EventTable;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class EventTableTest {

    private EventTable eventTable;

    @Mock
    FirebaseFirestore firestore;

    @Mock
    DocumentSnapshot doc;

    @Mock
    DatabaseManager databaseManager;

    @Mock
    CollectionReference collectionReference;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Use Mockito's static method to bypass the singleton nature of DatabaseManager
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());

        // Mock DatabaseManager to use the mocked FirebaseFirestore instance
        Mockito.when(databaseManager.getDb()).thenReturn(firestore);
        eventTable = new EventTable();
    }

    /**
     * Tests the successful retrieval of an event from the database.
     * A mock DocumentReference is created to simulate the return of an existing profile.
     * Success is confirmed with the callback's onSuccess method where the returned profile's data is validated.
     */
    @Test
    @Order(1)
    public void fetchEvent_success() {
        DocumentReference documentReference = Mockito.mock(DocumentReference.class);
        Mockito.when(collectionReference.document(ArgumentMatchers.anyString()))
                .thenReturn(documentReference);
        Mockito.when(doc.exists()).thenReturn(true);

        UUID eventId = UUID.randomUUID();
        UUID eventCreatorId = UUID.randomUUID();

        // Mock document to return some expected profile data
        Mockito.when(doc.getString("eventName")).thenReturn("Test Event Name");
        Mockito.when(doc.getString("eventID")).thenReturn(eventId.toString());
        Mockito.when(doc.getString("eventCreator")).thenReturn(eventCreatorId.toString());

        eventTable.fetchDocument("testID", new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event result) {
                assertNotNull(result);
                assertEquals("Test Event Name", result.getEventName());
                assertEquals(eventId, result.getEventID());
                assertEquals(eventCreatorId, result.getEventCreator());
            }

            @Override
            public void onFailure(Exception e) {
                fail("Should not fail on success.");
            }
        });
    }

    /**
     * Tests the scenario where an event is not found in the database.
     * A mock DocumentReference is created and its get method return is simulated with a document that doesn't exist.
     * The failure is confirmed if the onFailure method is invoked in the callback.
     */
    @Test
    @Order(2)
    public void fetchEvent_noEventFound() {
        DocumentReference documentReference = Mockito.mock(DocumentReference.class);
        Mockito.when(collectionReference.document(ArgumentMatchers.anyString()))
                .thenReturn(documentReference);
        Mockito.when(documentReference.get()).thenReturn(Tasks.forResult(doc));
        Mockito.when(doc.exists()).thenReturn(false);

        eventTable.fetchDocument("testID", new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event result) {
                fail("Should not succeed when event not found.");
            }

            @Override
            public void onFailure(Exception e) {
                assertNotNull(e);
            }
        });
    }

    /**
     * Tests the failure scenario when fetching an event from the database.
     * The get method of the document reference is mocked to throw an exception.
     * Failure is confirmed in the callback's onFailure method with a non-null exception.
     */
    @Test
    @Order(3)
    public void fetchEvent_failure() {
        DocumentReference documentReference = Mockito.mock(DocumentReference.class);
        Mockito.when(collectionReference.document(ArgumentMatchers.anyString()))
                .thenReturn(documentReference);
        Mockito.when(documentReference.get())
                .thenReturn(Tasks.forException(new Exception("Test Exception")));

        eventTable.fetchDocument("testID", new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event result) {
                fail("Should not succeed when there is an exception.");
            }

            @Override
            public void onFailure(Exception e) {
                assertNotNull(e);
            }
        });
    }

    /**
     * Tests the successful addition of an event to the database.
     * The addition is mocked to return a successful Task.
     * Success is confirmed if a non-null profile is received in the callback's onSuccess method.
     */
    @Test
    @Order(4)
    public void addEvent_success() {
        Event testEvent = new Event(UUID.randomUUID());
        testEvent.setEventName("Test Event Name");
        testEvent.setEventCreator(UUID.randomUUID());

        // Simulate successful addition to Firestore
        Mockito.when(collectionReference.add(testEvent.toDocument())).thenReturn(Tasks.forResult(Mockito.mock(DocumentReference.class)));

        eventTable.pushDocument(testEvent, testEvent.getEventID().toString(), new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event result) {
                assertNotNull(result);
            }

            @Override
            public void onFailure(Exception e) {
                fail("Expected event to be added successfully, but it failed.");
            }
        });
    }

    /**
     * Tests the successful deletion of a profile in the database.
     * A mock is created for DocumentReference, and its delete method is mocked to return a successful Task.
     * The test successfully deletes the profile if the callback's onSuccess method is invoked.
     */
    @Test
    @Order(5)
    public void deleteProfile_success() {
        String testEventId = "uniqueEventId";

        // Assume the profile exists and simulate successful deletion
        DocumentReference documentReference = Mockito.mock(DocumentReference.class);
        Mockito.when(collectionReference.document(testEventId)).thenReturn(documentReference);
        Mockito.when(documentReference.delete()).thenReturn(Tasks.forResult(null));

        eventTable.deleteDocument(testEventId, new Table.DocumentCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                assertTrue(true);
            }

            @Override
            public void onFailure(Exception e) {
                fail("Expected event to be deleted successfully, but it failed.");
            }
        });
    }

    /**
     * Tests the successful update of an event in the database.
     * A mock profile and DocumentReference are created. The document's set method is mocked to return a successful Task.
     * The test then attempts to update the document and confirms success by checking that the result is non-null.
     */
    @Test
    @Order(6)
    public void testUpdateEvent_success() {
        UUID testProfileUUID = UUID.randomUUID();
        String testProfileId = testProfileUUID.toString();
        Event updatedEvent = new Event(testProfileUUID);
        updatedEvent.setEventName("Updated Name");
        updatedEvent.setEventCreator(UUID.randomUUID());

        // Simulate successful update
        DocumentReference documentReference = Mockito.mock(DocumentReference.class);
        Mockito.when(collectionReference.document(testProfileId)).thenReturn(documentReference);
        Mockito.when(documentReference.set(updatedEvent.toDocument())).thenReturn(Tasks.forResult(null));

        eventTable.updateDocument(updatedEvent, testProfileId, new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event result) {
                assertNotNull(result);
            }

            @Override
            public void onFailure(Exception e) {
                fail("Expected event to be updated successfully, but it failed.");
            }
        });
    }

    /**
     * This test is an integration test to verify an event's lifecycle in the LIVE(!) database.
     * It executes the following steps:
     * <p>
     * Step 1: Create a new event and add it to the database. The generated ID of the
     * newly added event is captured.
     * <p>
     * Step 2: Update the event's name and save the update to the database.
     * <p>
     * Step 3: Fetch the updated event from the database and verify if the name was successfully updated.
     * <p>
     * Step 4: Delete the event from the database.
     * <p>
     * Step 5: Attempt to fetch the deleted event from the database. This should return null
     * since the event has been deleted.
     *
     * @throws InterruptedException if any of the database operations are interrupted
     */
//    @Test
//    @Order(7)
    public void profileLifecycle_integration() throws InterruptedException {
        // Step 1: Add a new event
        Event testEvent = new Event(UUID.randomUUID());
        testEvent.setEventName("Integration Test User");
        testEvent.setEventCreator(UUID.randomUUID());

        // Add the event to the database and capture the generated ID
        String generatedId = addEventToDatabase(testEvent);

        // Step 2: Update the event
        testEvent.setEventName("Updated Name");
        updateEventInDatabase(generatedId, testEvent);

        // Step 3: Fetch and verify the update
        Event fetchedEvent = fetchEventFromDatabase(generatedId);
        assertEquals("Updated Name", fetchedEvent.getEventName());

        // Step 4: Delete the event
        deleteEventFromDatabase(generatedId);

        // Step 5: Try to fetch the deleted event
        Event deletedEvent = fetchEventFromDatabase(generatedId);
        assertNull(deletedEvent);
    }

    /**
     * Adds an event to the database and returns its document ID.
     *
     * @param event The event to add to the database.
     * @return The document ID of the added event.
     * @throws InterruptedException If the thread is interrupted while waiting for the operation to complete.
     */
    private String addEventToDatabase(Event event) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<String> documentIdRef = new AtomicReference<>();

        // Simulate adding an event to the database and obtaining its document ID
        eventTable.pushDocument(event, event.getEventID().toString(), new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event profile) {
                documentIdRef.set(profile.getEventID().toString());
                latch.countDown(); // Signal completion
            }

            @Override
            public void onFailure(Exception e) {
                latch.countDown(); // Signal completion even in case of failure
            }
        });

        latch.await(); // Wait for the operation to complete
        return documentIdRef.get();
    }

    /**
     * Updates an event in the database with the given document ID.
     *
     * @param documentId The ID of the event document to update.
     * @param event The updated event information.
     * @throws InterruptedException If the thread is interrupted while waiting for the operation to complete.
     */
    private void updateEventInDatabase(String documentId, Event event) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        eventTable.updateDocument(event, documentId, new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event result) {
                latch.countDown(); // Signal completion
            }

            @Override
            public void onFailure(Exception e) {
                latch.countDown(); // Signal completion even in case of failure
            }
        });

        latch.await(); // Wait for the operation to complete
    }

    /**
     * Fetches an event from the database based on the document ID.
     *
     * @param documentId The ID of the event document.
     * @return The event fetched from the database.
     * @throws InterruptedException If the thread is interrupted while waiting for the operation to complete.
     */
    private Event fetchEventFromDatabase(String documentId) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Event> eventResult = new AtomicReference<>();

        eventTable.fetchDocument(documentId, new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event event) {
                eventResult.set(event);
                latch.countDown(); // Signal completion
            }

            @Override
            public void onFailure(Exception e) {
                latch.countDown(); // Signal completion even in case of failure
            }
        });

        latch.await(); // Wait for the operation to complete
        return eventResult.get();
    }

    /**
     * Deletes an event from the database.
     *
     * @param documentId The ID of the event document to delete.
     * @throws InterruptedException If the thread is interrupted while waiting for the operation to complete.
     */
    private void deleteEventFromDatabase(String documentId) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        eventTable.deleteDocument(documentId, new Table.DocumentCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                latch.countDown(); // Signal completion
            }

            @Override
            public void onFailure(Exception e) {
                latch.countDown(); // Signal completion even in case of failure
            }
        });

        latch.await(); // Wait for the operation to complete
    }
}
