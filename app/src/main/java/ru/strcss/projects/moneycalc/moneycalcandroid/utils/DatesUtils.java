package ru.strcss.projects.moneycalc.moneycalcandroid.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction;

public class DatesUtils {
    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String formatDateToString(Date date) {
        String dateString = dateFormat.format(date);
        String[] splittedDate = dateString.split("-");
        if (splittedDate[1].length() == 1)
            return String.format("%s-0%s-%s", splittedDate[0], splittedDate[1], splittedDate[2]);

        return dateString;
    }

    public static String getIsoDate(int year, int month, int day) {
        return String.format("%d-%02d-%d", year, month, day);
    }

    public static Date formatDateFromString(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            snackBarAction(e.getMessage());
            return new Date();
        }
    }

    public static Calendar getCalendarFromString(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(formatDateFromString(date));
        return cal;
    }
}
