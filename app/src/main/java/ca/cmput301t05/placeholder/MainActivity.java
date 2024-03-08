package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.qrcode.QRcodeScanner;
import ca.cmput301t05.placeholder.ui.events.EnterEventDetailsActivity;
import ca.cmput301t05.placeholder.ui.notifications.NotificationsFragment;

public class MainActivity extends AppCompatActivity {

    private PlaceholderApp app;

    private Button guideToEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (PlaceholderApp) getApplicationContext();
        setContentView(R.layout.activity_main);

        Button profileButton = findViewById(R.id.btnProfile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileEditActivity.class);
                startActivity(intent);
            }
        });

        Button buttonStartScanner = findViewById(R.id.btnJoinEvent);

        buttonStartScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start QRcodeScanner activity
                Intent intent = new Intent(MainActivity.this, QRcodeScanner.class);
                startActivity(intent);
            }
        });

        guideToEvent = findViewById(R.id.btnCreateEvent);

        guideToEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EnterEventDetailsActivity.class);
                startActivity(intent);
            }
        });

        Button notificationsButton = findViewById(R.id.btnNotifications);
        notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start NotificationsActivity
                Intent intent = new Intent(MainActivity.this, NotificationsFragment.class);
                startActivity(intent);
            }
        });




    }

}