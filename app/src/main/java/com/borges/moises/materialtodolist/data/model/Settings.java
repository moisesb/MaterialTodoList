package com.borges.moises.materialtodolist.data.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by moises.anjos on 02/06/2016.
 */
public class Settings {
    private static final String USER_SETTINGS = "user_settings";
    private static final String NOTIFICATION_ENABLED = "notificationEnabled";
    private static final String NOTIFICATION_HOUR = "notification_hour";
    private static final String NOTIFICATION_MINUTE = "notification_minute";

    private final Context mContext;
    private boolean notificationEnabled;
    private int notificationHour;
    private int notificationMinute;

    private static Settings sSettings;

    private Settings(Context context){
        mContext = context;

        final SharedPreferences sharedPreferences =
                context.getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);

        notificationEnabled = sharedPreferences.getBoolean(NOTIFICATION_ENABLED,true);
        notificationHour = sharedPreferences.getInt(NOTIFICATION_HOUR, 8);
        notificationMinute = sharedPreferences.getInt(NOTIFICATION_MINUTE,0);
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

    public int getNotificationHour() {
        return notificationHour;
    }

    public void setNotificationHour(int notificationHour) {
        this.notificationHour = notificationHour;
    }

    public int getNotificationMinute() {
        return notificationMinute;
    }

    public void setNotificationMinute(int notificationMinute) {
        this.notificationMinute = notificationMinute;
    }

    public void save() {
        final SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);

        sharedPreferences.edit()
                .putBoolean(NOTIFICATION_ENABLED,notificationEnabled)
                .putInt(NOTIFICATION_HOUR, notificationHour)
                .putInt(NOTIFICATION_MINUTE, notificationMinute)
                .apply();
    }

    public static void init(@NonNull Context context){
        if (sSettings == null) {
            sSettings = new Settings(context);
        }
    }

    @NonNull
    public static Settings getInstance() {
        if (sSettings == null) {
            throw new IllegalStateException("settings should be initialized first");
        }

        return sSettings;
    }

}
