package ca.cmput301t05.placeholder.database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

/**
 * The DeviceIDManager class is responsible for managing the unique device ID.
 */
public class DeviceIDManager {

    private static final String SHARED_PREFS_FILE_NAME = "PlaceholderAppPrefs";
    private static final String DEV_ID_KEY = "deviceID";
    private Context context;

    /**
     * The DeviceIDManager constructor.
     *
     * @param context The application context.
     */
    public DeviceIDManager(Context context){
        this.context = context;
    }

    /**
     * Checks if the device ID is stored in the SharedPreferences.
     *
     * @return true if the device ID is stored, false otherwise.
     */
    public boolean deviceHasIDStored() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        String deviceIDString = sharedPrefs.getString(DEV_ID_KEY, null);
        return deviceIDString != null;
    }

    /**
     * Deletes the device ID from the SharedPreferences.
     */
    public void deleteDeviceID() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        sharedPrefs.edit().remove(DEV_ID_KEY).apply();
    }

    /**
     * Retrieves the unique device ID from the SharedPreferences.
     * Puts a device ID into the SharedPreferences if one doesn't exist already.
     *
     * @return The unique device ID as a UUID object.
     */
    public UUID getDeviceID() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);

        // Try to get the device ID from the preferences file
        String deviceIDString = sharedPrefs.getString(DEV_ID_KEY, null);
        // Check if the device ID exists
        if(deviceIDString == null){
            // Generate a new ID since there is no pre-existing one
            UUID newID = UUID.randomUUID();
            deviceIDString = newID.toString();

            // Store the new ID in the preferences file
            SharedPreferences.Editor prefsEditor = sharedPrefs.edit();
            prefsEditor.putString(DEV_ID_KEY, deviceIDString);
            prefsEditor.apply();
        }

        // Return the device ID
        return UUID.fromString(deviceIDString);
    }
}
