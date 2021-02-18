package com.gauranga.alphavision;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class NewDirectoryActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    List<Uri> image_uris;
    EditText directory_name;

    // add new image to the directory
    public void add_image_gallery(View view) {
        // create an intent to launch the gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    // create a new directory
    public void create_directory(View view) throws IOException {
        // get the name of the directory we want to create
        String name = directory_name.getText().toString();

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        // root directory where all data is stored
        File root_dir = wrapper.getDir("IMAGES5", MODE_PRIVATE);
        // sub-directory we want to create
        File new_dir = new File(root_dir, name);
        // create the sub-directory
        new_dir.mkdir();
        for (Uri image_uri : image_uris) {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
            // create an image file in the sub-directory
            File image_file = new File(new_dir, System.currentTimeMillis() + ".jpg");
            OutputStream stream = null;
            // an output stream that writes bytes to a file
            stream = new FileOutputStream(image_file);
            // write a compressed version of the bitmap to the specified output stream
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();
        }

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
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

        image_uris = new LinkedList<>();
        directory_name = findViewById(R.id.directoryNameText);
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