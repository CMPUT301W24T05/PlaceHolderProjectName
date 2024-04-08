package ca.cmput301t05.placeholder.database;

import androidx.test.core.app.ApplicationProvider;
import ca.cmput301t05.placeholder.database.tables.ProfileTable;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.profile.Profile;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * This class is used for testing the ProfileTable class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProfileTableTest {

    private ProfileTable profileTable;

    @Mock
    FirebaseFirestore firestore;

    @Mock
    DocumentSnapshot doc;

    @Mock
    DatabaseManager databaseManager;

    @Mock
    CollectionReference collectionReference;

    /**
     * Sets up the necessary configurations and mock objects for testing the ProfileTable class.
     * Initializes the Mockito annotations and bypasses the singleton nature of DatabaseManager.
     * Mocks the DatabaseManager to use the mocked FirebaseFirestore instance.
     * Creates an instance of ProfileTable for testing.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Use Mockito's static method to bypass the singleton nature of DatabaseManager
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());

        // Mock DatabaseManager to use the mocked FirebaseFirestore instance
        Mockito.when(databaseManager.getDb()).thenReturn(firestore);
        // Mock Firestore to use the mocked CollectionReference instance
        //Mockito.when(firestore.collection(ProfileTable.COLLECTION_NAME)).thenReturn(collectionReference);
        profileTable = new ProfileTable();
    }

    /**
     * Tests the successful retrieval of a profile from the database.
     * A mock DocumentReference is created to simulate the return of an existing profile.
     * Success is confirmed with the callback's onSuccess method where the returned profile's data is validated.
     */
    @Test
    public void fetchProfile_success() {
        DocumentReference documentReference = Mockito.mock(DocumentReference.class);
        Mockito.when(collectionReference.document(ArgumentMatchers.anyString()))
                .thenReturn(documentReference);
        Mockito.when(doc.exists()).thenReturn(true);

        // Mock document to return some expected profile data
        Mockito.when(doc.getString("name")).thenReturn("John Doe");
        Mockito.when(doc.getString("profileID")).thenReturn("testID");

        profileTable.fetchDocument("testID", new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                assertNotNull(profile);
                assertEquals("John Doe", profile.getName());
            }

            @Override
            public void onFailure(Exception e) {
                fail("Should not fail on success.");
            }
        });
    }

    /**
     * Tests the scenario where a profile is not found in the database.
     * A mock DocumentReference is created and its get method return is simulated with a document that doesn't exist.
     * The failure is confirmed if the onFailure method is invoked in the callback.
     */
    @Test
    public void fetchProfile_noProfileFound() {
        DocumentReference documentReference = Mockito.mock(DocumentReference.class);
        Mockito.when(collectionReference.document(ArgumentMatchers.anyString()))
            .thenReturn(documentReference);
        Mockito.when(documentReference.get()).thenReturn(Tasks.forResult(doc));
        Mockito.when(doc.exists()).thenReturn(false);

        profileTable.fetchDocument("testID", new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                fail("Should not succeed when profile not found.");
            }

            @Override
            public void onFailure(Exception e) {
                assertNotNull(e);
            }
        });
    }

    /**
     * Tests the failure scenario when fetching a profile from the database.
     * The get method of the document reference is mocked to throw an exception.
     * Failure is confirmed in the callback's onFailure method with a non-null exception.
     */
    @Test
    public void fetchProfile_failure() {
        DocumentReference documentReference = Mockito.mock(DocumentReference.class);
        Mockito.when(collectionReference.document(ArgumentMatchers.anyString()))
                .thenReturn(documentReference);
        Mockito.when(documentReference.get())
                .thenReturn(Tasks.forException(new Exception("Test Exception")));

        profileTable.fetchDocument("testID", new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                fail("Should not succeed when there is an exception.");
            }

            @Override
            public void onFailure(Exception e) {
                assertNotNull(e);
            }
        });
    }

    /**
     * Tests the successful addition of a profile to the database.
     * The addition is mocked to return a successful Task.
     * Success is confirmed if a non-null profile is received in the callback's onSuccess method.
     */
    @Test
    public void addProfile_success() {
        Profile testProfile = new Profile("Test User", UUID.randomUUID());

        // Simulate successful addition to Firestore
        Mockito.when(collectionReference.add(testProfile.toDocument())).thenReturn(Tasks.forResult(Mockito.mock(DocumentReference.class)));

        profileTable.pushDocument(testProfile, testProfile.getProfileID().toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                assertNotNull(profile);
            }

            @Override
            public void onFailure(Exception e) {
                fail("Expected profile to be added successfully, but it failed.");
            }
        });
    }

    /**
     * Tests the successful deletion of a profile in the database.
     * A mock is created for DocumentReference, and its delete method is mocked to return a successful Task.
     * The test successfully deletes the profile if the callback's onSuccess method is invoked.
     */
    @Test
    public void deleteProfile_success() {
        String testProfileId = "uniqueProfileId";

        // Assume the profile exists and simulate successful deletion
        DocumentReference documentReference = Mockito.mock(DocumentReference.class);
        Mockito.when(collectionReference.document(testProfileId)).thenReturn(documentReference);
        Mockito.when(documentReference.delete()).thenReturn(Tasks.forResult(null));

        profileTable.deleteDocument(testProfileId, new Table.DocumentCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                // This block being called already indicates success
            }

            @Override
            public void onFailure(Exception e) {
                fail("Expected profile to be deleted successfully, but it failed.");
            }
        });
    }

    /**
     * Tests the successful update of a profile in the database.
     * A mock profile and DocumentReference are created. The document's set method is mocked to return a successful Task.
     * The test then attempts to update the document and confirms success by checking that the result is non-null.
     */
    @Test
    public void updateProfile_success() {
        UUID testProfileUUID = UUID.randomUUID();
        String testProfileId = testProfileUUID.toString();
        Profile updatedProfile = new Profile("Updated Name", testProfileUUID);
        updatedProfile.setName("Updated Name");

        // Simulate successful update
        DocumentReference documentReference = Mockito.mock(DocumentReference.class);
        Mockito.when(collectionReference.document(testProfileId)).thenReturn(documentReference);
        Mockito.when(documentReference.set(updatedProfile.toDocument())).thenReturn(Tasks.forResult(null));

        profileTable.updateDocument(updatedProfile, testProfileId, new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile result) {
                assertNotNull(result);
            }

            @Override
            public void onFailure(Exception e) {
                fail("Expected profile to be updated successfully, but it failed.");
            }
        });
    }

    /**
     * This test is an integration test to verify a profile's lifecycle in the LIVE(!) database.
     * It executes the following steps:
     * <p>
     * Step 1: Create a new profile and add it to the database. The generated ID of the
     * newly added profile is captured.
     * <p>
     * Step 2: Update the profile's name and save the update to the database.
     * <p>
     * Step 3: Fetch the updated profile from the database and verify if the name was successfully updated.
     * <p>
     * Step 4: Delete the profile from the database.
     * <p>
     * Step 5: Attempt to fetch the deleted profile from the database. This should return null
     * since the profile has been deleted.
     *
     * @throws InterruptedException if any of the database operations are interrupted
     */
