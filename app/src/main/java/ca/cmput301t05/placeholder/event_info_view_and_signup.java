package ca.cmput301t05.placeholder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import ca.cmput301t05.placeholder.events.Event;

/**
 * A DialogFragment that presents detailed information about an event and provides an interface for user signup.
 * This fragment displays the event's name, description, poster, and organizer details. Users can sign up for the event
 * or close the dialog to go back to the previous screen.
 */
public class event_info_view_and_signup extends DialogFragment {

    private Button signup_button;
    private Event event;

    /**
     * Called when the fragment is first attached to its context. {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context The context to which the fragment is being attached.
     */
    
    @Override
    public void onAttach(@Nullable Context context) {
        super.onAttach(context);
    }

    //To do:
    // when scanning the QR code, implement the functionality in scanning to get the event object and pass
    // the event object when creating the fragment using the fragment object and call this object
    static event_info_view_and_signup newInstance(Event event, PlaceholderApp app) {
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        args.putSerializable("app", app);
        event_info_view_and_signup fragment = new event_info_view_and_signup();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates a new instance of event_info_view_and_signup DialogFragment with the specified Event and PlaceholderApp objects.
     * This method packages the Event and PlaceholderApp objects into a Bundle to be used when creating the fragment.
     *
     * @param event The Event object containing details about the event.
     * @param app   The PlaceholderApp object for accessing application-wide resources and databases.
     * @return A new instance of event_info_view_and_signup DialogFragment with the event and app data.
     */
    @NotNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.event_infor_view_and_signup, null);
        //PlaceholderApp app = (PlaceholderApp)getApplicationContext();
        // check whether there is Null passed into the fragment
        TextView event_name = view.findViewById(R.id.event_name_textview);
        ImageView event_poster = view.findViewById(R.id.event_poster_view);
        TextView event_description = view.findViewById(R.id.Event_description_view);
        TextView organizer = view.findViewById(R.id.organizer_view);
        Button signup = view.findViewById(R.id.signup_button);
        Button back = view.findViewById(R.id.back_button);

        // get the Image table so that we can set the poster view
        Serializable appSerial = getArguments().getSerializable("app");
        PlaceholderApp app = (PlaceholderApp) appSerial;

        if (getArguments() != null) {
            Serializable eventSerial = getArguments().getSerializable("event");
            if (eventSerial != null) {
                event = (Event) eventSerial;
                event_name.setText(event.getEventName());
                if (event.getEventPosterID() != null) {
                    app.getPosterImageHandler().getPosterPicture(event, event_poster);
                }
                event_description.setText(event.getEventInfo());
                // To do:
                //organizer.setText(event.);
            }
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign up to the event
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog = builder.setView(view).create();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }
}
