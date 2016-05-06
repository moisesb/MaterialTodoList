package com.borges.moises.materialtodolist.addtodoitem.presenter;

import com.borges.moises.materialtodolist.addtodoitem.view.AddTodoItemView;
import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.mvp.Presenter;

/**
 * Created by moises.anjos on 06/05/2016.
 */
public interface AddTodoItemPresenter extends Presenter<AddTodoItemView>{

    void addTodoItem(String title, String description, Priority priority, String location, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);
}
