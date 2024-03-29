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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.ProfileEditActivity;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.profile.ProfileImageGenerator;
import ca.cmput301t05.placeholder.ui.admin.AdminHomeActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileFragment extends Fragment {
    private static final int COMPRESSION_QUALITY = 1024;
    private static final int IMAGE_MAX_HEIGHT = 1080;
    private static final int IMAGE_MAX_WIDTH = 1080;

    private PlaceholderApp app;
    private Uri profilePicUri;
    private Profile profile;
    private Button saveButton;
    private Button cancelButton;
    private Button adminButton;
    private EditText editName;
    private EditText editContact;
    private EditText editHomepage;
    private ImageView profilePic;
    private FloatingActionButton cameraButton;
    private FloatingActionButton removeProfilePicButton;
    private boolean removePic = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_creation, container, false);
        initializeComponents(view);
        setUp();
        setupListeners();
        return view;
    }

    private void initializeComponents(View view) {
        app = (PlaceholderApp) getActivity().getApplicationContext();
        profile = app.getUserProfile();
        saveButton = view.findViewById(R.id.save_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        adminButton = view.findViewById(R.id.admin_button);
        editName = view.findViewById(R.id.edit_name);
        editHomepage = view.findViewById(R.id.edit_homepage);
        editContact = view.findViewById(R.id.edit_contact);
        profilePic = view.findViewById(R.id.profile_pic);
        cameraButton = view.findViewById(R.id.button_camera);
        removeProfilePicButton = view.findViewById(R.id.button_remove_profile_pic);
    }

    private void setupListeners() {
        cancelButton.setOnClickListener(createOnCancelButtonClickListener());
        saveButton.setOnClickListener(createOnSaveButtonClickListener());
        adminButton.setOnClickListener(createOnAdminButtonClickListener());
        cameraButton.setOnClickListener(createOnCameraButtonClickListener());
        removeProfilePicButton.setOnClickListener(createOnRemoveProfilePicButtonClickListener());
    }

    private View.OnClickListener createOnCancelButtonClickListener() {
        return v -> {
        };
    }

    // Clicking on the save button will save the information and go back to the Main Activity
    private View.OnClickListener createOnSaveButtonClickListener() {
        return v -> {
            update();
        };
    }

    // Clicking on the admin button will direct you to the Administrator page
    private View.OnClickListener createOnAdminButtonClickListener() {
        return v -> {
            update();
            Intent intent = new Intent(getActivity(), AdminHomeActivity.class);
            startActivity(intent);
        };
    }

    // Clicking on the camera button will present the user with the option to take a picture or upload one from their library
    private View.OnClickListener createOnCameraButtonClickListener() {
        return v -> ImagePicker.with(getActivity())
                .crop()                                             // Crop the image
                .compress(COMPRESSION_QUALITY)                      // Final image size will be less than COMPRESSION_QUALITY KB
                .maxResultSize(IMAGE_MAX_WIDTH, IMAGE_MAX_HEIGHT)   // Final image resolution will be less than IMAGE_MAX_HEIGHT x IMAGE_MAX_WIDTH
                .start();
    }

    // Clicking on the remove button will remove the user's uploaded profile picture (if any) and replace it with the default generated picture
    private View.OnClickListener createOnRemoveProfilePicButtonClickListener() {
        return v -> {
            removePic = true;
            Bitmap defaultPic = ProfileImageGenerator.defaultProfileImage(profile.getName());
            profilePic.setImageBitmap(defaultPic);
            profilePicUri = null;
        };
    }

    /**
     * Handles the result from the image picker activity, updating the profile picture view
     * and storing the new picture's URI.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Activity Result", "IN FUNC");
        Uri uri = data.getData();
        //display the new picture and upload to the ImageTable
        profilePic.setImageURI(uri);
        profilePicUri = uri;
    }

    /**
     * Initializes the activity's views with the user's current profile information,
     * including their name, contact information, homepage, and profile picture.
     * The admin button visibility is set based on the user's admin status.
     */
    private void setUp() {
        // set up the profile picture
        profile = app.getUserProfile();

        if(profile.hasProfileBitmap()){
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
            editName.setText(profile.getName());
        }
        if (profile.getContactInfo() != null) {
            editContact.setText(profile.getContactInfo());
        }
        if (profile.getHomePage() != null) {
            editHomepage.setText(profile.getHomePage());
        }
        boolean admin = profile.isAdmin();
        if (!admin) {
            adminButton.setVisibility(View.GONE);
        }
    }

    /**
     * Updates the user's profile with the information entered in the activity's views.
     * This includes updating the name, contact information, homepage, and profile picture.
     * Changes are saved to the application's database.
     */
    private void update() {
        profile.setName(editName.getText().toString());
        profile.setContactInfo(editContact.getText().toString());
        profile.setHomePage(editHomepage.getText().toString());

        if (removePic) {
            app.getProfileImageHandler().removeProfilePic(profile, getContext());
            profile.setProfilePictureToDefault();
        }

        if (profilePicUri != null) {
            app.getProfileImageHandler().uploadProfilePicture(profilePicUri, profile, getContext());
            profile.setProfilePictureFromUri(profilePicUri, getContext());
        }

        app.getProfileTable().pushDocument(profile, profile.getProfileID().toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile profile) {

            }

            @Override
            public void onFailure(Exception e) {

            }

        });


    }
}
