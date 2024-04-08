package ca.cmput301t05.placeholder.database.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

/**
 * DeviceIDManagerTest is a unit test class for testing DeviceIDManager class.
 * It tests the deviceHasIDStored method which checks if device ID is stored in SharedPreferences.
 */
public class DeviceIDManagerTest {
    private DeviceIDManager deviceIDManager;

    @Mock
    private Context context;

    @Mock
    private SharedPreferences sharedPrefs;

    @Mock
    private SharedPreferences.Editor editor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(context.getSharedPreferences(DeviceIDManager.SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)).thenReturn(sharedPrefs);
        when(sharedPrefs.edit()).thenReturn(editor);
        when(editor.remove(DeviceIDManager.DEV_ID_KEY)).thenReturn(editor);
        doNothing().when(editor).apply();
        deviceIDManager = new DeviceIDManager(context);
    }

    /**
     * Test to verify that the device ID stored status is correctly identified as true when there is
     * an existing device ID stored.
     */
    @Test
    public void deviceHasIDStoredTrueTest() {
        when(context.getSharedPreferences(DeviceIDManager.SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)).thenReturn(sharedPrefs);
        when(sharedPrefs.getString(DeviceIDManager.DEV_ID_KEY, null)).thenReturn(UUID.randomUUID().toString());
        assertTrue(deviceIDManager.deviceHasIDStored());
    }
    
    /**
     * Test to verify that the device ID stored status is correctly identified as false when there is
     * no device ID stored.
     */
    @Test
    public void deviceHasIDStoredFalseTest() {
        when(context.getSharedPreferences(DeviceIDManager.SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)).thenReturn(sharedPrefs);
        when(sharedPrefs.getString(DeviceIDManager.DEV_ID_KEY, null)).thenReturn(null);
        assertFalse(deviceIDManager.deviceHasIDStored());
    }

    /**
     * Test for the deleteDeviceID method.
     * This test ensures that the device ID is deleted correctly.
     */
    @Test
    public void testDeleteDeviceIDCorrectly() {
        UUID initialUUID = deviceIDManager.getDeviceID();
        deviceIDManager.deleteDeviceID();
        String deviceIDString = sharedPrefs.getString(DeviceIDManager.DEV_ID_KEY, null);
        assertNull(deviceIDString);
    }

    /**
     * Test for the deleteDeviceID method.
     * This test ensures that the method does not delete other SharedPreferences keys.
     */
    @Test
    public void testDeleteDeviceIDDoesNotDeleteOtherKeys() {
        when(editor.putString("TestKey", "TestValue")).thenReturn(editor);
        doNothing().when(editor).apply();
        deviceIDManager.deleteDeviceID();
        when(sharedPrefs.getString("TestKey", null)).thenReturn("TestValue");
        assertNotNull(sharedPrefs.getString("TestKey", null));
    }

    /**
     * Test for the deleteDeviceID method.
     * This test will generate a new ID if one does not exist after deletion.
     */
    @Test
    public void testDeleteDeviceIDGeneratesNewID() {
        UUID initialUUID = deviceIDManager.getDeviceID();
        deviceIDManager.deleteDeviceID();
        UUID newUUID = deviceIDManager.getDeviceID();
        assertNotEquals(initialUUID, newUUID);
    }
}