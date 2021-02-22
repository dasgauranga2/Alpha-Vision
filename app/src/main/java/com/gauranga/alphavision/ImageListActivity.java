package com.gauranga.alphavision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;

public class ImageListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView dir_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        recyclerView = findViewById(R.id.imageListRecyclerView);
        dir_name = findViewById(R.id.dirText);

        Intent intent = getIntent();
        // get the path of the directory
        // that was clicked
        String file_path = intent.getStringExtra("DIR_PATH");
        // create a file object
        File image_dir = new File(file_path);
        // array of all the image files
        File[] image_files = image_dir.listFiles();
        // set the name of the current directory
        dir_name.setText(intent.getStringExtra("DIR_NAME"));

        ImageListAdapter imageListAdapter = new ImageListAdapter(this, image_files);
        recyclerView.setAdapter(imageListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}