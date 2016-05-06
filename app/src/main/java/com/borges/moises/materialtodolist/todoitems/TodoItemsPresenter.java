package com.borges.moises.materialtodolist.todoitems;

import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoItemRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemRepository;
import com.borges.moises.materialtodolist.data.repository.specification.QueryAllTodoItemsSqlSpecification;

import java.util.List;

/**
 * Created by Moisés on 13/04/2016.
 */
public class TodoItemsPresenter implements TodoItemsContract.PresenterOps {

    private TodoItemsContract.View mView;
    private TodoItemRepository mTodoItemRepository;

    public TodoItemsPresenter(@NonNull TodoItemsContract.View view) {
        mView = view;
        mTodoItemRepository = SqliteTodoItemRepository.getInstance();
    }

    @Override
    public void loadTodoItems()    {
        List<TodoItem> todoItems = mTodoItemRepository.query(new QueryAllTodoItemsSqlSpecification());
        if (todoItems.size() > 0) {
            mView.showTodoItems(todoItems);
        }else {
            mView.showNoTodoItemMessage();
        }
    }

    @Override
    public void deleteTodoItem(TodoItem todoItem) {
        boolean removed = mTodoItemRepository.removeTodoItem(todoItem);
        if (removed) {
            mView.removeTodoItem(todoItem);
            mView.showUndoDeleteOption(todoItem);
        }
        // TODO: 06/05/2016 if que list if empty, view should change
    }

    @Override
    public void undoDelete(TodoItem todoItem) {
        boolean inserted = mTodoItemRepository.addTodoItem(todoItem);
        if (inserted) {
            mView.showTodoItem(todoItem);
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
        mView = null;
    }
}
