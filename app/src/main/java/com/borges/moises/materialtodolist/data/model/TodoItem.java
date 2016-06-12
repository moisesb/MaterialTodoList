package com.borges.moises.materialtodolist.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by Moisés on 11/04/2016.
 */
public class TodoItem {
    private long id;
    private String title;
    private String description;
    private Date date;
    private Priority priority;
    private String location;
    private boolean done;
    private Date doneAt;
    private String serverId;
    private boolean dirty;
    private boolean deleted;
    private int version;
    private Date createdAt;
    private Date updatedAt;
    private Long tagId = null;

    // TODO: 06/06/2016 add tag attribute
    public TodoItem() {
        priority = Priority.NORMAL;
        deleted = false;
        dirty = false;
        version = 1;
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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @JsonProperty("done_at")
    public Date getDoneAt() {
        return doneAt;
    }

    @JsonProperty("done_at")
    public void setDoneAt(Date doneAt) {
        this.doneAt = doneAt;
    }

    @JsonIgnore
    public String getServerId() {
        return serverId;
    }

    @JsonIgnore
    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    @JsonIgnore
    public boolean isDirty() {
        return dirty;
    }

    @JsonIgnore
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @JsonIgnore
    public boolean isDeleted() {
        return deleted;
    }

    @JsonIgnore
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @JsonProperty("created_at")
    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("updated_at")
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("tag_id")
    public Long getTagId() {
        return tagId;
    }

    @JsonProperty("tag_id")
    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    @JsonIgnore
    public void incrementVersion(){
        this.version++;
    }
}
