package ca.cmput301t05.placeholder.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.databinding.FragmentHomeBinding;


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

        // "Create New Event" Button
//        Button btnCreateEvent = root.findViewById(R.id.btnCreateEvent);
//        btnCreateEvent.setOnClickListener(view ->
//                Navigation.findNavController(view).navigate(R.id.action_home_to_createEvent));

        // "Notifications" Button
        Button btnNotifications = root.findViewById(R.id.btnNotifications);
        btnNotifications.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.navigation_notifications));




        // "Profile" Button
//        Button btnProfile = root.findViewById(R.id.btnProfile);
//        btnProfile.setOnClickListener(view ->
//                Navigation.findNavController(view).navigate(R.id.action_home_to_profile));
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
