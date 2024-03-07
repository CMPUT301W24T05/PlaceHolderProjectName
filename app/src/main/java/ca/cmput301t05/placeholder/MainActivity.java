package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.UUID;

import ca.cmput301t05.placeholder.databinding.ActivityMainBinding;
import ca.cmput301t05.placeholder.database.ImageTable;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.ui.events.EnterEventDetailsActivity;
import ca.cmput301t05.placeholder.qrcode.QRcodeScanner;

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
        NavigationUI.setupWithNavController(binding.navView, navController);

        //test image view here
        ImageTable i = new ImageTable();


        //i.testImage("5e7acd28-10c6-45c9-aa91-23b050286fa7", findViewById(R.id.imageTest));


        Event e = new Event(UUID.fromString("201a67cf-1aee-4ae7-95e9-6808a1a6fb85"));

        e.getEventFromDatabase();

        //this has a picture
        //display it
        //i.getPosterPicture(e, findViewById(R.id.imageTest));


        //app.getImageTable().uploadResource(R.raw.yeet_yah);

        //Event test_event = new Event("Test", "Testing", 5);
        //test_event.sendEventToDatabase();

        //Intent intent = new Intent(this, EnterEventDetailsActivity.class);
        //startActivity(intent);
        //finish();
        Intent intent = new Intent(this, ProfileEditActivity.class);
        startActivity(intent);

//        ImageTable i = new ImageTable(this);
//        i.uploadResource(R.raw.yeet_yah);


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