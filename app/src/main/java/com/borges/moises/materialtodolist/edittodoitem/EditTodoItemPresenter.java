package com.borges.moises.materialtodolist.edittodoitem;

import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.services.TodoItemService;
import com.borges.moises.materialtodolist.utils.DateUtils;

/**
 * Created by Moisés on 24/04/2016.
 */
public class EditTodoItemPresenter implements EditTodoItemMvp.Presenter {

    private EditTodoItemMvp.View mView;
    private TodoItemService mService;

    public EditTodoItemPresenter() {
        mService = new TodoItemService();
    }

    @Override
    public void openTodoItem(long todoItemId) {
        TodoItem todoItem = mService.getTodoItem(todoItemId);
        mView.showTitle(todoItem.getTitle());
        mView.showDescription(todoItem.getDescription());
        mView.showDate(todoItem.getDate());
        mView.showPriority(todoItem.getPriority());
        mView.showLocation(todoItem.getLocation());
    }

    @Override
    public void editTodoItem(long todoItemId, String title, String description, Priority priority, String location, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
        verifyView();

        TodoItem todoItem = mService.getTodoItem(todoItemId);
        todoItem.setTitle(title);
        todoItem.setDescription(description);
        todoItem.setPriority(priority == null? Priority.NORMAL : priority);
        todoItem.setLocation(location);
        todoItem.setDate(DateUtils.getDate(year, monthOfYear, dayOfMonth, hourOfDay, minute));

        if (mService.isTodoItemValid(todoItem)) {
            mService.editTodoItem(todoItem);
            mView.showTodoItemEdited();
            mView.close();
        }else {
            mView.showMissingTitle();
        }
    }

    @Override
    public void openDeleteConfirmationDialog() {
        mView.showDeleteConfirmationDialog();
    }

    @Override
    public void deleteTodoItem(long todoItemId) {
        verifyView();

        TodoItem todoItem = mService.getTodoItem(todoItemId);
        mService.deleteTodoItem(todoItem);
        mView.close();
    }

    private void verifyView() {
        if (mView == null) {
            throw new IllegalStateException("View is unbinded");
        }
    }


    @Override
    public void bindView(@NonNull EditTodoItemMvp.View view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
