package com.borges.moises.materialtodolist.data.services;

import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoItemRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemRepository;
import com.borges.moises.materialtodolist.data.repository.specification.QueryAllTodoItemsSqlSpec;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by moises.anjos on 06/05/2016.
 */
public class TodoItemService {

    private TodoItemRepository mRepository;

    public TodoItemService() {
        mRepository = SqliteTodoItemRepository.getInstance();
    }

    public Observable<List<TodoItem>> getTodoItems(){
        return Observable.just(getTodoItemsFromDb());
    }

    public void addTodoItem(TodoItem todoItem) {
        final Date currentTime = getCurrentTime();
        todoItem.setCreateAt(currentTime);
        todoItem.setUpdateAt(currentTime);
        if (!mRepository.addTodoItem(todoItem)) {
            throw new IllegalStateException("Todo item was not added in the database");
        }
    }

    public void editTodoItem(TodoItem todoItem) {
        final Date currentTime = getCurrentTime();
        todoItem.setUpdateAt(currentTime);
        if (!mRepository.updateTodoItem(todoItem)) {
            throw new IllegalStateException("Todo item was not updated in the database");
        }
    }

    public void markTodoItemAsDone(TodoItem todoItem, boolean done){
        final Date currentTime = getCurrentTime();
        todoItem.setDone(done);
        todoItem.setDoneAt(done? currentTime: null);
        todoItem.setUpdateAt(currentTime);
        if (!mRepository.updateTodoItem(todoItem)) {
            throw new IllegalStateException("Todo item was not deleted from database");
        }
    }

    public void deleteTodoItem(TodoItem todoItem) {
        final Date currentTime = getCurrentTime();
        todoItem.setDeleted(true);
        todoItem.setUpdateAt(currentTime);
        if (!mRepository.updateTodoItem(todoItem)) {
            throw new IllegalStateException("Todo item was not deleted from database");
        }
    }

    public void recycleTodoItem(TodoItem todoItem){
        final Date currentTime = getCurrentTime();
        todoItem.setDeleted(false);
        todoItem.setUpdateAt(currentTime);
        if (!mRepository.updateTodoItem(todoItem)) {
            throw new IllegalStateException("Todo item was not deleted from database");
        }
    }

    public boolean isTodoItemValid(TodoItem todoItem) {
        return !(todoItem.getTitle() == null || todoItem.getTitle().isEmpty());
    }

    public TodoItem getTodoItem(long todoItemId) {
        return mRepository.getTodoItem(todoItemId);
    }

    private List<TodoItem> getTodoItemsFromDb() {
        List<TodoItem> todoItems = mRepository.query(new QueryAllTodoItemsSqlSpec());
        return todoItems;
    }

    private Date getCurrentTime(){
        return Calendar.getInstance().getTime();
    }
}
