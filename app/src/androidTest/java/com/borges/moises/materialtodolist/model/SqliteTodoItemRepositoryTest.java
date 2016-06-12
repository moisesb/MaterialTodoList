package com.borges.moises.materialtodolist.model;

import android.test.AndroidTestCase;

import com.borges.moises.materialtodolist.data.MaterialTodoItemsDatabase;
import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTagsRepository;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoItemsRepository;
import com.borges.moises.materialtodolist.data.repository.TagsRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemsRepository;
import com.borges.moises.materialtodolist.data.repository.specification.QueryAllTodoItemsSqlSpec;
import com.borges.moises.materialtodolist.data.repository.specification.QueryTodoItemsByTag;
import com.borges.moises.materialtodolist.helpers.DatabaseHelper;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Moisés on 12/04/2016.
 */
public class SqliteTodoItemRepositoryTest extends AndroidTestCase {

    private TodoItem mTodoItem;
    private Tag mTag;
    private TodoItemsRepository mTodoItemsRepository;
    private TagsRepository mTagsRepository;
    @Override
    public void setUp() throws Exception {
        super.setUp();

        MaterialTodoItemsDatabase.init(mContext);
        mTodoItemsRepository = SqliteTodoItemsRepository.getInstance();
        mTagsRepository = SqliteTagsRepository.getInstance();
        DatabaseHelper.beginTransaction();
        mTodoItem = createTodoItem();
        Tag tag1 = new Tag();
        tag1.setName("college");
        mTag = new Tag();
        mTag.setName("work");
        mTagsRepository.addTag(tag1);
        mTagsRepository.addTag(mTag);
        mTodoItem.setTagId(mTag.getId());
    }

    private TodoItem createTodoItem() {
        TodoItem todoItem = new TodoItem();
        todoItem.setTitle("Some title");
        todoItem.setDescription("Do that");
        Calendar calendar = Calendar.getInstance();
        todoItem.setCreatedAt(calendar.getTime());
        todoItem.setUpdatedAt(calendar.getTime());
        return todoItem;
    }

    @Override
    public void tearDown() throws Exception {
        DatabaseHelper.rollbackTransaction();
        super.tearDown();
    }

    public void testInsertTodoItem() throws Exception {
        boolean inserted = mTodoItemsRepository.addTodoItem(mTodoItem);
        assertTrue(inserted);
        TodoItem todoItemFromRepository = mTodoItemsRepository.getTodoItem(mTodoItem.getId());
        assertEquals(mTodoItem.getTitle(),todoItemFromRepository.getTitle());
        assertEquals(mTodoItem.getDescription(), todoItemFromRepository.getDescription());
        assertEquals((long)mTodoItem.getTagId(),mTag.getId());
        Tag tagFromDb = mTagsRepository.getTag(todoItemFromRepository.getTagId());
        assertNotNull(tagFromDb);
        assertEquals(mTag.getId(), tagFromDb.getId());
        assertEquals(mTag.getName(), tagFromDb.getName());
    }

    public void testDeleteTodoItem() throws Exception {
        boolean inserted = mTodoItemsRepository.addTodoItem(mTodoItem);
        assertTrue(inserted);
        boolean deleted = mTodoItemsRepository.removeTodoItem(mTodoItem);
        assertTrue(deleted);
        assertNull(mTodoItemsRepository.getTodoItem(mTodoItem.getId()));
    }

    public void testUpdateTodoItem() throws Exception {
        boolean inserted = mTodoItemsRepository.addTodoItem(mTodoItem);
        assertTrue(inserted);
        mTodoItem.setDescription("new description");
        boolean updated = mTodoItemsRepository.updateTodoItem(mTodoItem);
        TodoItem todoItemFromRepository =  mTodoItemsRepository.getTodoItem(mTodoItem.getId());
        assertEquals(mTodoItem.getDescription(),todoItemFromRepository.getDescription());
    }

    public void testQueryAll() throws Exception {
        mTodoItemsRepository.addTodoItem(mTodoItem);
        mTodoItemsRepository.addTodoItem(mTodoItem);
        mTodoItemsRepository.addTodoItem(mTodoItem);

        List<TodoItem> todoItems = mTodoItemsRepository.query(new QueryAllTodoItemsSqlSpec());
        assertTrue(todoItems.size() >= 3);
    }

    public void testQueryByTag() throws Exception {
        Tag tag = new Tag();
        tag.setName("foo");
        mTagsRepository.addTag(tag);
        TodoItem item1 = createTodoItem();
        item1.setTagId(tag.getId());
        TodoItem item2 = createTodoItem();
        item2.setTagId(tag.getId());
        TodoItem item3 = createTodoItem();
        item3.setTagId(tag.getId());
        mTodoItemsRepository.addTodoItem(item1);
        mTodoItemsRepository.addTodoItem(item2);
        mTodoItemsRepository.addTodoItem(item3);

        final TodoItem todoItemFromDb = mTodoItemsRepository.getTodoItem(item1.getId());
        final Long tagId = todoItemFromDb.getTagId();
        assertEquals(tag.getId(),(long) tagId);

        List<TodoItem> todoItems = mTodoItemsRepository.query(new QueryTodoItemsByTag(tag));
        assertTrue(todoItems.size() == 3);
    }
}
