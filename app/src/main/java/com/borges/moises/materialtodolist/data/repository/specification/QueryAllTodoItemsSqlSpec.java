package com.borges.moises.materialtodolist.data.repository.specification;

import static com.borges.moises.materialtodolist.data.scheme.TodoItemsTable.TABLE_NAME;

/**
 * Created by Moisés on 13/04/2016.
 */
public class QueryAllTodoItemsSqlSpec implements SqlSpecification {
    @Override
    public String toSqlQuery() {
        return "select * from " + TABLE_NAME;
    }

    @Override
    public String[] getSelectionArgs() {
        return new String[0];
    }
}
