package com.borges.moises.materialtodolist.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Moisés on 11/04/2016.
 */
public class DateUtils {

    private static final String PATTERN = "yyyy-MM-dd";
    private static DateFormat DB_DATE_FORMAT = new SimpleDateFormat(PATTERN,Locale.getDefault());
    private static DateFormat UI_DATE_FORMAT = SimpleDateFormat.getInstance();

    public static String dateToDbString(Date date) {
        try {
            return DB_DATE_FORMAT.format(date);
        }catch (Exception e) {
            return null;
        }
    }

    public static Date dbStringToDate(String str) {
        if (str == null) {
            return null;
        }
        try {
            return DB_DATE_FORMAT.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateToUiString(Date date) {
        try {
            return UI_DATE_FORMAT.format(date).split(" ")[0];
        }catch (Exception e) {
            return null;
        }
    }

    public static Date getDate(int year, int monthOfYear, int dayOfMonth) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year,monthOfYear,dayOfMonth);
        return calendar.getTime();
    }

}
