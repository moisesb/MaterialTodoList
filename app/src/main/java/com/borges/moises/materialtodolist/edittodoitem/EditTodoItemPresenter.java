package com.borges.moises.materialtodolist.edittodoitem;

import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTagsRepository;
import com.borges.moises.materialtodolist.data.repository.TagsRepository;
import com.borges.moises.materialtodolist.data.services.TodoItemService;
import com.borges.moises.materialtodolist.utils.DateUtils;

import static com.borges.moises.materialtodolist.edittodoitem.EditTodoItemMvp.*;

/**
 * Created by Moisés on 24/04/2016.
 */
public class EditTodoItemPresenter implements Presenter {

    private View mView;
    private TodoItemService mService;
    private TagsRepository mTagsRepository;

    public EditTodoItemPresenter() {
        mService = new TodoItemService();
        mTagsRepository = SqliteTagsRepository.getInstance();
    }

    @Override
    public void openTodoItem(long todoItemId) {
        TodoItem todoItem = mService.getTodoItem(todoItemId);
        mView.showTitle(todoItem.getTitle());
        mView.showDescription(todoItem.getDescription());
        mView.showDate(todoItem.getDate());
        mView.showPriority(todoItem.getPriority());
        mView.showLocation(todoItem.getLocation());
        mView.showTag(mTagsRepository.getTag(todoItem.getTagId()));
    }

    @Override
    public void editTodoItem(long todoItemId, String title, String description, Priority priority, String location, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, Tag tag) {
        verifyView();

        TodoItem todoItem = mService.getTodoItem(todoItemId);
        todoItem.setTitle(title);
        todoItem.setDescription(description);
        todoItem.setPriority(priority == null? Priority.NORMAL : priority);
        todoItem.setLocation(location);
        todoItem.setDate(DateUtils.getDate(year, monthOfYear, dayOfMonth, hourOfDay, minute));
        if (tag != null) {
            todoItem.setTagId(tag.getId());
        }

        if (mService.isTodoItemValid(todoItem)) {
            mService.editTodoItem(todoItem);
            mView.showTodoItemEdited();
            mView.openTodoItems(todoItemId, Operation.EDITED);
        }else {
            mView.showMissingTitle();
        }
    }

    @Override
    public void openDeleteConfirmationDialog() {
        mView.showDeleteConfirmationDialog();
    }

    @Override
    public void deleteTodoItem(long todoItemId) {
        verifyView();

        TodoItem todoItem = mService.getTodoItem(todoItemId);
        mService.deleteTodoItem(todoItem);
        mView.openTodoItems(todoItemId, Operation.DELETED);
    }

    @Override
    public void loadTags() {
        mView.addTags(mTagsRepository.getTags());
    }

    private void verifyView() {
        if (mView == null) {
            throw new IllegalStateException("View is unbinded");
        }
    }


    @Override
    public void bindView(@NonNull View view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
