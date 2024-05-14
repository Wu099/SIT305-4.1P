package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText dueDateEditText;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        setId();

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dueDateEditText = findViewById(R.id.dueDateEditText);
        addButton = findViewById(R.id.addTaskButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                String dueDate = dueDateEditText.getText().toString().trim();

                if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty()) {
                    Toast.makeText(AddTaskActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    id += 1;
                    Task task = new Task(Integer.toString(id+1), title, description, dueDate);
                    TaskManagerDB taskManagerDB = new TaskManagerDB(AddTaskActivity.this);
                    boolean isAdded = taskManagerDB.addTask(task);

                    if (isAdded) {
                        Toast.makeText(AddTaskActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddTaskActivity.this, "Failed to add task", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    int id = 0;

    private void setId() {
        TaskManagerDB taskManagerDB = new TaskManagerDB(AddTaskActivity.this);
        List<Task> list = taskManagerDB.getAllTask();
        int current_id = 0;
        for (Task t : list) {
            current_id = Math.max(current_id, Integer.parseInt(t.getId()));
        }
        id = current_id;
    }
}
