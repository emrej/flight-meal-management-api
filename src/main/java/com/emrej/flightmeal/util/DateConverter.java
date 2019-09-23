package com.emrej.flightmeal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    /**
     * Convert string to date
     */
    public static Date stringToDate(String dateStr) throws ParseException {
        // May be replaced with Joda Time
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.parse(dateStr);
    }
}
