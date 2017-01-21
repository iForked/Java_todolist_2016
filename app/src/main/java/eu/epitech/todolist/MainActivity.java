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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import eu.epitech.todolist.db.TaskContract;
import eu.epitech.todolist.db.TaskDBHelper;


public class MainActivity extends AppCompatActivity {

    private TaskDBHelper mHelper;
    private ArrayAdapter<String> mAdapter;
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

                dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("Input", "Task:" + userInput.getText().toString());
                        String task = String.valueOf(userInput.getText().toString());
                        SQLiteDatabase db = mHelper.getReadableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                        db.insertWithOnConflict(TaskContract.TaskEntry.Table,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        Toast.makeText(c, "Add task " + task, Toast.LENGTH_SHORT).show();
                        db.close();
                        updateUI();
                    }
                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.d("Cancel", "Add task is cancel");
                                Toast.makeText(c, "Add task canceled", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialogAndroid = dialog.create();
                alertDialogAndroid.show();
            }
        });
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.Table,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(id));
        }
        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_title,
                    taskList);
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
        Log.d("DeleteTask", "Task " + task + " is now deleted");
        Toast.makeText(c, "Task " + task + " deleted", Toast.LENGTH_LONG).show();
        db.close();
        updateUI();
    }
}
