package ca.cmput301t05.placeholder.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.Query;

import ca.cmput301t05.placeholder.MainActivity;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.DatabaseManager;

/**
 * An activity for the admin home page, providing navigation to different administrative functions
 * like browsing events, profiles, and images.
 */
public class AdminHomeActivity extends AppCompatActivity {

    TextView profileCount, eventCount;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_mainpage);

        PlaceholderApp app = (PlaceholderApp) getApplicationContext();

        profileCount = findViewById(R.id.admin_main_profiles_num);
        eventCount = findViewById(R.id.admin_main_events_num);

        Query query = DatabaseManager.getInstance().getDb().collection("profiles");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {

                if (task.isSuccessful()) {
                    profileCount.setText(String.valueOf(task.getResult().getCount()));
                } else {

                    //do some error

                }
            }
        });

        query = DatabaseManager.getInstance().getDb().collection("events");
        countQuery = query.count();

        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {

                if (task.isSuccessful()) {
                    eventCount.setText(String.valueOf(task.getResult().getCount()));
                } else {

                }
            }
        });

        //add buttons and whatnot




//        getSupportFragmentManager().beginTransaction()
//                .setReorderingAllowed(true)
//                .add(R.id.admin_fragment_container, AdminViewAllImages.class, null)
//                .commit();


    }
}
