package com.borges.moises.materialtodolist.model;

import android.test.AndroidTestCase;

import com.borges.moises.materialtodolist.data.MaterialTodoItemsDatabase;
import com.borges.moises.materialtodolist.data.model.TodoList;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoListRepository;
import com.borges.moises.materialtodolist.data.repository.TodoListRepository;
import com.borges.moises.materialtodolist.data.repository.specification.QueryAllTodoListsSqlSpecification;
import com.borges.moises.materialtodolist.helpers.DatabaseHelper;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Moisés on 18/04/2016.
 */
public class SqliteTodoListRepositoryTest extends AndroidTestCase {

    private TodoListRepository mTodoListRepository;
    private TodoList mTodoList;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        MaterialTodoItemsDatabase.init(mContext);
        DatabaseHelper.beginTransaction();
        mTodoListRepository = SqliteTodoListRepository.getInstance();
        mTodoList = new TodoList();
        mTodoList.setName("Inbox");

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        DatabaseHelper.rollbackTransaction();
    }

    public void testAddTodoList() throws Exception {
        boolean inserted = mTodoListRepository.addTodoList(mTodoList);
        assertTrue(inserted);
        TodoList todoListFromDb = mTodoListRepository.getTodoList(mTodoList.getId());
        assertEquals(mTodoList.getId(),todoListFromDb.getId());
        assertEquals(mTodoList.getName(),todoListFromDb.getName());
    }

    public void testDeleteTodoList() throws Exception {
        mTodoListRepository.addTodoList(mTodoList);
        boolean deleted = mTodoListRepository.deleteTodoList(mTodoList);
        assertTrue(deleted);
        TodoList todoListFromDb = mTodoListRepository.getTodoList(mTodoList.getId());
        assertNull(todoListFromDb);
    }

    public void testUpdateTodoList() throws Exception {
        mTodoListRepository.addTodoList(mTodoList);
        mTodoList.setName("Work");
        mTodoListRepository.updateTodoList(mTodoList);
        TodoList todoListFromDb = mTodoListRepository.getTodoList(mTodoList.getId());
        assertEquals(mTodoList.getName(),todoListFromDb.getName());
    }

    public void testQueryAllTodoLists() throws Exception {
        TodoList tl1 = new TodoList();
        tl1.setName("Work");
        TodoList tl2 = new TodoList();
        tl2.setName("Personal");
        TodoList tl3 = new TodoList();
        tl3.setName("College");

        List<TodoList> todoLists = Arrays.asList(tl1,tl2,tl3);

        boolean i1 = mTodoListRepository.addTodoList(tl1);
        boolean l2 = mTodoListRepository.addTodoList(tl2);
        boolean l3 = mTodoListRepository.addTodoList(tl3);

        List<TodoList> todoListsFromDb = mTodoListRepository.query(new QueryAllTodoListsSqlSpecification());
        assertTrue(todoListsFromDb.size() >= 3);

        for (int i = 0; i < todoLists.size(); i++) {
            assertEquals(todoLists.get(i).getId(), todoListsFromDb.get(i).getId());
            assertEquals(todoLists.get(i).getName(), todoListsFromDb.get(i).getName());
        }

    }
}
