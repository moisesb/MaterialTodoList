package com.borges.moises.materialtodolist.utils;

import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by moises.anjos on 21/07/2016.
 */

public class LogHelper {

    public static void log(String tag, String message) {
        FirebaseCrash.logcat(1, tag, message);
    }

    public static void report(Exception exception) {
        FirebaseCrash.report(exception);
    }

    public static void report(String errorMessage) {
        FirebaseCrash.report(new IllegalStateException(errorMessage));
    }
}
