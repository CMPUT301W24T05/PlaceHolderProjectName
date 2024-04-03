package ca.cmput301t05.placeholder.ui.events;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

public class ViewEventDetailsActivity extends AppCompatActivity {
    private static final String LOG_TAG = "EventDetailsDialogFragment";
    private static final String AM = " AM";
    private static final String PM = " PM";

    private TextView eventTitleView;
    private TextView eventDateView;
    private TextView eventLocationView;
    private TextView eventInterestCountView;
    private TextView eventOrganizerView;
    private TextView eventDescriptionView;
    private ShapeableImageView eventPosterImage;
    private ImageView eventOrganizerProfileImage;

    private PlaceholderApp app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (PlaceholderApp) getApplicationContext();
        Event displayEvent = app.getCachedEvent();

        setContentView(R.layout.event_vieweventdetails);

        initTextViews();
        updateEventDetails(displayEvent);
        updateEventPoster(displayEvent);
    }

    private void initTextViews() {
        eventTitleView = findViewById(R.id.event_view_title);
        eventDateView = findViewById(R.id.event_view_date);
        eventLocationView = findViewById(R.id.event_view_location);
        eventPosterImage = findViewById(R.id.event_view_poster);
        eventInterestCountView = findViewById(R.id.event_view_interest_count);
        eventOrganizerView = findViewById(R.id.event_view_creator);
        eventDescriptionView = findViewById(R.id.event_view_description);
        eventOrganizerProfileImage = findViewById(R.id.event_view_creator_image);
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
            app.getProfileImageHandler().getProfilePicture(organizerProfile, getApplicationContext(), new BaseImageHandler.ImageCallback() {
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
        app.getPosterImageHandler().getPosterPicture(displayEvent, this,
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
