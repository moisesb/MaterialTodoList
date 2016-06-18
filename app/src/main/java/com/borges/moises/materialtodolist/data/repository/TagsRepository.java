package com.borges.moises.materialtodolist.data.repository;

import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.data.model.TasksByTag;

import java.util.List;

/**
 * Created by moises.anjos on 06/06/2016.
 */
public interface TagsRepository {
    void addTag(Tag tag);

    void updateTag(Tag tag);

    void deleteTag(Tag tag);

    Tag getTag(long id);

    List<Tag> getTags();

    List<TasksByTag> getTasksByTag();
}
