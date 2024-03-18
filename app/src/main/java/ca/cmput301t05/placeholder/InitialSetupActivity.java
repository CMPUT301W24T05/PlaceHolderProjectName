package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.database.tables.Table;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.cmput301t05.placeholder.database.utils.DeviceIDManager;
import ca.cmput301t05.placeholder.profile.Profile;

import java.util.UUID;
/**
 * InitialSetupActivity is an activity for initial setup where users can enter their name as part of
 * the application's onboarding process. This activity is responsible for creating a user profile
 * with the provided name and associating it with the device's unique ID. The profile is then saved
 * to the application's database.
 */
public class InitialSetupActivity extends AppCompatActivity {
    private EditText nameEdit;
    private FloatingActionButton submitButton;
    private DeviceIDManager idManager;
    private PlaceholderApp app;


    /**
     * Called when the activity is starting. This method initializes the activity, retrieves the
     * application context, sets up the UI components, and configures the submit button's click listener.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied. Otherwise, it is null.
     */
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

    /**
     * Handles the click event of the submit button. This method retrieves the user's name from
     * the EditText, generates a unique profile using the device ID, and saves the profile to the database.
     * Upon successful creation and saving of the profile, it transitions the user to the MainActivity.
     *
     * @param view The view (button) that was clicked.
     */
    private void submitName(View view) {
        String name = nameEdit.getText().toString();
        if (!name.isEmpty()) {
            UUID deviceId = idManager.getDeviceID();
            Profile userProfile = new Profile(name, deviceId);
            app.getProfileTable().pushDocument(userProfile, userProfile.getProfileID().toString(), new Table.DocumentCallback<Profile>() {
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
