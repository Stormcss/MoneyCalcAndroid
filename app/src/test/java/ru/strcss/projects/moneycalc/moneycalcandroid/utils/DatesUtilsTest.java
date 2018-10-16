package ru.strcss.projects.moneycalc.moneycalcandroid.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DatesUtilsTest {

    @Test
    public void formatDateToString() {
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