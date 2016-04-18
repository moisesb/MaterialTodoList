package com.borges.moises.materialtodolist.addtodoitem;

import com.borges.moises.materialtodolist.data.model.TodoItem;

import java.util.Date;

/**
 * Created by Moisés on 14/04/2016.
 */
public interface AddTodoItemContract {

    interface View {
        void showMissingTitle();
        void showDateInThePast();
        void close();
    }

    interface PresenterOps {
        void addTodoItem(String title, String description, boolean urgent, Date date, String time);
        void onDestroy();
    }
}
