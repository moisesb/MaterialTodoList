package com.borges.moises.materialtodolist.todoitemdetails.presenter;

import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.mvp.MvpPresenter;
import com.borges.moises.materialtodolist.todoitemdetails.view.TodoItemDetailsMvpView;

/**
 * Created by moises.anjos on 09/05/2016.
 */
public interface TodoItemDetailsMvpPresenter extends MvpPresenter<TodoItemDetailsMvpView> {
    void openTodoItem(long todoItemId);

    void editTodoItem(long todoItemId, String title, String description, Priority priority, String location, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);

    void openDeleteConfirmationDialog();

    void deleteTodoItem(long todoItemId);
}
