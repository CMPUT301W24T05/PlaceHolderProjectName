package ca.cmput301t05.placeholder.profile;

import android.graphics.Bitmap;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Test for profile image generation.
 */
public class ProfileImageGeneratorTest {

    @Test
    public void testCalculateHueFromName() throws Exception {
        Method method = ProfileImageGenerator.class.getDeclaredMethod("calculateHueFromName", String.class);
        method.setAccessible(true);

        assertEquals(8, method.invoke(null, "Alice"));
        assertEquals(280, method.invoke(null, "Bob"));
        assertEquals(16, method.invoke(null, "Charlie"));
        assertEquals(296, method.invoke(null, "Delta"));
        assertEquals(328, method.invoke(null, "Echo"));
    }

    @Test
    public void testDefaultProfileImage() throws Exception {
        //Arrange
        String name = "Max Ritchie";
        Method method = ProfileImageGenerator.class.getDeclaredMethod("defaultProfileImage", String.class);
        method.setAccessible(true);

        //Act
        Bitmap bitmap = (Bitmap) method.invoke(null, name);

        //Assert
        assertNotNull(bitmap);
        assertEquals(ProfileImageGenerator.PROFILE_IMAGE_SIZE, bitmap.getWidth());
        assertEquals(ProfileImageGenerator.PROFILE_IMAGE_SIZE, bitmap.getHeight());
    }

    @Test
    public void testProfileImageInitials() throws Exception {
        //Arrange
        String name = "John Doe";
        String expectedInitials = "JD";
        Method methodProfileImg = ProfileImageGenerator.class.getDeclaredMethod("defaultProfileImage", String.class);
        Method methodInitials = ProfileImageGenerator.class.getDeclaredMethod("extractInitials", String.class);
        methodProfileImg.setAccessible(true);
        methodInitials.setAccessible(true);

        //Act
        Bitmap bitmap = (Bitmap) methodProfileImg.invoke(null, name);
        String actualInitials = (String) methodInitials.invoke(null, name);

        //Assert
        assertEquals(expectedInitials, actualInitials);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultProfileImageWithNullName() {
        ProfileImageGenerator.defaultProfileImage(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultProfileImageWithEmptyName() {
        ProfileImageGenerator.defaultProfileImage("");
    }

    @Test
    public void testCalculateHueFromNameWithSpecialCharacters() throws Exception{
        Method calculateHueMethod = ProfileImageGenerator.class.getDeclaredMethod("calculateHueFromName", String.class);
        calculateHueMethod.setAccessible(true);
        Integer hue = (Integer) calculateHueMethod.invoke(null, "\"@$%^^&*\"");
        assertNotNull(hue);
        assertTrue("Hue should be within 0-360", hue >= 0 && hue <= 360);
    }

    @Test
    public void testCalculateHueFromNameWithLongName() throws Exception {
        // Testing with a very long name
        String longName = "ThisIsAReallyLongNameDesignedToTestTheBoundariesOfTheHueCalculationFunctionWithinTheProfileImageGeneratorClass";
        Method calculateHueMethod = ProfileImageGenerator.class.getDeclaredMethod("calculateHueFromName", String.class);
        calculateHueMethod.setAccessible(true);
        Integer hue = (Integer) calculateHueMethod.invoke(null, longName);
        assertNotNull(hue);
        assertTrue("Hue should be within 0-360", hue >= 0 && hue <= 360);
    }

    @Test
    public void testExtractInitialsMultipleSpaces() throws Exception{
        // Testing with a name that has multiple spaces
        String name = "John    Doe";
        String expectedInitials = "JD";
        Method methodInitials = ProfileImageGenerator.class.getDeclaredMethod("extractInitials", String.class);
        methodInitials.setAccessible(true);
        String actualInitials = (String) methodInitials.invoke(null, name);
        assertEquals(expectedInitials, actualInitials);
    }

    @Test
    public void testExtractInitialsSpecialCharacters() throws Exception {
        // Testing names with special characters
        String name = "John-Doe";
        String expectedInitials = "JD";
        Method methodInitials = ProfileImageGenerator.class.getDeclaredMethod("extractInitials", String.class);
        methodInitials.setAccessible(true);
        String actualInitials = (String) methodInitials.invoke(null, name);
        assertEquals(expectedInitials, actualInitials);
    }


}