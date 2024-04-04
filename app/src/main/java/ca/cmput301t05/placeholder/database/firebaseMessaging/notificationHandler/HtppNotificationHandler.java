package ca.cmput301t05.placeholder.database.firebaseMessaging.notificationHandler;

import ca.cmput301t05.placeholder.notifications.Notification;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class HtppNotificationHandler {

    /**
     * Uses okHtpp to send notifications to a google cloud function which allows us to send notifications through firebase Messaging
     * @param message
     * @param notification
     */
    public static void sendNotificationToServer(String message, Notification notification){

        String url = "";
        

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");



    }
}
