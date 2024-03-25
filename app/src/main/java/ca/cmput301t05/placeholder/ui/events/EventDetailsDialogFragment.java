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
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.profile.Profile;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

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

    private void setupEventDetails() {
        if (event != null) {
            eventName.setText(event.getEventName());
            loadEventPoster();
            eventDescription.setText(event.getEventInfo());
            // TODO: organizer.setText(event.);
        }
    }

    private void loadEventPoster() {
        if (event.getEventPosterID() != null) {
            if(event.hasEventPosterBitmap()){
                // Preferred way
                eventPoster.setImageBitmap(event.getEventPosterBitmap());
            } else {
                // Load from remote server
                loadPosterFromServer();
            }
        }
    }

    private void loadPosterFromServer() {
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

    private AlertDialog setupDialog() {
        return new AlertDialog.Builder(getContext())
                .setView(getLayoutInflater().inflate(R.layout.event_infor_view_and_signup, null))
                .create();
    }

    private void setupButtonActions(AlertDialog dialog) {
        signupButton.setOnClickListener(v -> {
            List<String> interestedEvents = app.getUserProfile().getInterestedEvents();
            final boolean signingUp = !interestedEvents.contains(event.getEventID().toString());
            if (signingUp) {
                interestedEvents.add(event.getEventID().toString());
            } else {
                interestedEvents.remove(event.getEventID().toString());
            }

            app.getProfileTable().pushDocument(app.getUserProfile(), app.getUserProfile().getProfileID().toString(), new Table.DocumentCallback<Profile>() {
                @Override
                public void onSuccess(Profile document) {
                    if (signingUp) {
                        signupButton.setText("Un-Sign up");
                    } else {
                        signupButton.setText("Sign up");
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        });

        backButton.setOnClickListener(v -> dialog.dismiss());
    }
}
