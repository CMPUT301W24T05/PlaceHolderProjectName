package ca.cmput301t05.placeholder.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.qrcode.QRCode;
import ca.cmput301t05.placeholder.qrcode.QRCodeManager;
import ca.cmput301t05.placeholder.ui.codescanner.ReuseQRCodeScannerActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class ReuseQRActivity extends AppCompatActivity {

    private TextView eventNameTextView;
    private ActivityResultLauncher<Intent> qrCodeScannerLauncher;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reuse_qr);

        // Retrieve the event name from the intent extras
        String eventName = getIntent().getStringExtra("eventName");

        // Find the TextView for event name
        eventNameTextView = findViewById(R.id.event_name_text_view);

        // Set the event name to the TextView
        eventNameTextView.setText(eventName);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the current activity and return to the previous one
            }
        });

        // Initialize the launcher for the QR code scanner activity
        qrCodeScannerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    // Retrieve the scanned QR code from the result
                    String scannedQRCode = data.getStringExtra("scannedQRCode");

                    // Retrieve the event associated with the QR code
                    PlaceholderApp app = (PlaceholderApp) getApplicationContext();
                    final Event event = app.getCachedEvent();

                    // Set the scanned QR code as the check-in QR code of the event
                    event.setCheckInQR(scannedQRCode);

                    // Update the event in the database
                    app.getEventTable().pushDocument(event, event.getEventID().toString(), new Table.DocumentCallback<Event>() {
                        @Override
                        public void onSuccess(Event document) {
                            // Handle success, if needed
                        }

                        @Override
                        public void onFailure(Exception e) {
                            // Handle failure, if needed
                        }
                    });
                }
            }
        });

        Button reuseButton = findViewById(R.id.reuse_button);
        reuseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the QR code scanner activity to scan a new QR code
                Intent intent = new Intent(ReuseQRActivity.this, ReuseQRCodeScannerActivity.class);
                qrCodeScannerLauncher.launch(intent); // Use the launcher to start the activity
            }
        });
    }
}
