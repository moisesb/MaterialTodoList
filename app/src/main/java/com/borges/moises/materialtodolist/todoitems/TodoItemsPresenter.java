package com.borges.moises.materialtodolist.todoitems;

import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoItemRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemRepository;

import java.util.List;

/**
 * Created by Moisés on 13/04/2016.
 */
public class TodoItemsPresenter implements TodoItemsContract.PresenterOps, TodoItemsContract.RequestedPresenterOps {

    private TodoItemsContract.View mView;
    private TodoItemsContract.Model mModel;
    private TodoItemRepository mTodoItemRepository;

    public TodoItemsPresenter(@NonNull TodoItemsContract.View view) {
        mView = view;
        mModel = new TodoItemsModel(this);
        mTodoItemRepository = SqliteTodoItemRepository.getInstance();
    }

    @Override
    public void loadTodoItems() {
        mModel.loadTodoItems();
    }

    @Override
    public void deleteTodoItem(TodoItem todoItem) {
        boolean removed = mTodoItemRepository.removeTodoItem(todoItem);
        if (removed) {
            mView.removeTodoItem(todoItem);
        }
    }

    @Override
    public void doneTodoItem(TodoItem todoItem, boolean done) {
        todoItem.setCompleted(done);
        mTodoItemRepository.updateTodoItem(todoItem);
    }

    @Override
    public void addNewTodoItem() {
        mView.openNewTodoItem();
    }

    @Override
    public void openTodoItem(TodoItem todoItem) {
        mView.openTodoItemDetails(todoItem.getId());
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
