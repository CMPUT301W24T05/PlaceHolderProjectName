package ca.cmput301t05.placeholder.utils;

public class StringManip {

    public static String truncateString(String message, int maxLength) {
        if (message != null && message.length() > maxLength) {
            return message.substring(0, maxLength) + "...";
        }
        return message;
    }
}
