package com.borges.moises.materialtodolist.data.services;

import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoItemsRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemsRepository;
import com.borges.moises.materialtodolist.data.repository.specification.QueryAllTodoItemsSqlSpec;
import com.borges.moises.materialtodolist.data.repository.specification.Specification;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by moises.anjos on 06/05/2016.
 */
public class TodoItemService {

    private TodoItemsRepository mRepository;

    public TodoItemService() {
        mRepository = SqliteTodoItemsRepository.getInstance();
    }

    public Observable<List<TodoItem>> getTodoItems() {
        return Observable.just(getTodoItemsFromDb());
    }

    public Observable<List<TodoItem>> getTodoItems(Specification specification) {
        return Observable.just(getTodoItemsFromDb(specification));
    }



    public void addTodoItem(TodoItem todoItem) {
        final Date currentTime = getCurrentTime();
        todoItem.setCreatedAt(currentTime);
        todoItem.setUpdatedAt(currentTime);
        if (!mRepository.addTodoItem(todoItem)) {
            throw new IllegalStateException("Todo item was not added in the database");
        }
    }

    public void setTodoItemSynchronized(TodoItem todoItem, String serverId) {
        todoItem.setServerId(serverId);
        todoItem.setDirty(false);
        if (!mRepository.updateTodoItem(todoItem)) {
            throw new IllegalStateException("could not add server id");
        }
    }

    public void setTodoItemSynchronized(TodoItem todoItem) {
        todoItem.setDirty(false);
        if (!mRepository.updateTodoItem(todoItem)) {
            throw new IllegalStateException("could synchronize");
        }
    }

    public void editTodoItem(TodoItem todoItem) {
        updateTodoItemInDb(todoItem, "Todo item was not updated in the database");
    }

    private void updateTodoItemInDb(TodoItem todoItem, String detailMessage) {
        final Date currentTime = getCurrentTime();
        todoItem.setUpdatedAt(currentTime);
        todoItem.incrementVersion();
        todoItem.setDirty(true);
        if (!mRepository.updateTodoItem(todoItem)) {
            throw new IllegalStateException(detailMessage);
        }
    }

    public void markTodoItemAsDone(TodoItem todoItem, boolean done) {
        final Date currentTime = getCurrentTime();
        todoItem.setDone(done);
        todoItem.setDoneAt(done ? currentTime : null);
        updateTodoItemInDb(todoItem, "Todo item was not updated from database");
    }

    public void changeStarredStatus(TodoItem todoItem) {
        todoItem.setPriority(todoItem.getPriority() == Priority.NORMAL ? Priority.HIGH : Priority.NORMAL);
        updateTodoItemInDb(todoItem, "Todo item was not updated from database");
    }

    public void deleteTodoItem(TodoItem todoItem) {
        todoItem.setDeleted(true);
        updateTodoItemInDb(todoItem, "Todo item was not deleted from database");
    }

    public void recycleTodoItem(TodoItem todoItem) {
        todoItem.setDeleted(false);
        updateTodoItemInDb(todoItem, "Todo item was not deleted from database");
    }

    public boolean isTodoItemValid(TodoItem todoItem) {
        return !(todoItem.getTitle() == null || todoItem.getTitle().isEmpty());
    }

    public TodoItem getTodoItem(long todoItemId) {
        return mRepository.getTodoItem(todoItemId);
    }

    private List<TodoItem> getTodoItemsFromDb() {
        return mRepository.query(new QueryAllTodoItemsSqlSpec());
    }

    private List<TodoItem> getTodoItemsFromDb(Specification specification) {
        return mRepository.query(specification);
    }

    private Date getCurrentTime() {
        return Calendar.getInstance().getTime();
    }

    public void removeTodoItemFromDb(TodoItem todoItem) {
        if (!mRepository.removeTodoItem(todoItem)){
            throw new IllegalStateException("could not delete todo item from database");
        }
    }

}
