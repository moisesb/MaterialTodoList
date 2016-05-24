package com.borges.moises.materialtodolist.todoitems;

import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.services.TodoItemService;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Moisés on 13/04/2016.
 */
public class TodoItemsPresenter implements TodoItemsMvp.Presenter {

    private TodoItemsMvp.View mView;
    private TodoItemService mService;

    public TodoItemsPresenter() {
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
    public void bindView(TodoItemsMvp.View view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
