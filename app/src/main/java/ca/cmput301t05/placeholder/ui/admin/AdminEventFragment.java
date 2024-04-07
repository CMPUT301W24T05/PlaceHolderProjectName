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

public class AdminEventFragment extends Fragment implements AdminEventAdapter.OnItemClickListener {

    public AdminEventFragment(){super(R.layout.admin_view_image_page);}
    private PlaceholderApp app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.admin_view_image_page, container, false);
        app = (PlaceholderApp) getActivity().getApplicationContext();
        return view;
    }

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
    public void onItemClick(Event event) {
            app.setCachedEvent(event);
            ViewEventDetailsFragment bottomSheet = new ViewEventDetailsFragment();
            Bundle bundle = new Bundle();
            bottomSheet.setArguments(bundle);
            bottomSheet.show(getChildFragmentManager(), getTag());
    }
}
