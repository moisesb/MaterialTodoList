package com.borges.moises.materialtodolist.data.repository;

import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.specification.Specification;

import java.util.List;

/**
 * Created by Moisés on 11/04/2016.
 */
public interface TodoItemRepository {
    boolean addTodoItem(@NonNull TodoItem todoItem);
    boolean updateTodoItem(@NonNull TodoItem todoItem);
    boolean removeTodoItem(@NonNull TodoItem todoItem);
    TodoItem getTodoItem(long todoItemid);
    List<TodoItem> query(Specification specification);
}
