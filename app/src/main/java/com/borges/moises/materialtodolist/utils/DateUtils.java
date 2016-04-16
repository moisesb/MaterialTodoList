package com.borges.moises.materialtodolist.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Moisés on 11/04/2016.
 */
public class DateUtils {

    private static final String PATTERN = "yyyy-MM-dd";
    private static DateFormat DATE_FORMAT;

    public static String dateToString(Date date) {
        initalizeDateFormat();
        try {
            return DATE_FORMAT.format(date);
        }catch (Exception e) {
            return null;
        }
    }

    private static void initalizeDateFormat() {
        if (DATE_FORMAT == null) {
            DATE_FORMAT = new SimpleDateFormat(PATTERN, Locale.US);
        }
    }

    public static Date stringToDate(String str) {
        if (str == null) {
            return null;
        }
        try {
            return DATE_FORMAT.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }
}
