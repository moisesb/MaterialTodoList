package com.borges.moises.materialtodolist.todoitems;

import com.borges.moises.materialtodolist.data.model.TodoItem;

import java.util.List;

/**
 * Created by Moisés on 11/04/2016.
 */
public interface TodoItemsContract {

    interface View {
        void showTodoItems(List<TodoItem> todoItems);
        void showNewTodoItem();
        void showNoTodoItemMessage();
    }

    interface PresenterOps {
        void loadTodoItems();
        void addNewTodoItem();
        void onDestroy();
    }

    interface RequestedPresenterOps {
        void onTodoItemsLoaded(List<TodoItem> todoItems);
        void onNoneTodoItemFound();
    }

    interface Model {
        void loadTodoItems();
        void onDestroy();
    }
}
