package com.borges.moises.materialtodolist.todoitems.presenter;

import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoItemRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemRepository;
import com.borges.moises.materialtodolist.data.repository.specification.QueryAllTodoItemsSqlSpecification;
import com.borges.moises.materialtodolist.data.services.TodoItemService;
import com.borges.moises.materialtodolist.todoitems.view.TodoItemsView;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Moisés on 13/04/2016.
 */
public class TodoItemsPresenterImpl implements TodoItemsPresenter {

    private TodoItemsView mView;
    private TodoItemService mService;

    public TodoItemsPresenterImpl(@NonNull TodoItemsView view) {
        mView = view;
        mService = new TodoItemService();
    }

    @Override
    public void loadTodoItems() {

        mService.getTodoItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TodoItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<TodoItem> todoItems) {
                        if (todoItems.size() > 0) {
                            mView.showTodoItems(todoItems);
                        }else {
                            mView.showNoTodoItemMessage();
                        }
                    }
                });
    }

    @Override
    public void deleteTodoItem(final TodoItem todoItem) {
        mService.deleteTodoItem(todoItem);
        mView.removeTodoItem(todoItem);
        mView.showUndoDeleteOption(todoItem);

        mService.getTodoItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TodoItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<TodoItem> todoItems) {
                        if (todoItems.size() == 0) {
                            mView.showNoTodoItemMessage();
                        }
                    }
                });
    }

    @Override
    public void undoDelete(TodoItem todoItem) {
        mService.addTodoItem(todoItem);
        mView.showTodoItem(todoItem);
    }

    @Override
    public void doneTodoItem(TodoItem todoItem, boolean done) {
        todoItem.setCompleted(done);
        mService.editTodoItem(todoItem);
    }

    @Override
    public void addNewTodoItem() {
        mView.openNewTodoItem();
    }

    @Override
    public void openTodoItem(TodoItem todoItem) {
        mView.openTodoItemDetails(todoItem.getId());
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void bindView(TodoItemsView view) {

    }

    @Override
    public void unbindView() {

    }
}
