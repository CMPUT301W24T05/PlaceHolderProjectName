package ca.cmput301t05.placeholder.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.Query;

import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.DatabaseManager;
/**
 * Default constructor for the AdminHomeFragment class.
 */
public class AdminHomeFragment extends Fragment {

    public AdminHomeFragment(){super(R.layout.admin_mainpage_fragment);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.admin_mainpage_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView profileCount, eventCount;

        profileCount = getView().findViewById(R.id.admin_main_profiles_num);
        eventCount = getView().findViewById(R.id.admin_main_events_num);

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





    }
}
