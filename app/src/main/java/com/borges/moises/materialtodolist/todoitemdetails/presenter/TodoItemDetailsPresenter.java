package com.borges.moises.materialtodolist.todoitemdetails.presenter;

import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.mvp.Presenter;
import com.borges.moises.materialtodolist.todoitemdetails.view.TodoItemDetailsView;

/**
 * Created by moises.anjos on 09/05/2016.
 */
public interface TodoItemDetailsPresenter extends Presenter<TodoItemDetailsView>{
    void openTodoItem(long todoItemId);

    void editTodoItem(long todoItemId, String title, String description, Priority priority, String location, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);

    void openDeleteConfirmationDialog();

    void deleteTodoItem(long todoItemId);
}
