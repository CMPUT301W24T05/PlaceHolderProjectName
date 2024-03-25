package ca.cmput301t05.placeholder.ui.events;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.ViewPagerAdapter;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.qrcode.QRCode;
import ca.cmput301t05.placeholder.qrcode.QRCodeManager;
import ca.cmput301t05.placeholder.qrcode.QRCodeType;
import me.relex.circleindicator.CircleIndicator3;

/**
 * The ViewQRCodesActivity is an activity class for viewing and sharing the generated QR codes
 */
public class ViewQRCodesActivity extends AppCompatActivity {

    Button back, shareqr;

    ImageView display;

    QRCode checkIn, info;

    Button shareButton;

    QRCodeManager qrm = new QRCodeManager();

    ViewPager2 viewPager;

    ViewPagerAdapter adapter;

    private List<String> title;
    private List<Bitmap> qrImage;

    CircleIndicator3 indicator3;



    /**
     * This on create method sets the bitmap of the qr code to an image view for display.
     * It also initializes all views
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_viewqr);
        PlaceholderApp app = (PlaceholderApp) getApplicationContext();
        Event curEvent = app.getCachedEvent();

        back = findViewById(R.id.back_viewqr);
        shareButton = findViewById(R.id.share_qr);
        title = new ArrayList<>();
        qrImage = new ArrayList<>();
        viewPager = findViewById(R.id.swipe_fragment);

        checkIn = qrm.generateQRCode(curEvent, "checkIn");
        info = qrm.generateQRCode(curEvent, "eventInfo");

        title.add("Event Info QR Code");
        title.add("Check In QR Code");

        qrImage.add(info.getBitmap());
        qrImage.add(checkIn.getBitmap());

        adapter = new ViewPagerAdapter(title, qrImage);
        viewPager.setAdapter(adapter);

        indicator3 = findViewById(R.id.swipe_indicator);
        indicator3.setViewPager(viewPager);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewPager.getCurrentItem();

                if(pos==0){
                    shareQRCode(info);
                }
                else{
                    shareQRCode(checkIn);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewQRCodesActivity.this, EventMenuActivity.class);
                startActivity(intent);
            }
        });

    }


    private void shareQRCode(QRCode qr) {
        String text;
        if (qr.getType() == QRCodeType.INFO) {
            text = "My Event Info QR code";
        } else {
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
