package com.gauranga.alphavision;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class NewDirectoryActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    ImageView image;
    List<Uri> image_uris;

    // add new image to the directory
    public void add_image_gallery(View view) {
        // create an intent to launch the gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the image is successfully retrieved
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            // get the image in uri format
            Uri image_uri = data.getData();
            // add the image uri to the list
            image_uris.add(image_uri);
            // setup the recycler view
            setup_recyclerview();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_directory);

        image = findViewById(R.id.imageView);
        image_uris = new LinkedList<>();
        Toast.makeText(getApplicationContext(), "HELLO", Toast.LENGTH_SHORT).show();
    }

    // setup the recycler view
    // containing a list of images
    public void setup_recyclerview() {
        RecyclerView recyclerView = findViewById(R.id.imageRecyclerView);
        NewDirectoryAdapter newDirectoryAdapter = new NewDirectoryAdapter(this,image_uris);
        recyclerView.setAdapter(newDirectoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}