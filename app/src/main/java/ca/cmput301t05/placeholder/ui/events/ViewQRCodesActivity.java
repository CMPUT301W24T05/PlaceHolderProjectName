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
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.ViewPagerAdapter;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.qrcode.QRCode;
import ca.cmput301t05.placeholder.qrcode.QRCodeManager;
import ca.cmput301t05.placeholder.qrcode.QRCodeType;
import ca.cmput301t05.placeholder.ui.codescanner.ReuseQRCodeScannerActivity;
import me.relex.circleindicator.CircleIndicator3;

/**
 * The ViewQRCodesActivity is an activity class for viewing and sharing the generated QR codes
 */
public class ViewQRCodesActivity extends AppCompatActivity {

    private Button reuseQrButton;
    private QRCode checkIn, info;
    private Button shareButton;
    private final QRCodeManager qrm = new QRCodeManager();
    private ViewPager2 viewPager;
    private List<String> title;
    private CircleIndicator3 indicator3;
    private Toolbar toolbar;
    private PlaceholderApp app;
    private ActivityResultLauncher<Intent> qrCodeScannerLauncher;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_viewqr);

        initializeViews();

        populatePages();

        setupShareButtonClickListener();
        setupReuseQRButtonClickListener();
        setupToolbarClickListener();

        qrCodeScannerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleQRCodeScanResult);
    }

    /**
     * Populates the pages in the ViewQRCodesActivity. This method generates QR codes for the current event
     * and adds them to the ViewPager along with their corresponding titles.
     */
    private void populatePages() {
        int pos = viewPager.getCurrentItem();

        List<Bitmap> qrImage = new ArrayList<>();
        Event curEvent = app.getCachedEvent();

        checkIn = qrm.generateQRCode(curEvent, "checkIn");
        info = qrm.generateQRCode(curEvent, "eventInfo");

        qrImage.add(info.getBitmap());
        qrImage.add(checkIn.getBitmap());

        ViewPagerAdapter adapter = new ViewPagerAdapter(title, qrImage);
        viewPager.setAdapter(adapter);
        indicator3.setViewPager(viewPager);

        viewPager.setCurrentItem(pos);
    }

    /**
     * Initializes the views used in the ViewQRCodesActivity.
     * This method locates the views from the layout XML file and assigns them to the corresponding variables.
     * It also initializes the title ArrayList with two elements.
     */
    private void initializeViews() {
        app = (PlaceholderApp) getApplicationContext();
        toolbar = findViewById(R.id.toolbarViewQRcode);
        shareButton = findViewById(R.id.share_qr);
        reuseQrButton = findViewById(R.id.reuse_qr);
        viewPager = findViewById(R.id.swipe_fragment);
        indicator3 = findViewById(R.id.swipe_indicator);

        title = new ArrayList<>();
        title.add("Event Info QR Code");
        title.add("Check In QR Code");
    }

    /**
     * Handles share button click.
     */
    private void setupShareButtonClickListener() {
        shareButton.setOnClickListener(v -> {
            int pos = viewPager.getCurrentItem();
            if (pos == 0) {
                shareQRCode(info);
            } else {
                shareQRCode(checkIn);
            }
        });
    }

    /**
     * Handles reuse QR button click.
     */
    private void setupReuseQRButtonClickListener() {
        reuseQrButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewQRCodesActivity.this, ReuseQRCodeScannerActivity.class);
            qrCodeScannerLauncher.launch(intent); // Use the launcher to start the activity
        });
    }

    /**
     * Handles toolbar navigation click.
     */
    private void setupToolbarClickListener() {
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(ViewQRCodesActivity.this, EventMenuActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Handles result of QR code scan.
     */
    private void handleQRCodeScanResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                updateScannedQRCode(data.getStringExtra("scannedQRCode"));
            }
        }
    }

    /**
     * Processes retrieved QR code
     */
    private void updateScannedQRCode(String scannedQRCode) {
        PlaceholderApp app = (PlaceholderApp) getApplicationContext();
        final Event event = app.getCachedEvent();

        QRCodeType type;
        int pos = viewPager.getCurrentItem();
        if (pos == 0) {
            type = QRCodeType.INFO;
        } else {
            type = QRCodeType.CHECK_IN;
        }

        if (!validateQRString(scannedQRCode, type)) {
            scannedQRCode = scannedQRCode + ";" + type;
        }

        if (type == QRCodeType.INFO) {
            // Set the scanned QR code as the event info QR code of the event
            event.setInfoQRCode(scannedQRCode);
        } else {
            // Set the scanned QR code as the check-in QR code of the event
            event.setCheckInQR(scannedQRCode);
        }

        // Update the event in the database
        app.getEventTable().pushDocument(event, event.getEventID().toString(), new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event document) {
                // Handle success, if needed
                Toast.makeText(ViewQRCodesActivity.this, "Update successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure, if needed
                Toast.makeText(ViewQRCodesActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
        populatePages();
    }

    private boolean validateQRString(String rawText, QRCodeType type) {
        // Verify that the rawText string ends in ;CHECK_IN for CHECK_IN type
        // or ends in ;INFO for INFO type
        return rawText.endsWith(";" + type.name());
    }


    /**
     * Shares the given QR code via a sharing intent. The method generates an image with the QR code and text,
     * and inserts it into the media store. Then, it creates a sharing intent and starts the activity to show
     * a chooser dialog with available apps to share the QR code.
     *
     * @param qr The QR code to share.
     */
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