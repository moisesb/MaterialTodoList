package com.borges.moises.materialtodolist.utils;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Moisés on 20/07/2016.
 */

public class FirebaseAnalyticsHelper {

    public static void notifyClickEvent(@NonNull FirebaseAnalytics firebaseAnalytics,
                                        String buttonId) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, buttonId);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
