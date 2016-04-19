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
public class TodoItemsModel implements TodoItemsContract.Model {

    private TodoItemsContract.RequestedPresenterOps mRequestedPresenterOps;
    private TodoItemRepository mTodoItemRepository = SqliteTodoItemRepository.getInstance();

    public TodoItemsModel(@NonNull TodoItemsContract.RequestedPresenterOps requestedPresenterOps) {
        mRequestedPresenterOps = requestedPresenterOps;
    }

    @Override
    public void loadTodoItems() {
        if (mRequestedPresenterOps == null) {
            return;
        }

        List<TodoItem> todoItems = mTodoItemRepository.query(new QueryAllTodoItemsSqlSpecification());
        if (todoItems.size() > 0) {
            mRequestedPresenterOps.onTodoItemsLoaded(todoItems);
        }else {
            mRequestedPresenterOps.onNoneTodoItemFound();
        }
    }

    @Override
    public void onDestroy() {
        mRequestedPresenterOps = null;
    }
}
