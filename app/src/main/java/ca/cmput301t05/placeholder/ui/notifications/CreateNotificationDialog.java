package ca.cmput301t05.placeholder.ui.notifications;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.notifications.Notification;
/**
 * Dialog fragment for creating and sending notifications.
 */
public class CreateNotificationDialog extends DialogFragment {


    public interface NotificationListener{

        void onNotificationCreated(Notification notification, Boolean push);
    }

    private EditText notificationMessageEditText;
    private CheckBox sendPushCheckbox, pinCheckbox;

    private PlaceholderApp app;

    private NotificationListener notificationListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        app = (PlaceholderApp) getContext().getApplicationContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.event_create_notification_dialog, null);

        notificationMessageEditText = view.findViewById(R.id.create_event_notification_dialog_messageEditText);
        sendPushCheckbox = view.findViewById(R.id.create_event_notification_dialog_checkbox);
        pinCheckbox = view.findViewById(R.id.create_event_notification_dialog_checkbox_pin);

        builder.setView(view)
                .setTitle("Send Notification")
                .setPositiveButton("Send", (dialog, id) -> {
                    // Handle "Create" button click
                    // Extract and use your EditText and CheckBox values here

                    String notificationMessage = notificationMessageEditText.getText().toString();
                    notificationMessage = notificationMessage.trim();

                    if (notificationMessage.isEmpty()){
                        notificationMessageEditText.setError("Please Enter a Notification Message");
                    }   else {

                        boolean sendPush = sendPushCheckbox.isChecked();



                        Notification newNotification = new Notification(notificationMessage,app.getUserProfile().getProfileID(), app.getCachedEvent().getEventID());
                        newNotification.setPinned(pinCheckbox.isChecked());
                        newNotification.setPush(sendPush);

                        notificationListener.onNotificationCreated(newNotification, sendPush); //lets us send the notification back

                        dialog.dismiss();

                    }

                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    // User cancelled the dialog
                    dialog.dismiss();
                });


        return builder.create();

    }

    /**
     * basically makes sure that we implementing our dialog
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            notificationListener = (NotificationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NotificationListener");
        }
    }
}
