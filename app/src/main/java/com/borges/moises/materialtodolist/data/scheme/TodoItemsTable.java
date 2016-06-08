package com.borges.moises.materialtodolist.data.scheme;

/**
 * Created by Moisés on 11/04/2016.
 */
public class TodoItemsTable {

    public static final String TABLE_NAME = "todoitems";

    public static class Columns {
        public static final String ID = "_id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String DATE = "date";
        public static final String PRIORITY = "priority";
        public static final String LOCATION = "location";
        public static final String DONE = "done";
        public static final String DONE_AT = "done_at";
        public static final String CREATED_AT = "created_at";
        public static final String UPDATED_AT = "updated_at";
        public static final String DELETED = "deleted";
        public static final String DIRTY = "dirty";
        public static final String VERSION = "version";
        public static final String SERVER_ID = "server_id";
        public static final String TAG = "tag";

        private Columns() {

        }
    }

    public static class Sql {
        public static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + " (" +
                Columns.ID + " integer primary key autoincrement " + "," +
                Columns.TITLE + "," +
                Columns.DESCRIPTION + "," +
                Columns.DATE + "," +
                Columns.PRIORITY + "," +
                Columns.DELETED + "," +
                Columns.DONE + "," +
                Columns.DONE_AT + "," +
                Columns.DIRTY + "," +
                Columns.VERSION + "," +
                Columns.SERVER_ID + "," +
                Columns.CREATED_AT + "," +
                Columns.UPDATED_AT + "," +
                Columns.LOCATION + "," +
                Columns.TAG + "," +
                "foreign key(" + Columns.TAG + ") references " + TagsTable.TABLE_NAME + "(" + TagsTable.Columns.ID + ")" + ")";

        public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;

        private Sql() {

        }
    }

    private TodoItemsTable() {

    }

}
