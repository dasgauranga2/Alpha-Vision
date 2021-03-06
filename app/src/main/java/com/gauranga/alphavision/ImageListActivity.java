package com.gauranga.alphavision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ImageListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView dir_name;
    File image_dir;
    File[] image_files;

    // delete the image directory
    // and all the images in it
    public void delete_image_dir(View view) {
        // launch an alert dialog
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.alert_triangle)
                .setTitle("Do you want to delete the folder ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete all the images inside the image directory
                        for (File f : image_files) {
                            f.delete();
                        }
                        // delete the image directory
                        image_dir.delete();

                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("NO", null).show();
    }

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
        image_dir = new File(file_path);
        // array of all the image files
        image_files = image_dir.listFiles();
        // set the name of the current directory
        dir_name.setText(intent.getStringExtra("DIR_NAME"));

        setup_recyclerview();
    }

    public void setup_recyclerview() {

        List<File> file_list = new LinkedList<>();
        for (File f : image_files) {
            file_list.add(f);
        }

        ImageListAdapter imageListAdapter = new ImageListAdapter(this, file_list);
        recyclerView.setAdapter(imageListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }
}