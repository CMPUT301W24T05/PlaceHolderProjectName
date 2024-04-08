package ca.cmput301t05.placeholder.notifications;

import com.google.firebase.messaging.FirebaseMessaging;
/**
 * Manages the sending and receiving of push notifications using Firebase Cloud Messaging.
 */
public class PushNotificationManager {

    //TODO still need to do this, but it'll take a long time

    /**
     * The singleton instance of the PushNotificationManager.
     */
    private static PushNotificationManager instance;
    /**
     * The FirebaseMessaging instance used for sending and receiving push notifications.
     */
    public FirebaseMessaging fbm = FirebaseMessaging.getInstance();
    /**
     * Constructs a new PushNotificationManager instance.
     * This constructor is private to enforce the singleton pattern.
     */
    public PushNotificationManager() {}



}
