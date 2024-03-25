package ca.cmput301t05.placeholder.ui.events;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.qrcode.QRCode;
import ca.cmput301t05.placeholder.qrcode.QRCodeManager;
import ca.cmput301t05.placeholder.qrcode.QRCodeType;
import ca.cmput301t05.placeholder.ui.events.creation.UploadPosterActivity;

public class GenerateInfoCheckinActivity extends AppCompatActivity {

    private Button generate1;

    private Button generate2;

    private Button next;

    private ImageView qrCode1;

    private ImageView qrCode2;

    private String qrToSave;
    private Button back;

    private ActivityResultLauncher<String> createDocumentLauncher;
    @SuppressLint("MissingInflatedId")
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


        next = findViewById(R.id.event_generate_qr_next);
        qrCode1 = findViewById(R.id.event_generate_qrcode1);
        qrCode2 = findViewById(R.id.event_generate_qr2);
        back = findViewById(R.id.event_generate_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        QRCode checkIn = qrm.generateQRCode(curEvent, "checkIn");
        QRCode info = qrm.generateQRCode(curEvent, "eventInfo");

        createDocumentLauncher = registerForActivityResult(new ActivityResultContracts.CreateDocument("image/jpeg"), uri -> {
            if (uri != null) {
                // Your logic to save the bitmap to the uri

                if ("checkIn".equals(qrToSave)){
                    qrToUri(checkIn, uri);
                }   else if ("info".equals(qrToSave)){
                    qrToUri(info,uri);
                }

            }
        });

        //check in
        generate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (generate1.getText().equals("Generate")){


                    qrCode1.setImageBitmap(checkIn.getBitmap());
                    generate1.setText("Export QR Code");
                    curEvent.setCheckInQR(checkIn.getRawText());


                }   else {

                    //export code goes
                    qrToSave = "checkIn";
                    createDocumentLauncher.launch("checkInQRCode.jpeg");

                }
            }
        });


        generate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (generate2.getText().equals("Generate")){


                    qrCode2.setImageBitmap(info.getBitmap());
                    generate2.setText("Export QR Code");
                    curEvent.setInfoQRCode(info.getRawText());


                }   else {

                    //export code goes here
                    qrToSave = "info";
                    createDocumentLauncher.launch("infoQRCode.jpeg");


                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(GenerateInfoCheckinActivity.this, UploadPosterActivity.class);
                startActivity(i);
                finish();

            }
        });



    }

    private void qrToUri(QRCode qr, Uri uri) {
        //essentially converts qr codes and puts them @ the uri
        try {
            OutputStream os = getContentResolver().openOutputStream(uri);
            qr.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void shareQRCode(QRCode qr){
        String text;
        if(qr.getType() == QRCodeType.INFO){
            text = "My Event Info QR code";
        }
        else{
            text = "My CheckIn QR code";
        }

        String stringPath = MediaStore.Images.Media
                .insertImage(this.getContentResolver(), qr.getBitmap(), text, null);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(stringPath));
        startActivity(Intent.createChooser(intent, "Share this qr code"));
    }


}
