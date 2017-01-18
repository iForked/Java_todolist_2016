package org.epitech.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by theo on 1/18/17.
 */


public class AddTask extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);
        final Button button = (Button) findViewById(R.id.save);

        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                EditText task = (EditText) findViewById(R.id.task);
                final String TAG = "AddTask";
                Log.d(TAG, "Task to add " + task.getText().toString());
            }
        }
        );
    }
}