package com.gauranga.alphavision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    // add a new image directory
    public void new_directory(View view) {
        Intent intent = new Intent(getApplicationContext(),NewDirectoryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.directoryRecyclerView);

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        // root directory where all the data is stored
        File root_dir = wrapper.getDir("IMAGES6", MODE_PRIVATE);
        // array of all the sub-directories we created
        File[] files = root_dir.listFiles();

        // setup recyclerview to display the list of all the directories
        DirectoryListAdapter directoryListAdapter = new DirectoryListAdapter(this,files);
        recyclerView.setAdapter(directoryListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}