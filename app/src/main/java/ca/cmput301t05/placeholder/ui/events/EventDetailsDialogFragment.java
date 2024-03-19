package ca.cmput301t05.placeholder.ui.events;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import ca.cmput301t05.placeholder.events.Event;

/**
 * A DialogFragment that presents detailed information about an event and provides an interface for user signup.
 * This fragment displays the event's name, description, poster, and organizer details. Users can sign up for the event
 * or close the dialog to go back to the previous screen.
 */
public class EventDetailsDialogFragment extends DialogFragment {

    private static final String EVENT_KEY = "event";
    private static final String APP_KEY = "app";

    private Event event;
    private PlaceholderApp app;
    private TextView eventName;
    private ImageView eventPoster;
    private TextView eventDescription;
    private Button backButton;
    private Button signupButton;

    //To do:
    // when scanning the QR code, implement the functionality in scanning to get the event object and pass
    // the event object when creating the fragment using the fragment object and call this object
    static public EventDetailsDialogFragment newInstance(Event event, PlaceholderApp app) {
        Bundle args = new Bundle();
        args.putSerializable(EVENT_KEY, event);
        args.putSerializable(APP_KEY, app);
        EventDetailsDialogFragment fragment = new EventDetailsDialogFragment();
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
        setupView(getLayoutInflater());
        extractArguments();
        setupEventDetails();
        AlertDialog dialog = setupDialog();
        setupButtonActions(dialog);
        return dialog;
    }

    private void setupView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.event_infor_view_and_signup, null);
        eventName = view.findViewById(R.id.event_name_textview);
        eventPoster = view.findViewById(R.id.event_poster_view);
        eventDescription = view.findViewById(R.id.Event_description_view);
        signupButton = view.findViewById(R.id.signup_button);
        backButton = view.findViewById(R.id.back_button);
    }

    private void extractArguments() {
        Serializable appSerialized = getArguments().getSerializable(APP_KEY);
        app = (PlaceholderApp) appSerialized;
        Serializable eventSerialized = getArguments().getSerializable(EVENT_KEY);
        event = (Event) eventSerialized;
    }

    private void setupEventDetails(){
        if (event != null) {
            eventName.setText(event.getEventName());
            if (event.getEventPosterID() != null) {
                if(event.hasEventPosterBitmap()){
                    eventPoster.setImageBitmap(event.getEventPosterBitmap());
                } else {
                    app.getPosterImageHandler().getPosterPicture(event, getContext(), new BaseImageHandler.ImageCallback() {
                        @Override
                        public void onImageLoaded(Bitmap bitmap) {
                            eventPoster.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onError(Exception e) {
                            // Handle error
                            Log.e("EventDetailsDialogFragment", "Error loading image: " + e.getMessage());
                        }
                    });
                }
            }
            eventDescription.setText(event.getEventInfo());
            // TODO: organizer.setText(event.);
        }
    }

    private AlertDialog setupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(getLayoutInflater().inflate(R.layout.event_infor_view_and_signup, null)).create();
    }

    private void setupButtonActions(AlertDialog dialog) {
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Sign up to the event
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
