package ca.cmput301t05.placeholder.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import ca.cmput301t05.placeholder.MainActivity;
import ca.cmput301t05.placeholder.R;

/**
 * An activity for the admin home page, providing navigation to different administrative functions
 * like browsing events, profiles, and images.
 */
public class AdminHomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_mainpage);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.admin_fragment_container, AdminViewAllImages.class, null)
                .commit();


    }
}
