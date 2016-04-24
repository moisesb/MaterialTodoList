package com.borges.moises.materialtodolist.data.scheme;

/**
 * Created by Moisés on 18/04/2016.
 */
public class TodoListTable {

    public static final String TABLE_NAME = "todo_lists";

    public static class Columns {
        public static final String ID = "_id";
        public static final String NAME = "name";

        private Columns() {

        }
    }

    public static class Sql {
        public static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(" +
                Columns.ID + " integer primary key autoincrement" + "," +
                Columns.NAME + " unique)";

        public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;
    }

    private  TodoListTable() {

    }
}
