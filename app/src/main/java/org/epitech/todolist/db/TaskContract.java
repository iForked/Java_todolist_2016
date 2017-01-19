package org.epitech.todolist.db;

/**
 * Created by theo on 1/19/17.
 */

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "org.epitech.eu.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns{
        public static final String Table = "tasks";
        public static final String COL_TASK_TITLE = "title";
    }
}
