package com.borges.moises.materialtodolist.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by moises.anjos on 25/05/2016.
 */
public class AuthenticatorService extends Service {


    private StubAuthenticator mAutenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        mAutenticator = new StubAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAutenticator.getIBinder();
    }
}
