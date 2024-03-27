package ca.cmput301t05.placeholder.ui.events.creation;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.atomic.AtomicReference;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.ui.events.GenerateInfoCheckinActivity;

/**
 * UploadPosterActivity allows users to upload a poster image for an event. This activity is part of the event
 * creation process where users can select an image from their device to represent the event. The activity handles
 * selecting and uploading the image to the database and links the image with the specified event.
 */
public class UploadPosterActivity extends BottomSheetDialogFragment {

    public interface OnPosterImageSelectedListener {
        void onImageSelected(Uri imageUri);
    }

    private OnPosterImageSelectedListener mListener;

    private ImageView eventPoster;
    private FloatingActionButton selectPosterButton;
    private ExtendedFloatingActionButton confirmPosterButton;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (OnPosterImageSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnPosterImageSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_uploadposter, container, false);

        eventPoster = view.findViewById(R.id.eventPosterImage);
        selectPosterButton = view.findViewById(R.id.select_poster_image);
        confirmPosterButton = view.findViewById(R.id.confirm_poster_image);

        // Get the arguments
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("imageUri")) {
            String imageUriString = arguments.getString("imageUri");
            Uri imageUri = Uri.parse(imageUriString);
            // Now you can display the image in the ImageView
            eventPoster.setImageURI(imageUri);
            cropPosterToImage();
            confirmPosterButton.setEnabled(true);
        }

        AtomicReference<Uri> curPic = new AtomicReference<>();

        setupActions(curPic, view);

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri == null) {
                Log.d("PhotoPicker", "No media selected");
                confirmPosterButton.setEnabled(false);
            } else {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                eventPoster.setImageURI(uri);
                cropPosterToImage();
                curPic.set(uri);
                confirmPosterButton.setEnabled(true);
            }
        });

        return view;
    }

    private void cropPosterToImage() {
        eventPoster.post(() -> {
            // Get the Drawable's dimensions
            Drawable drawable = eventPoster.getDrawable();
            int imageHeight = drawable.getIntrinsicHeight();
            int imageWidth = drawable.getIntrinsicWidth();

            // Calculate the aspect ratio
            float aspectRatio = (float) imageWidth / (float) imageHeight;

            // Assuming you have a fixed maximum height
            int imageViewHeight = eventPoster.getHeight(); // or a specific value in pixels
            int imageViewWidth = Math.round(imageViewHeight * aspectRatio);

            // Set the ImageView's dimensions
            ViewGroup.LayoutParams params = eventPoster.getLayoutParams();
            params.width = imageViewWidth;
            params.height = imageViewHeight; // You can keep this as is if it's already constrained
            eventPoster.setLayoutParams(params);
        });
    }

    private void imagePicked(Uri imageUri) {
        mListener.onImageSelected(imageUri);
        dismiss();
    }

    private void setupActions(AtomicReference<Uri> curPic, View view) {
        selectPosterButton.setOnClickListener(view12 -> {
            PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE) // For images only
                    .build();
            pickMedia.launch(request);
        });

        confirmPosterButton.setOnClickListener(view13 -> {
            imagePicked(curPic.get());
        });
    }
}
