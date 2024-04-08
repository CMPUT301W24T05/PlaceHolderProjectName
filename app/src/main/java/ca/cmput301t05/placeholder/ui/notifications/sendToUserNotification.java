package ca.cmput301t05.placeholder.ui.notifications;

import com.google.firebase.firestore.DocumentSnapshot;

import ca.cmput301t05.placeholder.notifications.Notification;
/**
 * Extension of the Notification class to include a token for sending notifications to specific users.
 * This class allows for the inclusion of a token along with the notification data.
 */
public class sendToUserNotification extends Notification {
    /**
     * Constructor for SendToUserNotification class.
     * Initializes the notification with the provided data and includes a token.
     * @param n The original notification data to be included.
     * @param token The token associated with the user to whom the notification will be sent.
     */
    private String token;
    public sendToUserNotification(Notification n, String token){
        super(n);
        this.token = token;
    }
    /**
     * Setter method to update the token associated with the notification.
     * @param token The new token value to be set.
     */
    public void setToken(String token) {
        this.token = token;
    }
    /**
     * Getter method to retrieve the token associated with the notification.
     * @return The token associated with the notification.
     */
    public String getToken() {
        return token;
    }
}
