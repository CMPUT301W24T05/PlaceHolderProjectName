package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import ca.cmput301t05.placeholder.database.ProfileTable;
import ca.cmput301t05.placeholder.database.Table;
import ca.cmput301t05.placeholder.profile.Profile;

public class LoadingScreenActivity extends AppCompatActivity {

    PlaceholderApp app;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        app = (PlaceholderApp) getApplicationContext();

        fetchProfileAndContinue();
    }

    private void fetchProfileAndContinue() {
        if(!app.getIdManager().deviceHasIDStored()){
            Intent intent = new Intent(getApplicationContext(), InitialSetupActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        UUID deviceId = app.getIdManager().getDeviceID();
        app.getProfileTable().fetchDocument(deviceId.toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                // The profile exists in firebase! We can continue to the Main activity
                app.setUserProfile(profile);
                Log.i("Placeholder App", String.format("Profile with id %s and name %s has been loaded!", profile.getProfileID(), profile.getName()));
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                // The profile does not exist in firebase, let's ask for the name of the user
                Log.e("App/ProfileFetch", e.toString());
                Intent intent = new Intent(getApplicationContext(), InitialSetupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
