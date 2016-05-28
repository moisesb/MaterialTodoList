package com.borges.moises.materialtodolist.data.firebase;

import com.borges.moises.materialtodolist.data.model.User;
import com.firebase.client.Firebase;

/**
 * Created by Moisés on 28/05/2016.
 */
public class FirebaseRef {
    private static final String ENDPOINT = "https://material-todo-list.firebaseio.com/";
    private static final String USERS = "users";
    private static final String TODO_ITEMS = "todo_items";

    private Firebase ref = new Firebase(ENDPOINT);

    public FirebaseRef() {
    }

    public Firebase users(){
        return ref.child(USERS);
    }

    public Firebase userTodoItems(User user){
        return ref.child(TODO_ITEMS).child(user.getUid());
    }
}
