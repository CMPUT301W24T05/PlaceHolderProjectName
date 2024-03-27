package ca.cmput301t05.placeholder.ui.events.creation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.ui.events.GenerateInfoCheckinActivity;

import java.util.Calendar;
import java.util.Locale;

/**
 * EnterEventDetailsActivity is an activity class for entering the details of a new event.
 * It allows the user to input event name, location, date, time, description, and capacity.
 * This activity includes date and time pickers to facilitate the entry of date and time information.
 * After entering the details, the user can proceed to the next step which involves uploading an event poster.
 */
public class EnterEventDetailsActivity extends AppCompatActivity {

    private EditText eventName;
    private EditText eventLocation;
    private EditText eventDate;
    private EditText eventTime;
    private EditText eventCapacity;
    private EditText eventDescripiton;
    private ExtendedFloatingActionButton nextButton;
    private Button openPosterDialog;
    private ImageView posterImage;
    private PlaceholderApp app;

    private Event newEvent, curEvent;
    private Calendar cal;
    private Uri currentImage;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    private Intent fromEdit;

    /**
     * Called when the activity is starting. Initializes the UI components, sets up click listeners
     * for the date and time inputs, and prepares the next button for finalizing event details.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied. Otherwise, it is null.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_enterdetails);
        fromEdit = getIntent();

        initializeEventDetails();
        newEvent = new Event();

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Handling the navigation icon click
        toolbar.setNavigationOnClickListener(v -> backAction());

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri == null) {
                Log.d("PhotoPicker", "No media selected");
            } else {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                posterImage.setImageURI(uri);
                cropPosterToImage();
                currentImage = uri;
            }
        });

        eventTime.setOnClickListener(view -> openTimePickerDialog());
        eventDate.setOnClickListener(view -> openDatePickerDialog());
        openPosterDialog.setOnClickListener(view -> openPosterSelectSheet());

        setupNextButtonClick();
    }

    private void backAction(){
        Log.d("CreateEvent", "Back nav action activated!");
        finish();
    }

    /**
     * Initializes the UI components used for entering event details and retrieves the application context.
     */
    private void initializeEventDetails() {
        eventName = findViewById(R.id.enterEventName);
        eventLocation = findViewById(R.id.enterLocation);
        eventDate = findViewById(R.id.enterDate);
        eventTime = findViewById(R.id.enterTime);
        eventDescripiton = findViewById(R.id.enterEventDescription);
        eventCapacity = findViewById(R.id.enterEventCapacity);
        openPosterDialog = findViewById(R.id.open_poster_dialog);
        posterImage = findViewById(R.id.create_event_poster);
        nextButton = findViewById(R.id.eventDetailNextPage);

        app = (PlaceholderApp) getApplicationContext();


        if(fromEdit.hasExtra("edit")){
            curEvent = app.getCachedEvent();
            cal = curEvent.getEventDate();

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            eventName.setText(curEvent.getEventName());
            eventLocation.setText(curEvent.getEventLocation());
            eventDate.setText(String.format(Locale.getDefault(), "%02d-%02d-%04d", day, month, year));
            eventTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            eventCapacity.setText(String.valueOf(curEvent.getMaxAttendees()));
            eventDescripiton.setText(curEvent.getEventInfo());
            // TODO LOAD POSTER

        }
    }

    private void openPosterSelectSheet() {
        PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build();
        pickMedia.launch(request);
    }

    /**
     * Opens a dialog for picking the time of the event. Initializes the dialog with the current hour and minute.
     */
    private void openTimePickerDialog() {
        if (cal == null) return;

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(EnterEventDetailsActivity.this, (view, hourOfDay, minuteOfHour) -> eventTime.setText(String.format(Locale.CANADA, "%d:%d", hourOfDay, minuteOfHour)), hour, minute, false);

        timePickerDialog.show();
    }

