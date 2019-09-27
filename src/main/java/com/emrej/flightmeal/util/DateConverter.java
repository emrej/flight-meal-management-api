package com.emrej.flightmeal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateConverter {

    public static final String PATTERN = "yyyy-MM-dd";

    /**
     * Convert date to string
     */
    public static String dateToString(Date date) {
        // May be replaced with Joda Time
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        return sdf.format(cleanUpTime(date));
    }

    /**
     * Convert string to date
     */
    public static Date stringToDate(String dateStr) throws ParseException {
        // May be replaced with Joda Time
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        return cleanUpTime(sdf.parse(dateStr));
    }

    public static Date cleanUpTime(Date date) {
        // Get Calendar object set to the date and time of the given Date object
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        // Set time fields to zero
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Put it back in the Date object
        return cal.getTime();
    }
}
