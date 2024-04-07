package ca.cmput301t05.placeholder.ui.notifications;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.Comparator;


import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.firebaseMessaging.notificationHandler.HttpNotificationHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.Notification;
import ca.cmput301t05.placeholder.notifications.EventNotificationAdapter;

/**
 * This is the create Notification page for events.
 * Events can now create notifications, which are then added to every single profile who is inside of the event.
 * Push notifications aren't added yet since they are a pain and require a lot of set up
 *
 * TODO: Add functionality to maybe delete / pin events with the 3 dots. In Adapter
 *
 * NOTE MAKE SURE TO PASS AN EVENT THROUGH CACHED EVENT IN APP.SETCACHEDEVENT() TO ACCESS THIS PAGE OR IT WILL BREAK
 */

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
                    return o2.getTimeCreated().compareTo(o1.getTimeCreated());
                }
            }
        });


        eventNotificationAdapter.notifyDataSetChanged();

        //send the notification to the database
        app.getNotificationTable().pushDocument(notification, notification.getNotificationID().toString(), new Table.DocumentCallback<Notification>() {
            @Override
            public void onSuccess(Notification document) {
                curEvent.addNotification(document.getNotificationID().toString()); //add this to the event


                //update the event to have the notification

                app.getEventTable().pushDocument(curEvent, curEvent.getEventID().toString(), new Table.DocumentCallback<Event>() {
                    @Override
                    public void onSuccess(Event document) {

                        //FIREBASE NOTIFICATION HANDLER SENT TO USER
                        HttpNotificationHandler.sendNotificationTopicToServer(notification);

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


        //FIREBASE NOTIFICATION HANDLER
        HttpNotificationHandler.sendNotificationTopicToServer(notification, new HttpNotificationHandler.httpHandlercallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });







    }

    private PlaceholderApp app;

    private Event curEvent;

    private Button back, create_notification, refresh;

    private RecyclerView notificationList;

    private ArrayList<Notification> notifications;

    private EventNotificationAdapter eventNotificationAdapter;





    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.event_notification_page);

        app = (PlaceholderApp) getApplicationContext();

        back = findViewById(R.id.event_notification_page_back);
        create_notification = findViewById(R.id.event_notification_page_create_notification);

        refresh = findViewById(R.id.refresh_noti);

        //ASSUMING THAT OUR EVENT IS IN THE CACHEDEVENTS
        curEvent = app.getCachedEvent();
        Log.d("NOTIFICATION", String.valueOf(curEvent.getNotifications().size()));

        notifications = new ArrayList<>();


        notificationList = findViewById(R.id.event_notification_page_recyclerview);
        eventNotificationAdapter = new EventNotificationAdapter(this, notifications);
        notificationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notificationList.setAdapter(eventNotificationAdapter);





        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO maybe make is so that when you swipe up it changes

                refresh();
            }
        });

        //grab notifications if event isnt empty
        if (!curEvent.getNotifications().isEmpty()) {

            app.getNotificationTable().fetchMultipleDocuments(curEvent.getNotifications(), new Table.DocumentCallback<ArrayList<Notification>>() {
                @Override
                public void onSuccess(ArrayList<Notification> document) {
                    //add all of our notifications
                    notifications.addAll(document);

                    Log.d("GETNOTIFROMTABLE", "SUCESS");

                    Log.d("DOCSIZE",String.valueOf(document.size()));
                    Log.d("EVENTSIZE", String.valueOf(curEvent.getNotifications().size()));

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
                                return o2.getTimeCreated().compareTo(o1.getTimeCreated());
                            }
                        }
                    });

                    eventNotificationAdapter.notifyDataSetChanged(); //this error is fine since we're basically loading everything
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

        refresh();
    }

    public void refresh(){
        app.getEventTable().fetchDocument(curEvent.getEventID().toString(), new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event document) {
                app.getNotificationTable().fetchMultipleDocuments(document.getNotifications(), new Table.DocumentCallback<ArrayList<Notification>>() {
                    @Override
                    public void onSuccess(ArrayList<Notification> document) {
                        notifications.clear();
                        notifications.addAll(document);
                        eventNotificationAdapter.notifyDataSetChanged();
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
