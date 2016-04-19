package com.borges.moises.materialtodolist.data.repository;

import com.borges.moises.materialtodolist.data.model.TodoList;
import com.borges.moises.materialtodolist.data.repository.specification.Specification;

import java.util.List;

/**
 * Created by Moisés on 18/04/2016.
 */
public interface TodoListRepository {
    boolean addTodoList(TodoList todoList);
    boolean updateTodoList(TodoList todoList);
    boolean deleteTodoList(TodoList todoList);
    TodoList getTodoList(long id);

    List<TodoList> query(Specification specification);
}
