package com.borges.moises.materialtodolist.tags;

import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.data.model.TasksByTag;
import com.borges.moises.materialtodolist.data.repository.TagsRepository;

import java.util.List;

/**
 * Created by moises.anjos on 14/06/2016.
 */

public class TagsPresenter implements TagsMvp.Presenter {

    private final TagsRepository mTagsRepository;
    private TagsMvp.View mView;

    public TagsPresenter(TagsRepository tagsRepository) {
        mTagsRepository = tagsRepository;
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
    public void deleteTag(TasksByTag tasksByTag) {
        checkView();
        final Tag tag = tasksByTag.getTag();
        mTagsRepository.deleteTag(tag);
        mView.showTagDeleted(tasksByTag);
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
    public void bindView(TagsMvp.View view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
