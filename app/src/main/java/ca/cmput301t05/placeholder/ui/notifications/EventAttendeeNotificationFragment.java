package ca.cmput301t05.placeholder.ui.notifications;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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
/**
 * A bottom sheet dialog fragment for displaying event attendee notifications.
 * This fragment displays a list of notifications related to event attendees.
 */
public class EventAttendeeNotificationFragment extends BottomSheetDialogFragment {

    private int heightInDp;

    public EventAttendeeNotificationFragment(int heightInDp){
        this.heightInDp = heightInDp;
    }
    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
    /**
     * Called to create the dialog shown by this fragment.
     * @param savedInstanceState The last saved instance state of the Fragment, or null if this is a freshly created Fragment.
     * @return The Dialog to be displayed.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog1;
            View bottomSheetInternal = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            if(bottomSheetInternal != null) {
                ViewGroup.LayoutParams layoutParams = bottomSheetInternal.getLayoutParams();

                layoutParams.height = heightInDp;
                bottomSheetInternal.setLayoutParams(layoutParams);
            }
        });

        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameText = view.findViewById(R.id.recycler_page_name_text);
        back = view.findViewById(R.id.recycler_page_back);
        recyclerView = view.findViewById(R.id.recycler_page_list);
        context = getContext();

        app = (PlaceholderApp) getContext().getApplicationContext();

        nameText.setText("Notifications");

        back.setOnClickListener(v -> {

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
        });

        notifications = new ArrayList<>();
        eventNotificationAdapter = new EventNotificationAdapter(context, notifications, EventAdapterType.ATTENDEE);
        recyclerView.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        recyclerView.setAdapter(eventNotificationAdapter);
    }
}