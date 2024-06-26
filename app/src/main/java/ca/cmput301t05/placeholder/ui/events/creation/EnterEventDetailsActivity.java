package ca.cmput301t05.placeholder.ui.events.creation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.EventTable;
import ca.cmput301t05.placeholder.database.tables.ProfileTable;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.qrcode.QRCode;
import ca.cmput301t05.placeholder.qrcode.QRCodeManager;
import ca.cmput301t05.placeholder.ui.events.ViewQRCodesActivity;
import ca.cmput301t05.placeholder.utils.ImageViewHelper;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * EnterEventDetailsActivity is an activity class for entering the details of a new event.
 * It allows the user to input event name, location, date, time, description, and capacity.
 * This activity includes date and time pickers to facilitate the entry of date and time information.
 * After entering the details, the user can proceed to the next step which involves uploading an event poster.
 */
public class EnterEventDetailsActivity extends AppCompatActivity {

    private static int COMPRESSION_QUALITY = 1024;
    private static int MAX_WIDTH = 2048;
    private static int MAX_HEIGHT = 2048;

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

    // Declare instance variables to store the date and time from DatePicker and TimePicker
    private int selectedHour;
    private int selectedMinute;
    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;

    private boolean isEditing;

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
        loadCachedEvent();

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Handling the navigation icon click
        toolbar.setNavigationOnClickListener(v -> backAction());

        eventTime.setOnClickListener(view -> openTimePickerDialog());
        eventDate.setOnClickListener(view -> openDatePickerDialog());
        openPosterDialog.setOnClickListener(view -> openPosterSelectSheet());

