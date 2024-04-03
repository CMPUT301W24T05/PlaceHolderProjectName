package ca.cmput301t05.placeholder.ui.notifications;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.notifications.Notification;
import ca.cmput301t05.placeholder.notifications.NotificationAdapter;

import java.util.ArrayList;


/**
 * User notification activity, grabs from the app's preloaded notifications and displays them on the notifications screen
 * Updates the notifications from database every time you open.
 * TODO: Create a seperate Notification Adapter for User Notifications so we can display which event sent the notification and guide the user to it
 *
 */
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


        if(app.getUserProfile().getNotifications() == null){
            app.getUserProfile().setNotifications(new ArrayList<>());
        }

        //first grab the pre-loaded notifications

        notificationAdapter = new NotificationAdapter(this, app.getUserNotifications());
        notificationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notificationList.setAdapter(notificationAdapter);

        //now query for new notifications
        app.getNotificationTable().fetchMultipleDocuments(app.getUserProfile().getNotifications(), new Table.DocumentCallback<ArrayList<Notification>>() {
            @Override
            public void onSuccess(ArrayList<Notification> document) {
                app.getUserNotifications().clear();
                app.getUserNotifications().addAll(document);
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }

}
