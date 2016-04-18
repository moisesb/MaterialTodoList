package com.borges.moises.materialtodolist.todoitemdetails;

import com.borges.moises.materialtodolist.data.model.TodoItem;

/**
 * Created by Moisés on 16/04/2016.
 */
public interface TodoItemDetailsContract {
    interface View {

    }

    interface PresenterOps {
        void openTodoItem(TodoItem todoItem);
        void editTodoItem(TodoItem todoItem);
        void deleteTodoItem();
    }
}
