package com.borges.moises.materialtodolist.model;

import android.test.AndroidTestCase;

import com.borges.moises.materialtodolist.data.MaterialTodoItemsDatabase;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoItemsRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemsRepository;
import com.borges.moises.materialtodolist.data.repository.specification.QueryAllTodoItemsSqlSpec;
import com.borges.moises.materialtodolist.helpers.DatabaseHelper;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Moisés on 12/04/2016.
 */
public class TodoItemRepositoryImplTest extends AndroidTestCase {

    private TodoItem mTodoItem;
    private TodoItemsRepository mTodoItemRepository;
    @Override
    public void setUp() throws Exception {
        super.setUp();

        MaterialTodoItemsDatabase.init(mContext);
        mTodoItemRepository = SqliteTodoItemsRepository.getInstance();
        DatabaseHelper.beginTransaction();
        mTodoItem = new TodoItem();
        mTodoItem.setTitle("Some title");
        mTodoItem.setDescription("Do that");
        Calendar calendar = Calendar.getInstance();
        mTodoItem.setCreatedAt(calendar.getTime());
        mTodoItem.setUpdatedAt(calendar.getTime());
    }

    @Override
    public void tearDown() throws Exception {
        DatabaseHelper.rollbackTransaction();
        super.tearDown();
    }

    public void testInsertTodoItem() throws Exception {
        boolean inserted = mTodoItemRepository.addTodoItem(mTodoItem);
        assertTrue(inserted);
        TodoItem todoItemFromRepository = mTodoItemRepository.getTodoItem(mTodoItem.getId());
        assertEquals(mTodoItem.getTitle(),todoItemFromRepository.getTitle());
        assertEquals(mTodoItem.getDescription(), todoItemFromRepository.getDescription());
    }

    public void testDeleteTodoItem() throws Exception {
        boolean inserted = mTodoItemRepository.addTodoItem(mTodoItem);
        assertTrue(inserted);
        boolean deleted = mTodoItemRepository.removeTodoItem(mTodoItem);
        assertTrue(deleted);
        assertNull(mTodoItemRepository.getTodoItem(mTodoItem.getId()));
    }

    public void testUpdateTodoItem() throws Exception {
        boolean inserted = mTodoItemRepository.addTodoItem(mTodoItem);
        assertTrue(inserted);
        mTodoItem.setDescription("new description");
        boolean updated = mTodoItemRepository.updateTodoItem(mTodoItem);
        TodoItem todoItemFromRepository =  mTodoItemRepository.getTodoItem(mTodoItem.getId());
        assertEquals(mTodoItem.getDescription(),todoItemFromRepository.getDescription());
    }

    public void testQueryAll() throws Exception {
        mTodoItemRepository.addTodoItem(mTodoItem);
        mTodoItemRepository.addTodoItem(mTodoItem);
        mTodoItemRepository.addTodoItem(mTodoItem);

        List<TodoItem> todoItems = mTodoItemRepository.query(new QueryAllTodoItemsSqlSpec());
        assertTrue(todoItems.size() >= 3);
    }
}
