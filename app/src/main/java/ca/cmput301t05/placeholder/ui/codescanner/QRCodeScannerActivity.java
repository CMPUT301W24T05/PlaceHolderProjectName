package ca.cmput301t05.placeholder.ui.codescanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;

import ca.cmput301t05.placeholder.qrcode.QRCodeManager;
import ca.cmput301t05.placeholder.qrcode.QRCodeType;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;

import com.google.zxing.Result;


import android.Manifest;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.ui.events.EventDetailsDialogFragment;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.ui.events.EventSignUpActivity;
import ca.cmput301t05.placeholder.ui.events.ViewEventDetailsActivity;
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

    private PlaceholderApp app;
    private Profile user;


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
        user = app.getUserProfile();



        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) { // Result is a string of the info from decoded image
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() { // Here is when the scanner reads event id

                        Log.d("QR", "Scanned");

                        String rawText = result.getText(); // raw text embedded in QR code

                        Log.d("QR_RAW", rawText);

                        QRCodeManager manager = new QRCodeManager();

                        //grab qrcode type
                        QRCodeType type = manager.checkQRcodeType(rawText);

                        //now grab event id
                        String qrEventID = manager.getEventID(rawText).toString();

                        app.getEventTable().fetchDocument(qrEventID, new Table.DocumentCallback<Event>() {
                            @Override
                            public void onSuccess(Event event){

                                Log.e("QR_SERVER", "Server Query Successful");

                                //check its type then go to the corresponding activity/fragment

                                if(type == QRCodeType.CHECK_IN){

                                    app.setCachedEvent(event); //sets the cached event so we can use it on the next page
                                    Intent intent = new Intent(QRCodeScannerActivity.this, ViewEventDetailsActivity.class);

                                    startActivity(intent);

                                    finish();


                                } else if (type == QRCodeType.INFO) {

                                    //need to go back to main activity and add to bundle so we can open a fragment
                                    Log.d("QR_CODE", "Type is Info, and we're in the if");
                                    app.setCachedEvent(event);

                                    Intent intent = new Intent(QRCodeScannerActivity.this, EventSignUpActivity.class);

                                    intent.putExtra("openFragment", true);

                                    startActivity(intent);

                                    finish();


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
