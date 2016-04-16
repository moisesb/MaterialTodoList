package com.borges.moises.materialtodolist;

import android.app.Application;

import com.borges.moises.materialtodolist.data.MaterialTodoItemsDatabase;

/**
 * Created by Moisés on 14/04/2016.
 */
public class MaterialTodoList extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MaterialTodoItemsDatabase.init(getApplicationContext());
    }
}
