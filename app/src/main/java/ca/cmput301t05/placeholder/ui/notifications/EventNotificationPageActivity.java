package ca.cmput301t05.placeholder.ui.notifications;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.Notification;
import ca.cmput301t05.placeholder.notifications.NotificationAdapter;
import ca.cmput301t05.placeholder.profile.Profile;

public class EventNotificationPageActivity extends AppCompatActivity implements CreateNotificationDialog.NotificationListener {

    @Override
    public void onNotificationCreated(Notification notification, Boolean push) {
        // we get the notification from the dialog
        notifications.add(notification);



        notifications.sort(new Comparator<Notification>() {
            @Override
            public int compare(Notification o1, Notification o2) {
                // Check if either or both notifications are pinned
                if (o1.isPinned() && !o2.isPinned()) {
                    return -1; // o1 comes before o2
                } else if (!o1.isPinned() && o2.isPinned()) {
                    return 1; // o2 comes before o1
                } else {
                    // If both have the same pinned status, compare by time
                    return o1.getTimeCreated().compareTo(o2.getTimeCreated());
                }
            }
        });


        notificationAdapter.notifyDataSetChanged();

        //send the notification to the database
        app.getNotificationTable().pushDocument(notification, notification.getNotificationID().toString(), new Table.DocumentCallback<Notification>() {
            @Override
            public void onSuccess(Notification document) {
                curEvent.addNotification(document.getNotificationID().toString()); //add this to the event

                //update the event to have the notification

                app.getEventTable().pushDocument(curEvent, curEvent.getEventID().toString(), new Table.DocumentCallback<Event>() {
                    @Override
                    public void onSuccess(Event document) {

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

        //get all profiles inside of the event then give the notification to them, if it is a push notification we can get their firebase noti id and send


        if (!curEvent.getAttendees().isEmpty()) {

            app.getProfileTable().fetchMultipleDocuments(curEvent.getAttendees(), new Table.DocumentCallback<ArrayList<Profile>>() {
                @Override
                public void onSuccess(ArrayList<Profile> document) {

                    ArrayList<String> profileIDS = new ArrayList<>(); //getting ready to upload everything again

                    for (Profile p : document) {
                        p.addNotification(notification); //add notification to each profile

                        profileIDS.add(p.getProfileID().toString());

                        if (push) {
                            //do push notification things here
                        }

                    }

                    //now upload profiles back

                    app.getProfileTable().pushMultipleDocuments(document, profileIDS, new Table.DocumentCallback<ArrayList<Profile>>() {
                        @Override
                        public void onSuccess(ArrayList<Profile> document) {

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

        }

    }

    private PlaceholderApp app;

    private Event curEvent;

    private Button back, create_notification, refresh;

    private RecyclerView notificationList;

    private ArrayList<Notification> notifications;

    private NotificationAdapter notificationAdapter;



    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.event_notification_page);

        app = (PlaceholderApp) getApplicationContext();

        back = findViewById(R.id.event_notification_page_back);
        create_notification = findViewById(R.id.event_notification_page_create_notification);

        refresh = findViewById(R.id.refresh_noti);

        //ASSUMING THAT OUR EVENT IS IN THE CACHEDEVENTS
        curEvent = app.getCachedEvent();

        notifications = new ArrayList<>();

        notificationList = findViewById(R.id.event_notification_page_recyclerview);
        notificationAdapter = new NotificationAdapter(getApplicationContext(), notifications);
        notificationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notificationList.setAdapter(notificationAdapter);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("noti", String.valueOf(notifications.size()));
                Log.d("noti", String.valueOf(curEvent.getNotifications().size()));

                notifications.add(new Notification("Test Notification", app.getUserProfile().getProfileID(), curEvent.getEventID()));
                notificationAdapter.notifyDataSetChanged();
            }
        });

        //grab notifications is event isnt empty
        if (!curEvent.getNotifications().isEmpty()) {

            app.getNotificationTable().fetchMultipleDocuments(curEvent.getNotifications(), new Table.DocumentCallback<ArrayList<Notification>>() {
                @Override
                public void onSuccess(ArrayList<Notification> document) {
                    //add all of our notifications
                    notifications.addAll(document);

                    notifications.sort(new Comparator<Notification>() {
                        @Override
                        public int compare(Notification o1, Notification o2) {
                            // Check if either or both notifications are pinned
                            if (o1.isPinned() && !o2.isPinned()) {
                                return -1; // o1 comes before o2
                            } else if (!o1.isPinned() && o2.isPinned()) {
                                return 1; // o2 comes before o1
                            } else {
                                // If both have the same pinned status, compare by time
                                return o1.getTimeCreated().compareTo(o2.getTimeCreated());
                            }
                        }
                    });

                    notificationAdapter.notifyDataSetChanged(); //this error is fine since we're basically loading everything
                }

                @Override
                public void onFailure(Exception e) {

                }
            });

        }

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

                CreateNotificationDialog dialog = new CreateNotificationDialog();
                dialog.show(getSupportFragmentManager(), "CreateNotificationDialog");


            }
        });

    }
}
