package ru.strcss.projects.moneycalc.moneycalcandroid.utils;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DatesUtilsTest {

    @Test
    public void formatDateToString() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 10, 18);
        String stringDate = DatesUtils.formatDateToString(new Date(calendar.getTimeInMillis()));
//        assertEquals("Not equal!", "18.10.2018", stringDate);
    }

    @Test
    public void getIsoDate() {
        String isoDate = DatesUtils.getIsoDate(2018, 9, 1);
        assertEquals("Not equal!", "2018-09-01", isoDate);
    }

    @Test
    public void formatDateFromString() {
    }

    @Test
    public void getCalendarFromString() {
    }
}