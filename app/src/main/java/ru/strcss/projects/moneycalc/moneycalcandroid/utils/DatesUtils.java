package ru.strcss.projects.moneycalc.moneycalcandroid.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction;

public class DatesUtils {
    private static DateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);

    /**
     * Format date from ISO8601 to pretty view.
     * <p>
     * String 2018-10-21 -> String 21.10.2018
     */
    public static String formatDateToPretty(String isoStringDate) {
        String[] splittedDate = isoStringDate.split("-");
        if (splittedDate[1].length() == 1)
            return String.format("%s.0%s.%s", splittedDate[2], splittedDate[1], splittedDate[0]);

        return String.format("%s.%s.%s", splittedDate[2], splittedDate[1], splittedDate[0]);
    }

    /**
     * Format date from pretty to ISO8601 view.
     * <p>
     * String 21.10.2018 -> String 2018-10-21
     */
    public static String formatDateToIso(String prettyStringDate) {
        String[] splittedDate = prettyStringDate.split("\\.");
        if (splittedDate[1].length() == 1)
            return String.format("%s-0%s-%s", splittedDate[2], splittedDate[1], splittedDate[0]);

        return String.format("%s-%s-%s", splittedDate[2], splittedDate[1], splittedDate[0]);
    }

    /**
     * convert Date to String iso format.
     * <p>
     * Date 2018-10-21 -> String 2018-10-21
     */
    public static String formatDateToIsoString(Date date) {
        String dateString = serverDateFormat.format(date);
        String[] splittedDate = dateString.split("-");
        if (splittedDate[1].length() == 1)
            return String.format("%s-0%s-%s", splittedDate[0], splittedDate[1], splittedDate[2]);

        return dateString;
    }

    /**
     * get String iso date format from year, month and day.
     * <p>
     * 2018,10,21 -> String 2018-10-21
     */
    public static String getIsoDate(int year, int month, int day) {
        return String.format(Locale.ROOT, "%d-%02d-%02d", year, month, day);
    }

    /**
     * get Date from String iso date format.
     * <p>
     * String 2018-10-21 -> Date 2018-10-21
     */
    public static Date formatDateFromString(String date) {
        try {
            return serverDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            snackBarAction(e.getMessage());
            return new Date();
        }
    }

    /**
     * get Calendar from String iso date format.
     * <p>
     * String 2018-10-21 -> Calendar 2018-10-21
     */
    public static Calendar getCalendarFromString(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(formatDateFromString(date));
        return cal;
    }

    /**
     * get String iso date format from Calendar.
     * <p>
     * Calendar 2018-10-21 -> String 2018-10-21
     */
    public static String getStringIsoDateFromCalendar(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        if (month == 0) {
            month = 12;
            year -= 1;
        }
        return getIsoDate(year, month, calendar.get(Calendar.DAY_OF_MONTH));
    }
}
