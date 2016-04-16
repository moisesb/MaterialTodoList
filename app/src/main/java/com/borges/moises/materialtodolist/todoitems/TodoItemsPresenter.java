package com.borges.moises.materialtodolist.todoitems;

import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.TodoItem;

import java.util.List;

/**
 * Created by Moisés on 13/04/2016.
 */
public class TodoItemsPresenter implements TodoItemsContract.PresenterOps, TodoItemsContract.RequestedPresenterOps {

    private TodoItemsContract.View mView;
    private TodoItemsContract.Model mModel;

    public TodoItemsPresenter(@NonNull TodoItemsContract.View view) {
        mView = view;
        mModel = new TodoItemsModel(this);
    }

    @Override
    public void loadTodoItems() {
        mModel.loadTodoItems();
    }

    @Override
    public void addNewTodoItem() {
        mView.showNewTodoItem();
    }

    @Override
    public void onDestroy() {
        mModel.onDestroy();
        mView = null;
    }

    @Override
    public void onTodoItemsLoaded(List<TodoItem> todoItems) {
        mView.showTodoItems(todoItems);
    }

    @Override
    public void onNoneTodoItemFound() {
        mView.showNoTodoItemMessage();
    }
}
