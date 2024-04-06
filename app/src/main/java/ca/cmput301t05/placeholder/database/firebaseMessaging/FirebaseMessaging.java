package ca.cmput301t05.placeholder.database.firebaseMessaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ca.cmput301t05.placeholder.MainActivity;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.notifications.Notification;
import ca.cmput301t05.placeholder.profile.Profile;

public class FirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        PlaceholderApp app = (PlaceholderApp) getApplicationContext();

        Map<String, String> messageData = remoteMessage.getData();

        Log.d("Remote_message",remoteMessage.getData().toString());

        Notification n = null;
        try {
            n = new Notification(messageData);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Log.d("noti", n.getTimeCreated().toString());



        //TODO handle the new notifications by adding it to profile's database and then sending push notification (If we need to)

        //upload notification to database

        app.getNotificationTable().pushDocument(n, n.getNotificationID().toString(), new Table.DocumentCallback<Notification>() {
            @Override
            public void onSuccess(Notification document) {
                app.getUserProfile().getNotifications().add(document.getNotificationID().toString());
                app.getUserNotifications().add(document);

                //now upload profile back

                app.getProfileTable().pushDocument(app.getUserProfile(), app.getUserProfile().getProfileID().toString(), new Table.DocumentCallback<Profile>() {
                    @Override
                    public void onSuccess(Profile document) {


                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        //check if its a push notification
        if (n.isPush()){

            //on notification click we open user notifications
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("redirect", "userNotifications");

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this , getString(R.string.channel_name))
                    .setSmallIcon(R.drawable.event_capacity_icon)
                    .setContentTitle("Announcement")
                    .setContentText(n.getMessage())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);


            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());
            Log.d("noti", "Showing push noti");

        }





        Log.d("noti", "Message data payload: " + remoteMessage.getData());


    }

    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(String token) {
        //runs whenever a new token is registered for the user
        PlaceholderApp app = (PlaceholderApp) getApplicationContext();

        if(!app.currentProfileExists()){
            return;
        }

        app.getUserProfile().setMessagingToken(token);

        app.getProfileTable().pushDocument(app.getUserProfile(), app.getUserProfile().getProfileID().toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile document) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }
}
