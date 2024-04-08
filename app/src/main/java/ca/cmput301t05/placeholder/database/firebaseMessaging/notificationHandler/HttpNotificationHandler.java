package ca.cmput301t05.placeholder.database.firebaseMessaging.notificationHandler;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.database.DatabaseManager;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.notifications.Notification;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.ui.notifications.sendToUserNotification;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpNotificationHandler {

    public interface httpHandlercallback{

        void onSuccess();

        void onError(Exception e);

    }



    /**
     * Uses okHtpp to send notifications to a google cloud function which allows us to send notifications through firebase Messaging
     * sends it to a topic (Aka an event a user has registered for)
     * @param notification
     */
    public static void sendNotificationTopicToServer(Notification notification, httpHandlercallback callback){

        //google python function which handles sending notifications
        String url = "https://us-central1-silver-adapter-419318.cloudfunctions.net/Cmput301AppNotifications";

        Gson gson = new GsonBuilder().setPrettyPrinting()
                .serializeNulls()
                .create();

        String notiJson = gson.toJson(notification);

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(notiJson, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Log.d("Notification", "Sending to client");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Sending_Notification", e.getMessage());
                callback.onError(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //potentially add some error stuff here
                Log.d("Response", "Response from server");
                callback.onSuccess();
            }
        });



    }

    /**
     * Sends a notification to a specific user via an HTTP request to a Google Cloud Function.
     * @param notification The notification to be sent.
     * @param token The messaging token of the user.
     * @param callback The callback to handle HTTP request responses.
     */
    public static void sendNotificationToUser(Notification notification, String token, httpHandlercallback callback){

        //google python function
        String url = "https://us-central1-silver-adapter-419318.cloudfunctions.net/sendToUser";

        Gson gson = new GsonBuilder().setPrettyPrinting()
                .serializeNulls()
                .create();


        sendToUserNotification userNotification = new sendToUserNotification(notification, token);
        String newJson = gson.toJson(userNotification);


        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(newJson, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Sending_Notification", e.getMessage());
                callback.onError(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //potentially add some error stuff here
                callback.onSuccess();

            }
        });



    }
}
