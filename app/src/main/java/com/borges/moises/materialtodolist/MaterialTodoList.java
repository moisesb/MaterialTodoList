package com.borges.moises.materialtodolist;

import android.app.Application;

import com.borges.moises.materialtodolist.data.MaterialTodoItemsDatabase;
import com.borges.moises.materialtodolist.data.services.SessionManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.client.Firebase;

/**
 * Created by Moisés on 14/04/2016.
 */
public class MaterialTodoList extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        setupApplication();
    }

    private void setupApplication() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        SessionManager.init(getApplicationContext());
        Firebase.setAndroidContext(getApplicationContext());
        MaterialTodoItemsDatabase.init(getApplicationContext());
    }
}
