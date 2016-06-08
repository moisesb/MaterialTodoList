package com.borges.moises.materialtodolist.data.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.borges.moises.materialtodolist.data.MaterialTodoItemsDatabase;
import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.data.scheme.TagsTable;

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
    public Tag getTag(long id) {
        Cursor cursor = mDatabase.query(TABLE_NAME, null, sWhereClause, new String[]{String.valueOf(id)}, null, null, TagsTable.Columns.NAME + " ASC");
        cursor.moveToFirst();

        return getTag(cursor);
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
        Cursor cursor = mDatabase.query(TABLE_NAME, null, null,null, null, null, TagsTable.Columns.NAME + " ASC");
        cursor.moveToFirst();

        List<Tag> tags = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            tags.add(getTag(cursor));
            cursor.moveToNext();
        }
        return tags;
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
