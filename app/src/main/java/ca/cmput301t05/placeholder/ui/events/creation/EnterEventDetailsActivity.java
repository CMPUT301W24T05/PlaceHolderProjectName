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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;

import java.util.Calendar;
import java.util.Locale;

/**
 * EnterEventDetailsActivity is an activity class for entering the details of a new event.
 * It allows the user to input event name, location, date, time, description, and capacity.
 * This activity includes date and time pickers to facilitate the entry of date and time information.
 * After entering the details, the user can proceed to the next step which involves uploading an event poster.
 */
public class EnterEventDetailsActivity extends AppCompatActivity implements UploadPosterActivity.OnPosterImageSelectedListener {

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

    private Event newEvent;
    private Calendar cal;
    private Uri currentImage;

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

        initializeEventDetails();

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Handling the navigation icon click
        toolbar.setNavigationOnClickListener(v -> finish());

        eventTime.setOnClickListener(view -> openTimePickerDialog());
        eventDate.setOnClickListener(view -> openDatePickerDialog());
        openPosterDialog.setOnClickListener(view -> openPosterSelectSheet());

        nextButton = findViewById(R.id.eventDetailNextPage);

        newEvent = new Event();

        setupNextButtonClick();
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
        app = (PlaceholderApp) getApplicationContext();
    }

    private void openPosterSelectSheet() {
        UploadPosterActivity posterDialog = new UploadPosterActivity();

        if(currentImage != null){
            Bundle args = new Bundle();
            args.putString("imageUri", currentImage.toString());
            posterDialog.setArguments(args);
        }

        posterDialog.show(getSupportFragmentManager(), posterDialog.getTag());
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

            newEvent.setMaxAttendees(Integer.parseInt(eventCapacity.getText().toString()));
            newEvent.setEventDate(cal);
            newEvent.setEventName(eventName.getText().toString().trim());
            newEvent.setEventInfo(eventDescripiton.getText().toString().trim());
            newEvent.setEventCreator(app.getUserProfile().getProfileID());

            app.setCachedEvent(newEvent);
            Intent posterPick = new Intent(EnterEventDetailsActivity.this, UploadPosterActivity.class);
            startActivity(posterPick);

        });
    }

    /**
     * Validates that all event details have been entered and are valid.
     *
     * @return true if all details are valid, false otherwise.
     */
    private boolean hasValidEventDetails() {
        return validateEditTextNotEmpty(eventName) && validateEditTextNotEmpty(eventLocation) && validateEditTextNotEmpty(eventDate) && validateEditTextNotEmpty(eventTime) && validateEditTextNotEmpty(eventDescripiton) && validateEditTextNotEmpty(eventCapacity) && cal != null;
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

    /**
     * Adds the event to the database and transitions to the UploadPosterActivity on success.
     */
    private void addEventToDatabase() {
        app.getEventTable().pushDocument(newEvent, newEvent.getEventID().toString(), new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event document) {
                String eventID = newEvent.getEventID().toString();
                Log.d("Event_ID", eventID);
                Intent posterPick = new Intent(EnterEventDetailsActivity.this, UploadPosterActivity.class);
                posterPick.putExtra("created_event_ID", eventID);
                startActivity(posterPick);
            }

            @Override
            public void onFailure(Exception e) {
                // TODO Handle the failure of uploading the new event to the database
            }
        });
    }

    @Override
    public void onImageSelected(Uri imageUri) {
        newEvent.setEventPosterFromUri(imageUri, getApplicationContext());
        currentImage = imageUri;
        posterImage.setImageURI(currentImage);
        cropPosterToImage();
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