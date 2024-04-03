package ca.cmput301t05.placeholder.utils;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageViewHelper {

    public static void cropPosterToImage(ImageView poster) {
        poster.post(() -> {
            // Get the Drawable's dimensions
            Drawable drawable = poster.getDrawable();
            int imageHeight = drawable.getIntrinsicHeight();
            int imageWidth = drawable.getIntrinsicWidth();

            // Calculate the aspect ratio
            float aspectRatio = (float) imageWidth / (float) imageHeight;

            // Assuming you have a fixed maximum height
            int imageViewHeight = poster.getHeight(); // or a specific value in pixels
            int imageViewWidth = Math.round(imageViewHeight * aspectRatio);

            // Set the ImageView's dimensions
            ViewGroup.LayoutParams params = poster.getLayoutParams();
            params.width = imageViewWidth;
            params.height = imageViewHeight; // You can keep this as is if it's already constrained
            poster.setLayoutParams(params);
        });
    }
}
