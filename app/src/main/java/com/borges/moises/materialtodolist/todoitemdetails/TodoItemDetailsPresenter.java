package com.borges.moises.materialtodolist.todoitemdetails;

import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoItemRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemRepository;

import java.util.Date;

/**
 * Created by Moisés on 24/04/2016.
 */
public class TodoItemDetailsPresenter implements TodoItemDetailsContract.PresenterOps {

    private TodoItemDetailsContract.View mView;
    private TodoItemRepository mRepository;

    public TodoItemDetailsPresenter(TodoItemDetailsContract.View view) {
        mView = view;
        mRepository = SqliteTodoItemRepository.getInstance();
    }

    @Override
    public void openTodoItem(long todoItemId) {
        TodoItem todoItem = mRepository.getTodoItem(todoItemId);
        mView.showTitle(todoItem.getTitle());
        mView.showDescription(todoItem.getDescription());
        mView.showDate(todoItem.getDate());
        mView.showUrgent(todoItem.getPriority() == Priority.HIGH);
    }

    @Override
    public void editTodoItem(long todoItemId, String title, String description, boolean urgent, Date date, String time) {
        // TODO: 06/05/2016 Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteTodoItem(long todoItemId) {
        TodoItem todoItem = mRepository.getTodoItem(todoItemId);
        mRepository.removeTodoItem(todoItem);
        mView.close();
    }

    @Override
    public void onDestroy() {
        mView = null;
    }
}
