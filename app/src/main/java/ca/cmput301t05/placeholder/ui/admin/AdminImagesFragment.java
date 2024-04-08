package ca.cmput301t05.placeholder.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ca.cmput301t05.placeholder.R;
/**
 * A fragment for displaying all images in the admin view.
 */
public class AdminImagesFragment extends Fragment {

    public AdminImagesFragment(){
        super(R.layout.admin_view_image_page);
    }
    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.admin_view_image_page, container, false);
    }
    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned,
     * but before any saved state has been restored in to the view. It is always followed by onStart().
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView imagesRecycler = view.findViewById(R.id.admin_view_image_recycler);

        ViewAllImagesAdapter adapter = new ViewAllImagesAdapter(getContext());

        imagesRecycler.setAdapter(adapter);
        adapter.setImageRefresh(imagesRecycler);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2); // 2 columns
        imagesRecycler.setLayoutManager(layoutManager);


    }


}
