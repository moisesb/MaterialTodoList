package com.borges.moises.materialtodolist.data.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.MaterialTodoItemsDatabase;
import com.borges.moises.materialtodolist.data.model.TodoList;
import com.borges.moises.materialtodolist.data.repository.specification.Specification;
import com.borges.moises.materialtodolist.data.repository.specification.SqlSpecification;

import java.util.ArrayList;
import java.util.List;

import static com.borges.moises.materialtodolist.data.scheme.TodoListTable.Columns;
import static com.borges.moises.materialtodolist.data.scheme.TodoListTable.TABLE_NAME;

/**
 * Created by Moisés on 18/04/2016.
 */
public class SqliteTodoListRepository implements TodoListRepository {

    public static final String WHERE_CLAUSE = Columns.ID + " = ?";
    private static SqliteTodoListRepository INSTANCE;
    private SQLiteDatabase mDatabase;

    private SqliteTodoListRepository() {
        mDatabase = MaterialTodoItemsDatabase.getInstance()
                .getWritableDatabase();
    }

    public static TodoListRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SqliteTodoListRepository();
        }
        return INSTANCE;
    }

    @Override
    public boolean addTodoList(TodoList todoList) {
        try {
            ContentValues contentValues = getContentValues(todoList);
            long id = mDatabase.insertOrThrow(TABLE_NAME,null, contentValues);
            todoList.setId(id);
            return true;
        }catch (SQLException e) {
            return false;
        }
    }

    private ContentValues getContentValues(TodoList todoList) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.NAME, todoList.getName());
        return contentValues;
    }

    @Override
    public boolean updateTodoList(TodoList todoList) {
        try {
            ContentValues contentValues = getContentValues(todoList);
            return mDatabase.update(TABLE_NAME,contentValues, WHERE_CLAUSE, getWhereArgs(todoList)) > 0;
        }catch (SQLException e) {
            return false;
        }
    }

    @NonNull
    private String[] getWhereArgs(TodoList todoList) {
        return new String[] {String.valueOf(todoList.getId())};
    }

    @Override
    public boolean deleteTodoList(TodoList todoList) {
        try {
            return mDatabase.delete(TABLE_NAME,WHERE_CLAUSE,getWhereArgs(todoList)) > 0;
        }catch (SQLException e) {
            return false;
        }
    }

    @Override
    public TodoList getTodoList(long id) {
        Cursor cursor = mDatabase.query(TABLE_NAME,null,WHERE_CLAUSE,new String[]{String.valueOf(id)},null, null, null);
        if (cursor.moveToFirst()) {
            return getTodoList(cursor);
        }else {
            return null;
        }
    }

    private TodoList getTodoList(Cursor cursor) {
        TodoList todoList = new TodoList();
        todoList.setId(cursor.getLong(cursor.getColumnIndex(Columns.ID)));
        todoList.setName(cursor.getString(cursor.getColumnIndex(Columns.NAME)));
        return todoList;
    }

    @Override
    public List<TodoList> query(Specification specification) {
        if (!(specification instanceof SqlSpecification)) {
            throw new IllegalArgumentException("Specification should be instance of SqlSpecification");
        }
            SqlSpecification sqlSpecification = (SqlSpecification) specification;
            Cursor cursor = mDatabase.rawQuery(sqlSpecification.toSqlQuery(),sqlSpecification.getSelectionArgs());
            cursor.moveToFirst();
            List<TodoList> todoLists = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                TodoList todoList = getTodoList(cursor);
                todoLists.add(todoList);
                cursor.moveToNext();
            }
            return todoLists;
    }
}
