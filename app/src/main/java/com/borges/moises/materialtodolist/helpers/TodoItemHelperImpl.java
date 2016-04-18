package com.borges.moises.materialtodolist.helpers;

import com.borges.moises.materialtodolist.data.model.TodoItem;

import java.util.Date;

/**
 * Created by Moisés on 16/04/2016.
 */
public class TodoItemHelperImpl implements TodoItemHelper {
    @Override
    public boolean todoItemIsValid(TodoItem todoItem, View view) {
        if (todoItem.getTitle() == null || todoItem.getTitle().isEmpty()) {
            view.showMissingTitle();
            return false;
        }
        if (todoItem.getDate() != null && dateInPast(todoItem.getDate())) {
            view.showInvalidDate();
            return false;
        }
        return true;
    }

    // TODO: 16/04/2016 implement this method
    private boolean dateInPast(Date date) {
        return false;
    }
}
