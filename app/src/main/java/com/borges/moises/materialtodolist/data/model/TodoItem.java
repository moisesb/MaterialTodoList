package com.borges.moises.materialtodolist.data.model;

import java.util.Date;

/**
 * Created by Moisés on 11/04/2016.
 */
public class TodoItem {
    private long id;
    private String title;
    private String description;
    private Date date;
    private boolean completed;
    private Priority priority;

    public TodoItem() {
        priority = Priority.NORMAL;
    }

    public TodoItem(String title, String description, Date date, boolean isUrgent) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.priority = isUrgent? Priority.URGENT: Priority.NORMAL;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
