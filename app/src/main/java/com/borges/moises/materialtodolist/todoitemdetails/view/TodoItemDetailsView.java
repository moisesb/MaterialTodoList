package com.borges.moises.materialtodolist.todoitemdetails.view;

import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.mvp.View;

import java.util.Date;

/**
 * Created by moises.anjos on 09/05/2016.
 */
public interface TodoItemDetailsView extends View {
    void showTitle(String title);

    void showDescription(String description);

    void showDate(Date date);

    void showPriority(Priority priority);

    void showLocation(String location);

    void showMissingTitle();

    void close();

    void showTodoItemEdited();

    void showDeleteConfirmationDialog();
}
