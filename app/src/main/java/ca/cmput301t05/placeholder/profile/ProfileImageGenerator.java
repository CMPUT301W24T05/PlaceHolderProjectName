package ca.cmput301t05.placeholder.profile;

import android.graphics.*;
/**
 * Utility class for generating profile images and performing related operations.
 */
public class ProfileImageGenerator {
    protected static final int PROFILE_IMAGE_SIZE = 500;
    protected static final int TEXT_SIZE = PROFILE_IMAGE_SIZE / 2;
    /**
     * Generates a default profile image bitmap based on the given name.
     *
     * @param name The name used to generate the profile image.
     * @return A Bitmap representing the default profile image.
     * @throws IllegalArgumentException If the name is null or empty.
     */
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
    /**
     * Calculates the hue value based on the provided name.
     *
     * @param name The name used to calculate the hue value.
     * @return The hue value within the range of 0 to 360 for HSV color space.
     */
    private static int calculateHueFromName(String name) {
        int hash = 0;
        for (char c : name.toCharArray()) {
            hash = c + ((hash << 5) - hash);
        }
        return Math.abs(hash * 56 % 360); // Ensure the hue is within the 0-360 range for HSV
    }
    /**
     * Creates a Paint object with the specified color.
     *
     * @param color The color value for the Paint object.
     * @return A Paint object with the specified color.
     */
    private static Paint createPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        return paint;
    }
    /**
     * Creates a Paint object for text rendering with the specified color, text size, and typeface.
     *
     * @param color The color value for the Paint object.
     * @return A Paint object configured for text rendering with the specified color, text size, and typeface.
     */
    private static Paint createTextPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(TEXT_SIZE);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        return paint;
    }
    /**
     * Generates a circular bitmap from the given bitmap.
     *
     * @param bitmap The bitmap to be cropped into a circular shape.
     * @return A circular bitmap.
     */
    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    /**
     * Extracts initials from the provided name. If the name is null or empty, an empty string is returned.
     * The initials are obtained by taking the first character of the first and last parts of the name after splitting
     * it by spaces or hyphens. Non-alphabetic characters are ignored.
     *
     * @param name The name from which to extract initials.
     * @return The extracted initials or an empty string if the name is null or empty.
     */
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