package com.borges.moises.materialtodolist.todoitems.presenter;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.mvp.MvpPresenter;
import com.borges.moises.materialtodolist.todoitems.view.TodoItemsMvpView;

/**
 * Created by moises.anjos on 10/05/2016.
 */
public interface TodoItemsMvpPresenter extends MvpPresenter<TodoItemsMvpView> {
    void loadTodoItems();

    void deleteTodoItem(TodoItem todoItem);

    void undoDelete(TodoItem todoItem);

    void doneTodoItem(TodoItem todoItem, boolean done);

    void addNewTodoItem();

    void openTodoItem(TodoItem todoItem);

    void onDestroy();
}
