package com.borges.moises.materialtodolist.tags;

import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.data.model.TasksByTag;
import com.borges.moises.materialtodolist.mvp.MvpPresenter;
import com.borges.moises.materialtodolist.mvp.MvpView;

import java.util.List;

/**
 * Created by moises.anjos on 14/06/2016.
 */

public interface TagsMvp {
    interface View extends MvpView{
        void showTags(List<TasksByTag> tasksByTags);

        void showTagNotAddedError();

        void showTagAdded(TasksByTag tasksByTag);

        void showTagDeleted(TasksByTag tasksByTag);

        void showTagNotEditedError();

        void updateTag(TasksByTag tasksByTag);
    }

    interface Presenter extends MvpPresenter<View> {
        void loadTags();

        void addTag(String tagName);

        void deleteTag(TasksByTag tasksByTag);

        void renameTag(TasksByTag tasksByTag, String newTagName);
    }
}
