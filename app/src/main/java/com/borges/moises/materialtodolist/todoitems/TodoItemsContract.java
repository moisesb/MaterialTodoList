package com.borges.moises.materialtodolist.todoitems;

import com.borges.moises.materialtodolist.data.model.TodoItem;

import java.util.List;

/**
 * Created by Moisés on 11/04/2016.
 */
public interface TodoItemsContract {

    interface View {
        void showTodoItems(List<TodoItem> todoItems);
        void showTodoItem(TodoItem todoItem);
        void removeTodoItem(TodoItem todoItem);
        void openTodoItemDetails(long todoItemId);
        void openNewTodoItem();
        void showNoTodoItemMessage();
        void showUndoDeleteOption(TodoItem todoItem);
    }

    interface PresenterOps {
        void loadTodoItems();
        void deleteTodoItem(TodoItem todoItem);
        void undoDelete(TodoItem todoItem);
        void doneTodoItem(TodoItem todoItem, boolean done);
        void addNewTodoItem();
        void openTodoItem(TodoItem todoItem);
        void onDestroy();
    }
}