        setupNextButtonClick();
    }

    private void loadCachedEvent() {
        Intent fromEdit = getIntent();
        isEditing = fromEdit.hasExtra("edit");
        if (isEditing) {
            newEvent = app.getCachedEvent();
            cal = newEvent.getEventDate();

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            eventName.setText(newEvent.getEventName());
            eventLocation.setText(newEvent.getEventLocation());
            eventDate.setText(String.format(Locale.getDefault(), "%02d-%02d-%04d", day, month, year));
            eventTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            eventCapacity.setText(String.valueOf(newEvent.getMaxAttendees()));
            eventDescripiton.setText(newEvent.getEventInfo());
            if (newEvent.hasEventPosterBitmap()) {
                posterImage.setImageBitmap(newEvent.getEventPosterBitmap());
                ImageViewHelper.cropImageToAspectRatio(posterImage);
            }
            nextButton.setText("Edit Event");
            nextButton.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.check_icon, getTheme()));
        } else {
            newEvent = new Event();
            nextButton.setText("Create Event");
            nextButton.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.add_icon, getTheme()));
        }
    }

    private void backAction() {
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
    }

    private void openPosterSelectSheet() {
        ImagePicker.with(this)
                .crop()                                             // Crop the image
                .compress(COMPRESSION_QUALITY)                      // Final image size will be less than COMPRESSION_QUALITY KB
                .maxResultSize(MAX_WIDTH, MAX_HEIGHT)   // Final image resolution will be less than IMAGE_MAX_HEIGHT x IMAGE_MAX_WIDTH
                .start();
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
        if (uri == null) {
            Log.d("PhotoPicker", "No media selected");
        } else {
            Log.d("PhotoPicker", "Selected URI: " + uri);
            posterImage.setImageURI(uri);
            ImageViewHelper.cropImageToAspectRatio(posterImage);
            currentImage = uri;
        }
    }

    /**
     * Opens a dialog for picking the time of the event. Initializes the dialog with the current hour and minute.
     */
    private void openTimePickerDialog() {
        if (cal == null) return;

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(EnterEventDetailsActivity.this, (view, hourOfDay, minuteOfHour) -> {
            selectedHour = hourOfDay;
            selectedMinute = minuteOfHour;
            eventTime.setText(String.format(Locale.CANADA, "%d:%d", hourOfDay, minuteOfHour));
        }, hour, minute, false);

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
                EnterEventDetailsActivity.this, (view1, year1, monthOfYear1, dayOfMonth1) -> {
            selectedDay = dayOfMonth1;
            selectedMonth = monthOfYear1;
            selectedYear = year1;
            eventDate.setText(String.format(Locale.CANADA, "%d-%d-%d", dayOfMonth1, monthOfYear1 + 1, year1));
        }, year, month, day);

        datePickerDialog.show();
    }

    /**
     * Sets up the click listener for the "next" button. Validates the event details entered by the user,
     * updates the event object, and proceeds to add the event to the database.
     */
    private void setupNextButtonClick() {
        nextButton.setOnClickListener(view -> {
            if (!hasValidEventDetails()) {
                return;
            }

            // Grab the values from eventDate and eventTime if selectedYear, month, day, etc are 0
            checkAndSetDateAndTime();

            // Set the Calendar instance to the selected date and time
            cal.set(Calendar.YEAR, selectedYear);
            cal.set(Calendar.MONTH, selectedMonth);
            cal.set(Calendar.DAY_OF_MONTH, selectedDay);
            cal.set(Calendar.HOUR_OF_DAY, selectedHour);
            cal.set(Calendar.MINUTE, selectedMinute);

            try {
                newEvent.setMaxAttendees(Integer.parseInt(eventCapacity.getText().toString()));
            } catch(NumberFormatException e){
//                e.printStackTrace();
                newEvent.setMaxAttendees(0);
            }
            newEvent.setEventDate(cal);
            newEvent.setEventName(eventName.getText().toString().trim());
            newEvent.setEventInfo(eventDescripiton.getText().toString().trim());
            newEvent.setEventLocation(eventLocation.getText().toString().trim());
            newEvent.setEventCreator(app.getUserProfile().getProfileID());
            newEvent.setEventLocation(eventLocation.getText().toString());
            newEvent.setEventPosterFromUri(currentImage, getApplicationContext());
            if (currentImage != null) {
                newEvent.setEventPosterFromUri(currentImage, getApplicationContext());
            }

            if(!isEditing){
                QRCodeManager codeManager = new QRCodeManager();
                QRCode checkInQr = codeManager.generateQRCode(newEvent, "checkIn");
                QRCode infoQr = codeManager.generateQRCode(newEvent, "eventInfo");
                newEvent.setCheckInQR(checkInQr.getRawText());
                newEvent.setInfoQRCode(infoQr.getRawText());
            }

            app.setCachedEvent(newEvent);
            handleEventCreation();
        });
    }

    private void checkAndSetDateAndTime() {
        if (selectedYear == 0 && selectedMonth == 0 && selectedDay == 0) {
            // values in editText are in format: dd-mm-yyyy
            String[] dateParts = eventDate.getText().toString().split("-");
            selectedDay = Integer.valueOf(dateParts[0]);
            selectedMonth = Integer.valueOf(dateParts[1]) - 1; // 0-indexed month
            selectedYear = Integer.valueOf(dateParts[2]);
        }

        if (selectedHour == 0 && selectedMinute == 0) {
            // values in editText are in format: hh:mm
            String[] timeParts = eventTime.getText().toString().split(":");
            selectedHour = Integer.valueOf(timeParts[0]);
            selectedMinute = Integer.valueOf(timeParts[1]);
        }
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
                && validateCapacityNonNegative(eventCapacity)
                && cal != null
                && validateEventNameLength(eventName)
                && validateImageHasBeenChosen();
    }

    private boolean validateImageHasBeenChosen() {
        if(isEditing)
            return true;

        boolean imageChosen = currentImage != null;
        if (!imageChosen)
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
    private boolean validateCapacityNonNegative(EditText editText) {
        if (Integer.parseInt(editText.getText().toString()) < 0) {
            editText.setError("Capacity cannot be negative!");
            return false;
        }
        return true;
    }
    private boolean validateEventNameLength(EditText editText) {
        if (editText.getText().toString().length() > 25) {
            editText.setError("Event Name too long!");
            return false;
        }
        return true;
    }

    private void handleEventCreation() {
        if(currentImage != null) {
            // Initiate uploading of event poster
            app.getPosterImageHandler().uploadPoster(currentImage, newEvent, getApplicationContext());
        }

        // Use a single event handling path for both edit and new event creation
        EventTable eventTable = app.getEventTable();
        Table.DocumentCallback<Event> eventCallback = new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event document) {
                if(!isEditing) {
                    AddHostedEventToProfile(document);
                }
                else{
                    finish();
                }
            }

            @Override
            public void onFailure(Exception e) {
                // Handle the failure of updating the event in the database
            }
        };

        // Update or push event document based on event type
        if (isEditing) {
            eventTable.updateDocument(newEvent, newEvent.getEventID().toString(), eventCallback);
        } else {
            eventTable.pushDocument(newEvent, newEvent.getEventID().toString(), eventCallback);
        }

    }

    private void AddHostedEventToProfile(Event currentEvent) {
        String currentEventId = currentEvent.getEventID().toString();
        List<String> hostedEvents = app.getUserProfile().getHostedEvents();
        hostedEvents.add(currentEventId);
        app.getUserProfile().setHostedEvents(hostedEvents);
        app.getHostedEvents().put(currentEvent.getEventID(), currentEvent);

        // Update or push profile document
        ProfileTable profileTable = app.getProfileTable();
        profileTable.pushDocument(app.getUserProfile(), app.getUserProfile().getProfileID().toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile document) {
                String message = "Event, " + currentEvent.getEventName() + " , Successfully created";
                Toast.makeText(app.getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                // Navigate to access QR code page
                Intent genQRActivity = new Intent(EnterEventDetailsActivity.this, ViewQRCodesActivity.class);
                startActivity(genQRActivity);
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure of updating the profile in the database
            }
        });
    }
}