package ca.cmput301t05.placeholder;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import ca.cmput301t05.placeholder.database.DeviceIDManager;
import ca.cmput301t05.placeholder.profile.Profile;

public class ProfileEditActivity extends AppCompatActivity{

    private DeviceIDManager idManager;
    private UUID deviceID;
    private Profile profile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_creation);

        idManager = new DeviceIDManager(this);
        deviceID = idManager.getDeviceID();
        //profile =

    }
}
