package com.borges.moises.materialtodolist.addnewtodoitem;

import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.data.model.TodoItem;

import java.util.Date;

/**
 * Created by Moisés on 14/04/2016.
 */
public class AddNewTodoItemPresenter implements AddNewTodoItemContract.PresenterOps, AddNewTodoItemContract.RequestedPresenterOps {

    private AddNewTodoItemContract.Model mModel;
    private AddNewTodoItemContract.View mView;

    public AddNewTodoItemPresenter(@NonNull AddNewTodoItemContract.View view) {
        mView = view;
        mModel = new AddNewTodoItemModel(this);
    }

    @Override
    public void addTodoItem(String title, String description, Priority priority, Date date) {
        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(title);
        todoItem.setDescription(description);
        todoItem.setPriority(priority);
        todoItem.setDate(date);

        mModel.addTodoItem(todoItem);
    }

    @Override
    public void onDestroy() {
        mModel.onDestroy();
        mModel = null;
    }

    @Override
    public void onTodoItemAdded() {
        mView.showTodoItems();
    }

    @Override
    public void onError(String message) {
        mView.showErrorMessage(message);
    }
}
