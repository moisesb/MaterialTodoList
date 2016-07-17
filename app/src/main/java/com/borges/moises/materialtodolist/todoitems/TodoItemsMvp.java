package com.borges.moises.materialtodolist.todoitems;

import android.os.Bundle;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.mvp.MvpPresenter;
import com.borges.moises.materialtodolist.mvp.MvpView;

import java.util.List;

/**
 * Created by moises.anjos on 24/05/2016.
 */
public interface TodoItemsMvp {
    interface View extends MvpView {
        void showTodoItem(TodoItem todoItem);

        void removeTodoItem(TodoItem todoItem);

        void openTodoItemDetails(long todoItemId);

        void openNewTodoItem();

        void showNoTodoItemMessage();

        void showUndoDeleteOption(TodoItem todoItem);

        void showProgress(boolean loading);

        void clearTodoItems();

        void updateTodoItem(TodoItem todoItem);

        void showTodoItems(List<TodoItem> todoItems);
    }

    interface Presenter extends MvpPresenter<View> {
        void loadTodoItems(Long tag);

        void deleteTodoItem(TodoItem todoItem);

        void undoDelete(TodoItem todoItem);

        void doneTodoItem(TodoItem todoItem, boolean done);

        void changeStarred(TodoItem todoItem);

        void addNewTodoItem();

        void openTodoItem(TodoItem todoItem);

        void onDestroy();

        void onSaveInstanceState(Bundle outState);

        void onRestoreInstanceState(Bundle savedInstanceState);
    }
}
