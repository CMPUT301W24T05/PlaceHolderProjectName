package ca.cmput301t05.placeholder.database.firebaseMessaging.notificationHandler;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import ca.cmput301t05.placeholder.notifications.Notification;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HtppNotificationHandler {

    /**
     * Uses okHtpp to send notifications to a google cloud function which allows us to send notifications through firebase Messaging
     * @param notification
     */
    public static void sendNotificationToServer(Notification notification){

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

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Sending_Notification", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //potentially add some error stuff here
            }
        });



    }
}
