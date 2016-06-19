package com.borges.moises.materialtodolist.tags;

import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.data.model.TasksByTag;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.TagsRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemsRepository;
import com.borges.moises.materialtodolist.data.repository.specification.QueryTodoItemsByTag;
import com.borges.moises.materialtodolist.data.services.TodoItemService;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by moises.anjos on 14/06/2016.
 */

public class TagsPresenter implements TagsMvp.Presenter {

    private final TagsRepository mTagsRepository;
    private final TodoItemService mTodoItemService;
    private TagsMvp.View mView;

    public TagsPresenter(@NonNull TagsRepository tagsRepository,
                         @NonNull TodoItemService todoItemService) {
        mTagsRepository = tagsRepository;
        mTodoItemService = todoItemService;
    }

    @Override
    public void loadTags() {
        checkView();

        final List<TasksByTag> tasksByTags = mTagsRepository.getTasksByTag();
        mView.showTags(tasksByTags);
    }

    private void checkView() {
        if (mView == null) {
            throw new IllegalStateException("Should bind view first");
        }
    }

    @Override
    public void addTag(String tagName) {
        checkView();
        if (tagName == null || tagName.isEmpty()) {
            mView.showTagNotAddedError();
        }

        Tag tag = new Tag();
        tag.setName(tagName);
        mTagsRepository.addTag(tag);
        if (tag.getId() >= 0) {
            TasksByTag tasksByTag = new TasksByTag(tag,0);
            mView.showTagAdded(tasksByTag);
        }else {
            mView.showTagNotAddedError();
        }
    }

    @Override
    public void askOrDeleteTag(TasksByTag tasksByTag) {
        checkView();
        if (tasksByTag.getNumOfTasks() > 0) {
            mView.showAskToDeleteTagDialog(tasksByTag);
        }else {
            deleteTag(tasksByTag);
        }
    }

    @Override
    public void renameTag(TasksByTag tasksByTag, String newTagName) {
        checkView();
        if (newTagName == null || newTagName.isEmpty()) {
            mView.showTagNotEditedError();
        }
        final Tag tag = tasksByTag.getTag();
        tag.setName(newTagName);
        mTagsRepository.updateTag(tag);
        mView.updateTag(tasksByTag);
    }

    @Override
    public void deleteTag(TasksByTag tasksByTag) {
        checkView();
        final Tag tag = tasksByTag.getTag();
        mTagsRepository.deleteTag(tag);
        mView.showTagDeleted(tasksByTag);
        mTodoItemService.getTodoItems(new QueryTodoItemsByTag(tag))
                .observeOn(Schedulers.computation())
                .subscribeOn(Schedulers.computation())
                .flatMap(new Func1<List<TodoItem>, Observable<TodoItem>>() {
                    @Override
                    public Observable<TodoItem> call(List<TodoItem> todoItems) {
                        return Observable.from(todoItems);
                    }
                })
                .subscribe(new Subscriber<TodoItem>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TodoItem todoItem) {
                        mTodoItemService.deleteTodoItem(todoItem);
                    }
                });
    }

    @Override
    public void bindView(TagsMvp.View view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
