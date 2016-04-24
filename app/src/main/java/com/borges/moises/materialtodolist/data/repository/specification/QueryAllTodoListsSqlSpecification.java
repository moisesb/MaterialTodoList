package com.borges.moises.materialtodolist.data.repository.specification;

import com.borges.moises.materialtodolist.data.scheme.TodoListTable;

/**
 * Created by Moisés on 19/04/2016.
 */
public class QueryAllTodoListsSqlSpecification implements SqlSpecification {
    @Override
    public String toSqlQuery() {
        return "select * from " + TodoListTable.TABLE_NAME;
    }

    @Override
    public String[] getSelectionArgs() {
        return new String[0];
    }
}
