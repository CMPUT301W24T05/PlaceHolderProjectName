package ca.cmput301t05.placeholder.profile;

import android.graphics.Bitmap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class ProfileImageGeneratorTest {

    @Test
    public void testDefaultProfileImage() {
        //Arrange
        ProfileImageGenerator profileImageGenerator = new ProfileImageGenerator();
        String name = "Max Ritchie";

        //Act
        Bitmap bitmap = profileImageGenerator.defaultProfileImage(name);

        //Assert
        assertNotNull(bitmap);
        assertEquals(ProfileImageGenerator.PROFILE_IMAGE_SIZE, bitmap.getWidth());
        assertEquals(ProfileImageGenerator.PROFILE_IMAGE_SIZE, bitmap.getHeight());
    }

    @Test
    public void testProfileImageInitials() {
        //Arrange
        ProfileImageGenerator profileImageGenerator = new ProfileImageGenerator();
        String name = "John Doe";
        String expectedInitials = "JD";

        //Act
        Bitmap bitmap = profileImageGenerator.defaultProfileImage(name);
        String actualInitials = profileImageGenerator.extractInitials(name);

        //Assert
        assertEquals(expectedInitials, actualInitials);
    }
}