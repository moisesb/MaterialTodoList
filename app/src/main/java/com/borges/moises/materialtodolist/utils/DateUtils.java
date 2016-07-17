package com.borges.moises.materialtodolist.utils;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

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

    private static final String PATTERN = "yyyy-MM-dd HH:mm";
    private static DateFormat DB_DATE_FORMAT = new SimpleDateFormat(PATTERN, Locale.getDefault());
    private static DateFormat UI_DATE_FORMAT = SimpleDateFormat.getInstance();

    public static String dateToDbString(Date date) {
        try {
            return DB_DATE_FORMAT.format(date);
        } catch (Exception e) {
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
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getDate(int year, int monthOfYear, int dayOfMonth) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        return calendar.getTime();
    }

    public static Date getDate(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
        final Calendar calendar = Calendar.getInstance();
        if (year < 0 || monthOfYear < 0 || dayOfMonth < 0) {
            return null;
        }

        if (hourOfDay < 0 || minute < 0) {
            calendar.set(year, monthOfYear, dayOfMonth);
        } else {
            calendar.set(year, monthOfYear, dayOfMonth, hourOfDay, minute);
        }

        return calendar.getTime();
    }

    @NonNull
    public static String getTime(int hourOfDay, int minute) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        final SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(calendar.getTime());
    }

    /***
     * @param date
     * @return -1 if date in before today, 0 if date is today and 1 if date if after today
     */
    @IntRange(from = -1, to = 1)
    public static int compareDateWithToday(@NonNull Date date) {
        Calendar today = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int todayYear = today.get(Calendar.YEAR);
        int dateYear = calendar.get(Calendar.YEAR);
        int todayDayOfYear = today.get(Calendar.DAY_OF_YEAR);
        int dateDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        if (dateYear == todayYear) {
            return dateDayOfYear == todayDayOfYear ? 0 : dateDayOfYear > todayDayOfYear ? 1 : -1;
        } else {
            return dateYear > todayYear ? 1 : -1;
        }
    }


}
