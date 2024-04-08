package ca.cmput301t05.placeholder.ui.events;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;
import ca.cmput301t05.placeholder.ui.notifications.EventAttendeeNotificationFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import static ca.cmput301t05.placeholder.profile.ProfileImageGenerator.getCircularBitmap;

public class ViewEventDetailsFragment extends BottomSheetDialogFragment {
    private static final String LOG_TAG = "EventDetailsDialogFragment";

    private TextView eventTitleView;
    private TextView eventDateView;
    private TextView eventLocationView;
    private TextView eventInterestCountView;
    private TextView eventOrganizerView;
    private TextView eventDescriptionView;
    private ShapeableImageView eventPosterImage;
    private ImageView eventOrganizerProfileImage;
    private ExtendedFloatingActionButton markInterestedButton;
    private ImageButton notificationButton;

    private PlaceholderApp app;
    private boolean interestedMode;

    private int bottomSheetHeightInDp; // Add this line to store bottom sheet height in dp

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_vieweventdetails, container, false);

        Bundle arguments = getArguments();
        if(arguments != null) {
            interestedMode = arguments.getBoolean("interestedMode", false);
        }

        app = (PlaceholderApp) getActivity().getApplicationContext();
        Event displayEvent = app.getCachedEvent();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeightInDp = Math.round(displayMetrics.heightPixels / displayMetrics.density);
        bottomSheetHeightInDp = screenHeightInDp * 2;
//        bottomSheetHeightInDp = view.getHeight(); // Calculate bottom sheet height in dp

        initTextViews(view);
        updateEventDetails(displayEvent);
        updateEventPoster(displayEvent);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View)view.getParent());
        // Set the peek height (minimum height when closed)
        bottomSheetBehavior.setPeekHeight(2000, true);
        // Set the max height when fully expanded
        bottomSheetBehavior.setExpandedOffset(0);
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
        markInterestedButton = view.findViewById(R.id.event_view_mark_interested);
        notificationButton = view.findViewById(R.id.notificationButton);

        if(interestedMode){
            notificationButton.setVisibility(View.GONE);
        } else {
            notificationButton.setVisibility(View.VISIBLE);
            notificationButton.setOnClickListener(v -> {
                // Pass the bottom sheet height to the constructor
                EventAttendeeNotificationFragment bottomSheet = new EventAttendeeNotificationFragment(bottomSheetHeightInDp);
                bottomSheet.show(getChildFragmentManager(), "AttendeeNotificationBottomSheet");
            });
        }
    }

    private void updateEventDetails(Event displayEvent) {
        eventTitleView.setText(displayEvent.getEventName());
        formatAndDisplayDate(displayEvent);
        eventLocationView.setText(displayEvent.getLocation());

        eventInterestCountView.setText(String.format(Locale.CANADA, "%d people are interested", displayEvent.getRegisteredUsers().size()));
        retrieveEventOrganizerName(displayEvent.getEventCreator());
        eventDescriptionView.setText(displayEvent.getEventInfo());

        if(interestedMode){
            markInterestedButton.setOnClickListener(v -> handleMarkInterestedAction(v, displayEvent));
            markInterestedButton.setVisibility(View.VISIBLE);
            if (displayEvent.userHasSignedUp(app.getUserProfile())) {
                markInterestedButton.setText("I'm no longer interested");
                markInterestedButton.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.close_icon,
                        getActivity().getTheme()));
            } else {
                markInterestedButton.setText("I'm interested");
                markInterestedButton.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.check_icon,
                        getActivity().getTheme()));
            }
        }
    }

    private void handleMarkInterestedAction(View v, Event displayEvent) {
        boolean signingUp = !displayEvent.userHasSignedUp(app.getUserProfile());
        updateInterestedState(signingUp, displayEvent);

        app.getEventTable().updateDocument(displayEvent, displayEvent.getEventID().toString(), new Table.DocumentCallback<Event>() {
            @Override
            public void onSuccess(Event document) {
                app.getProfileTable().updateDocument(app.getUserProfile(), app.getUserProfile().getProfileID().toString(), new Table.DocumentCallback<Profile>() {
                    @Override
                    public void onSuccess(Profile document) {
                        updateInterestedButtonView(signingUp);
                        eventInterestCountView.setText(String.format(Locale.CANADA, "%d people are interested", displayEvent.getRegisteredUsers().size()));
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // Handle profile update failure
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                // Handle event update failure
            }
        });
    }

    private void updateInterestedState(boolean signingUp, Event displayEvent) {
        if (signingUp) {
            app.getUserProfile().addInterestedEvent(displayEvent);
            app.getInterestedEvents().put(displayEvent.getEventID(), displayEvent);
            displayEvent.userSignup(app.getUserProfile());
        } else {
            app.getUserProfile().removeInterestedEvent(displayEvent);
            app.getInterestedEvents().remove(displayEvent.getEventID());
            displayEvent.userUnsignup(app.getUserProfile());
        }
    }

    private void updateInterestedButtonView(boolean signingUp) {
        if (signingUp) {
            markInterestedButton.setText("I'm no longer interested");
            markInterestedButton.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.close_icon,
                    getActivity().getTheme()));
        } else {
            markInterestedButton.setText("I'm interested");
            markInterestedButton.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.check_icon,
                    getActivity().getTheme()));
        }
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
            ImageViewHelper.cropImageToAspectRatio(eventPosterImage);
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
                        ImageViewHelper.cropImageToAspectRatio(eventPosterImage);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(LOG_TAG, "Error loading image: " + e.getMessage());
                    }
                }
        );
    }
}