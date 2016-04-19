package com.borges.moises.materialtodolist.addtodoitem;

import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoItemRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemRepository;
import com.borges.moises.materialtodolist.helpers.TodoItemHelper;
import com.borges.moises.materialtodolist.helpers.TodoItemHelperImpl;

import java.util.Date;

/**
 * Created by Moisés on 14/04/2016.
 */
public class AddTodoItemPresenter implements AddTodoItemContract.PresenterOps {

    private AddTodoItemContract.View mView;
    private TodoItemRepository mTodoItemRepository;
    private TodoItemHelper mTodoItemHelper = new TodoItemHelperImpl();
    private TodoItemHelper.View helperView = new TodoItemHelper.View() {
        @Override
        public void showMissingTitle() {
            mView.showMissingTitle();
        }

        @Override
        public void showInvalidDate() {
            mView.showDateInThePast();
        }
    };

    public AddTodoItemPresenter(@NonNull AddTodoItemContract.View view) {
        mView = view;
        mTodoItemRepository = SqliteTodoItemRepository.getInstance();
    }

    @Override
    public void addTodoItem(String title, String description, boolean urgent, Date date, String time) {
        TodoItem todoItem = new TodoItem(title,description,date,urgent);

        if (mTodoItemHelper.todoItemIsValid(todoItem,helperView)) {
            mTodoItemRepository.addTodoItem(todoItem);
            mView.close();
        }
    }


    @Override
    public void onDestroy() {
        helperView = null;
        mView = null;
    }

}
