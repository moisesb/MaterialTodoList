package com.borges.moises.materialtodolist.data.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.borges.moises.materialtodolist.data.MaterialTodoItemsDatabase;
import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.specification.Specification;
import com.borges.moises.materialtodolist.data.repository.specification.SqlSpecification;
import com.borges.moises.materialtodolist.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.borges.moises.materialtodolist.data.scheme.TodoItemTable.Columns;
import static com.borges.moises.materialtodolist.data.scheme.TodoItemTable.TABLE_NAME;

/**
 * Created by Moisés on 11/04/2016.
 */
public final class SqliteTodoItemRepository implements TodoItemRepository {

    private SQLiteDatabase mDatabase;

    private static SqliteTodoItemRepository INSTANCE;
    private static final String WHERE_CLAUSE = Columns.ID + " = ?";

    private SqliteTodoItemRepository() {
        mDatabase = MaterialTodoItemsDatabase.getInstance()
                .getWritableDatabase();
    }

    public static TodoItemRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SqliteTodoItemRepository();
        }

        return INSTANCE;
    }


    @Override
    public boolean addTodoItem(@NonNull TodoItem todoItem) {
        try {
            final ContentValues contentValues = getContentValues(todoItem);
            long id = mDatabase.insertOrThrow(TABLE_NAME, null, contentValues);
            todoItem.setId(id);
            return id >= 0;
        }catch (SQLiteException e) {
            return false;
        }

    }

    private ContentValues getContentValues(TodoItem todoItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.TITLE, todoItem.getTitle());
        contentValues.put(Columns.DESCRIPTION, todoItem.getDescription());
        contentValues.put(Columns.PRIORITY, todoItem.getPriority().name());
        contentValues.put(Columns.DATE, DateUtils.dateToDbString(todoItem.getDate()));
        contentValues.put(Columns.LOCATION, todoItem.getLocation());
        contentValues.put(Columns.DELETED, todoItem.isDeleted());
        contentValues.put(Columns.VERSION, todoItem.getVersion());
        contentValues.put(Columns.DIRTY, todoItem.isDirty());
        contentValues.put(Columns.SERVER_ID, todoItem.getServerId());
        contentValues.put(Columns.DONE, todoItem.isDone());
        contentValues.put(Columns.DONE_AT, DateUtils.dateToDbString(todoItem.getDoneAt()));
        contentValues.put(Columns.CREATED_AT, todoItem.getCreatedAt().getTime());
        contentValues.put(Columns.UPDATED_AT, todoItem.getUpdatedAt().getTime());
        return contentValues;
    }

    @Override
    public boolean updateTodoItem(@NonNull TodoItem todoItem) {
        try {
            final String whereClause = Columns.ID + " = ?";
            final String[] whereArgs = getWhereArgsForId(todoItem);
            final ContentValues contentValues = getContentValues(todoItem);
            mDatabase.update(TABLE_NAME, contentValues, whereClause, whereArgs);
            return true;
        }catch (SQLiteException e) {
            return false;
        }
    }

    @Override
    public boolean removeTodoItem(@NonNull TodoItem todoItem) {

        final String[] whereArgs = getWhereArgsForId(todoItem);
        mDatabase.delete(TABLE_NAME, WHERE_CLAUSE, whereArgs);
        return true;
    }

    @Nullable
    @Override
    public TodoItem getTodoItem(long todoItemid) {
        final String[] whereArgs = {String.valueOf(todoItemid)};
        Cursor cursor = mDatabase.query(TABLE_NAME,null,WHERE_CLAUSE, whereArgs, null, null, null);
        cursor.moveToFirst();
        return getTodoItem(cursor);
    }

    private TodoItem getTodoItem(Cursor cursor) {
        if (cursor.isAfterLast() || cursor.getCount() == 0) {
            return null;
        }
        TodoItem todoItem = new TodoItem();
        todoItem.setId(cursor.getLong(cursor.getColumnIndex(Columns.ID)));
        todoItem.setDate(DateUtils.dbStringToDate(cursor.getString(cursor.getColumnIndex(Columns.DATE))));
        todoItem.setDescription(cursor.getString(cursor.getColumnIndex(Columns.DESCRIPTION)));
        todoItem.setPriority(Priority.valueOf(cursor.getString(cursor.getColumnIndex(Columns.PRIORITY))));
        todoItem.setTitle(cursor.getString(cursor.getColumnIndex(Columns.TITLE)));
        todoItem.setDeleted(cursor.getInt(cursor.getColumnIndex(Columns.DELETED)) > 0);
        todoItem.setVersion(cursor.getInt(cursor.getColumnIndex(Columns.VERSION)));
        todoItem.setDirty(cursor.getInt(cursor.getColumnIndex(Columns.DIRTY)) > 0);
        todoItem.setServerId(cursor.getString(cursor.getColumnIndex(Columns.SERVER_ID)));
        todoItem.setDone(cursor.getInt(cursor.getColumnIndex(Columns.DONE)) > 0);
        todoItem.setLocation(cursor.getString(cursor.getColumnIndex(Columns.LOCATION)));
        todoItem.setDoneAt(DateUtils.dbStringToDate(cursor.getString(cursor.getColumnIndex(Columns.DONE_AT))));
        todoItem.setCreatedAt(new Date(cursor.getLong(cursor.getColumnIndex(Columns.CREATED_AT))));
        todoItem.setUpdatedAt(new Date(cursor.getLong(cursor.getColumnIndex(Columns.UPDATED_AT))));
        return todoItem;
    }

    @Override
    public List<TodoItem> query(Specification specification) {
        if (!(specification instanceof SqlSpecification)) {
            throw new IllegalArgumentException("Specification should implement SqlSpecfication");
        }
        SqlSpecification sqlSpecification = (SqlSpecification) specification;
        Cursor cursor = mDatabase.rawQuery(sqlSpecification.toSqlQuery(),sqlSpecification.getSelectionArgs());
        cursor.moveToFirst();
        List<TodoItem> todoItems = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            TodoItem todoItem = getTodoItem(cursor);
            todoItems.add(todoItem);
            cursor.moveToNext();
        }
        return todoItems;
    }

    @NonNull
    private String[] getWhereArgsForId(@NonNull TodoItem todoItem) {
        return new String[]{String.valueOf(todoItem.getId())};
    }

}