//    @Test
    public void profileLifecycle_integration() throws InterruptedException {
        // Step 1: Add a new profile
        Profile testProfile = new Profile("Integration Test User", UUID.randomUUID());

        // Add the profile to the database and capture the generated ID
        String generatedId = addProfileToDatabase(testProfile);

        // Step 2: Update the profile
        testProfile.setName("Updated Name");
        updateProfileInDatabase(generatedId, testProfile);

        // Step 3: Fetch and verify the update
        Profile fetchedProfile = fetchProfileFromDatabase(generatedId);
        assertEquals("Updated Name", fetchedProfile.getName());

        // Step 4: Delete the profile
        deleteProfileFromDatabase(generatedId);

        // Step 5: Try to fetch the deleted profile
        Profile deletedProfile = fetchProfileFromDatabase(generatedId);
        assertNull(deletedProfile);
    }

    /**
     * Adds a profile to the database and returns its document ID.
     *
     * @param profile The profile to add to the database.
     * @return The document ID of the added profile.
     * @throws InterruptedException If the thread is interrupted while waiting for the operation to complete.
     */
    private String addProfileToDatabase(Profile profile) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<String> documentIdRef = new AtomicReference<>();

        // Simulate adding a profile to the database and obtaining its document ID
        profileTable.pushDocument(profile, profile.getProfileID().toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                documentIdRef.set(profile.getProfileID().toString());
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
     * Updates a profile in the database with the given document ID.
     *
     * @param documentId The ID of the profile document to update.
     * @param profile The updated profile information.
     * @throws InterruptedException If the thread is interrupted while waiting for the operation to complete.
     */
    private void updateProfileInDatabase(String documentId, Profile profile) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        profileTable.updateDocument(profile, documentId, new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile result) {
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
     * Fetches a profile from the database based on the document ID.
     *
     * @param documentId The ID of the profile document.
     * @return The profile fetched from the database.
     * @throws InterruptedException If the thread is interrupted while waiting for the operation to complete.
     */
    private Profile fetchProfileFromDatabase(String documentId) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Profile> profileRef = new AtomicReference<>();

        profileTable.fetchDocument(documentId, new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                profileRef.set(profile);
                latch.countDown(); // Signal completion
            }

            @Override
            public void onFailure(Exception e) {
                latch.countDown(); // Signal completion even in case of failure
            }
        });

        latch.await(); // Wait for the operation to complete
        return profileRef.get();
    }

    /**
     * Deletes a profile from the database.
     *
     * @param documentId The ID of the profile document to delete.
     * @throws InterruptedException If the thread is interrupted while waiting for the operation to complete.
     */
    private void deleteProfileFromDatabase(String documentId) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        profileTable.deleteDocument(documentId, new Table.DocumentCallback<Void>() {
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