package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.UUID;

import ca.cmput301t05.placeholder.databinding.ActivityMainBinding;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.ui.events.EnterEventDetailsActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PlaceholderApp app;

    private ImageView picTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (PlaceholderApp) getApplicationContext();


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(binding.navView, navController); //This line is replaced by code below

        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                navController.navigate(R.id.navigation_home);
                return true;
            } else if (id == R.id.navigation_dashboard) {
                navController.navigate(R.id.navigation_dashboard);
                return true;
            } else if (id == R.id.navigation_notifications) {
                navController.navigate(R.id.navigation_notifications);
                return true;
            }
            return false;
        });

        //test image view here
//        ImageTable i = new ImageTable();


        //i.testImage("5e7acd28-10c6-45c9-aa91-23b050286fa7", findViewById(R.id.imageTest));


//        Event e = new Event(UUID.fromString("201a67cf-1aee-4ae7-95e9-6808a1a6fb85"));
//
//        e.getEventFromDatabase();

        //app.getImageTable().uploadResource(R.raw.yeet_yah);

        //Event test_event = new Event("Test", "Testing", 5);
        //test_event.sendEventToDatabase();

        //Intent intent = new Intent(this, EnterEventDetailsActivity.class);
        //startActivity(intent);
        //finish();

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
    }

}