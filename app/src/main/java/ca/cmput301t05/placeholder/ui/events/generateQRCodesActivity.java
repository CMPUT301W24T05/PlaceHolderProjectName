package ca.cmput301t05.placeholder.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.QRCode;
import ca.cmput301t05.placeholder.events.QRCodeManager;

public class generateQRCodesActivity extends AppCompatActivity {

    Button existingQR_btn, generateQR_btn, back_btn;
    QRCodeManager QRCM;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_generateqr);

        existingQR_btn = findViewById(R.id.scan_existing_qr);
        generateQR_btn = findViewById(R.id.generate_new_qr);
        back_btn = findViewById(R.id.back_button);
        QRCM = new QRCodeManager();

        //Here should I get the event from the previous intent as a Serializable object or should I pull it from the database
        //String event = getIntent().getExtras().getString("event");

        generateQR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // QRCode qr = QRCM.generateQRCode(event);

                // complete this
                //Intent i = new Intent(generateQRCodesActivity.this, qrCompleteFrag.class);
                //startActivity(i);


            }
        });
    }
}
