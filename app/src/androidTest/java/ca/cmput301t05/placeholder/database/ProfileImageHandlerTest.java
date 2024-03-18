package ca.cmput301t05.placeholder.database;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import android.net.Uri;
import android.widget.ImageView;

import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.profile.Profile;
import junit.framework.TestCase;
import static org.junit.Assert.*;

import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.UUID;

public class ProfileImageHandlerTest {

    private ProfileImageHandler profileImageHandler;
    private Uri mockUri;


    @Before
    public void setUp() {
        profileImageHandler = new ProfileImageHandler();
        mockUri = mock(Uri.class);
    }

    @Test
    public void testUploadProfilePictureNotNull() {
        // Will not upload an actual image to Firebase as mockUri passed is null
        // Not allowed by putFile()
        UUID profileID = UUID.randomUUID();
        Profile profile = new Profile("Jack", profileID);
        Mockito.when(mockUri.toString()).thenReturn("mock_uri_string"); // Stub the mockUri so that it returns null whenever toString() is called on it.
        assertNull(profile.getProfilePictureID());
        profileImageHandler.uploadProfilePicture(mockUri, profile);
        // Initially a user's profile picture ID is null as they don't have one.
        // Check that one was uploaded and set by having a Profile Picture ID after method called.
        assertNotNull(profile.getProfilePictureID());
    }


    @Test
    public void testUploadProfilePictureThrowsException() {
        // Exception should be thrown when an invalid Uri is passed
        UUID profileID = UUID.randomUUID();
        Profile profile = new Profile("Jack", profileID);
        assertNull(profile.getProfilePictureID());
        Mockito.when(mockUri.toString()).thenReturn("mock_uri_string"); // Invalid Uri, should throw an exception
        assertThrows(StorageException.class, () -> {
            profileImageHandler.uploadProfilePicture(mockUri, profile); // Should throw an exception
        });

    }

    @Test
    public void testRemoveProfilePic() {

        Profile profile = new Profile("John", UUID.randomUUID());
        profile.setProfilePictureID(UUID.randomUUID());
        profileImageHandler.removeProfilePic(profile);
        assertNull(profile.getProfilePictureID());


    }
}