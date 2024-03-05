package ca.cmput301t05.placeholder.events;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.type.PostalAddress;

import java.util.Calendar;

import ca.cmput301t05.placeholder.R;

public class EnterEventDetailsActivity extends AppCompatActivity {

    private EditText eventName;

    private EditText eventLocation;

    private EditText eventTime;

    private EditText eventCapacity;

    private EditText eventDescripiton;

    private Button nextButton;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_enterdetails);

        eventName = findViewById(R.id.enterEventName);
        eventLocation = findViewById(R.id.enterLocation);
        eventTime = findViewById(R.id.enterDateTime);
        eventDescripiton = findViewById(R.id.enterEventDescription);
        eventCapacity = findViewById(R.id.enterEventCapacity);

        nextButton = findViewById(R.id.eventDetailNextPage);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String eName = eventName.toString();
                //need to change this to the geolocation in google
                String eLocation = eventLocation.toString();

                String eTime = eventTime.toString();

                String eDescription = eventDescripiton.toString();

                String cap = eventCapacity.toString();

                

                int eCapacity = Integer.parseInt(eventCapacity.toString());





            }
        });




        Event createdEvent = new Event(eventName.toString(), eventDescripiton.toString(), 100);






    }

}