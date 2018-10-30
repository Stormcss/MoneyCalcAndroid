package ru.strcss.projects.moneycalc.moneycalcandroid.utils;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatesUtilsTest {

    @ParameterizedTest
    @CsvSource({"2018-10-21, 21.10.2018", "2018-01-09, 09.01.2018", "2018-12-13, 13.12.2018"})
    void formatDateToPretty(String incomeDate, String expectedDate) {
        String formattedDate = DatesUtils.formatDateToPretty(incomeDate);
        assertEquals(expectedDate, formattedDate, "Not equal!");
    }

    @ParameterizedTest
    @CsvSource({"21.10.2018, 2018-10-21", "09.01.2018, 2018-01-09", "13.12.2018, 2018-12-13"})
    void formatDateToIso(String incomeDate, String expectedDate) {
        String formattedDate = DatesUtils.formatDateToIso(incomeDate);
        assertEquals(expectedDate, formattedDate, "Not equal!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2018-10-21", "2018-01-09", "2018-12-13"})
    void formatDateToIsoString(String incomeDate) {
        Calendar calendar = Calendar.getInstance();
        String[] dateArr = incomeDate.split("-");
        calendar.set(Integer.valueOf(dateArr[0]), Integer.valueOf(dateArr[1]) - 1,
                Integer.valueOf(dateArr[2]));

        String stringDate = DatesUtils.formatDateToIsoString(new Date(calendar.getTimeInMillis()));

        assertEquals(incomeDate, stringDate, "Not equal!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2018-10-21", "2018-01-09", "2018-12-13"})
    void getIsoDate(String incomeDate) {
        String[] dateArr = incomeDate.split("-");

        String isoDate = DatesUtils.getIsoDate(Integer.valueOf(dateArr[0]), Integer.valueOf(dateArr[1]),
                Integer.valueOf(dateArr[2]));

        assertEquals(incomeDate, isoDate, "Not equal!");
    }

    @ParameterizedTest
    @CsvSource({"2018-10-21, 21.10.2018", "2018-01-09, 09.01.2018", "2018-12-13, 13.12.2018"})
    void formatDateFromString(String incomeDate) {
        String[] dateArr = incomeDate.split("-");

        Date date = DatesUtils.formatDateFromString(incomeDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        assertEquals((int) Integer.valueOf(dateArr[0]), year, "Year is not equal!");
        assertEquals((int) Integer.valueOf(dateArr[1]), month, "Month is not equal!");
        assertEquals((int) Integer.valueOf(dateArr[2]), day, "Day is not equal!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2018-10-21", "2018-01-09", "2018-12-13"})
    void getCalendarFromString(String incomeDate) {
        String[] dateArr = incomeDate.split("-");

        Calendar calendar = DatesUtils.getCalendarFromString(incomeDate);

        assertEquals((int) Integer.valueOf(dateArr[0]), calendar.get(Calendar.YEAR), "Year is not equal!");
        assertEquals((int) Integer.valueOf(dateArr[1]), calendar.get(Calendar.MONTH) + 1, "Month is not equal!");
        assertEquals((int) Integer.valueOf(dateArr[2]), calendar.get(Calendar.DATE), "Day is not equal!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2018-10-21", "2018-01-09", "2018-12-13"})
    void getStringIsoDateFromCalendar(String incomeDate) {
        String[] dateArr = incomeDate.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf(dateArr[0]), Integer.valueOf(dateArr[1]), Integer.valueOf(dateArr[2]));

        String stringDate = DatesUtils.getStringIsoDateFromCalendar(calendar);

        assertEquals(incomeDate, stringDate, "Not equal!");
    }
}