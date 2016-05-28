package com.borges.moises.materialtodolist.edittodoitem;

import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.mvp.MvpPresenter;
import com.borges.moises.materialtodolist.mvp.MvpView;

import java.util.Date;

/**
 * Created by Moisés on 28/05/2016.
 */
public interface EditTodoItemMvp {

    interface View extends MvpView {
        void showTitle(String title);

        void showDescription(String description);

        void showDate(Date date);

        void showPriority(Priority priority);

        void showLocation(String location);

        void showMissingTitle();

        void close();

        void showTodoItemEdited();

        void showDeleteConfirmationDialog();
    }

    interface Presenter extends MvpPresenter<View> {
        void openTodoItem(long todoItemId);

        void editTodoItem(long todoItemId, String title, String description, Priority priority, String location, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);

        void openDeleteConfirmationDialog();

        void deleteTodoItem(long todoItemId);
    }
}
