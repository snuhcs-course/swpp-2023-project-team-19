package com.example.gathernow.utils;

import java.util.Calendar;
import java.util.Locale;

public class DateTimeHelper {
    public static String getFormattedDate(Calendar calendar) {
        if (calendar != null) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // month starts from 0
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return String.format(Locale.ENGLISH, "%d-%02d-%02d", year, month, day);
        }
        return "";
    }

    public static String getFormattedTime(Calendar calendar) {
        if (calendar != null) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            return String.format(Locale.ENGLISH, "%02d:%02d:00", hour, minute);
        }
        return "";
    }

}
