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
import java.util.function.Consumer;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.qrcode.QRCode;
import ca.cmput301t05.placeholder.qrcode.QRCodeManager;
import ca.cmput301t05.placeholder.qrcode.QRCodeType;
import ca.cmput301t05.placeholder.ui.events.creation.PreviewEventActivity;

public class GenerateInfoCheckinActivity extends AppCompatActivity {
    private Button generateCheckin, generateInfo, next, back;
    private ImageView checkInQrCodeImageView, InfoQrCodeImageView;
    private String qrTypeToSave;
    private ActivityResultLauncher<Intent> createDocumentLauncher;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlaceholderApp app = (PlaceholderApp) getApplicationContext();
        Event currentEvent = app.getCachedEvent();
        setContentView(R.layout.event_generate_info_checkin);
        QRCodeManager qrCodeManager = new QRCodeManager();

        generateCheckin = findViewById(R.id.event_generate_genBtn1);
        generateInfo = findViewById(R.id.event_generate_generate2);
        next = findViewById(R.id.event_generate_qr_next);
        checkInQrCodeImageView = findViewById(R.id.event_generate_qrcode1);
        InfoQrCodeImageView = findViewById(R.id.event_generate_qr2);
        back = findViewById(R.id.event_generate_back);

        QRCode checkInQr = qrCodeManager.generateQRCode(currentEvent, "checkIn");
        QRCode infoQr = qrCodeManager.generateQRCode(currentEvent, "eventInfo");

        createDocumentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , result -> {
                    if (result.getData() != null)
                        qrToUri((qrTypeToSave.equals("checkIn")) ? checkInQr : infoQr, result.getData().getData());
                });

        setUpViewListeners(checkInQr, infoQr, currentEvent);
    }

    private void setUpViewListeners(QRCode checkInQr, QRCode infoQr, Event currentEvent) {
        back.setOnClickListener(v -> finish());
        next.setOnClickListener(v -> {
            startActivity(new Intent(GenerateInfoCheckinActivity.this, PreviewEventActivity.class));
            finish();
        });

        generateCheckin.setOnClickListener(v -> generateOrExportQRCode(generateCheckin, checkInQrCodeImageView, checkInQr, currentEvent::setCheckInQR, "checkIn"));
        generateInfo.setOnClickListener(v -> generateOrExportQRCode(generateInfo, InfoQrCodeImageView, infoQr, currentEvent::setInfoQRCode, "info"));
    }

    private void generateOrExportQRCode(Button generateButton, ImageView qrCodeImageView, QRCode qr, Consumer<String> saveMethod, String qrType) {
        if (generateButton.getText().equals("Generate")) {
            qrCodeImageView.setImageBitmap(qr.getBitmap());
            generateButton.setText("Export QR Code");
            saveMethod.accept(qr.getRawText());
        } else {
            exportQrCode(qrType);
        }
    }

    private void exportQrCode(String qrType) {
        qrTypeToSave = qrType;
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("JPEG");
        intent.putExtra(Intent.EXTRA_TITLE, qrType + "QRCode.jpeg");
        createDocumentLauncher.launch(intent);
    }

    private void qrToUri(QRCode qr, Uri uri) {
        try (OutputStream os = getContentResolver().openOutputStream(uri)) {
            if (os != null) {
                qr.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, os);
            }
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