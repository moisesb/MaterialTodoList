package com.borges.moises.materialtodolist.data.model;

/**
 * Created by Moisés on 18/04/2016.
 */
public class TodoList {
    private long id;
    private String name;

    public TodoList() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
