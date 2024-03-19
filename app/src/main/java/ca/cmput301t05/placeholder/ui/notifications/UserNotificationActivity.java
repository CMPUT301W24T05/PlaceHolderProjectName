package ca.cmput301t05.placeholder.ui.notifications;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.UUID;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.notifications.Notification;
import ca.cmput301t05.placeholder.notifications.NotificationAdapter;

public class UserNotificationActivity extends AppCompatActivity {

    private PlaceholderApp app;

    private RecyclerView notificationList;

    private ArrayList<Notification> notifications;

    private Button back;

    private NotificationAdapter notificationAdapter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_notification_list);

        app = (PlaceholderApp) getApplicationContext();
        back = findViewById(R.id.user_notification_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        notificationList = findViewById(R.id.user_notification_recyclerview);
        notifications = new ArrayList<>();


        if(app.getUserProfile().getNotifications() == null){
            app.getUserProfile().setNotifications(new ArrayList<>());
        }

        notifications.addAll(app.getUserNotifications());
        notificationAdapter = new NotificationAdapter(this, notifications);
        notificationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notificationList.setAdapter(notificationAdapter);





    }
}
