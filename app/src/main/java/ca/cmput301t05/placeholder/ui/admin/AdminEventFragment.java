package ca.cmput301t05.placeholder.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.ui.events.EventMenuActivity;
import ca.cmput301t05.placeholder.ui.events.ViewEventDetailsFragment;
/**
 * Fragment class for displaying a list of events in the admin panel.
 * This fragment is responsible for creating and managing the UI elements
 * to show a list of events in a RecyclerView.
 */
public class AdminEventFragment extends Fragment implements AdminEventAdapter.OnItemClickListener {
    /**
     * Constructor for the AdminEventFragment class.
     * Initializes the layout resource ID for the fragment.
     */
    public AdminEventFragment(){super(R.layout.admin_view_image_page);}
    private PlaceholderApp app;
    /**
     * Called to create the view hierarchy associated with the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.admin_view_image_page, container, false);
        app = (PlaceholderApp) getActivity().getApplicationContext();
        return view;
    }
    /**
     * Called immediately after onCreateView() has returned, but before any saved state has been restored in to the view.
     * @param view The View returned by onCreateView().
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView imagesRecycler = view.findViewById(R.id.admin_view_image_recycler);

        //AdminEventAdapter adapter = new AdminEventAdapter(getContext());
        AdminEventAdapter adapter = new AdminEventAdapter(getContext());
        adapter.setListener(this);


        imagesRecycler.setAdapter(adapter);
        adapter.setEventRefresh(imagesRecycler);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2); // 2 columns
        imagesRecycler.setLayoutManager(layoutManager);

    }
    /**
     * Callback method invoked when an event item in the RecyclerView is clicked.
     * This method launches a fragment to display details of the selected event.
     * @param event The event object associated with the clicked item.
     */
    public void onItemClick(Event event) {
            app.setCachedEvent(event);
            ViewEventDetailsFragment bottomSheet = new ViewEventDetailsFragment();
            Bundle bundle = new Bundle();
            bottomSheet.setArguments(bundle);
            bottomSheet.show(getChildFragmentManager(), getTag());
    }
}
