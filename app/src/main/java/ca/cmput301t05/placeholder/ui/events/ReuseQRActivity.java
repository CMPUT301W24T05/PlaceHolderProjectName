package ca.cmput301t05.placeholder.ui.events;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.PlaceholderApp;


public class ReuseQRActivity extends AppCompatActivity {

    private PlaceholderApp app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reuse_qr);

        // Initialize PlaceholderApp instance
        app = (PlaceholderApp) getApplicationContext();


        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the current activity and return to the previous one
            }
        });

        // Call fetchEventDetails method here with the event ID you want to fetch
        fetchEventDetails("your_event_id_here");
    }

    // Inside fetchEventDetails method
    private void fetchEventDetails(String eventId) {
        // Fetch event details from the database using eventId
        app.getEventTable().fetchDocument(eventId, new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event event) {
                // Now you have the event details
                // You can proceed with the logic to reuse QR code
                TextView eventNameTextView = findViewById(R.id.event_name_textView);
                eventNameTextView.setText(event.getEventName());
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure to fetch event details
                Toast.makeText(ReuseQRActivity.this, "Failed to fetch event details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
