package com.borges.moises.materialtodolist.model;

import android.test.AndroidTestCase;

import com.borges.moises.materialtodolist.data.MaterialTodoItemsDatabase;
import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.data.repository.SqliteTagsRepository;
import com.borges.moises.materialtodolist.data.repository.TagsRepository;
import com.borges.moises.materialtodolist.helpers.DatabaseHelper;

import java.util.List;

/**
 * Created by moises.anjos on 06/06/2016.
 */
public class SqliteTagsRepositosyTest extends AndroidTestCase {

    private TagsRepository mTagsRepository;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        MaterialTodoItemsDatabase.init(mContext);
        DatabaseHelper.beginTransaction();
        mTagsRepository = SqliteTagsRepository.getInstance();
    }


    @Override
    public void tearDown() throws Exception {
        DatabaseHelper.rollbackTransaction();
        super.tearDown();
    }

    public void testAddTag() throws Exception {
        Tag tag = new Tag();
        tag.setName("work");

        mTagsRepository.addTag(tag);

        assertTrue(tag.getId() >= 0);

        Tag tagFromDb = mTagsRepository.getTag(tag.getId());

        assertEquals(tag.getName(), tagFromDb.getName());
        assertEquals(tag.getId(), tagFromDb.getId());
    }

    public void testUpdateTag() throws Exception {
        Tag tag = new Tag();
        tag.setName("school");

        mTagsRepository.addTag(tag);

        tag.setName("colege");
        mTagsRepository.updateTag(tag);

        Tag tagFromDb = mTagsRepository.getTag(tag.getId());

        assertEquals(tag.getName(), tagFromDb.getName());
        assertEquals(tag.getId(), tagFromDb.getId());
    }

    public void testDeleteTag() throws Exception {
        Tag tag = new Tag();
        tag.setName("wrong tag");

        mTagsRepository.addTag(tag);

        assertNotNull(mTagsRepository.getTag(tag.getId()));

        mTagsRepository.deleteTag(tag);

        Tag tagFromDB = mTagsRepository.getTag(tag.getId());

        assertNull(tagFromDB);

    }

    public void testGetTags() throws Exception {
        Tag tag1 = new Tag();
        tag1.setName("tag1");

        Tag tag2 = new Tag();
        tag1.setName("tag2");

        Tag tag3 = new Tag();
        tag1.setName("tag2");

        mTagsRepository.addTag(tag1);
        mTagsRepository.addTag(tag2);
        mTagsRepository.addTag(tag3);;

        List<Tag> tags = mTagsRepository.getTags();

        assertTrue(tags.size() >= 3);
    }
}
