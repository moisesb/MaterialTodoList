package com.borges.moises.materialtodolist.addtodoitem;

import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.mvp.MvpPresenter;
import com.borges.moises.materialtodolist.mvp.MvpView;

/**
 * Created by Moisés on 28/05/2016.
 */
public interface AddTodoItemMvp {
    interface View extends MvpView {
        void showMissingTitle();

        void showTodoItemAdded();

        void close();
    }

    interface Presenter extends MvpPresenter<View> {
        void addTodoItem(String title, String description, Priority priority, String location, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);
    }
}
