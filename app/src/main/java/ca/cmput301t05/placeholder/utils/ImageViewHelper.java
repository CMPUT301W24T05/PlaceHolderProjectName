package ca.cmput301t05.placeholder.utils;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageViewHelper {

    public static void cropImageToAspectRatio(ImageView image) {
        image.post(() -> {

            // Get the Drawable's dimensions
            Drawable drawable = image.getDrawable();
            int imageHeight = drawable.getIntrinsicHeight();
            int imageWidth = drawable.getIntrinsicWidth();

            // Layout Params
            ViewGroup.LayoutParams params = image.getLayoutParams();

            // Check if the image is vertical or horizontal
            if(imageWidth > imageHeight){
                // The image is horizontal

                // Assuming you have a fixed maximum width
                int imageViewWidth = image.getWidth(); // or a specific value in pixels

                // Calculate the aspect ratio
                float aspectRatio = (float) imageHeight / (float) imageWidth;

                // Calculate and Set ImageView Height based on aspect ratio
                int imageViewHeight = Math.round(imageViewWidth * aspectRatio);

                params.height = imageViewHeight;

            } else {
                // The image is vertical or square

                // Assuming you have a fixed maximum height
                int imageViewHeight = image.getHeight(); // or a specific value in pixels

                // Calculate the aspect ratio
                float aspectRatio = (float) imageWidth / (float) imageHeight;

                // Calculate and Set ImageView Width based on aspect ratio
                int imageViewWidth = Math.round(imageViewHeight * aspectRatio);

                params.width = imageViewWidth;
            }

            // Set the ImageView's dimensions
            image.setLayoutParams(params);
        });
    }
}