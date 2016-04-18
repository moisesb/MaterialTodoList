package com.borges.moises.materialtodolist.helpers;

import com.borges.moises.materialtodolist.data.model.TodoItem;

/**
 * Created by Moisés on 16/04/2016.
 */
public interface TodoItemHelper {
    boolean todoItemIsValid(TodoItem todoItem, View view);

    interface View {
        void showMissingTitle();
        void showInvalidDate();
    }
}
