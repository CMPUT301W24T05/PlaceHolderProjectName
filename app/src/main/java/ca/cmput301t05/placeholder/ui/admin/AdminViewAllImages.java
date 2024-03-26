package ca.cmput301t05.placeholder.ui.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import ca.cmput301t05.placeholder.R;

public class AdminViewAllImages extends Fragment {

    private ImageView backButton;
    private TextView numberImages;

    private RecyclerView imagesRecycler;



    public AdminViewAllImages(){
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



        backButton = view.findViewById(R.id.admin_view_image_back);
        numberImages = view.findViewById(R.id.admin_view_image_page_images_found);
        imagesRecycler = view.findViewById(R.id.admin_view_image_recycler);

        ViewAllImagesAdapter adapter = new ViewAllImagesAdapter(getContext());

        imagesRecycler.setAdapter(adapter);
        adapter.setImageRefresh(imagesRecycler);




    }


}
