package com.borges.moises.materialtodolist.model;

import android.test.AndroidTestCase;

import com.borges.moises.materialtodolist.data.MaterialTodoItemsDatabase;
import com.borges.moises.materialtodolist.data.model.TodoList;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoListRepository;
import com.borges.moises.materialtodolist.data.repository.TodoListRepository;
import com.borges.moises.materialtodolist.helpers.DatabaseHelper;

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

        mTodoList.setName("Inbox");

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        DatabaseHelper.rollbackTransaction();
    }
}
