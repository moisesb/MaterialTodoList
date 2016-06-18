package com.borges.moises.materialtodolist.data.scheme;

/**
 * Created by moises.anjos on 06/06/2016.
 */
public abstract class TagsTable {

    public static final String TABLE_NAME = "tags";

    public abstract class Columns {
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String ID_WITH_PREFIX = TABLE_NAME + "." + ID;
        public static final String NAME_WITH_PREFIX = TABLE_NAME + "." + NAME;
    }


    public static class Sql {
        public static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(" +
                Columns.ID + " integer primary key autoincrement ," +
                Columns.NAME + " unique)";

        public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;
    }


    private TagsTable() {

    }


}
