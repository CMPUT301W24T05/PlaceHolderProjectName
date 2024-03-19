package ca.cmput301t05.placeholder.utils;

import java.util.Calendar;

public class DateStrings {

    public static String getMonthName(int month) {
        String[] monthNames = {
                "January", "February", "March",
                "April", "May", "June",
                "July", "August", "September",
                "October", "November", "December"
        };

        if (month < 1 || month > 12) {
            return "Invalid month"; // Return an error message or handle this case as you see fit
        }

        return monthNames[month];
    }

    public static String getAmPM(int ampm){

        return ampm == Calendar.AM ? "AM" : "PM";
    }
}
