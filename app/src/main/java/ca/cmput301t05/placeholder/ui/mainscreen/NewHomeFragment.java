package ca.cmput301t05.placeholder.ui.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;
import ca.cmput301t05.placeholder.ui.events.ViewEventDetailsFragment;
import ca.cmput301t05.placeholder.ui.events.creation.EnterEventDetailsActivity;
import ca.cmput301t05.placeholder.ui.notifications.UserNotificationFragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class NewHomeFragment extends Fragment implements EventAdapter.OnItemClickListener {
    private PlaceholderApp app;
//    private RecyclerView signedUpEventsList;
    private RecyclerView attendingEventsList;
//    private EventAdapter signedUpEventsAdapter;
    private EventAdapter attendingEventsAdapter;
    private ExtendedFloatingActionButton createEventButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MaterialToolbar topBar;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        initializeViews(view);
        app = (PlaceholderApp) requireActivity().getApplication();
        ArrayList<Event> attendingEvents = new ArrayList<>(app.getJoinedEvents().values());
        attendingEventsAdapter.setEvents(attendingEvents);


        createEventButton.setOnClickListener(v -> {
            // Start EnterEventDetailsActivity
            Intent intent = new Intent(getActivity(), EnterEventDetailsActivity.class);
            startActivity(intent);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            ArrayList<Event> newAttendingEvents = new ArrayList<>(app.getJoinedEvents().values());
            attendingEventsAdapter.setEvents(newAttendingEvents);
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    private void initializeViews(View view) {
//        signedUpEventsList = view.findViewById(R.id.);
        attendingEventsList = view.findViewById(R.id.listJoinedEvents);
//        signedUpEventsAdapter = new EventAdapter(getContext(), new ArrayList<>(), EventAdapter.adapterType.HOSTED);
        attendingEventsAdapter = new EventAdapter(getContext(), new ArrayList<>(), EventAdapter.adapterType.ATTENDING);
//        signedUpEventsAdapter.setListener(this);
        attendingEventsAdapter.setListener(this);
//        signedUpEventsList.setLayoutManager(new LinearLayoutManager(getContext()));
        attendingEventsList.setLayoutManager(new LinearLayoutManager(getContext()));
//        signedUpEventsList.setAdapter(signedUpEventsAdapter);
        attendingEventsList.setAdapter(attendingEventsAdapter);

        topBar = view.findViewById(R.id.main_page_toolbar);
        topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.home_notification_item){

                    //open user notification fragment
                    UserNotificationFragment dialogFragment = new UserNotificationFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    // For a polished look, specify a transition animation.
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    // To make it fullscreen, use the 'content' root view as the container
                    // for the fragment, which is always the root view for the activity.
                    transaction.add(android.R.id.content, dialogFragment)
                            .addToBackStack(null).commit();


                }

                return true;
            }
        });


        createEventButton = view.findViewById(R.id.btnCreateEvent);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
    }
    @Override
    public void onItemClick(Event event, EventAdapter.adapterType type) {
        if (type == EventAdapter.adapterType.HOSTED) {
            app.setCachedEvent(event);
            Intent i = new Intent(getActivity(), EventMenuActivity.class);
            startActivity(i);
        } else if (type == EventAdapter.adapterType.ATTENDING) {
            app.setCachedEvent(event);
            ViewEventDetailsFragment eventDetailsFragment = new ViewEventDetailsFragment();
            eventDetailsFragment.show(getActivity().getSupportFragmentManager(), eventDetailsFragment.getTag());
        }
    }
}
