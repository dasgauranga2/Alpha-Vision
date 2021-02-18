package com.gauranga.alphavision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // add a new image directory
    public void new_directory(View view) {
        Intent intent = new Intent(getApplicationContext(),NewDirectoryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        // root directory where all the data is stored
        File root_dir = wrapper.getDir("IMAGES5", MODE_PRIVATE);
        // array of all the sub-directories
        File[] files = root_dir.listFiles();

        for (File f : files) {
            // array of images files in each sub-directory
            File[] image_files = f.listFiles();
            for (File image : image_files) {
                Log.i("FILE_INFO", f.getName() + " " + image.getName());
            }
        }
    }
}