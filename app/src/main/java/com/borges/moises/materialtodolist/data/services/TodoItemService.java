package com.borges.moises.materialtodolist.data.services;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoItemRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemRepository;

/**
 * Created by moises.anjos on 06/05/2016.
 */
public class TodoItemService {

    private TodoItemRepository mRepository;

    public TodoItemService() {
        mRepository = SqliteTodoItemRepository.getInstance();
    }

    public void addTodoItem(TodoItem todoItem) {
        if (!mRepository.addTodoItem(todoItem)) {
            throw new IllegalStateException("Todo item was not added in the database");
        }
    }

    public void editTodoItem(TodoItem todoItem) {
        if (!mRepository.updateTodoItem(todoItem)) {
            throw new IllegalStateException("Todo item was not updated in the database");
        }
    }

    public void deleteTodoItem(TodoItem todoItem) {
        if (!mRepository.removeTodoItem(todoItem)) {
            throw new IllegalStateException("Todo item was not deleted from database");
        }
    }

    public boolean isTodoItemValid(TodoItem todoItem) {
        return !(todoItem.getTitle() == null || todoItem.getTitle().isEmpty());
    }

    public TodoItem getTodoItem(long todoItemId) {
        return mRepository.getTodoItem(todoItemId);
    }
}
