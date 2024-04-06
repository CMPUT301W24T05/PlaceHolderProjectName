package ca.cmput301t05.placeholder.ui.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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