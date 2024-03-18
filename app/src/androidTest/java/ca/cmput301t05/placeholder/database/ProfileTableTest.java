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

import static org.junit.Assert.*;

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
}