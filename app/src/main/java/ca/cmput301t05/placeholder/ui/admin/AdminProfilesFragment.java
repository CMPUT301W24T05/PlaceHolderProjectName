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

public class AdminProfilesFragment extends Fragment {


    public AdminProfilesFragment(){
        super(R.layout.admin_view_image_page);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.admin_view_image_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView imagesRecycler = view.findViewById(R.id.admin_view_image_recycler);

        AdminProfilesAdapter adapter = new AdminProfilesAdapter(getContext());

        imagesRecycler.setAdapter(adapter);
        adapter.setProfileRefresh(imagesRecycler);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2); // 2 columns
        imagesRecycler.setLayoutManager(layoutManager);

    }


}

