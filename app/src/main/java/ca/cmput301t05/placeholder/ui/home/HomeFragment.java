package ca.cmput301t05.placeholder.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import ca.cmput301t05.placeholder.ProfileEditActivity;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.databinding.FragmentHomeBinding;

//test
public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //For HomeViewModel
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // "Scan An Event" Button
//        Button btnJoinEvent = root.findViewById(R.id.btnJoinEvent);
//        btnJoinEvent.setOnClickListener(view ->
//                Navigation.findNavController(view).navigate(R.id.action_home_to_joinEvent));

        // List of Joined Events
        ListView listJoinedEvents = root.findViewById(R.id.listJoinedEvents);
        String[] events = {"Event 1", "Event 2", "Event 3"}; // hardcoded event names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, events);
        listJoinedEvents.setAdapter(adapter);

        // "Create New Event" Button
        Button btnCreateEvent = root.findViewById(R.id.btnCreateEvent);
        btnCreateEvent.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_home_to_enterEventDetailsActivity));

        // List of Created Events
        ListView listCreatedEvents = root.findViewById(R.id.listCreatedEvents);
        String[] createdEvents = {"Created Event 1", "Created Event 2", "Created Event 3"}; // hardcoded event names
        ArrayAdapter<String> createdAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, createdEvents);
        listCreatedEvents.setAdapter(createdAdapter);

        // "Notifications" Button
        Button btnNotifications = root.findViewById(R.id.btnNotifications);
        btnNotifications.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.navigation_notifications));

        // "Profile" Button
        Button btnProfile = root.findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
            startActivity(intent);
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
