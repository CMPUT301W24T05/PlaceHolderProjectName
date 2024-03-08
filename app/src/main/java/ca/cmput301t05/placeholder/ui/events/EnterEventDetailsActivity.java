package ca.cmput301t05.placeholder.ui.events;

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
import ca.cmput301t05.placeholder.database.Table;
import ca.cmput301t05.placeholder.events.Event;

import java.util.Calendar;
import java.util.Locale;

public class EnterEventDetailsActivity extends AppCompatActivity {

    private EditText eventName;
    private EditText eventLocation;
    private EditText eventDate;
    private EditText eventTime;
    private EditText eventCapacity;
    private EditText eventDescripiton;
    private Button nextButton;

    private PlaceholderApp app;

    private Event newEvent;
    private Calendar cal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_enterdetails);

        initializeEventDetails();
        eventTime.setOnClickListener(view -> openTimePickerDialog());
        eventDate.setOnClickListener(view -> openDatePickerDialog());

        nextButton = findViewById(R.id.eventDetailNextPage);

        newEvent = new Event();

        setupNextButtonClick();
    }

    private void initializeEventDetails() {
        eventName = findViewById(R.id.enterEventName);
        eventLocation = findViewById(R.id.enterLocation);
        eventDate = findViewById(R.id.enterDate);
        eventTime = findViewById(R.id.enterTime);
        eventDescripiton = findViewById(R.id.enterEventDescription);
        eventCapacity = findViewById(R.id.enterEventCapacity);
        app = (PlaceholderApp) getApplicationContext();
    }

    private void openTimePickerDialog() {
        if (cal == null) return;

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(EnterEventDetailsActivity.this, (view, hourOfDay, minuteOfHour) -> eventTime.setText(String.format(Locale.CANADA, "%d:%d", hourOfDay, minuteOfHour)), hour, minute, false);

        timePickerDialog.show();
    }

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

    private void setupNextButtonClick() {
        nextButton.setOnClickListener(view -> {
            if (!hasValidEventDetails()) return;

            newEvent.setMaxAttendees(Integer.parseInt(eventCapacity.getText().toString()));
            newEvent.setEventDate(cal);
            newEvent.setEventName(eventName.getText().toString().trim());
            newEvent.setEventInfo(eventDescripiton.getText().toString().trim());

            addEventToDatabase();
        });
    }

    private boolean hasValidEventDetails() {
        return cal != null && validateEditTextNotEmpty(eventName) && validateEditTextNotEmpty(eventLocation) && validateEditTextNotEmpty(eventDate) && validateEditTextNotEmpty(eventTime) && validateEditTextNotEmpty(eventDescripiton) && validateEditTextNotEmpty(eventCapacity);
    }

    private boolean validateEditTextNotEmpty(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError("Field cannot be empty");
            return false;
        }
        return true;
    }

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