package com.gauranga.alphavision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<File> image_files;
    File root_dir;
    File[] dirs;

    public void search_images(View view) {
        Toast.makeText(getApplicationContext(), "BUTTON CLICKED", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.searchRecyclerView);
        image_files = new LinkedList<>();

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        // root directory where all the data is stored
        root_dir = wrapper.getDir("IMAGES6", MODE_PRIVATE);
        // array of all the sub-directories we created
        dirs = root_dir.listFiles();
        for (File dir : dirs) {
            for (File image_file : dir.listFiles()) {
                // add all the image files
                image_files.add(image_file);
            }
        }

        setup_recyclerview();
    }

    public void setup_recyclerview() {
        SearchAdapter searchAdapter = new SearchAdapter(this,image_files);
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}