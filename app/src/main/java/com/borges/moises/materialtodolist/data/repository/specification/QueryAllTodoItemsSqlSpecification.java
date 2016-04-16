package com.borges.moises.materialtodolist.data.repository.specification;

import com.borges.moises.materialtodolist.data.scheme.TodoItemTable;

import static com.borges.moises.materialtodolist.data.scheme.TodoItemTable.*;

/**
 * Created by Moisés on 13/04/2016.
 */
public class QueryAllTodoItemsSqlSpecification implements SqlSpecification {
    @Override
    public String toSqlQuery() {
        return "select * from " + TABLE_NAME;
    }

    @Override
    public String[] getSelectionArgs() {
        return new String[0];
    }
}
