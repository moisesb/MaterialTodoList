package com.borges.moises.materialtodolist.tags;

import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.mvp.MvpPresenter;
import com.borges.moises.materialtodolist.mvp.MvpView;

import java.util.List;

/**
 * Created by moises.anjos on 14/06/2016.
 */

public interface TagsMvp {
    interface View extends MvpView{
        void showTags(List<Tag> tags);

        void showTagNotAddedError();

        void showTagAdded(Tag tag);

        void showTagDeleted(Tag tag);

        void showTagNotEditedError();

        void updateTag(Tag tag);
    }

    interface Presenter extends MvpPresenter<View> {
        void loadTags();

        void addTag(String tagName);

        void deleteTag(Tag tag);

        void renameTag(Tag tag, String newTagName);
    }
}
