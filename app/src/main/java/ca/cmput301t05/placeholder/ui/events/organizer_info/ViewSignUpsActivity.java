package ca.cmput301t05.placeholder.ui.events.organizer_info;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.events.Event;

public class ViewSignUpsActivity extends AppCompatActivity {

    private PlaceholderApp app;
    private Event event;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (PlaceholderApp) getApplicationContext();

        event = app.getCachedEvent();
    }
}
