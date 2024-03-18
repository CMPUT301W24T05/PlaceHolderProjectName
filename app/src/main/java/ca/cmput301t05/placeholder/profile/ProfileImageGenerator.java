package ca.cmput301t05.placeholder.profile;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class ProfileImageGenerator {
    protected static final int PROFILE_IMAGE_SIZE = 500;
    protected static final int TEXT_SIZE = PROFILE_IMAGE_SIZE / 2;

    public static Bitmap defaultProfileImage(String name) {
        if(name == null){
            throw new IllegalArgumentException("Name cannot be null!");
        } else if (name.trim().isEmpty()){
            throw new IllegalArgumentException("Name cannot be empty!");
        }

        Bitmap bitmap = Bitmap.createBitmap(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Calculate hue based on the name
        int hue = calculateHueFromName(name);
        // Convert hue to Android Color (using full saturation and lightness for visibility)
        int backgroundColor = Color.HSVToColor(new float[]{hue, 1f, 1f});

        Paint circlePaint = createPaint(backgroundColor);
        circlePaint.setAntiAlias(true);
        canvas.drawCircle((float) PROFILE_IMAGE_SIZE /2, (float) PROFILE_IMAGE_SIZE /2, (float) PROFILE_IMAGE_SIZE /2, circlePaint);

        String initials = extractInitials(name);
        Paint textPaint = createTextPaint(Color.WHITE);
        float textWidth = textPaint.measureText(initials);
        float x = (bitmap.getWidth() - textWidth)/2;
        float y = (bitmap.getHeight() - textPaint.ascent() - textPaint.descent())/2;
        canvas.drawText(initials, x, y, textPaint);

        return bitmap;
    }

    private static int calculateHueFromName(String name) {
        int hash = 0;
        for (char c : name.toCharArray()) {
            hash = c + ((hash << 5) - hash);
        }
        return Math.abs(hash * 56 % 360); // Ensure the hue is within the 0-360 range for HSV
    }

    private static Paint createPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        return paint;
    }

    private static Paint createTextPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(TEXT_SIZE);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        return paint;
    }

    private static String extractInitials(String name) {
        if (name == null || name.trim().isEmpty()) {
            return ""; // Return an empty string or some default initials if the name is null or empty
        }

        // Remove any leading and trailing spaces, and split the name into parts
        String[] parts = name.trim().split("[\\s\\-]+");

        // Now we'll work with the first and last parts for initials
        String firstPart = parts[0];
        String lastPart = parts.length > 1 ? parts[parts.length - 1] : ""; // Use the last part if it exists

        // Extract the first character of the first part, ignoring non-alphabetic characters
        String firstInitial = firstPart.replaceAll("[^a-zA-Z]", "").toUpperCase();
        if (!firstInitial.isEmpty()) {
            firstInitial = firstInitial.substring(0, 1);
        }

        // Extract the first character of the last part, if it's different from the first part
        String lastInitial = "";
        if (!lastPart.isEmpty() && !firstPart.equals(lastPart)) {
            lastInitial = lastPart.replaceAll("[^a-zA-Z]", "").toUpperCase();
            if (!lastInitial.isEmpty()) {
                lastInitial = lastInitial.substring(0, 1);
            }
        }

        // Combine the initials
        return firstInitial + lastInitial;
    }

}