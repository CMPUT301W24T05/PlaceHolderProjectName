package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ca.cmput301t05.placeholder.database.DeviceIDManager;
import ca.cmput301t05.placeholder.database.ProfileTable;
import ca.cmput301t05.placeholder.databinding.ActivityMainBinding;
import ca.cmput301t05.placeholder.database.ImageTable;
import ca.cmput301t05.placeholder.events.EnterEventDetailsActivity;

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
        ImageTable i = new ImageTable(app);

        i.testImage("5e7acd28-10c6-45c9-aa91-23b050286fa7", findViewById(R.id.imageTest));



        app.getImageTable().uploadResource(R.raw.yeet_yah);

        Intent intent = new Intent(this, EnterEventDetailsActivity.class);
        startActivity(intent);
        finish();
    }

}