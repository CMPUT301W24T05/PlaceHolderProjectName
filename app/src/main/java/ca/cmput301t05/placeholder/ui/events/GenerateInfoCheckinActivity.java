package ca.cmput301t05.placeholder.ui.events;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;

import ca.cmput301t05.placeholder.MainActivity;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.qrcode.QRCode;
import ca.cmput301t05.placeholder.qrcode.QRCodeManager;
import ca.cmput301t05.placeholder.qrcode.QRCodeType;

public class GenerateInfoCheckinActivity extends AppCompatActivity {

    private Button generate1;

    private Button generate2;

    private Button createEvent;

    private ImageButton shareqr1;

    private ImageButton shareqr2;

    private ImageView qrCode1;

    private ImageView qrCode2;

    private String qrToSave;
    private Button back;

    private ActivityResultLauncher<String> createDocumentLauncher;
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

        //share check in qr
        shareqr1 = findViewById(R.id.share_btn1);

        //share info qr
        shareqr2 = findViewById(R.id.share_btn2);

        createEvent = findViewById(R.id.event_generate_create);
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

                    shareqr1.setVisibility(View.VISIBLE);


                }   else {

                    //export code goes
                    qrToSave = "checkIn";
                    createDocumentLauncher.launch("checkInQRCode.jpeg");

                }
            }
        });

        shareqr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareQRCode(checkIn);

            }
        });

        generate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (generate2.getText().equals("Generate")){


                    qrCode2.setImageBitmap(info.getBitmap());
                    generate2.setText("Export QR Code");
                    curEvent.setInfoQRCode(info.getRawText());

                    shareqr2.setVisibility(View.VISIBLE);

                }   else {

                    //export code goes here
                    qrToSave = "info";
                    createDocumentLauncher.launch("infoQRCode.jpeg");


                }
            }
        });

        shareqr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareQRCode(info);
            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //basically we want to upload the qr strings
                curEvent.toDocument();

                //push changes
                app.getPosterImageHandler().uploadPoster(app.getPicCache(), curEvent); //updates the event

                // Pushes the current event (currEvent) to the event table in the database
                // This push is also asynchronous, we go back to the Main activity in the onSuccess callback
                app.getEventTable().pushDocument(curEvent, curEvent.getEventID().toString(), new Table.DocumentCallback<Event>() {
                    @Override
                    public void onSuccess(Event document) {
                        // If the document was successfully updated in the database, start the Main activity and finish this activity
                        String message = "Event," + curEvent.getEventName() +  " , Successfully created";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();


                        Intent i = new Intent(GenerateInfoCheckinActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // TODO Handle the failure of updating the event in the database
                    }
                });

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
