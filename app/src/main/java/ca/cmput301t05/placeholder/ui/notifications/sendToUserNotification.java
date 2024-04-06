package ca.cmput301t05.placeholder.ui.notifications;

import com.google.firebase.firestore.DocumentSnapshot;

import ca.cmput301t05.placeholder.notifications.Notification;

public class sendToUserNotification extends Notification {

    private String token;
    public sendToUserNotification(Notification n, String token){
        super(n);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
