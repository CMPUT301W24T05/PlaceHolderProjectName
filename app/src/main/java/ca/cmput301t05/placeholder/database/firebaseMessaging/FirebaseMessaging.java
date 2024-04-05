package ca.cmput301t05.placeholder.database.firebaseMessaging;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.notifications.Notification;
import ca.cmput301t05.placeholder.profile.Profile;

public class FirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //TODO ON MESAGE PAYLOAD, GRAB JSON CONSTRUCT NOTIFICATION FROM JSON AND SEND NOTIFICATION TO PHONE THROUGH THAT
        //TODO ALSO UPDATE USER PROFILE BY ADDING THE NOTIFICATION TO THEIR PROFILE

        Map<String, String> messageData = remoteMessage.getData();

        Notification n = new Notification(messageData);

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
