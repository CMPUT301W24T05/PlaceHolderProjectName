package ca.cmput301t05.placeholder.ui.events;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.qrcode.QRCode;
import ca.cmput301t05.placeholder.qrcode.QRCodeManager;

public class GenerateInfoCheckinActivity extends AppCompatActivity {

    private Button generate1;

    private Button generate2;

    private Button createEvent;

    private ImageView qrCode1;

    private ImageView qrCode2;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlaceholderApp app = (PlaceholderApp) getApplicationContext();

        Event curEvent = app.getCachedEvent();

        setContentView(R.layout.event_generate_info_checkin);

        QRCodeManager qrm = new QRCodeManager();

        //gen1 = Checkin
        generate1 = findViewById(R.id.event_generate_genBtn1);

        //gen2 = info
        generate2 = findViewById(R.id.event_generate_generate2);

        createEvent = findViewById(R.id.event_generate_create);
        qrCode1 = findViewById(R.id.event_generate_qrcode1);
        qrCode2 = findViewById(R.id.event_generate_qr2);


        generate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (generate1.getText().equals("Generate")){
                    QRCode checkIn = qrm.generateQRCode(curEvent, "checkIn");

                    qrCode1.setImageBitmap(checkIn.getBitmap());
                    generate1.setText("Export QR Code");
                    curEvent.setCheckInQR(checkIn.getRawText());


                }   else {

                    //export code goes here
                }
            }
        });

        generate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (generate2.getText().equals("Generate")){
                    QRCode info = qrm.generateQRCode(curEvent, "eventInfo");

                    qrCode2.setImageBitmap(info.getBitmap());
                    generate2.setText("Export QR Code");
                    curEvent.setInfoQRCode(info.getRawText());

                }   else {

                    //export code goes here
                }
            }
        });



    }
}
