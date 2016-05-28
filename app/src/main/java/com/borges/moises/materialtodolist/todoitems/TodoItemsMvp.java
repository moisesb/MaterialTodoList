package com.borges.moises.materialtodolist.todoitems;

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
    }

    interface Presenter extends MvpPresenter<View> {
        void loadTodoItems();

        void deleteTodoItem(TodoItem todoItem);

        void undoDelete(TodoItem todoItem);

        void doneTodoItem(TodoItem todoItem, boolean done);

        void addNewTodoItem();

        void openTodoItem(TodoItem todoItem);

        void onDestroy();
    }
}
