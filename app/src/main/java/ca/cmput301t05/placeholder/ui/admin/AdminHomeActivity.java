package ca.cmput301t05.placeholder.ui.admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.Query;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.DatabaseManager;

/**
 * An activity for the admin home page, providing navigation to different administrative functions
 * like browsing events, profiles, and images.
 */
public class AdminHomeActivity extends AppCompatActivity {


    BottomNavigationView bottomBar;
    ImageView backButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_mainpage);

        PlaceholderApp app = (PlaceholderApp) getApplicationContext();


        bottomBar = findViewById(R.id.admin_main_bottombar);
        backButton = findViewById(R.id.admin_mainpage_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.admin_fragment_container);
                if (menuItem.getItemId() == R.id.admin_menu_home){

                    if (!(fragment instanceof AdminHomeFragment)) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.admin_fragment_container, new AdminHomeFragment(), "HOME_FRAGMENT")
                                .commit();
                    }

                } else if (menuItem.getItemId() == R.id.admin_menu_images) {

                    if (!(fragment instanceof AdminImagesFragment)) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.admin_fragment_container, new AdminImagesFragment(), "IMAGES_FRAGMENT")
                                .commit();
                    }
                    
                } else if (menuItem.getItemId() == R.id.admin_menu_profiles) {

                    if (!(fragment instanceof AdminProfilesFragment)) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.admin_fragment_container, new AdminProfilesFragment(), "PROFILES_FRAGMENT")
                                .commit();
                    }
                    
                } else if (menuItem.getItemId() == R.id.admin_menu_events) {
                    
                }


                return true;
            }
        });



        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.admin_fragment_container, AdminHomeFragment.class, null)
                .commit();



    }
}
