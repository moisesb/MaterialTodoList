package com.borges.moises.materialtodolist.todoitemdetails;

import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoItemRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemRepository;
import com.borges.moises.materialtodolist.helpers.TodoItemHelper;
import com.borges.moises.materialtodolist.helpers.TodoItemHelperImpl;
import com.borges.moises.materialtodolist.utils.DateUtils;

import java.util.Date;

/**
 * Created by Moisés on 24/04/2016.
 */
public class TodoItemDetailsPresenter implements TodoItemDetailsContract.PresenterOps {

    private TodoItemDetailsContract.View mView;
    private TodoItemRepository mRepository;

    private TodoItemHelper mTodoItemHelper = new TodoItemHelperImpl();
    private TodoItemHelper.View mHelperView = new TodoItemHelper.View() {
        @Override
        public void showMissingTitle() {
            mView.showMissingTitle();
        }

        @Override
        public void showInvalidDate() {
            mView.showDateInThePast();
        }
    };

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
        mView.showUrgent(todoItem.getPriority() == Priority.URGENT);
    }

    @Override
    public void editTodoItem(long todoItemId, String title, String description, boolean urgent, Date date, String time) {
        TodoItem todoItem = new TodoItem(title,description,date,urgent);
        todoItem.setId(todoItemId);
        if (mTodoItemHelper.todoItemIsValid(todoItem,mHelperView)) {
            mRepository.updateTodoItem(todoItem);
            mView.close();
        }
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
