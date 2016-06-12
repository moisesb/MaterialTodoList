package com.borges.moises.materialtodolist;

import android.app.Application;
import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;

import com.borges.moises.materialtodolist.data.MaterialTodoItemsDatabase;
import com.borges.moises.materialtodolist.data.model.Settings;
import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.data.repository.SqliteTagsRepository;
import com.borges.moises.materialtodolist.data.repository.TagsRepository;
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
        initDatabase();
    }

    private void initDatabase() {
        TagsRepository repository = SqliteTagsRepository.getInstance();
        if (repository.getTags().size() == 0) {
            Resources res = getResources();
            final String[] tagNames = res.getStringArray(R.array.initial_tags);
            for (String tagName : tagNames) {
                Tag tag = new Tag();
                tag.setName(tagName);
                repository.addTag(tag);
            }
        }
    }

    private void setupApplication() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Settings.init(getApplicationContext());
        SessionManager.init(getApplicationContext());
        Firebase.setAndroidContext(getApplicationContext());
        MaterialTodoItemsDatabase.init(getApplicationContext());
    }
}
