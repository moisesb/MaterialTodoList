package com.borges.moises.materialtodolist.addtodoitem.presenter;

import com.borges.moises.materialtodolist.addtodoitem.view.AddTodoItemMvpView;
import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.mvp.MvpPresenter;

/**
 * Created by moises.anjos on 06/05/2016.
 */
public interface AddTodoItemMvpPresenter extends MvpPresenter<AddTodoItemMvpView> {

    void addTodoItem(String title, String description, Priority priority, String location, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);
}
