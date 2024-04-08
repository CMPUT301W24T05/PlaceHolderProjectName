package ca.cmput301t05.placeholder.ui.mainscreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;
import ca.cmput301t05.placeholder.ui.events.ViewEventDetailsFragment;
import ca.cmput301t05.placeholder.ui.notifications.UserNotificationFragment;

import ca.cmput301t05.placeholder.utils.CenterItemDecoration;
import ca.cmput301t05.placeholder.utils.CustomNestedScrollView;
import ca.cmput301t05.placeholder.utils.datafetchers.DataFetchCallback;
import ca.cmput301t05.placeholder.utils.datafetchers.EventFetcher;
import com.google.android.material.appbar.MaterialToolbar;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.Collectors;
/**
 * Fragment for displaying the home screen with lists of current and future events.
 * Also handles user interactions such as clicking on events.
 */
public class NewHomeFragment extends Fragment implements DataFetchCallback, EventAdapter.OnItemClickListener {
    private PlaceholderApp app;
    private RecyclerView futureEventsView;
    private RecyclerView currentEventsView;
    private EventAdapter futureEventsAdapter;
    private EventAdapter currentEventsAdapter;
    private TextView currentEventsHeader;
    private TextView futureEventsHeader;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CustomNestedScrollView nestedScrollView;
    private MaterialToolbar topBar;

    private EventFetcher eventFetcher;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        app = (PlaceholderApp) requireActivity().getApplication();
        eventFetcher = app.getEventFetcher();
        eventFetcher.addCallback(this);

        initializeViews(view);

        String title = "Welcome, " + app.getUserProfile().getName() + "!";
        topBar.setTitle(title);

        populateLists();

        return view;
    }
    /**
     * Populates the lists of current and future events.
     */
    private void populateLists() {
        ArrayList<Event> attendingEvents = new ArrayList<>(app.getJoinedEvents().values());
        app.getInterestedEvents().values().stream()
                .filter(event -> !attendingEvents.contains(event))
                .forEach(attendingEvents::add);

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);

        ArrayList<Event> futureEvents = attendingEvents.stream()
                .filter(event -> event.getEventDate().after(today))
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Event> todaysEvents = attendingEvents.stream()
                .filter(event -> {
                    Calendar date = event.getEventDate();
                    return date.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                            && date.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                            && date.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
                })
                .collect(Collectors.toCollection(ArrayList::new));

        currentEventsAdapter.setEvents(todaysEvents);
        futureEventsAdapter.setEvents(futureEvents);

        if (todaysEvents.isEmpty()) {
            currentEventsView.setVisibility(View.GONE);
            currentEventsHeader.setVisibility(View.GONE);
        } else {
            currentEventsView.setVisibility(View.VISIBLE);
            currentEventsHeader.setVisibility(View.VISIBLE);
            String currentHeader = "You have " + todaysEvents.size() + " events today";
            currentEventsHeader.setText(currentHeader);
        }
        if (futureEvents.isEmpty()) {
            futureEventsView.setVisibility(View.GONE);
            futureEventsHeader.setText("You have no upcoming events");
        } else {
            futureEventsView.setVisibility(View.VISIBLE);
            String futureHeader = "You have " + futureEvents.size() + " upcoming events";
            futureEventsHeader.setText(futureHeader);
        }


    }
    /**
     * Initializes the views and adapters.
     * @param view The root view of the fragment.
     */
    private void initializeViews(View view) {
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        futureEventsView = view.findViewById(R.id.futureEvents);
        currentEventsView = view.findViewById(R.id.currentEvents);
        topBar = view.findViewById(R.id.main_page_toolbar);
        currentEventsHeader = view.findViewById(R.id.currentEventsHeader);
        futureEventsHeader = view.findViewById(R.id.futureEventsHeader);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        futureEventsView.setNestedScrollingEnabled(false);
        currentEventsView.setNestedScrollingEnabled(false);

        futureEventsAdapter = new EventAdapter(getContext(), new ArrayList<>(), EventAdapter.adapterType.ATTENDING, true);
        currentEventsAdapter = new EventAdapter(getContext(), new ArrayList<>(), EventAdapter.adapterType.ATTENDING, true);
        futureEventsAdapter.setListener(this);
        currentEventsAdapter.setListener(this);

        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(requireActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        futureEventsView.setLayoutManager(horizontalLayoutManager1);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        currentEventsView.setLayoutManager(horizontalLayoutManager);
        futureEventsView.setAdapter(futureEventsAdapter);
        currentEventsView.setAdapter(currentEventsAdapter);
        futureEventsView.addItemDecoration(new CenterItemDecoration());
        currentEventsView.addItemDecoration(new CenterItemDecoration());

        nestedScrollView.setControlledRecyclerView(futureEventsView);

        topBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.home_notification_item) {
                //open user notification fragment
                UserNotificationFragment dialogFragment = new UserNotificationFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, dialogFragment)
                        .addToBackStack(null).commit();
            }
            return true;
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                eventFetcher.fetchAllEvents(app.getUserProfile());
            }
        });
    }
    /**
     * Handles item click events on the RecyclerView.
     * @param event The event object clicked.
     * @param type The type of adapter clicked.
     */
    @Override
    public void onItemClick(Event event, EventAdapter.adapterType type) {
        if (type == EventAdapter.adapterType.HOSTED) {
            app.setCachedEvent(event);
            Intent i = new Intent(getActivity(), EventMenuActivity.class);
            startActivity(i);
        } else if (type == EventAdapter.adapterType.ATTENDING) {
            app.setCachedEvent(event);
            ViewEventDetailsFragment eventDetailsFragment = new ViewEventDetailsFragment();
            eventDetailsFragment.show(requireActivity().getSupportFragmentManager(), eventDetailsFragment.getTag());
        }
    }

    @Override
    public void onProfileFetched(Profile profile) {

    }

    @Override
    public void onPictureLoaded(Bitmap bitmap) {

    }

    @Override
    public void onProfileFetchFailure(Exception exc) {

    }

    @Override
    public void onNoIdFound() {

    }

    @Override
    public void onEventFetched(Profile profile) {
        populateLists();
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onEventFetchError(Exception exception) {
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
