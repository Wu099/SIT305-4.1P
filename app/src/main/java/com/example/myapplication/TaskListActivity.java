package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {
    RecyclerView recyclerViewMain;
    VerticalAdapter vAdapter;
    TaskManagerDB taskManagerDB;
    List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        recyclerViewMain = findViewById(R.id.recyclerView);
        taskManagerDB = new TaskManagerDB(this);

//        taskManagerDB.deleteAllTask();
//        taskManagerDB.addTask(new Task("0", "title0", "desp0", "2021-01-11"));
//        taskManagerDB.addTask(new Task("1", "title1", "desp1", "2021-01-01"));
//        taskManagerDB.addTask(new Task("2", "title22", "desp2", "2021-01-02"));
//        taskManagerDB.addTask(new Task("3", "title3", "desp3", "2021-01-03"));
//        taskManagerDB.addTask(new Task("4", "title42", "desp4", "2021-01-04"));
//        taskManagerDB.addTask(new Task("5", "title56", "desp5", "2021-01-05"));
//        taskManagerDB.addTask(new Task("6", "title16", "desp6", "2021-01-06"));
//        taskManagerDB.addTask(new Task("7", "title77", "desp7", "2021-01-07"));
//        taskManagerDB.addTask(new Task("8", "title8111", "desp8", "2021-01-08"));
//        taskManagerDB.addTask(new Task("9", "title789", "desp9", "2021-01-09"));

        createTaskList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        createTaskList();
    }

    private void createTaskList() {
        taskList = taskManagerDB.getAllTask();
        vAdapter = new VerticalAdapter(taskList, new onItemClickListener() {
            @Override
            public void itemClick(Task task) {
                Intent intent = new Intent(TaskListActivity.this, TaskDetailActivity.class);
                intent.putExtra("task_id", task.getId());
                startActivity(intent);
            }
        });
        recyclerViewMain.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMain.setAdapter(vAdapter);
    }
}

class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.ViewHolder> {

    private final onItemClickListener listener;
    private final List<Task> taskList;

    public VerticalAdapter(List<Task> taskList, onItemClickListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task, listener);
    }

    @Override
    public int getItemCount() {
        if (this.taskList == null) {
            return 0;
        }
        return this.taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView dueDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTV);
            dueDateTextView = itemView.findViewById(R.id.dueDateTV);
        }

        public void bind(Task task, final onItemClickListener listener) {
            titleTextView.setText(task.getTitle());
            dueDateTextView.setText(task.getDueDate());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClick(task);
                }
            });
        }
    }
}

