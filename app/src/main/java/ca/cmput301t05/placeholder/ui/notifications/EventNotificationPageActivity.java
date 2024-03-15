package ca.cmput301t05.placeholder.ui.notifications;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.Notification;
import ca.cmput301t05.placeholder.notifications.NotificationAdapter;

public class EventNotificationPageActivity extends AppCompatActivity {

    private PlaceholderApp app;

    private Button back, create_notification;

    private RecyclerView notificationList;

    private NotificationAdapter notificationAdapter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.event_notification_page);

        app = (PlaceholderApp) getApplicationContext();

        back = findViewById(R.id.event_notification_page_back);
        create_notification = findViewById(R.id.event_notification_page_create_notification);

        //ASSUMING THAT OUR EVENT IS IN THE CACHEDEVENTS
        Event e = app.getCachedEvent();

        ArrayList<Notification> notifications = new ArrayList<>();

        notificationList = findViewById(R.id.event_notification_page_recyclerview);
        notificationAdapter = new NotificationAdapter(getApplicationContext(), notifications);
        notificationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notificationList.setAdapter(notificationAdapter);


        app.getNotificationTable().fetchMultipleDocuments(e.getNotifications(), new Table.DocumentCallback<ArrayList<Notification>>() {
            @Override
            public void onSuccess(ArrayList<Notification> document) {
                //add all of our notifications
                notifications.addAll(document);
                notificationAdapter.notifyDataSetChanged(); //this error is fine since we're basically loading everything
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        create_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //have it open a dialog which allows us to create a notification similar to how we do it in the figma



            }
        });





    }
}
