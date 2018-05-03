package ru.strcss.projects.moneycalc.moneycalcandroid.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatesUtils {
    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String formatDateToString(Date date) {
        return dateFormat.format(date);
    }

    public static Date formatDateFromString(String date) throws ParseException {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
