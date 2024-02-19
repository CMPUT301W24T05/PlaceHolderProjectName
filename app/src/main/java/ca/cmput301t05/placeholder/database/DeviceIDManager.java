package ca.cmput301t05.placeholder.database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

public class DeviceIDManager {

    private static final String SHARED_PREFS_FILE_NAME = "PlaceholderAppPrefs";
    private static final String DEV_ID_KEY = "deviceID";
    private Context context;

    public DeviceIDManager(Context context){
        this.context = context;
    }

    public boolean deviceHasIDStored() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        String deviceIDString = sharedPrefs.getString(DEV_ID_KEY, null);
        return deviceIDString != null;
    }

    public void deleteDeviceID() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        sharedPrefs.edit().remove(DEV_ID_KEY).apply();
    }

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
