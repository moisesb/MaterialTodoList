package com.borges.moises.materialtodolist.data.scheme;

/**
 * Created by Moisés on 11/04/2016.
 */
public class TodoItemTable {

    public static final String TABLE_NAME = "todoitems";

    public static class Columns {
        public static final String ID = "_id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String DATE = "date";
        public static final String PRIORITY = "priority";
        public static final String COMPLETED = "completed";
        public static final String LOCATION = "location";
        public static final String DONE = "done";
        public static final String DONE_AT = "done_at";

        private Columns() {

        }
    }

    public static class Sql {
        // TODO: 18/04/2016 TodoItem Table should have a foreign key to TodoList Table
        public static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + " (" +
                Columns.ID + " integer primary key autoincrement " + "," +
                Columns.TITLE + "," +
                Columns.DESCRIPTION + "," +
                Columns.DATE + "," +
                Columns.COMPLETED + "," +
                Columns.PRIORITY + "," +
                Columns.DONE + "," +
                Columns.DONE_AT + "," +
                Columns.LOCATION + ")";

        public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;

        private Sql() {

        }
    }

    private TodoItemTable() {

    }

}
