package ca.cmput301t05.placeholder.ui.notifications;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ca.cmput301t05.placeholder.databinding.FragmentNotificationsBinding;

//not a fragment but an activity now!!
public class NotificationsFragment extends AppCompatActivity {

    private FragmentNotificationsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //for NotificationViewModel
        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(this, textView::setText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
