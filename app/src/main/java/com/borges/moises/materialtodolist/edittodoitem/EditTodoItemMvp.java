package com.borges.moises.materialtodolist.edittodoitem;

import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.mvp.MvpPresenter;
import com.borges.moises.materialtodolist.mvp.MvpView;

import java.util.Date;
import java.util.List;

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

        void addTags(List<Tag> tags);

        void showTag(Tag tag);
    }

    interface Presenter extends MvpPresenter<View> {
        void openTodoItem(long todoItemId);

        void editTodoItem(long todoItemId, String title, String description, Priority priority, String location, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, Tag tag);

        void openDeleteConfirmationDialog();

        void deleteTodoItem(long todoItemId);

        void loadTags();
    }
}
