package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TaskDetailActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText dueDateEditText;
    private Button updateButton;
    private Button deleteButton;
    private Button editButton;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dueDateEditText = findViewById(R.id.dueDateEditText);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);


        TaskManagerDB taskManagerDB = new TaskManagerDB(TaskDetailActivity.this);
        task = taskManagerDB.getTask(getIntent().getStringExtra("task_id"));

        titleEditText.setText(task.getTitle());
        descriptionEditText.setText(task.getDescription());
        dueDateEditText.setText(task.getDueDate());

        titleEditText.setEnabled(false);
        descriptionEditText.setEnabled(false);
        dueDateEditText.setEnabled(false);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descriptionEditText.setEnabled(true);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskManagerDB taskManagerDB = new TaskManagerDB(TaskDetailActivity.this);
                boolean isDeleted = taskManagerDB.deleteTask(task.getId());

                if (isDeleted) {
                    String title = titleEditText.getText().toString().trim();
                    String description = descriptionEditText.getText().toString().trim();
                    String dueDate = dueDateEditText.getText().toString().trim();
                    Task newTask = new Task(task.getId(), title, description, dueDate);
                    boolean isAdded = taskManagerDB.addTask(newTask);
                    if (isAdded) {
                        Toast.makeText(TaskDetailActivity.this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(TaskDetailActivity.this, "Failed to update task", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TaskDetailActivity.this, "Failed to delete original task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskManagerDB taskManagerDB = new TaskManagerDB(TaskDetailActivity.this);
                boolean isDeleted = taskManagerDB.deleteTask(task.getId());

                if (isDeleted) {
                    Toast.makeText(TaskDetailActivity.this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TaskDetailActivity.this, "Failed to delete task", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
