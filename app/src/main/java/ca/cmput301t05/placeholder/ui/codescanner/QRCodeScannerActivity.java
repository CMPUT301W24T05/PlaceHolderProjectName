package ca.cmput301t05.placeholder.ui.codescanner;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;

import ca.cmput301t05.placeholder.qrcode.QRCodeManager;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;

import com.google.zxing.Result;


import android.Manifest;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.Table;
import ca.cmput301t05.placeholder.ui.events.EventDetailsDialogFragment;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
//import ca.cmput301t05.placeholder.events;


/**
 * QRcodeScanner is an activity for scanning QR codes using the device's camera. It leverages the CodeScanner library
 * to decode QR codes and perform actions based on the decoded information. This class handles camera permission requests
 * and displays a dialog if the permission is denied. It provides feedback to the user via Toast messages upon successful
 * QR code scans.
 */
public class QRCodeScannerActivity extends AppCompatActivity{

    private CodeScanner mCodeScanner;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private QRCodeManager qrCodeManager = new QRCodeManager();

    PlaceholderApp app = (PlaceholderApp) getApplicationContext();
    Profile user = app.getUserProfile();


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
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) { // Result is a string of the info from decoded image
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() { // Here is when the scanner reads event id
                        String rawText = result.getText(); // raw text embedded in QR code
                        String eventID =  rawText.substring(0, 35); // Get the UUID as a string

                        app.getEventTable().fetchDocument(eventID, new Table.DocumentCallback<Event>() {
                            @Override
                            public void onSuccess(Event event){
                                // Do something with the fetched event here
                                if (qrCodeManager.checkQRcodeType(eventID)){
                                    viewEventInfo(event, app);
                                }
                                else {
                                    if (event.checkIn(user)) { // If user allowed to join the event
                                        user.joinEvent(event);
//                                    Toast.makeText(QRcodeScanner.this, "FALSE!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Exception e){
                                // Failed to get fetch the event for eventId with exception e
                            }
                        });


                    }
                });
            }
        });

        if (checkCurrentPermission()) {
            mCodeScanner.startPreview(); // Start the preview now
        }
        else {
            initializePermissionLauncher();
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }

    }

    /**
     * This method displays a fragment that shows the event info
     * @param event
     * @param app
     */
    private void viewEventInfo(Event event, PlaceholderApp app) {
        EventDetailsDialogFragment fragment =  EventDetailsDialogFragment.newInstance(event, app);
        fragment.show(getSupportFragmentManager(), "Show Event info");
    }


    /**
     * Checks if the app has been granted camera permission.
     *
     * @return true if the permission has been granted, false otherwise.
     */
    private boolean checkCurrentPermission() {
        // check if the user granted us permission from a previous session
        if (ContextCompat.checkSelfPermission(QRCodeScannerActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) { // Check if permission granted, if granted start camera!
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Initializes the requestPermissionLauncher used to request camera permission from the user. Defines the
     * behavior upon permission grant or denial.
     */
    private void initializePermissionLauncher(){
            // This method initializes and handles the logic of the permission launcher if we need to request permissions
            requestPermissionLauncher = registerForActivityResult( // Request launcher is being initialized
                    new ActivityResultContracts.RequestPermission(),
                    new ActivityResultCallback<Boolean> () {
                        @Override
                        public void onActivityResult(Boolean isGranted){
                            if (isGranted){
                                mCodeScanner.startPreview(); // starting camera

                            }
                            else{
                                showPermissionDeniedDialog();
                            }
                        }
                    });
        }


    /**
     * Shows a dialog informing the user that camera permission has been denied and the feature requires this permission.
     * The dialog provides an "OK" button to dismiss it.
     */
    private void showPermissionDeniedDialog() { // Shows the permission denied pop-up
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Denied");
        builder.setMessage("This feature requires camera permission. Please enable it in the app settings.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // You can add additional logic here if needed
                dialogInterface.dismiss();

            }
        });
        builder.show();
        //        finish();
    }

}
