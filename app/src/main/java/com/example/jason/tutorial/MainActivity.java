/*
Name: Jason Bugallon
ID: 85806059

The main activity, does everything else
that doesn't involve the FireBase database
*/


package com.example.jason.tutorial;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FireBaseHelper fireBaseHelper;

    private EditText newTask;
    private Button addButton;
    private ListView taskList;

    private ArrayList<String> tasks;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newTask = findViewById(R.id.new_task_text);
        addButton = findViewById(R.id.add_button);
        taskList = findViewById(R.id.task_list);

        tasks = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, R.layout.row, R.id.todo_item, tasks);
        taskList.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        fireBaseHelper = new FireBaseHelper(mDatabase, tasks, adapter);
    }

    public void addTask(View v)
    {
        String task = newTask.getText().toString().trim();
        newTask.setText("");

        if(task.length() <= 0)
        {
            newTask.setError("Task cannot be blank");
        }
        else if(tasks.contains(task))
        {
            newTask.setError("Task already exists");
        }
        else
        {
            Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
            fireBaseHelper.writeData(task);
        }
    }

    public void deleteTask(View v)
    {
        View parent = (View) v.getParent();
        TextView task = parent.findViewById(R.id.todo_item);
        String taskString = task.getText().toString();
        fireBaseHelper.deleteData(taskString);
        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
    }

}