    /**
     * Opens a dialog for picking the date of the event. Initializes the dialog with the current year, month, and day.
     */
    private void openDatePickerDialog() {
        cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                EnterEventDetailsActivity.this, (view1, year1, monthOfYear1, dayOfMonth1) -> eventDate.setText(String.format(Locale.CANADA, "%d-%d-%d", dayOfMonth1, monthOfYear1 + 1, year1)), year, month, day);

        datePickerDialog.show();
    }

    /**
     * Sets up the click listener for the "next" button. Validates the event details entered by the user,
     * updates the event object, and proceeds to add the event to the database.
     */
    private void setupNextButtonClick() {
        nextButton.setOnClickListener(view -> {
            if (!hasValidEventDetails()){
                return;
            }

            if(fromEdit.hasExtra("edit")){
                curEvent = app.getCachedEvent();
                curEvent.setMaxAttendees(Integer.parseInt(eventCapacity.getText().toString()));
                curEvent.setEventDate(cal);
                curEvent.setEventName(eventName.getText().toString().trim());
                curEvent.setEventInfo(eventDescripiton.getText().toString().trim());
                curEvent.setEventCreator(app.getUserProfile().getProfileID());
                curEvent.setEventLocation(eventLocation.getText().toString().trim());
                //TODO SET POSTER (IF CHOSEN)
            }else{
                newEvent.setMaxAttendees(Integer.parseInt(eventCapacity.getText().toString()));
                newEvent.setEventDate(cal);
                newEvent.setEventName(eventName.getText().toString().trim());
                newEvent.setEventInfo(eventDescripiton.getText().toString().trim());
                newEvent.setEventCreator(app.getUserProfile().getProfileID());
                newEvent.setEventPosterFromUri(currentImage, getApplicationContext());
                newEvent.setEventPosterFromUri(currentImage, getApplicationContext());
            }

            app.setCachedEvent(newEvent);
            Intent genQRActivity = new Intent(EnterEventDetailsActivity.this, GenerateInfoCheckinActivity.class);
            startActivity(genQRActivity);
        });
    }

    /**
     * Validates that all event details have been entered and are valid.
     *
     * @return true if all details are valid, false otherwise.
     */
    private boolean hasValidEventDetails() {
        return validateEditTextNotEmpty(eventName)
                && validateEditTextNotEmpty(eventLocation)
                && validateEditTextNotEmpty(eventDate)
                && validateEditTextNotEmpty(eventTime)
                && validateEditTextNotEmpty(eventDescripiton)
                && validateEditTextNotEmpty(eventCapacity)
                && cal != null
                && validateImageHasBeenChosen();
    }

    private boolean validateImageHasBeenChosen(){
        boolean imageChosen = currentImage != null;
        if(!imageChosen)
            Toast.makeText(getApplicationContext(), "Must choose an event poster!", Toast.LENGTH_SHORT).show();

        return imageChosen;
    }

    /**
     * Validates that a specific EditText field is not empty.
     *
     * @param editText The EditText field to validate.
     * @return true if the field is not empty, false otherwise.
     */
    private boolean validateEditTextNotEmpty(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError("Field cannot be empty");
            return false;
        }
        return true;
    }

    private void cropPosterToImage() {
        posterImage.post(() -> {
            // Get the Drawable's dimensions
            Drawable drawable = posterImage.getDrawable();
            int imageHeight = drawable.getIntrinsicHeight();
            int imageWidth = drawable.getIntrinsicWidth();

            // Calculate the aspect ratio
            float aspectRatio = (float) imageWidth / (float) imageHeight;

            // Assuming you have a fixed maximum height
            int imageViewHeight = posterImage.getHeight(); // or a specific value in pixels
            int imageViewWidth = Math.round(imageViewHeight * aspectRatio);

            // Set the ImageView's dimensions
            ViewGroup.LayoutParams params = posterImage.getLayoutParams();
            params.width = imageViewWidth;
            params.height = imageViewHeight; // You can keep this as is if it's already constrained
            posterImage.setLayoutParams(params);
        });
    }
}