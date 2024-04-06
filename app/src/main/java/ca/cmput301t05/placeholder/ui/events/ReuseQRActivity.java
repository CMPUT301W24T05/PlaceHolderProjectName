package ca.cmput301t05.placeholder.ui.events;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.R;

public class ReuseQRActivity extends AppCompatActivity {

    private TextView eventNameTextView;

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
    }
}
