package com.borges.moises.materialtodolist.todolists;

import com.borges.moises.materialtodolist.data.model.TodoList;

import java.util.List;

/**
 * Created by Moisés on 28/04/2016.
 */
public interface TodoListsContract {
    interface View {
        void showTodoLists(List<TodoList> todoLists);
        void openTodoList();
        void showTodoList(long todoListId);
    }

    interface Presenter {
        void loadTodoLists();
        void openNewTodoList();
        void addTodoList(String todoListName);
        void openTodoList(TodoList todoList);
        void onDestroy();
    }
}
