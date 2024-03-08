package ca.cmput301t05.placeholder.ui.events;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.qrcode.QRCode;

/**
 * The ViewQRCodesActivity is an activity class for viewing and sharing the generated QR codes
 */
public class ViewQRCodesActivity extends AppCompatActivity {

    Button back, shareqr;

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

        back = findViewById(R.id.back_viewqr);
        shareqr = findViewById(R.id.share_qr);
        display = findViewById(R.id.qr_display);

        //event object will be pull somehow to this class

        QRCode infoQR = event.infoQRCode;
        QRCode checkInQR = event.checkInQR;

        display.setImageBitmap(infoQR.bitmap);


    }
}
