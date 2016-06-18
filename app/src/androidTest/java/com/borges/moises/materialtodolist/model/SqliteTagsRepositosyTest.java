package com.borges.moises.materialtodolist.model;

import android.test.AndroidTestCase;

import com.borges.moises.materialtodolist.data.MaterialTodoItemsDatabase;
import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.data.model.TasksByTag;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTagsRepository;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoItemsRepository;
import com.borges.moises.materialtodolist.data.repository.TagsRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemsRepository;
import com.borges.moises.materialtodolist.helpers.DatabaseHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by moises.anjos on 06/06/2016.
 */
public class SqliteTagsRepositosyTest extends AndroidTestCase {

    private TagsRepository mTagsRepository;
    private TodoItemsRepository mTodoItemsRepository;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        MaterialTodoItemsDatabase.init(mContext);
        DatabaseHelper.beginTransaction();
        mTagsRepository = SqliteTagsRepository.getInstance();
        mTodoItemsRepository = SqliteTodoItemsRepository.getInstance();
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

    public void testTasksByTag() throws Exception {
        Tag tag = new Tag();
        tag.setName("tag4");

        mTagsRepository.addTag(tag);

        Calendar calendar = Calendar.getInstance();
        final Date time = calendar.getTime();

        TodoItem todoItem1 = new TodoItem();
        todoItem1.setTitle("foo");
        todoItem1.setTagId(tag.getId());
        todoItem1.setCreatedAt(time);
        todoItem1.setUpdatedAt(time);

        TodoItem todoItem2 = new TodoItem();
        todoItem2.setTitle("foo2");
        todoItem2.setTagId(tag.getId());
        todoItem2.setCreatedAt(time);
        todoItem2.setUpdatedAt(time);

        TodoItem todoItem3 = new TodoItem();
        todoItem3.setTitle("foo3");
        todoItem3.setTagId(tag.getId());
        todoItem3.setCreatedAt(time);
        todoItem3.setUpdatedAt(time);

        TodoItem todoItem4 = new TodoItem();
        todoItem4.setTitle("foo4");
        todoItem4.setTagId(tag.getId());
        todoItem4.setCreatedAt(time);
        todoItem4.setUpdatedAt(time);

        mTodoItemsRepository.addTodoItem(todoItem1);
        mTodoItemsRepository.addTodoItem(todoItem2);
        mTodoItemsRepository.addTodoItem(todoItem3);
        mTodoItemsRepository.addTodoItem(todoItem4);

        final List<TasksByTag> tasksByTags = mTagsRepository.getTasksByTag();
        assertNotNull(tasksByTags);
        assertTrue(tasksByTags.size() >= 1);
        final TasksByTag tasksByTag = getTasksByTag4(tasksByTags,tag.getName());
        assertEquals(tag.getId(),tasksByTag.getTag().getId());
        assertEquals(tag.getName(), tasksByTag.getTag().getName());
        assertEquals(4,tasksByTag.getNumOfTasks());
    }

    private TasksByTag getTasksByTag4(List<TasksByTag> tasksByTags, String tagName) {
        for (TasksByTag tasksByTag : tasksByTags) {
            if (tasksByTag.getTag().getName().equals(tagName)) {
                return tasksByTag;
            }
        }
        fail();
        return null;
    }
}
