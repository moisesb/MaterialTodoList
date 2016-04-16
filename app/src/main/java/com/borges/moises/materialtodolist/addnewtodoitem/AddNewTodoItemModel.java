package com.borges.moises.materialtodolist.addnewtodoitem;

import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoitemRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemRepository;

/**
 * Created by Moisés on 14/04/2016.
 */
public class AddNewTodoItemModel implements  AddNewTodoItemContract.Model {

    private AddNewTodoItemContract.RequestedPresenterOps mRequestedPresenterOps;
    private TodoItemRepository mTodoItemRepository;

    public AddNewTodoItemModel(@NonNull AddNewTodoItemContract.RequestedPresenterOps requestedPresenterOps) {
        mRequestedPresenterOps = requestedPresenterOps;
        mTodoItemRepository = SqliteTodoitemRepository.getInstance();
    }

    @Override
    public void addTodoItem(TodoItem todoItem) {
        boolean todoItemAdded = mTodoItemRepository.addTodoItem(todoItem);
        if (todoItemAdded) {
            mRequestedPresenterOps.onTodoItemAdded();
        }else {
            mRequestedPresenterOps.onError("Title and Description should be present");
        }
    }

    @Override
    public void onDestroy() {
        mRequestedPresenterOps = null;
    }
}
