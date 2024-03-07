package ca.cmput301t05.placeholder.ui.events;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.R;

public class ViewEventDetailsActivity extends AppCompatActivity {

    private Button Back;

    private Button profileButton;

    private TextView event_date;

    private TextView event_Location;

    private TextView event_details;

    private TextView event_author;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //assume we grab the event id from a bundle and use that to display
        setContentView(R.layout.event_vieweventdetails);



    }
}
