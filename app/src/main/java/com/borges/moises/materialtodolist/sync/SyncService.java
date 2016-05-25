package com.borges.moises.materialtodolist.sync;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by moises.anjos on 25/05/2016.
 */
public class SyncService extends Service {

    private static TodoItemsSyncAdapter sSyncAdapter;
    private static final Object sSyncLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (sSyncLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new TodoItemsSyncAdapter(getApplicationContext(),true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
