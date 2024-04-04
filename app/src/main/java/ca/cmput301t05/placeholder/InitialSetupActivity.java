package ca.cmput301t05.placeholder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.profile.ProfileImageGenerator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

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

    private static final String CHANNEL_ID = "default_channel";
    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });


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

        askNotificationPermission();

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

            //get token then push it to servers
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {

                if (!task.isSuccessful()){
                    Log.d("Token_Generation", "Notification Generation was unsuccessful");
                    return;
                }

                // Get new FCM registration token
                String token = task.getResult();
                userProfile.setMessagingToken(token);

                app.getProfileTable().pushDocument(userProfile, userProfile.getProfileID().toString(), new Table.DocumentCallback<Profile>() {
                    @Override
                    public void onSuccess(Profile profile) {
                        app.setUserProfile(userProfile);

                        userProfile.setProfilePictureToDefault();

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

            });

        }
    }

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
