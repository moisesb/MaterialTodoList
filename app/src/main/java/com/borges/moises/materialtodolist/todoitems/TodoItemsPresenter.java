package com.borges.moises.materialtodolist.todoitems;

import android.util.Log;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.services.TodoItemService;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Moisés on 13/04/2016.
 */
public class TodoItemsPresenter implements TodoItemsMvp.Presenter {

    private TodoItemsMvp.View mView;
    private TodoItemService mService;
    private Subscription mTodoItemsSubscription;
    private Subscription mRemainingTodoItemsSubscription;

    public TodoItemsPresenter() {
        mService = new TodoItemService();
    }

    @Override
    public void loadTodoItems() {
        checkView();
        mView.clearTodoItems();
        mView.showProgress(true);
        mTodoItemsSubscription = mService.getTodoItems()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<TodoItem>, Observable<TodoItem>>() {
                    @Override
                    public Observable<TodoItem> call(List<TodoItem> todoItems) {
                        return Observable.from(todoItems);
                    }
                })
                .filter(new Func1<TodoItem, Boolean>() {
                    @Override
                    public Boolean call(TodoItem todoItem) {
                        return !todoItem.isDeleted();
                    }
                })
                .subscribe(new Subscriber<TodoItem>() {
                    private boolean hasTodoItems = false;

                    @Override
                    public void onCompleted() {
                        if (!hasTodoItems) {
                            mView.showNoTodoItemMessage();
                        }
                        mView.showProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TodoItem todoItem) {
                        Log.d("TodoItemsPresenter", "showing " + todoItem.getTitle());
                        mView.showTodoItem(todoItem);
                        hasTodoItems = true;
                    }
                });
    }

    private void checkView() {
        if (mView == null) {
            throw new IllegalStateException("Should bind view first");
        }
    }

    @Override
    public void deleteTodoItem(final TodoItem todoItem) {
        mService.deleteTodoItem(todoItem);
        mView.removeTodoItem(todoItem);
        mView.showUndoDeleteOption(todoItem);

        mRemainingTodoItemsSubscription = mService.getTodoItems()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<TodoItem>, Observable<TodoItem>>() {
                    @Override
                    public Observable<TodoItem> call(List<TodoItem> todoItems) {
                        return Observable.from(todoItems);
                    }
                })
                .filter(new Func1<TodoItem, Boolean>() {
                    @Override
                    public Boolean call(TodoItem todoItem) {
                        return !todoItem.isDeleted();
                    }
                })
                .subscribe(new Subscriber<TodoItem>() {
                    private boolean hasTodoItem = false;

                    @Override
                    public void onCompleted() {
                        if (!hasTodoItem) {
                            mView.showNoTodoItemMessage();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TodoItem todoItem) {
                        hasTodoItem = true;
                    }
                });
    }

    @Override
    public void undoDelete(TodoItem todoItem) {
        mService.recycleTodoItem(todoItem);
        mView.showTodoItem(todoItem);
    }

    @Override
    public void doneTodoItem(TodoItem todoItem, boolean done) {
        mService.markTodoItemAsDone(todoItem, done);
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
        if (mTodoItemsSubscription != null &&
                !mTodoItemsSubscription.isUnsubscribed()) {
            mTodoItemsSubscription.unsubscribe();
        }
        if (mRemainingTodoItemsSubscription != null &&
                !mRemainingTodoItemsSubscription.isUnsubscribed()) {
            mRemainingTodoItemsSubscription.unsubscribe();
        }
        mView = null;
    }
}
