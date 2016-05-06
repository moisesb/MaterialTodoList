package com.borges.moises.materialtodolist.addtodoitem.presenter;

import com.borges.moises.materialtodolist.addtodoitem.view.AddTodoItemView;
import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoItemRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemRepository;

/**
 * Created by Moisés on 14/04/2016.
 */
public class AddTodoItemPresenterImpl implements AddTodoItemPresenter {

    private AddTodoItemView mView;
    private TodoItemRepository mTodoItemRepository;

    public AddTodoItemPresenterImpl() {
        mTodoItemRepository = SqliteTodoItemRepository.getInstance();
    }

    @Override
    public void addTodoItem(String title, String description, Priority priority, String location, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {

        if (mView == null) {
            throw new IllegalStateException("should bind view first!");
        }

        if (title == null || title.isEmpty()) {
            mView.showMissingTitle();
            return;
        }

        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(title);
        todoItem.setDescription(description);
        todoItem.setPriority(priority == null? Priority.NORMAL : priority);
        todoItem.setLocation(location);

        mTodoItemRepository.addTodoItem(todoItem);
        mView.close();

    }


    @Override
    public void bindView(AddTodoItemView view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
