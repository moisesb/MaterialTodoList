package com.borges.moises.materialtodolist.helpers;

import com.borges.moises.materialtodolist.data.MaterialTodoItemsDatabase;

/**
 * Created by Moisés on 18/04/2016.
 */
public class DatabaseHelper {

    public static void beginTransaction() {
        MaterialTodoItemsDatabase.getInstance().
                getWritableDatabase()
                .beginTransaction();
    }

    public static void rollbackTransaction() {
        MaterialTodoItemsDatabase.getInstance()
                .getWritableDatabase()
                .endTransaction();
    }
}
