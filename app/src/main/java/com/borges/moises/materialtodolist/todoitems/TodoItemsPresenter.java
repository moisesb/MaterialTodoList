package com.borges.moises.materialtodolist.todoitems;

import android.os.Bundle;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.services.TodoItemService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public static final String ARGS_TODO_ITEMS = "TodoItemsPresenter.mTodoItems";
    private TodoItemsMvp.View mView;
    private TodoItemService mService;
    private Subscription mTodoItemsSubscription;
    private Subscription mRemainingTodoItemsSubscription;
    private List<TodoItem> mTodoItems;

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
                        if (mTodoItems == null) {
                            if (todoItems.size() == 0) {
                                mView.showNoTodoItemMessage();
                            } else {
                                mView.showTodoItems(todoItems);
                                mTodoItems = todoItems;
                                Collections.sort(mTodoItems,TodoItem.comparator());
                            }
                        }

                        mView.showProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TodoItem todoItem) {
                        if (mTodoItems != null) {
                            int pos = Collections.binarySearch(mTodoItems,todoItem,TodoItem.comparator());
                            if (pos < 0) {
                                mTodoItems.add(todoItem);
                                mView.showTodoItem(todoItem);
                                Collections.sort(mTodoItems,TodoItem.comparator());
                            }else {
                                if (todoItem.isDeleted() && todoItem.getVersion() > mTodoItems.get(pos).getVersion()){
                                    mView.removeTodoItem(todoItem);
                                    replaceTodoItem(todoItem, pos);
                                }else if (todoItem.getVersion() > mTodoItems.get(pos).getVersion()){
                                    mView.updateTodoItem(todoItem);
                                    replaceTodoItem(todoItem,pos);
                                }
                            }
                        } else {
                            todoItems.add(todoItem);
                        }
                    }

                    private void replaceTodoItem(TodoItem todoItem, int pos) {
                        mTodoItems.remove(pos);
                        mTodoItems.add(pos,todoItem);
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARGS_TODO_ITEMS, (Serializable) mTodoItems);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mTodoItems = (List<TodoItem>) savedInstanceState.getSerializable(ARGS_TODO_ITEMS);
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
