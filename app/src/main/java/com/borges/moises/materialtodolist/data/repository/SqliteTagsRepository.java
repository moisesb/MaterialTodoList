package com.borges.moises.materialtodolist.data.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.borges.moises.materialtodolist.data.MaterialTodoItemsDatabase;
import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.data.model.TasksByTag;
import com.borges.moises.materialtodolist.data.scheme.TagsTable;
import com.borges.moises.materialtodolist.data.scheme.TodoItemsTable;

import java.util.ArrayList;
import java.util.List;

import static com.borges.moises.materialtodolist.data.scheme.TagsTable.*;

/**
 * Created by moises.anjos on 06/06/2016.
 */
public class SqliteTagsRepository implements TagsRepository {

    private static SqliteTagsRepository sInstance;
    private SQLiteDatabase mDatabase;
    private static final Object sLock = new Object();
    private static final String sWhereClause = TagsTable.Columns.ID + " = ?";
    private static final String NUM_OF_TASKS = "num_of_tasks";
    private static final String sQueryTasksByTag = "select " +
            TagsTable.Columns.ID_WITH_PREFIX + "," +
            TagsTable.Columns.NAME_WITH_PREFIX + "," +
            "count(" + TodoItemsTable.Columns.TAG + ") as " + NUM_OF_TASKS +
            " from " + TodoItemsTable.TABLE_NAME + " left join " + TagsTable.TABLE_NAME +
            " on " + TodoItemsTable.Columns.TAG + " = " + TagsTable.Columns.ID_WITH_PREFIX +
            " group by " + TodoItemsTable.Columns.TAG +
            " order by " + TodoItemsTable.Columns.TAG;

    private SqliteTagsRepository() {
        mDatabase = MaterialTodoItemsDatabase.getInstance()
                .getWritableDatabase();
    }

    @Override
    public void addTag(@NonNull Tag tag) {
        long id = mDatabase.insert(TABLE_NAME, null, contentValues(tag));
        tag.setId(id);
    }

    private ContentValues contentValues(@NonNull Tag tag) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TagsTable.Columns.NAME, tag.getName());
        return contentValues;
    }

    @Override
    public void updateTag(@NonNull Tag tag) {
        mDatabase.update(TABLE_NAME, contentValues(tag), sWhereClause, whereArgs(tag));
    }

    @NonNull
    private String[] whereArgs(@NonNull Tag tag) {
        return new String[]{String.valueOf(tag.getId())};
    }

    @Override
    public void deleteTag(@NonNull Tag tag) {
        mDatabase.delete(TABLE_NAME, sWhereClause, whereArgs(tag));
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public Tag getTag(long id) {
        Cursor cursor = mDatabase.query(TABLE_NAME, null, sWhereClause, new String[]{String.valueOf(id)}, null, null, TagsTable.Columns.NAME + " ASC");
        cursor.moveToFirst();

        final Tag tag = getTag(cursor);
        return tag;
    }

    @Nullable
    private Tag getTag(Cursor cursor) {
        if (cursor.getCount() == 0 || cursor.isAfterLast()) {
            return null;
        }

        Tag tag = new Tag();
        tag.setId(cursor.getLong(cursor.getColumnIndex(TagsTable.Columns.ID)));
        tag.setName(cursor.getString(cursor.getColumnIndex(TagsTable.Columns.NAME)));
        return tag;
    }

    @Override
    public List<Tag> getTags() {
        Cursor cursor = mDatabase.query(TABLE_NAME, null, null, null, null, null, TagsTable.Columns.NAME + " ASC");
        cursor.moveToFirst();

        List<Tag> tags = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            final Tag tag = getTag(cursor);
            tags.add(tag);
            cursor.moveToNext();
        }
        return tags;
    }

    @Override
    public List<TasksByTag> getTasksByTag() {

        Cursor cursor = mDatabase.rawQuery(sQueryTasksByTag, new String[0]);
        cursor.moveToFirst();

        List<TasksByTag> tasksByTags = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            TasksByTag tasksByTag = getTasksByTag(cursor);
            if (tasksByTag.getTag().getName() != null) {
                tasksByTags.add(tasksByTag);
            }
            cursor.moveToNext();
        }
        List<Tag> tags = getTags();

        return consolidateTasksByTag(tasksByTags, tags);
    }

    private List<TasksByTag> consolidateTasksByTag(List<TasksByTag> tasksByTags, List<Tag> tags) {
        List<TasksByTag> consolidateTasksByTag = new ArrayList<>();
        int pos = 0;
        if (tasksByTags.size() > 0) {
            for (int i = 0; i < tags.size(); i++) {
                final Tag tag = tags.get(i);
                final TasksByTag tasksByTag = tasksByTags.get(pos);
                if (tag.getId() == tasksByTag.getTag().getId()) {
                    consolidateTasksByTag.add(tasksByTag);
                    pos += tasksByTags.size() - 1 > pos ? 1 : 0;
                } else {
                    consolidateTasksByTag.add(new TasksByTag(tag, 0));
                }
            }
        } else {
            for (Tag tag : tags) {
                consolidateTasksByTag.add(new TasksByTag(tag,0));
            }
        }
        return consolidateTasksByTag;
    }

    private TasksByTag getTasksByTag(Cursor cursor) {
        int numOfTasks = cursor.getInt(cursor.getColumnIndex(NUM_OF_TASKS));
        Tag tag = getTag(cursor);
        return new TasksByTag(tag, numOfTasks);
    }

    @NonNull
    public static TagsRepository getInstance() {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new SqliteTagsRepository();
            }
        }
        return sInstance;
    }
}
