package com.borges.moises.materialtodolist.addtodoitem;

import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTagsRepository;
import com.borges.moises.materialtodolist.data.repository.TagsRepository;
import com.borges.moises.materialtodolist.data.services.TodoItemService;
import com.borges.moises.materialtodolist.utils.DateUtils;

/**
 * Created by Moisés on 14/04/2016.
 */
public class AddTodoItemPresenter implements AddTodoItemMvp.Presenter {

    private AddTodoItemMvp.View mView;
    private TodoItemService mService;
    private TagsRepository mTagRepository;

    public AddTodoItemPresenter() {
        mService = new TodoItemService();
        mTagRepository = SqliteTagsRepository.getInstance();
    }

    @Override
    public void addTodoItem(String title, String description, Priority priority, String location, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, Tag tag) {

        if (mView == null) {
            throw new IllegalStateException("should bind LoginView first!");
        }

        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(title);
        todoItem.setDescription(description);
        todoItem.setPriority(priority == null? Priority.NORMAL : priority);
        todoItem.setLocation(location);
        todoItem.setDate(DateUtils.getDate(year, monthOfYear, dayOfMonth, hourOfDay, minute));
        if (tag != null) {
            todoItem.setTagId(tag.getId());
        }

        if (mService.isTodoItemValid(todoItem)) {
            mService.addTodoItem(todoItem);
            mView.showTodoItemAdded();
            mView.openTodoItems(todoItem.getId());
        }else {
            mView.showMissingTitle();
        }
    }

    @Override
    public void loadTags() {
        mView.addTags(mTagRepository.getTags());
    }


    @Override
    public void bindView(AddTodoItemMvp.View view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
