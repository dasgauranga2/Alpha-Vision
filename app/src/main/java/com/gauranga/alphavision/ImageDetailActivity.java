package com.gauranga.alphavision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class ImageDetailActivity extends AppCompatActivity {

    ImageView image;
    File image_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        image = findViewById(R.id.detailImage);

        Intent intent = getIntent();
        // get the image file
        image_file = new File(intent.getStringExtra("IMAGE_PATH"));
        // get the image in bitmap format
        Bitmap bitmap = BitmapFactory.decodeFile(image_file.getPath());
        // set the image view
        image.setImageBitmap(bitmap);
    }
}