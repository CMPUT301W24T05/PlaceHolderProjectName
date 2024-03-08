package ca.cmput301t05.placeholder.ui.events.creation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;

import ca.cmput301t05.placeholder.database.Table;
import ca.cmput301t05.placeholder.events.Event;

import ca.cmput301t05.placeholder.qrcode.QRCode;
import ca.cmput301t05.placeholder.qrcode.QRCodeManager;

/**
 * GenerateQRCodesActivity is an activity class for creating the check-in QR code.
 * This is done either by generating a new, random one or scanning an already existing one for reuse.
 * After this is generated, the user moves on to the next stage of the event creation process, Preview
 */
public class GenerateQRCodesActivity extends AppCompatActivity {

    Button existingQR_btn, generateQR_btn, back_btn;
    QRCodeManager QRCM;
    PlaceholderApp app = (PlaceholderApp) getApplicationContext();


/**
 * Called when the activity is starting. Initializes the UI components, sets up click listeners
 * when the generate new qr button is clicked, a new random check in qr is initialized for the event object
 *
 * @param savedInstanceState If the activity is being re-initialized after
 *     previously being shut down then this Bundle contains the data it most
 *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
 */
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

                String eventId = app.getCachedEvent().getEventID().toString();

                // how to get eventId not yet determined
                app.getEventTable().fetchDocument(eventId, new Table.DocumentCallback<Event>() {
                    @Override
                    public void onSuccess(Event event){
                        // Do something with the fetched event here
                        QRCode qr = QRCM.generateQRCode(event, "checkIn");
                        //event.checkInQR = qr;

                        //display preview activity then move to preview page
                       // Intent i = new Intent(GenerateQRCodesActivity.this, EventPreview.class);

//                        Intent i = new Intent(GenerateQRCodesActivity.this, Preview.class);


                    }

                    @Override
                    public void onFailure(Exception e){
                        // Failed to get fetch the event for eventId with exception e
                    }
                });


            }
        });
    }
}
