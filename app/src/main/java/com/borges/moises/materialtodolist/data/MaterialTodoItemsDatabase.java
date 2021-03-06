package com.borges.moises.materialtodolist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.borges.moises.materialtodolist.data.scheme.TagsTable;
import com.borges.moises.materialtodolist.data.scheme.TodoItemsTable;

/**
 * Created by Moisés on 11/04/2016.
 */
public class MaterialTodoItemsDatabase extends SQLiteOpenHelper {


    private static final String NAME = "material_todo_items.db";
    private static final int VERSION = 1;

    private static MaterialTodoItemsDatabase INSTANCE;

    private MaterialTodoItemsDatabase(Context context) {
        super(context, NAME, null, VERSION);
    }

    public static void init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MaterialTodoItemsDatabase(context);
        }
    }

    public static MaterialTodoItemsDatabase getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("MaterialTodoItemsDatabase should be initialized first");
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TagsTable.Sql.CREATE_TABLE);
        db.execSQL(TodoItemsTable.Sql.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TodoItemsTable.Sql.DROP_TABLE);
        db.execSQL(TagsTable.Sql.DROP_TABLE);
        onCreate(db);
    }
}
