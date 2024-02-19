package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import ca.cmput301t05.placeholder.database.DeviceIDManager;

public class InitialSetupActivity extends AppCompatActivity {
    private EditText nameEdit;
    private FloatingActionButton submitButton;
    private DeviceIDManager idManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_prompt);

        idManager = new DeviceIDManager(this);

        nameEdit = findViewById(R.id.intro_name_edit);
        submitButton = findViewById(R.id.intro_submit_button);

        submitButton.setOnClickListener(this::submitName);
    }

    private void submitName(View view) {
        String name = nameEdit.getText().toString();
        if(!name.isEmpty()){
          String deviceId = String.valueOf(idManager.getDeviceID());
            // Upload to Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> user = new HashMap<>();
            user.put("name", name);
            user.put("deviceId", deviceId);

            db.collection("profiles").document(deviceId)
                    .set(user)
                    .addOnSuccessListener(aVoid -> {
                        // Transition to MainActivity
                        Intent intent = new Intent(InitialSetupActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(Throwable::printStackTrace);
        }
    }
}
