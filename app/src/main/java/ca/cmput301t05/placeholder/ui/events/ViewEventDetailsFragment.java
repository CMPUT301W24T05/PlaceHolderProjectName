package ca.cmput301t05.placeholder.ui.events;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.utils.ImageViewHelper;
import com.google.android.material.imageview.ShapeableImageView;

import static ca.cmput301t05.placeholder.profile.ProfileImageGenerator.getCircularBitmap;

public class ViewEventDetailsFragment extends Fragment {
    private static final String LOG_TAG = "EventDetailsDialogFragment";

    private TextView eventTitleView;
    private TextView eventDateView;
    private TextView eventLocationView;
    private TextView eventInterestCountView;
    private TextView eventOrganizerView;
    private TextView eventDescriptionView;
    private ShapeableImageView eventPosterImage;
    private ImageView eventOrganizerProfileImage;

    private PlaceholderApp app;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_vieweventdetails, container, false);

        app = (PlaceholderApp) getActivity().getApplicationContext();
        Event displayEvent = app.getCachedEvent();

        initTextViews(view);
        updateEventDetails(displayEvent);
        updateEventPoster(displayEvent);

        return view;
    }

    private void initTextViews(View view) {
        eventTitleView = view.findViewById(R.id.event_view_title);
        eventDateView = view.findViewById(R.id.event_view_date);
        eventLocationView = view.findViewById(R.id.event_view_location);
        eventPosterImage = view.findViewById(R.id.event_view_poster);
        eventInterestCountView = view.findViewById(R.id.event_view_interest_count);
        eventOrganizerView = view.findViewById(R.id.event_view_creator);
        eventDescriptionView = view.findViewById(R.id.event_view_description);
        eventOrganizerProfileImage = view.findViewById(R.id.event_view_creator_image);
    }

    private void updateEventDetails(Event displayEvent) {
        eventTitleView.setText(displayEvent.getEventName());
        formatAndDisplayDate(displayEvent);
        eventLocationView.setText(displayEvent.getLocation());

        eventInterestCountView.setText(String.format(Locale.CANADA, "%d people are interested", displayEvent.getRegisteredUsers().size()));
        retrieveEventOrganizerName(displayEvent.getEventCreator());
        eventDescriptionView.setText(displayEvent.getEventInfo());
    }

    private void formatAndDisplayDate(Event displayEvent) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm a", Locale.CANADA);
        String dateTime = dateFormat.format(displayEvent.getEventDate().getTime());
        eventDateView.setText(dateTime);
    }

    private void retrieveEventOrganizerName(UUID profileID) {
        app.getProfileTable().fetchDocument(profileID.toString(),
                new Table.DocumentCallback<Profile>() {
                    @Override
                    public void onSuccess(Profile document) {
                        eventOrganizerView.setText(String.format("Hosted by %s", document.getName()));
                        getOrganizerProfileImage(document);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // Add error handling logic here if necessary
                    }
                }
        );
    }

    private void getOrganizerProfileImage(Profile organizerProfile) {
        if (organizerProfile.hasProfileBitmap()) {
            eventOrganizerProfileImage.setImageBitmap(getCircularBitmap(organizerProfile.getProfilePictureBitmap()));
        } else {
            app.getProfileImageHandler().getProfilePicture(organizerProfile, getActivity(), new BaseImageHandler.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    eventOrganizerProfileImage.setImageBitmap(getCircularBitmap(bitmap));
                }

                @Override
                public void onError(Exception e) {
                    organizerProfile.setProfilePictureToDefault();
                    eventOrganizerProfileImage.setImageBitmap(getCircularBitmap(organizerProfile.getProfilePictureBitmap()));
                }
            });
        }
    }

    private void updateEventPoster(Event displayEvent) {
        if (displayEvent.hasEventPosterBitmap()) {
            eventPosterImage.setImageBitmap(displayEvent.getEventPosterBitmap());
            ImageViewHelper.cropPosterToImage(eventPosterImage);
        } else {
            retrieveAndSetPosterImage(displayEvent);
        }
    }

    private void retrieveAndSetPosterImage(Event displayEvent) {
        app.getPosterImageHandler().getPosterPicture(displayEvent, getContext(),
                new BaseImageHandler.ImageCallback() {
                    @Override
                    public void onImageLoaded(Bitmap bitmap) {
                        eventPosterImage.setImageBitmap(bitmap);
                        ImageViewHelper.cropPosterToImage(eventPosterImage);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(LOG_TAG, "Error loading image: " + e.getMessage());
                    }
                }
        );
    }
}