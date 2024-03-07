package ca.cmput301t05.placeholder.ui.events;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.R;

public class ViewEventDetailsActivity extends AppCompatActivity {

    private Button back;

    private ImageButton profile_button;

    private TextView event_date;

    private TextView event_location;

    private TextView event_details;

    private TextView event_author;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.event_vieweventdetails);


        back = findViewById(R.id.vieweventdetails_back);
        profile_button = findViewById(R.id.vieweventdetails_profile);

        event_date = findViewById(R.id.vieweventdetails_date);
        event_location = findViewById(R.id.vieweventdetails_location);

        event_details = findViewById(R.id.vieweventdetails_eventinfo);
        event_author = findViewById(R.id.vieweventdetails_eventAuthor);

        //assume we grab the event id from a bundle and use that to display
        //so just grab from the database

    }
}
