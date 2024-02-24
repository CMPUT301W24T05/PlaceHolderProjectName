package ca.cmput301t05.placeholder.events;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.type.PostalAddress;

import ca.cmput301t05.placeholder.R;

public class EnterEventDetailsActivity extends AppCompatActivity {

    private EditText eventName;

    private EditText eventLocation;

    private EditText eventTime;

    private EditText eventCapacity;

    private EditText eventDescripiton;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_enterdetails);


    }

}