package com.borges.moises.materialtodolist.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.borges.moises.materialtodolist.data.firebase.FirebaseRef;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.model.User;
import com.borges.moises.materialtodolist.data.services.TodoItemService;
import com.borges.moises.materialtodolist.data.services.UserService;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Moisés on 26/05/2016.
 */
public class SyncService extends IntentService {

    private static final String NAME = "sync_service";
    private final FirebaseRef ref = new FirebaseRef();

    public SyncService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final UserService userService = new UserService(getApplicationContext());
        final User user = userService.getLoggedUser();
        if (user == null || user.getUid() == null) {
            return;
        }
        ref.userTodoItems(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                syncTodoItemsWithServer(dataSnapshot, user);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void syncTodoItemsWithServer(DataSnapshot dataSnapshot, User user) {
        final TodoItemService todoItemService = new TodoItemService();
        if (dataSnapshot.exists()) {
            List<TodoItem> todoItemsFromServer = new ArrayList<>();
            for (DataSnapshot todoItemSnapshot : dataSnapshot.getChildren()) {
                TodoItem todoItem = todoItemSnapshot.getValue(TodoItem.class);
                todoItem.setServerId(todoItemSnapshot.getKey());
                todoItemsFromServer.add(todoItem);
            }
            solveConflicts(todoItemService, todoItemsFromServer, user);
        } else {
            sendTodoItemsToServer(todoItemService, user);
        }
    }

    private void solveConflicts(final TodoItemService todoItemService, final List<TodoItem> todoItemsFromServer, final User user) {
        final Comparator<TodoItem> comparator = new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem lhs, TodoItem rhs) {
                return lhs.getServerId().compareTo(rhs.getServerId());
            }
        };
        Collections.sort(todoItemsFromServer, comparator);
        todoItemService.getTodoItems()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<List<TodoItem>, Observable<TodoItem>>() {
                    @Override
                    public Observable<TodoItem> call(List<TodoItem> todoItems) {
                        return Observable.from(todoItems);
                    }
                })
                .subscribe(new Subscriber<TodoItem>() {
                    @Override
                    public void onCompleted() {
                        for (TodoItem todoItemFromServe : todoItemsFromServer) {
                            todoItemService.addTodoItem(todoItemFromServe);
                        }
                        Log.d(NAME,"conflicts solved");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(NAME,"Error on solve conflicts between todo items from server and local todo items");
                    }

                    @Override
                    public void onNext(TodoItem todoItem) {
                        final int index = Collections.binarySearch(todoItemsFromServer, todoItem, comparator);
                        if (index < 0) {
                            sendTodoItemToServer(todoItemService, todoItem, user);
                        } else {
                            TodoItem todoItemFromServer = todoItemsFromServer.get(index);
                            if (todoItem.getVersion() > todoItemFromServer.getVersion() &&
                                    todoItem.isDirty()) {
                                if (todoItem.isDeleted()) {
                                    deleteTodoItemFromServer(todoItem,user);
                                    todoItemService.removeTodoItemFromDb(todoItem);
                                }else {
                                    updateTodoItemInServer(todoItem, user);
                                    todoItemService.setTodoItemSynchronized(todoItem);
                                }
                            }
                            todoItemsFromServer.remove(todoItemFromServer);
                        }
                    }


                });
    }

    private void deleteTodoItemFromServer(TodoItem todoItem, User user) {
        ref.userTodoItems(user).child(todoItem.getServerId()).removeValue();
    }

    private void updateTodoItemInServer(TodoItem todoItem, User user) {
        ref.userTodoItems(user).child(todoItem.getServerId()).setValue(todoItem);
    }

    private void sendTodoItemToServer(final TodoItemService todoItemService, TodoItem todoItem, User user) {
        final Firebase childRef = ref.userTodoItems(user).push();
        childRef.setValue(todoItem);
        final String serverId = childRef.getKey();
        todoItemService.setTodoItemSynchronized(todoItem, serverId);
    }

    private void sendTodoItemsToServer(final TodoItemService todoItemService, final User user) {
        todoItemService.getTodoItems()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<List<TodoItem>, Observable<TodoItem>>() {
                    @Override
                    public Observable<TodoItem> call(List<TodoItem> todoItems) {
                        return Observable.from(todoItems);
                    }
                })
                .subscribe(new Subscriber<TodoItem>() {
                    @Override
                    public void onCompleted() {
                        Log.d(NAME,"todo items sent to server");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(NAME,"Error on sending todo items to server");
                    }

                    @Override
                    public void onNext(TodoItem todoItem) {
                        sendTodoItemToServer(todoItemService, todoItem, user);
                    }
                });
    }

    public static void start(Context context) {
        Intent intentService = new Intent(context, SyncService.class);
        context.startService(intentService);
    }
}