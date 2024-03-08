package ca.cmput301t05.placeholder.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

import ca.cmput301t05.placeholder.MainActivity;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.qrcode.QRCode;

/**
 * The ViewQRCodesActivity is an activity class for viewing and sharing the generated QR codes
 */
public class ViewQRCodesActivity extends AppCompatActivity implements Serializable {

    Button back, shareqr;
    Button nextButton;
    PlaceholderApp app;

    ImageView display;

    /**
     * This on create method sets the bitmap of the qr code to an image view for display.
     * It also initializes all views
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_viewqr);
        app = (PlaceholderApp) getApplicationContext();
        back = findViewById(R.id.back_viewqr);
        shareqr = findViewById(R.id.share_qr);
        display = findViewById(R.id.qr_display);
        nextButton = findViewById(R.id.goToMain);


        Event event = app.getCachedEvent();
//        //event object will be pull somehow to this class
//        Event event = intent.getStringExtra()

//        QRCode infoQR = event.infoQRCode;
        QRCode checkInQR = event.checkInQR;

        display.setImageBitmap(checkInQR.bitmap);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This code will be executed when the button is clicked
                Intent main = new Intent(ViewQRCodesActivity.this, MainActivity.class);
                startActivity(main);
                finish();
            }
        });
    }



    }

