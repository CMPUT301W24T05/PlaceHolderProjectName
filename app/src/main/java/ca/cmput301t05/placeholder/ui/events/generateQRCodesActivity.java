package ca.cmput301t05.placeholder.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.QRCode;
import ca.cmput301t05.placeholder.events.QRCodeManager;

public class generateQRCodesActivity extends AppCompatActivity {

    Button existingQR_btn, generateQR_btn, back_btn;
    QRCodeManager QRCM;
    PlaceholderApp app = (PlaceholderApp) getApplicationContext();



    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_generateqr);

        existingQR_btn = findViewById(R.id.scan_existing_qr);
        generateQR_btn = findViewById(R.id.generate_new_qr);
        back_btn = findViewById(R.id.back_button);
        QRCM = new QRCodeManager();

        generateQR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // how to get eventId not yet determined
                app.getEventTable().fetchDocument(eventId.toString(), new Table.DocumentCallback<Event>() {
                    @Override
                    public void onSuccess(Event event){
                        // Do something with the fetched event here
                        QRCode qr = QRCM.generateQRCode(event, "checkIn");
                        event.checkInQR = qr;

                        //display generation success fragment then move to preview page
                        //...


                    }

                    @Override
                    public void onFailure(Exception e){
                        // Failed to get fetch the event for eventId with exception e
                    }
                });



                // complete this
                //Intent i = new Intent(generateQRCodesActivity.this, qrCompleteFrag.class);
                //startActivity(i);


            }
        });
    }
}
