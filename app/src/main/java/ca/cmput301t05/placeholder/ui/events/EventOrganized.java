package ca.cmput301t05.placeholder.ui.events;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.MenuItem;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import com.google.android.material.bottomnavigation.BottomNavigationView;

        import ca.cmput301t05.placeholder.MainActivity;
        import ca.cmput301t05.placeholder.R;

public class EventOrganized extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_organized);

        // Initialize bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item1) {
            // Navigate to MainActivity
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.menu_item2) {
            // Navigate to EventExplore
            startActivity(new Intent(this, EventExplore.class));
            return true;
        } else if (id == R.id.menu_item3) {
            // Navigate to EventOrganized
            startActivity(new Intent(this, EventOrganized.class));
            return true;
        } else {
            return false;
        }
    }
}
