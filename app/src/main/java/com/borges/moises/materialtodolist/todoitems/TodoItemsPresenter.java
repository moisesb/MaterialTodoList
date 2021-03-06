package com.borges.moises.materialtodolist.todoitems;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.services.TodoItemService;

import java.util.ArrayList;
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
    public void loadTodoItems(final Long tag) {
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
                .filter(new Func1<TodoItem, Boolean>() {
                    @Override
                    public Boolean call(TodoItem todoItem) {
                        return tag == null || tag < 0 || todoItem.getTagId().equals(tag);
                    }
                })
                .subscribe(new Subscriber<TodoItem>() {
                    private List<TodoItem> todoItems = new ArrayList<TodoItem>();

                    @Override
                    public void onCompleted() {
                        if (todoItems.size() == 0) {
                            mView.showNoTodoItemMessage();
                        } else {
                            mView.showTodoItems(todoItems);
                        }

                        mView.showProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TodoItem todoItem) {
                        todoItems.add(todoItem);
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
        removeTodoItemFromView(todoItem);
    }

    private void removeTodoItemFromView(TodoItem todoItem) {
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
        checkView();
        mService.recycleTodoItem(todoItem);
        mView.showTodoItem(todoItem);
    }

    @Override
    public void doneTodoItem(TodoItem todoItem, boolean done) {
        checkView();
        mService.markTodoItemAsDone(todoItem, done);
        mView.updateTodoItem(todoItem);
    }

    @Override
    public void changeStarred(TodoItem todoItem) {
        checkView();
        mService.changeStarredStatus(todoItem);
        mView.updateTodoItem(todoItem);
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
    public void deleteTodoItem(long taskId) {
        final TodoItem todoItem = mService.getTodoItem(taskId);
        if (todoItem != null) {
            removeTodoItemFromView(todoItem);
        }
    }

    @Override
    public void loadTodoItem(long todoItemId) {
        final TodoItem todoItem = mService.getTodoItem(todoItemId);
        if (todoItem != null) {
            mView.showTodoItem(todoItem);
        }
    }

    @Override
    public void updateTodoItem(long todoItemId) {
        final TodoItem todoItem = mService.getTodoItem(todoItemId);
        if (todoItem != null) {
            mView.updateTodoItem(todoItem);
        }
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
