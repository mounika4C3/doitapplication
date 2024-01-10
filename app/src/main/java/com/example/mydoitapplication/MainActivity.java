package com.example.mydoitapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText taskInput = findViewById(R.id.taskInput);
        final Button addButton = findViewById(R.id.addButton);
        final ListView listView = findViewById(R.id.taskList);

        taskList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);

        listView.setAdapter(adapter);

        addButton.setOnClickListener(view -> {
            String task = taskInput.getText().toString().trim();
            if (!task.isEmpty()) {
                taskList.add(task);
                adapter.notifyDataSetChanged();
                taskInput.getText().clear();
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle item click (Edit, Complete, Delete)
            final String selectedItem = taskList.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Task Options")
                    .setItems(new CharSequence[]{"Edit", "Mark as Complete", "Delete"}, (dialogInterface, which) -> {
                        switch (which) {
                            case 0:
                                editTask(position, selectedItem);
                                break;
                            case 1:
                                markAsComplete(position, selectedItem);
                                break;
                            case 2:
                                deleteTask(position);
                                break;
                        }
                    });
            builder.create().show();
        });
    }

    private void editTask(final int position, String selectedTask) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(selectedTask);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newTask = input.getText().toString().trim();
            if (!newTask.isEmpty()) {
                taskList.set(position, newTask);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void markAsComplete(int position, String selectedTask) {
        taskList.set(position, "[Completed] " + selectedTask);
        adapter.notifyDataSetChanged();
    }

    private void deleteTask(int position) {
        taskList.remove(position);
        adapter.notifyDataSetChanged();
    }
}

