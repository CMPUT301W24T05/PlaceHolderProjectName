package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.cmput301t05.placeholder.database.DeviceIDManager;
import ca.cmput301t05.placeholder.database.ProfileTable;
import ca.cmput301t05.placeholder.profile.Profile;

import java.util.UUID;

public class InitialSetupActivity extends AppCompatActivity {
    private EditText nameEdit;
    private FloatingActionButton submitButton;
    private DeviceIDManager idManager;
    private PlaceholderApp app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_prompt);

        app = (PlaceholderApp) getApplicationContext();
        idManager = new DeviceIDManager(this);

        nameEdit = findViewById(R.id.intro_name_edit);
        submitButton = findViewById(R.id.intro_submit_button);

        submitButton.setOnClickListener(this::submitName);
    }

    private void submitName(View view) {
        String name = nameEdit.getText().toString();
        if (!name.isEmpty()) {
            UUID deviceId = idManager.getDeviceID();
            Profile userProfile = new Profile(name, deviceId);
            app.getProfileTable().pushProfile(userProfile, new ProfileTable.ProfileCallback() {
                @Override
                public void onSuccess(Profile profile) {
                    app.setUserProfile(userProfile);
                    // Transition to MainActivity
                    Intent intent = new Intent(InitialSetupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
