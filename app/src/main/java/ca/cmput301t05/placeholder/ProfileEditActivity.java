package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.UUID;

import ca.cmput301t05.placeholder.database.DeviceIDManager;
import ca.cmput301t05.placeholder.profile.Profile;

public class ProfileEditActivity extends AppCompatActivity{

    private PlaceholderApp app;
    private UUID deviceID;
    private Profile profile;
    private Button saveButton;
    private Button adminButton;
    private EditText editName;
    private EditText editContact;
    private EditText editHomepage;
    private ImageView profilePic;
    private FloatingActionButton cameraButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_creation);

        app = (PlaceholderApp) getApplicationContext();

        deviceID = app.getIdManager().getDeviceID();
        profile = app.getUserProfile();

        saveButton = findViewById(R.id.save_button);
        adminButton = findViewById(R.id.admin_button);
        editName = findViewById(R.id.edit_name);
        editHomepage = findViewById(R.id.edit_homepage);
        editContact = findViewById(R.id.edit_contact);
        profilePic = findViewById(R.id.profile_pic);
        cameraButton = findViewById(R.id.button_camera);

        // First display the information store in the database,
        // For example, getName will return "Name/Last Name" if no information get
        // for example, getContactInfo will return "Contact Info" if no information get
        setUp();
        // Click on the save button will save the information and go back to Mainactivity
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                Intent intent = new Intent(ProfileEditActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // Click on admin button will direct you to the admin tab
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                // will go to the admin page instead:
                //Intent intent = new Intent(ProfileEditActivity.this, MainActivity.class);
                //startActivity(intent);
                //finish();
            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ProfileEditActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        profilePic.setImageURI(uri);
    }

    private void setUp(){
        // Set image resource, converting byte code:
        // profilePic.setImageResource(R.drawable.your_image_resource);
        editName.setText(profile.getName());
        editContact.setText(profile.getContactInfo());
        editHomepage.setText(profile.getHomePage());
        boolean admin = profile.isAdmin();
        if (admin == false){adminButton.setVisibility(View.INVISIBLE);}
    }
    private void update(){
        profile.setName(editName.getText().toString());
        profile.setContactInfo(editContact.getText().toString());
        profile.setHomePage(editHomepage.getText().toString());
        // Set image resource, converting byte code:
        //profile.setProfilePictureID();

        // implement update dateBase function:
    }
}
