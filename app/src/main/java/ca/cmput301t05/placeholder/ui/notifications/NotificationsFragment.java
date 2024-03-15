package ca.cmput301t05.placeholder.ui.notifications;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.UUID;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;

import ca.cmput301t05.placeholder.database.Table;
import ca.cmput301t05.placeholder.databinding.FragmentNotificationsBinding;
import ca.cmput301t05.placeholder.notifications.Notification;

public class NotificationsFragment extends AppCompatActivity {

    private FragmentNotificationsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
