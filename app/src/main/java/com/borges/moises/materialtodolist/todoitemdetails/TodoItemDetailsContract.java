package com.borges.moises.materialtodolist.todoitemdetails;

import java.util.Date;

/**
 * Created by Moisés on 16/04/2016.
 */
public interface TodoItemDetailsContract {
    interface View {
        void showTitle(String title);
        void showDescription(String description);
        void showDate(Date date);
        void showUrgent(boolean urgent);
        void showMissingTitle();
        void showDateInThePast();
        void close();
    }

    interface PresenterOps {
        void openTodoItem(long todoItemId);
        void editTodoItem(long todoItemId, String title, String description, boolean urgent, Date date, String time);
        void deleteTodoItem(long todoItemId);
        void onDestroy();
    }
}
