package com.borges.moises.materialtodolist.addnewtodoitem;

import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.data.model.TodoItem;

import java.util.Date;

/**
 * Created by Moisés on 14/04/2016.
 */
public interface AddNewTodoItemContract {

    interface View {
        void showTodoItems();
        void showErrorMessage(String message);
        void close();
    }

    interface PresenterOps {
        void addTodoItem(String title, String description, Priority priority, Date date);
        void onDestroy();
    }

    interface RequestedPresenterOps {
        void onTodoItemAdded();
        void onError(String message);
    }

    interface Model {
        void addTodoItem(TodoItem todoItem);
        void onDestroy();
    }
}
