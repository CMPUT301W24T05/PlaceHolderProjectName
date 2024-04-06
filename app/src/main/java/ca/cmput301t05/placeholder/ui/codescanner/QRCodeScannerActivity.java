package ca.cmput301t05.placeholder.ui.codescanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.qrcode.QRCodeManager;
import ca.cmput301t05.placeholder.qrcode.QRCodeType;
import ca.cmput301t05.placeholder.ui.events.checkin.SuccessfulCheckinActivity;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

/**
 * QRcodeScanner is an activity for scanning QR codes using the device's camera. It leverages the CodeScanner library
 * to decode QR codes and perform actions based on the decoded information. This class handles camera permission requests
 * and displays a dialog if the permission is denied. It provides feedback to the user via Toast messages upon successful
 * QR code scans.
 */
public class QRCodeScannerActivity extends AppCompatActivity {

    public static final String QR_SCANNER_ID_KEY = "QRScannerActivityId";
    public static final String QR_SCANNER_ID_VALUE = "QRScannerActivityEVENTINFO";

    CodeScanner mCodeScanner;
    ActivityResultLauncher<String> requestPermissionLauncher;
    private PlaceholderApp app;
    boolean dialogBool;

    /**
     * Called when the activity is starting. This method initializes the CodeScanner, sets the content view to the
     * camera_activity layout, and sets up permission handling for accessing the camera.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this
     *                           Bundle contains the data it most recently supplied. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);

        //initialize app and profile
        app = (PlaceholderApp) getApplicationContext();

        mCodeScanner = new CodeScanner(this, scannerView);
        setupDecodeCallback();

        if (checkCurrentPermission()) {
            mCodeScanner.startPreview(); // Start the preview now
        } else {
            initializePermissionLauncher();
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    /**
     * Sets up the decode callback for the code scanner.
     * When a QR code is scanned, the callback function runs on the UI thread
     * and calls the handleQRCodeScan method with the scanned QR code text as a parameter.
     */
    private void setupDecodeCallback() {
        mCodeScanner.setDecodeCallback(result ->
                runOnUiThread(() ->
                        handleQRCodeScan(result.getText())
                )
        );
    }

    /**
     * Handles the QR code scan and performs corresponding actions based on the scanned QR code.
     *
     * @param rawText The raw string representation of the scanned QR code.
     */
    private void handleQRCodeScan(@NonNull String rawText) {
        Log.d("QR", "Scanned");
        Log.d("QR_RAW", rawText);

        QRCodeManager manager = new QRCodeManager();
        QRCodeType type = manager.checkQRcodeType(rawText);
        String qrEventID = manager.getEventID(rawText).toString();

        app.getEventTable().fetchDocument(qrEventID, new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event event) {
                handleQRCodeType(type, event);
            }

            @Override
            public void onFailure(Exception e) {
                // Failed to get fetch the event for eventId with exception e
            }
        });
    }

    /**
     * Handles the QR code type and performs corresponding actions.
     *
     * @param type  The type of QR code to handle (INFO or CHECK_IN).
     * @param event The event associated with the QR code.
     */
    private void handleQRCodeType(QRCodeType type, Event event) {
        Log.e("QR_SERVER", "Server Query Successful");
        app.setCachedEvent(event);

        Intent intent;
        if (type == QRCodeType.CHECK_IN) {
            intent = new Intent(QRCodeScannerActivity.this, SuccessfulCheckinActivity.class);
            startActivity(intent);
            finish();
        } else if (type == QRCodeType.INFO) {
            app.setCachedEvent(event);
            Intent data = new Intent();
            data.putExtra(QR_SCANNER_ID_KEY, QR_SCANNER_ID_VALUE);
            setResult(RESULT_OK, data);
        }

        finish();
    }


    /**
     * Checks if the app has been granted camera permission.
     *
     * @return true if the permission has been granted, false otherwise.
     */
    boolean checkCurrentPermission() {
        // check if the user granted us permission from a previous session
        // Check if permission granted, if granted start camera!
        return ContextCompat.checkSelfPermission(QRCodeScannerActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Initializes the requestPermissionLauncher used to request camera permission from the user. Defines the
     * behavior upon permission grant or denial.
     */
    void initializePermissionLauncher() {
        // This method initializes and handles the logic of the permission launcher if we need to request permissions
        requestPermissionLauncher = registerForActivityResult( // Request launcher is being initialized
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        mCodeScanner.startPreview(); // starting camera

                    } else {
                        showPermissionDeniedDialog();
                    }
                });

    }


    /**
     * Shows a dialog informing the user that camera permission has been denied and the feature requires this permission.
     * The dialog provides an "OK" button to dismiss it.
     */
    void showPermissionDeniedDialog() { // Shows the permission denied pop-up
        dialogBool = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Denied");
        builder.setMessage("This feature requires camera permission. Please enable it in the app settings.");
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            // You can add additional logic here if needed
            dialogInterface.dismiss();
        });
        builder.show();
        //        finish();
    }

    boolean dialogStatus() { // To be used strictly for testing.
        // Required as the code scanner is final and mockito states
        // to not mock types you do not own.

        return dialogBool;
    }

}
