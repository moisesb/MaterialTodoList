package com.borges.moises.materialtodolist.todoitems.view;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.mvp.View;

import java.util.List;

/**
 * Created by moises.anjos on 10/05/2016.
 */
public interface TodoItemsView extends View {
    void showTodoItems(List<TodoItem> todoItems);

    void showTodoItem(TodoItem todoItem);

    void removeTodoItem(TodoItem todoItem);

    void openTodoItemDetails(long todoItemId);

    void openNewTodoItem();

    void showNoTodoItemMessage();

    void showUndoDeleteOption(TodoItem todoItem);
}
