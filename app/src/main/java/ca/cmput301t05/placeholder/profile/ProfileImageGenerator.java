package ca.cmput301t05.placeholder.profile;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class ProfileImageGenerator {
    protected static final int PROFILE_IMAGE_SIZE = 500;
    protected static final int TEXT_SIZE = PROFILE_IMAGE_SIZE / 2;

    public Bitmap defaultProfileImage(String name) {
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

    protected Paint createPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        return paint;
    }

    protected Paint createTextPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(TEXT_SIZE);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        return paint;
    }

    protected String extractInitials(String name) {
        name = name.trim();
        String initials = name.substring(0, 1);
        if(name.contains(" ")) {
            initials += name.substring(name.indexOf(" ")+1, name.indexOf(" ")+2);
        }
        return initials.toUpperCase();
    }
}