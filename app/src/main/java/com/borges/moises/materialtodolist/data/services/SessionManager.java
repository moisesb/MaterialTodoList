package com.borges.moises.materialtodolist.data.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.borges.moises.materialtodolist.data.model.User;

/**
 * Created by moises.anjos on 11/05/2016.
 */
public class SessionManager {

    private static final String NAME = "user";
    private static final String IS_LOGGED_IN = "is_logged_in";
    private static final String USER_NAME = "user_name";
    private static final String USER_UID = "uid";
    private SharedPreferences mSharedPreferences;

    private static SessionManager INSTANCE;

    private SessionManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
    }

    public static SessionManager getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Session Manager should be initialize first");
        }

        return INSTANCE;
    }

    public static void init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SessionManager(context);
        }
    }

    public boolean isUserSignedIn() {
        return mSharedPreferences.getBoolean(IS_LOGGED_IN,false);
    }

    @Nullable
    public User getSignedInUser() {
        if (!isUserSignedIn()) {
            return null;
        }

        User user = new User();
        user.setUserName(mSharedPreferences.getString(USER_NAME, null));
        user.setEmail(mSharedPreferences.getString(USER_UID, null));
        return user;
    }

    public void signInUser(@NonNull User user){
        mSharedPreferences.edit()
                .putBoolean(IS_LOGGED_IN,true)
                .putString(USER_NAME,user.getUserName())
                .putString(USER_UID, user.getEmail())
                .apply();
    }

    public void logout() {
        mSharedPreferences.edit()
                .putBoolean(IS_LOGGED_IN,false)
                .putString(USER_NAME, null)
                .putString(USER_UID,null)
                .apply();
    }
}
