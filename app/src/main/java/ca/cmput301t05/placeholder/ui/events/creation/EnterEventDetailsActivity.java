package ca.cmput301t05.placeholder.ui.events.creation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
    private Button nextButton;

    private PlaceholderApp app;

    private Event newEvent, curEvent;
    private Calendar cal;

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
        eventTime.setOnClickListener(view -> openTimePickerDialog());
        eventDate.setOnClickListener(view -> openDatePickerDialog());

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


        }
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

                curEvent.setMaxAttendees(Integer.parseInt(eventCapacity.getText().toString()));
                curEvent.setEventDate(cal);
                curEvent.setEventName(eventName.getText().toString().trim());
                curEvent.setEventInfo(eventDescripiton.getText().toString().trim());
                curEvent.setEventCreator(app.getUserProfile().getProfileID());
                curEvent.setEventLocation(eventLocation.getText().toString().trim());
                Intent generateqr = new Intent(EnterEventDetailsActivity.this, UploadPosterActivity.class);
                generateqr.putExtra("edit", true);
                startActivity(generateqr);
            }else{

                newEvent.setMaxAttendees(Integer.parseInt(eventCapacity.getText().toString()));
                newEvent.setEventDate(cal);
                newEvent.setEventName(eventName.getText().toString().trim());
                newEvent.setEventInfo(eventDescripiton.getText().toString().trim());
                newEvent.setEventCreator(app.getUserProfile().getProfileID());
                newEvent.setEventLocation(eventLocation.getText().toString().trim());
                app.setCachedEvent(newEvent);
                Intent generateqr = new Intent(EnterEventDetailsActivity.this, GenerateInfoCheckinActivity.class);
                startActivity(generateqr);
            }


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
}