package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import ca.cmput301t05.placeholder.database.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.qrcode.QRCode;
import ca.cmput301t05.placeholder.qrcode.QRCodeManager;
import ca.cmput301t05.placeholder.qrcode.QRcodeScanner;
import ca.cmput301t05.placeholder.ui.events.EnterEventDetailsActivity;
import ca.cmput301t05.placeholder.ui.notifications.NotificationsFragment;

/**
 * MainActivity serves as the primary entry point for the Placeholder application. It provides navigation to different
 * parts of the application, such as creating events, joining events via QR code scanning, editing user profiles,
 * and viewing notifications. This activity sets up the main user interface and initializes action listeners for
 * navigation buttons.
 */
public class MainActivity extends AppCompatActivity {

    private PlaceholderApp app;

    private Button guideToEvent;


    /**
     * Called when the activity is starting. Initializes the application context, sets the content view,
     * and configures button listeners for navigating to various features of the app.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied. Otherwise, it is null.
     */
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

        ImageView testQR = findViewById(R.id.qr_test_pic);
        QRCodeManager qrM = new QRCodeManager();



        app.getEventTable().fetchDocument("e906a09f-a80b-4e2a-a598-ab12aec2b468", new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event document) {

                QRCode qr = qrM.generateQRCode(document, "eventinfo");

                testQR.setImageBitmap(qr.getBitmap());

            }

            @Override
            public void onFailure(Exception e) {

            }
        });




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