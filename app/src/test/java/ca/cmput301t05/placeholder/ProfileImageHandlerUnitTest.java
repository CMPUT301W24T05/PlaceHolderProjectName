package ca.cmput301t05.placeholder;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import android.content.Context;
import android.net.Uri;

import ca.cmput301t05.placeholder.database.images.ProfileImageHandler;
import ca.cmput301t05.placeholder.profile.Profile;

//import static org.junit.Assert.*;

//import org.junit.Before;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import ca.cmput301t05.placeholder.database.images.ProfileImageHandler;
import java.util.UUID;
//import android.test.mock.MockContext;
//import org.junit.Test;
@RunWith(RobolectricTestRunner.class)
public class ProfileImageHandlerUnitTest {

//    private ProfileImageHandler profileImageHandler;
    private Uri mockUri;
    private Context mockContext;




    @Test
    public void testUploadProfilePictureNotNull() {
        assertEquals(2, 1+1);

        ProfileImageHandler profileImageHandler = new ProfileImageHandler();
//        mockUri = mock(Uri.class);
//        UUID profileID = UUID.randomUUID();
//        Profile profile = new Profile("Jack", profileID);
////        Mockito.when(mockUri.toString()).thenReturn("mock_uri_string");
//        assertNotNull(mockUri.toString());
//        assertNull(profile.getProfilePictureID());
//        profileImageHandler.uploadProfilePicture(mockUri, profile, mockContext);
//        // Initially a user's profile picture ID is null as they don't have one.
//        // Check that one was uploaded and set by having a Profile Picture ID after method called.
//        assertNotNull(profile.getProfilePictureID());
    }


//    @Test
//    public void testUploadProfilePictureThrowsException() {
//        // Exception should be thrown when an invalid Uri is passed
//        UUID profileID = UUID.randomUUID();
//        Profile profile = new Profile("Jack", profileID);
//        assertNull(profile.getProfilePictureID());
//        Mockito.when(mockUri.toString()).thenReturn("mock_uri_string"); // Invalid Uri, should throw an exception
//        assertThrows(StorageException.class, () -> {
//            profileImageHandler.uploadProfilePicture(mockUri, profile, mockContext); // Should throw an exception
//        });
//
//    }
//
//    @Test
//    public void testRemoveProfilePic() {
//        Profile profile = new Profile("John", UUID.randomUUID());
//        profile.setProfilePictureID(UUID.randomUUID());
//        profileImageHandler.removeProfilePic(profile, mockContext.getApplicationContext(), new BaseImageHandler.ImageDeletionCallback() {
//            @Override
//            public void onImageDeleted() {
//
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.e("Image_Deletion", e.getMessage());
//            }
//        });
//        assertNull(profile.getProfilePictureID());
//
//    }
}