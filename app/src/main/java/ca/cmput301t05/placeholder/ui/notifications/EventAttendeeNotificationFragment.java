package ca.cmput301t05.placeholder.ui.notifications;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.notifications.EventAdapterType;
import ca.cmput301t05.placeholder.notifications.EventNotificationAdapter;
import ca.cmput301t05.placeholder.notifications.Notification;

public class EventAttendeeNotificationFragment extends DialogFragment {

    public EventAttendeeNotificationFragment(){

        super(R.layout.recycler_with_back);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        return inflater.inflate(R.layout.recycler_with_back, container, false);
    }

    private PlaceholderApp app;

    private Context context;

    private Event curEvent;

    private ImageView back;

    private TextView nameText;

    private RecyclerView recyclerView;

    private ArrayList<Notification> notifications;

    private EventNotificationAdapter eventNotificationAdapter;

    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        curEvent = app.getCachedEvent();

        //Disable the dialog to be dismissed when touched outside
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameText = view.findViewById(R.id.recycler_page_name_text);
        back = view.findViewById(R.id.recycler_page_back);
        recyclerView = view.findViewById(R.id.recycler_page_list);
        context = getContext();

        app = (PlaceholderApp) getContext().getApplicationContext();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //update the notifications to show they're read
                for (Notification notification : app.getUserNotifications()){

                    app.getNotificationTable().pushDocument(notification, notification.getNotificationID().toString(), new Table.DocumentCallback<Notification>() {
                        @Override
                        public void onSuccess(Notification document) {

                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });

                }

                dismiss();
            }
        });

        notifications = new ArrayList<>();
        eventNotificationAdapter = new EventNotificationAdapter(context, notifications, EventAdapterType.ATTENDEE);
        recyclerView.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        recyclerView.setAdapter(eventNotificationAdapter);

        
    }
}
