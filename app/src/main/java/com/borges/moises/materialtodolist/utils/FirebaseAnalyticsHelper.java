package com.borges.moises.materialtodolist.utils;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.User;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Moisés on 20/07/2016.
 */

public class FirebaseAnalyticsHelper {

    private static final String ACTION_PERFORMED = "action_performed";

    public static void notifyActionPerformed(@NonNull FirebaseAnalytics firebaseAnalytics,
                                             String action) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, action);
        firebaseAnalytics.logEvent(ACTION_PERFORMED, bundle);
    }

    public static void setUserData(@NonNull FirebaseAnalytics firebaseAnalytics, User loggedUser) {
        if (loggedUser != null) {
            firebaseAnalytics.setUserId(loggedUser.getUserName());
        }
    }
}
