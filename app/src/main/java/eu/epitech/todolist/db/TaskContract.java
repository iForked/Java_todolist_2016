package eu.epitech.todolist.db;

/**
 * Created by theo on 1/19/17.
 */

import android.provider.BaseColumns;

public class TaskContract {
    static final String DB_NAME = "eu.epitech.todolist.db";
    static final int DB_VERSION = 13;

    public class TaskEntry implements BaseColumns{
        public static final String Table = "tasks";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DES = "description";
        public static final String DATE = "date";
    }
}
