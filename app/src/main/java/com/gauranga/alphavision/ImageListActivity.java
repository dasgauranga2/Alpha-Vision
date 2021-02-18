package com.gauranga.alphavision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

public class ImageListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        recyclerView = findViewById(R.id.imageListRecyclerView);

        Intent intent = getIntent();
        // get the path of the directory
        // that was clicked
        String file_name = intent.getStringExtra("DIR_NAME");
        // create a file object
        File image_dir = new File(file_name);
        // array of all the image files
        File[] image_files = image_dir.listFiles();

        ImageListAdapter imageListAdapter = new ImageListAdapter(this, image_files);
        recyclerView.setAdapter(imageListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}