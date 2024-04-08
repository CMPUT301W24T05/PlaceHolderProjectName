package ca.cmput301t05.placeholder.ui.mainscreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import ca.cmput301t05.placeholder.utils.datafetchers.DataFetchCallback;
import ca.cmput301t05.placeholder.utils.datafetchers.EventFetcher;
import ca.cmput301t05.placeholder.utils.datafetchers.ProfileFetcher;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.ProfileEditActivity;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;
import ca.cmput301t05.placeholder.ui.events.ViewEventDetailsFragment;
/**
 * Fragment for displaying the user's profile information and joined events.
 */
public class ProfileViewFragment extends Fragment implements DataFetchCallback, EventAdapter.OnItemClickListener {
    private PlaceholderApp app;
    private Profile profile;
    private TextView name;
    private TextView contact;
    private TextView homepage;
    private TextView middleSeparator;
    private ImageView profilePic;
    private RecyclerView joinedEventsList;
    private EventAdapter joinedEventsAdapter;
    private FloatingActionButton edit;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ProfileFetcher profileFetcher;
    private EventFetcher eventFetcher;
    /**
     * Creates the view for the fragment.
     * @param inflater The layout inflater object.
     * @param container The view container.
     * @param savedInstanceState The saved instance state bundle.
     * @return The inflated view for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_page_updated, container, false);
        initializeComponents(view);

        profileFetcher = app.getProfileFetcher();
        eventFetcher = app.getEventFetcher();

        profileFetcher.addCallback(this);
        eventFetcher.addCallback(this);

        setUp();
        setupListeners();
        swipeRefreshLayout.setOnRefreshListener(() ->
                eventFetcher.fetchEventType(profile, EventFetcher.EventType.JOINED_EVENTS));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setProfileUpdate();
    }

    @Override
    public void onDestroy() {
        profileFetcher.removeCallback(this);
        eventFetcher.removeCallback(this);
        super.onDestroy();
    }
    /**
     * Initializes the view components.
     * @param view The root view of the fragment.
     */
    private void initializeComponents(View view) {
        app = (PlaceholderApp) requireActivity().getApplicationContext();
        profile = app.getUserProfile();

        name = view.findViewById(R.id.edit_name);
        homepage = view.findViewById(R.id.edit_homepage);
        contact = view.findViewById(R.id.edit_contact);
        profilePic = view.findViewById(R.id.profile_pic);
        middleSeparator = view.findViewById(R.id.middleSeperator);
        joinedEventsList = view.findViewById(R.id.recyclerView);
        edit = view.findViewById(R.id.edit_event_buttoon);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        joinedEventsList.setLayoutManager(layoutManager);
        joinedEventsAdapter = new EventAdapter(getActivity(), new ArrayList<>(), EventAdapter.adapterType.ATTENDING, true);
        joinedEventsList.setAdapter(joinedEventsAdapter);
        joinedEventsAdapter.setListener(this);
    }

    private void setupListeners() {
        edit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
            startActivity(intent);
        });
    }

    private void setProfileUpdate() {
        boolean shouldHideMiddleSep = false;
        if (profile.hasProfileBitmap()) {
            profilePic.setImageBitmap(profile.getProfilePictureBitmap());
        } else {
            profileFetcher.fetchProfileImage(profile);
        }
        if (profile.getName() != null) {
            name.setText(profile.getName());
        }
        if (profile.getContactInfo() != null && !profile.getContactInfo().isEmpty()) {
            contact.setText(profile.getContactInfo());
        } else {
            contact.setText("");
            shouldHideMiddleSep = true;
        }
        if (profile.getHomePage() != null && !profile.getHomePage().isEmpty()) {
            homepage.setText(profile.getHomePage());
        } else {
            homepage.setText("");
            shouldHideMiddleSep = true;
        }
        if (shouldHideMiddleSep){
            middleSeparator.setVisibility(View.GONE);
        } else {
            middleSeparator.setVisibility(View.VISIBLE);
        }
    }

    private void updateEventsToUI(ArrayList<Event> joinedEvents) {
        joinedEventsAdapter.addOrUpdateEvents(joinedEvents);
    }

    private void setUp() {
        setProfileUpdate();
        updateEventsToUI(new ArrayList<>(app.getJoinedEvents().values()));
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
            eventDetailsFragment.show(requireActivity().getSupportFragmentManager(), eventDetailsFragment.getTag());
        }
    }

    @Override
    public void onProfileFetched(Profile profile) {
        // Not using this in the fragment
    }

    @Override
    public void onPictureLoaded(Bitmap bitmap) {
        requireActivity().runOnUiThread(() -> profilePic.setImageBitmap(bitmap));
        profile.setProfilePictureBitmap(bitmap);
    }

    @Override
    public void onProfileFetchFailure(Exception exc) {
        // Not using this in the fragment
    }

    @Override
    public void onNoIdFound() {
        // This is irrelevant to this fragment!
    }

    @Override
    public void onEventFetched(Profile profile) {
        setUp();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);  // indicate that the refreshing has finished
        }
    }

    @Override
    public void onEventFetchError(Exception exception) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);  // indicate that the refreshing has finished
        }
        // Log the exception
        Log.e("ProfileViewFragment", exception.getMessage(), exception);

        // Create and display a toast containing the error message
        Toast.makeText(getActivity(), "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
