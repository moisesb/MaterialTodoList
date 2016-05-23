package com.borges.moises.materialtodolist.addtodoitem.presenter;

import com.borges.moises.materialtodolist.addtodoitem.view.AddTodoItemMvpView;
import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.services.TodoItemService;
import com.borges.moises.materialtodolist.utils.DateUtils;

/**
 * Created by Moisés on 14/04/2016.
 */
public class AddTodoItemMvpPresenterImpl implements AddTodoItemMvpPresenter {

    private AddTodoItemMvpView mView;
    private TodoItemService mService;

    public AddTodoItemMvpPresenterImpl() {
        mService = new TodoItemService();
    }

    @Override
    public void addTodoItem(String title, String description, Priority priority, String location, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {

        if (mView == null) {
            throw new IllegalStateException("should bind LoginView first!");
        }

        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(title);
        todoItem.setDescription(description);
        todoItem.setPriority(priority == null? Priority.NORMAL : priority);
        todoItem.setLocation(location);
        todoItem.setDate(DateUtils.getDate(year, monthOfYear, dayOfMonth, hourOfDay, minute));

        if (mService.isTodoItemValid(todoItem)) {
            mService.addTodoItem(todoItem);
            mView.showTodoItemAdded();
            mView.close();
        }else {
            mView.showMissingTitle();
        }
    }


    @Override
    public void bindView(AddTodoItemMvpView view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
