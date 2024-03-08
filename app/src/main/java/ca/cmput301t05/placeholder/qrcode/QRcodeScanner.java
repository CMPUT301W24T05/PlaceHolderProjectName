package ca.cmput301t05.placeholder.qrcode;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewbinding.ViewBinding;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.zxing.Result;


import android.Manifest;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import ca.cmput301t05.placeholder.MainActivity;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.databinding.CameraActivityBinding;
//import ca.cmput301t05.placeholder.events;



public class QRcodeScanner extends AppCompatActivity{

    private CodeScanner mCodeScanner;
    private ActivityResultLauncher<String> requestPermissionLauncher;

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
                        // get UUID from QR code
                        Toast.makeText(QRcodeScanner.this, result.getText(), Toast.LENGTH_SHORT).show();

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





    private boolean checkCurrentPermission() {
        // check if the user granted us permission from a previous session
        if (ContextCompat.checkSelfPermission(QRcodeScanner.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) { // Check if permission granted, if granted start camera!
            return true;
        }
        else{
            return false;
        }
    }
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
