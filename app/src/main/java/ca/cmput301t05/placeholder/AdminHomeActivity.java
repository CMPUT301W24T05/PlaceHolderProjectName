package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AdminHomeActivity extends AppCompatActivity {
    private ImageButton homeButton;
    private Button buttonBrowseEvents;
    private Button buttonBrowseProfiles;
    private Button buttonBrowseImages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_admin_mainpage);

        homeButton = findViewById(R.id.button_home);
        buttonBrowseEvents = findViewById(R.id.button_browseEvents);
        buttonBrowseImages = findViewById(R.id.button_browseImages);
        buttonBrowseProfiles = findViewById(R.id.button_browseProfiles);

        // When click on homeButton, go back to the
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //:
        buttonBrowseProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonBrowseEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonBrowseImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
