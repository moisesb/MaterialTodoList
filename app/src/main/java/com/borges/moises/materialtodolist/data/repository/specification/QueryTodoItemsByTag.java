package com.borges.moises.materialtodolist.data.repository.specification;

import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.data.scheme.TodoItemsTable;

import static com.borges.moises.materialtodolist.data.scheme.TodoItemsTable.*;

/**
 * Created by Moisés on 08/06/2016.
 */
public class QueryTodoItemsByTag implements SqlSpecification {

    private Tag mTag;

    public QueryTodoItemsByTag(@NonNull Tag tag) {
        mTag = tag;
    }

    @Override
    public String toSqlQuery() {
        return "select * from " + TABLE_NAME +
                " where " + Columns.TAG + " = " + mTag.getId();
    }

    @Override
    public String[] getSelectionArgs() {
        return new String[0];
    }
}
