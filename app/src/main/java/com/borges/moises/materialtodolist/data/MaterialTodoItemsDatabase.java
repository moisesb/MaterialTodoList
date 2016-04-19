package com.borges.moises.materialtodolist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.scheme.TodoItemTable;
import com.borges.moises.materialtodolist.data.scheme.TodoListTable;

import static com.borges.moises.materialtodolist.data.scheme.TodoItemTable.*;

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
        db.execSQL(TodoListTable.Sql.CREATE_TABLE);
        db.execSQL(TodoItemTable.Sql.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TodoItemTable.Sql.DROP_TABLE);
        db.execSQL(TodoListTable.Sql.DROP_TABLE);
        onCreate(db);
    }
}
