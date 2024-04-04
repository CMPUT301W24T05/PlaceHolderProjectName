package ca.cmput301t05.placeholder.ui.mainscreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.ProfileEditActivity;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.profile.ProfileImageGenerator;
import ca.cmput301t05.placeholder.ui.admin.AdminHomeActivity;

public class ProfileUpdatedFragment extends Fragment {

    private static final int COMPRESSION_QUALITY = 1024;
    private static final int IMAGE_MAX_HEIGHT = 1080;
    private static final int IMAGE_MAX_WIDTH = 1080;

    private PlaceholderApp app;
    private Profile profile;
    private TextView name;
    private TextView contact;
    private TextView homepage;
    private ImageView profilePic;
    private RecyclerView joinedEventsList;
    private EventAdapter joinedEventsAdapter;
    private FloatingActionButton edit;

    LinearLayoutManager linearLayoutManager;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_page_updated, container, false);
        initializeComponents(view);
        setUp();
        setupListeners();
        return view;
    }

    private void initializeComponents(View view) {
        app = (PlaceholderApp) getActivity().getApplicationContext();
        profile = app.getUserProfile();

        name = view.findViewById(R.id.edit_name);
        homepage = view.findViewById(R.id.edit_homepage);
        contact = view.findViewById(R.id.edit_contact);
        profilePic = view.findViewById(R.id.profile_pic);
        joinedEventsList = view.findViewById(R.id.recyclerView);
        edit = view.findViewById(R.id.edit_event_buttoon);
    }

    private void setupListeners() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Initializes the activity's views with the user's current profile information,
     * including their name, contact information, homepage, and profile picture.
     * The admin button visibility is set based on the user's admin status.
     */
    private void setUp() {
        // set up the profile picture
        profile = app.getUserProfile();

        if (profile.hasProfileBitmap()) {
            profilePic.setImageBitmap(profile.getProfilePictureBitmap());
        } else {
            app.getProfileImageHandler().getProfilePicture(profile, getContext(), new BaseImageHandler.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    getActivity().runOnUiThread(() -> profilePic.setImageBitmap(bitmap));
                    profile.setProfilePictureBitmap(bitmap);
                }

                @Override
                public void onError(Exception e) {
                    profile.setProfilePictureToDefault();
                    getActivity().runOnUiThread(() -> profilePic.setImageBitmap(profile.getProfilePictureBitmap()));
                }
            });
        }
        if (profile.getName() != null) {
            name.setText(profile.getName());
        }
        if (profile.getContactInfo() != null) {
            contact.setText(profile.getContactInfo());
        }
        if (profile.getHomePage() != null) {
            homepage.setText(profile.getHomePage());
        }
        ArrayList<Event> joinedEvents = new ArrayList<Event>(app.getJoinedEvents().values());
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        joinedEventsAdapter = new EventAdapter(getActivity().getApplicationContext(), joinedEvents, EventAdapter.adapterType.ATTENDING);
        joinedEventsList.setLayoutManager(linearLayoutManager);
        joinedEventsList.setAdapter(joinedEventsAdapter);

    }

    /**
     * Updates the user's profile with the information entered in the activity's views.
     * This includes updating the name, contact information, homepage, and profile picture.
     * Changes are saved to the application's database.
     */

}
