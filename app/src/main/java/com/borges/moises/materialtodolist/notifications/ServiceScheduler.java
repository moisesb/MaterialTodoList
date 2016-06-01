package com.borges.moises.materialtodolist.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Moisés on 25/04/2016.
 */
public class ServiceScheduler extends WakefulBroadcastReceiver {

    public static final String BOOT_COMPLETED_EVENT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: 31/05/2016 fix a bug, this service should run once a day
        Log.i("Action", intent.getAction() + "");
        if (BOOT_COMPLETED_EVENT.equals(intent.getAction())) {
            Log.i("Action", "set alarm");
            setAlarm(context);
        }

        PendingTasksService.start(context);
    }

    public void setAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,ServiceScheduler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        final long timeInMillis = calendar.getTimeInMillis();
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent);

        ComponentName receiver = new ComponentName(context, ServiceScheduler.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
}
