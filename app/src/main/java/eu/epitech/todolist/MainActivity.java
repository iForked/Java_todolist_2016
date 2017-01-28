package eu.epitech.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import eu.epitech.todolist.db.TaskContract;
import eu.epitech.todolist.db.TaskDBHelper;

public class MainActivity extends AppCompatActivity {

    private TaskDBHelper mHelper;
    private CustomAdapter mAdapter;
    private ListView mTaskListView;
    private Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = new TaskDBHelper(c);
        mTaskListView = (ListView) findViewById(R.id.list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        updateUI();
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(c);
                final LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
                dialog.setView(mView);
                final EditText userInput = (EditText) mView.findViewById(R.id.userInputDialog);
                final EditText userInputDescription = (EditText) mView.findViewById(R.id.userInputDialogDescription);

                dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String task = String.valueOf(userInput.getText().toString());
                        String description = String.valueOf(userInputDescription.getText().toString());
                        if (task.isEmpty()){
                            dialog.dismiss();
                            Toast.makeText(c, getResources().getString(R.string.empty_task), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            SQLiteDatabase db = mHelper.getReadableDatabase();
                            ContentValues values = new ContentValues();
                            values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                            values.put(TaskContract.TaskEntry.COL_TASK_DES, description);
                            db.insertWithOnConflict(TaskContract.TaskEntry.Table,
                                    null,
                                    values,
                                    SQLiteDatabase.CONFLICT_REPLACE);
                            Toast.makeText(c, getResources().getString(R.string.task_added) + task, Toast.LENGTH_SHORT).show();
                            db.close();
                        }
                    }
                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(c, getResources().getString(R.string.cancel_add), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialogAndroid = dialog.create();
                alertDialogAndroid.show();
            }
        });
    }

    private void updateUI() {
        ArrayList<TaskClass> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.Table,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_DES},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            int description = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_DES);
            taskList.add(new TaskClass(cursor.getString(id), cursor.getString(description)));
        }
        if (mAdapter == null) {
            mAdapter = new CustomAdapter(taskList, c);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView textView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(textView.getText().toString());
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete(TaskContract.TaskEntry.Table, TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        Toast.makeText(c, getResources().getString(R.string.task_space) + task + getResources().getString(R.string.deleted_space), Toast.LENGTH_LONG).show();
        db.close();
        updateUI();
    }

    public void editTask(View view)
    {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        final LayoutInflater layoutDialog = LayoutInflater.from(c);
        View editView = layoutDialog.inflate(R.layout.edit_task, null);

        final EditText taskText = (EditText) editView.findViewById(R.id.name);
        final EditText contentText = (EditText) editView.findViewById(R.id.description);

        TextView oldTaskText = (TextView) findViewById(R.id.task_title);
        TextView oldContentText = (TextView) findViewById(R.id.description);
        taskText.setText(String.valueOf(oldTaskText.getText()), TextView.BufferType.EDITABLE);
        contentText.setText(String.valueOf(oldContentText.getText()), TextView.BufferType.EDITABLE);

        dialog.setView(editView);
        dialog.setPositiveButton("Edit", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String task = String.valueOf(taskText.getText().toString());
                String description = String.valueOf(contentText.getText().toString());
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                if (task.isEmpty()){
                    dialog.dismiss();
                    Toast.makeText(c, getResources().getString(R.string.rename_empty), Toast.LENGTH_SHORT).show();
                }
                else {
                    values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                    values.put(TaskContract.TaskEntry.COL_TASK_DES, description);
                    db.updateWithOnConflict(TaskContract.TaskEntry.Table, values, null, null, SQLiteDatabase.CONFLICT_REPLACE);
                    db.close();
                    updateUI();
                }
            }
        });
        dialog.show();
    }
}

/*              private Notification.Builder notification;
                notification = new Notification.Builder(this);
                notification.setContentTitle("ToDo List").setContentText("New task added: " + task).setSmallIcon(R.drawable.ic_launcher);
                final Notification newNotification = notification.build();
                final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(0, newNotification); */
