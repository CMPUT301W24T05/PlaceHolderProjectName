package ca.cmput301t05.placeholder.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import ca.cmput301t05.placeholder.MainActivity;
import ca.cmput301t05.placeholder.R;

public class EnterEventDetailsActivity extends AppCompatActivity {

    private EditText eventName;

    private EditText eventLocation;

    private EditText eventDate;

    private EditText eventCapacity;

    private EditText eventDescripiton;

    private Button nextButton;

    private EditText eventTime;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_enterdetails);

        eventName = findViewById(R.id.enterEventName);

        //need to do this as well
        eventLocation = findViewById(R.id.enterLocation);

        //need to make this better
        eventDate = findViewById(R.id.enterDate);

        eventTime = findViewById(R.id.enterTime);

        final Calendar[] c = new Calendar[1];

        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = c[0].get(Calendar.HOUR_OF_DAY);
                int minute = c[0].get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(EnterEventDetailsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // on below line we are setting selected time
                                // in our text view.
                                eventTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
            }
        });

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c[0] = Calendar.getInstance();

                int year = c[0].get(Calendar.YEAR);
                int month = c[0].get(Calendar.MONTH);
                int day = c[0].get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        EnterEventDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                eventDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },

                        year, month, day);

                datePickerDialog.show();
            }
        });




        eventDescripiton = findViewById(R.id.enterEventDescription);
        eventCapacity = findViewById(R.id.enterEventCapacity);

        nextButton = findViewById(R.id.eventDetailNextPage);

        final Event newEvent[] = new Event[1];
        newEvent[0] = new Event();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String eName = eventName.getText().toString().trim();

                boolean error = false;

                if (eName.isEmpty()){
                    eventName.setError("Field cannot be empty");
                    error = true;
                }

                String location = eventLocation.getText().toString().trim();

                if (location.isEmpty()){
                    eventLocation.setError("Field cannot be empty");
                    error = true;
                }

                String eDate = eventDate.getText().toString().trim();

                if(eDate.isEmpty()){
                    eventDate.setError("Field cannot be empty");
                    error = true;
                }

                String eTime = eventTime.getText().toString().trim();

                if (eTime.isEmpty()){
                    eventTime.setError("Field cannot be empty");
                    error = true;
                }

                String eDescription = eventDescripiton.getText().toString().trim();

                if (eDescription.isEmpty()){
                    eventDescripiton.setError("Field cannot be empty");
                    error = true;
                }

                String capacity = eventCapacity.getText().toString().trim();

                if (capacity.isEmpty()){
                    eventCapacity.setError("Field cannot be empty");
                    error = true;
                }


                if(c[0] == null){
                    return;
                }

                if (error){
                    return;
                }

                int maxAttendees = Integer.parseInt(capacity);

                newEvent[0].setMaxAttendees(maxAttendees);
                newEvent[0].setEventDate(c[0]);
                newEvent[0].setEventName(eName);
                newEvent[0].setEventInfo(eDescription);
                newEvent[0].sendEventToDatabase();

                //now send the event id to the bundle
                savedInstanceState.putString("created_event_ID",newEvent[0].eventID.toString());

                //open the poster thing
                Intent posterPick = new Intent(EnterEventDetailsActivity.this, uploadPosterActivity.class);
                startActivity(posterPick);



            }
        });





    }


}