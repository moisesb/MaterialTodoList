package com.borges.moises.materialtodolist.data.model;

/**
 * Created by Moisés on 17/06/2016.
 */

public class TasksByTag {
    private final Tag tag;
    private final int numOfTasks;

    public TasksByTag(Tag tag, int numOfTasks) {
        this.tag = tag;
        this.numOfTasks = numOfTasks;
    }

    public Tag getTag() {
        return tag;
    }

    public int getNumOfTasks() {
        return numOfTasks;
    }
}
