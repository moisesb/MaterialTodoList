package com.borges.moises.materialtodolist.addtodoitem;

import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.mvp.MvpPresenter;
import com.borges.moises.materialtodolist.mvp.MvpView;

import java.util.List;

/**
 * Created by Moisés on 28/05/2016.
 */
public interface AddTodoItemMvp {
    interface View extends MvpView {
        void showMissingTitle();

        void showTodoItemAdded();

        void addTags(List<Tag> tags);

        void close();
    }

    interface Presenter extends MvpPresenter<View> {
        void addTodoItem(String title, String description, Priority priority, String location, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, Tag tag);

        void loadTags();
    }
}
