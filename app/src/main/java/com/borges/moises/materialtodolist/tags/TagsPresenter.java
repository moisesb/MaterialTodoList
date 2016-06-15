package com.borges.moises.materialtodolist.tags;

import com.borges.moises.materialtodolist.data.model.Tag;
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

        List<Tag> tags = mTagsRepository.getTags();
        mView.showTags(tags);
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
            mView.showTagAdded(tag);
        }else {
            mView.showTagNotAddedError();
        }
    }

    @Override
    public void deleteTag(Tag tag) {
        checkView();
        mTagsRepository.deleteTag(tag);
        mView.showTagDeleted(tag);
    }

    @Override
    public void renameTag(Tag tag, String newTagName) {
        checkView();
        if (newTagName == null || newTagName.isEmpty()) {
            mView.showTagNotEditedError();
        }
        tag.setName(newTagName);
        mTagsRepository.updateTag(tag);
        mView.updateTag(tag);
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
