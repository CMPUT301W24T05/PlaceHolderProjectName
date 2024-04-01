package ca.cmput301t05.placeholder.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ca.cmput301t05.placeholder.MainActivity;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;

import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.databinding.FragmentNotificationsBinding;
import ca.cmput301t05.placeholder.events.Event;

public class NotificationsFragment extends AppCompatActivity {

    private FragmentNotificationsBinding binding;

    private PlaceholderApp app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        //Button backButton = binding.backButton;


        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        app = (PlaceholderApp) getApplicationContext();


        app.getEventTable().fetchDocument("575a430e-4122-4e13-bc6a-ab73f1c96b5b", new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event document) {

                app.setCachedEvent(document);

                Intent i = new Intent(NotificationsFragment.this, UserNotificationActivity.class);
                startActivity(i);

            }

            @Override
            public void onFailure(Exception e) {

            }
        });







    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home_menu_item) {
            // Navigate to activity_main.xml
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.explore_menu_item) {
            // Handle item 2 selection
            return true;
        } else if (id == R.id.organized_menu_item) {
            // Handle item 3 selection
            return true;
        } else {
            return false;
        }
    }
}
