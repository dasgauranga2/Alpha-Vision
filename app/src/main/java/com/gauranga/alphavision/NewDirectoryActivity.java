package com.gauranga.alphavision;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class NewDirectoryActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 200;
    List<Uri> image_uris;
    List<Integer> image_orientations;
    EditText directory_name;
    Uri camera_image_uri;
    File camera_image_file;

    // add new image to the directory
    // using the gallery
    public void add_image_gallery(View view) {
        // create an intent to launch the gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    // add new image to the directory
    // using the camera
    public void add_image_camera(View view) {
        // create an intent to launch the camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // check if a camera is available
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // create the image file
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // check if the image file was successfully created
            if (photoFile != null) {
                // reference to the image file
                camera_image_file = photoFile;
                // uri where the image will be stored
                camera_image_uri = FileProvider.getUriForFile(this,
                        "com.gauranga.alphavision",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, camera_image_uri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // create a new directory
    public void create_directory(View view) throws IOException {
        // get the name of the directory we want to create
        String name = directory_name.getText().toString();
        // check if directory name is empty
        if (name.length()==0) {
            Toast.makeText(getApplicationContext(), "FOLDER NAME EMPTY", Toast.LENGTH_SHORT).show();
            return;
        }
        // check if no images are added
        if (image_uris.size()==0) {
            Toast.makeText(getApplicationContext(), "NO IMAGES ADDED", Toast.LENGTH_SHORT).show();
            return;
        }

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        // root directory where all data is stored
        File root_dir = wrapper.getDir("IMAGES7", MODE_PRIVATE);
        // sub-directory we want to create
        File new_dir = new File(root_dir, name);
        // create the sub-directory
        new_dir.mkdir();
        for (int i=0; i<image_uris.size(); i++) {
            Uri image_uri = image_uris.get(i);
            int orientation = image_orientations.get(i);
            // create bitmap from image uri
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);
            // rotate the bitmap according to image orientation
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
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
        // check if the image is successfully retrieved from the gallery
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            // get the image in uri format
            Uri image_uri = data.getData();
            // add the image uri to the list
            image_uris.add(image_uri);
            // setup the recycler view
            setup_recyclerview();
            // get the orientation of the image
            String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
            Cursor cur = getContentResolver().query(image_uri, orientationColumn, null, null, null);
            int orientation = -1;
            if (cur != null && cur.moveToFirst()) {
                orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
            }
            // add the image orientation to the list
            image_orientations.add(orientation);
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // get the image in uri format
            Uri image_uri = camera_image_uri;
            // add the image uri to the list
            image_uris.add(image_uri);
            // setup the recycler view
            setup_recyclerview();
            // get the orientation of the image
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(camera_image_file.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = -1;
            int img_ori = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch(img_ori) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    orientation = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    orientation = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    orientation = 270;
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    orientation = 0;
            }
            // add the image orientation to the list
            image_orientations.add(orientation);
        }
    }

    // given the uri of an image
    // generate the keywords
    public void generate_keywords(Uri uri) {

        InputImage input_image;
        try {
            input_image = InputImage.fromFilePath(getApplicationContext(), uri);
            TextRecognizer recognizer = TextRecognition.getClient();
            Task<Text> result = recognizer.process(input_image)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text visionText) {
                            String resultText = visionText.getText();
                            for (Text.TextBlock block : visionText.getTextBlocks()) {
                                String blockText = block.getText();
                                Log.i("TEXT_BLOCK", blockText);
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "TEXT RECOGNITION FAILED", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_directory);

        image_uris = new LinkedList<>();
        image_orientations = new LinkedList<>();
        directory_name = findViewById(R.id.directoryNameText);
    }

    // setup the recycler view
    // containing a list of images
    public void setup_recyclerview() {
        RecyclerView recyclerView = findViewById(R.id.imageRecyclerView);
        NewDirectoryAdapter newDirectoryAdapter = new NewDirectoryAdapter(this,image_uris);
        recyclerView.setAdapter(newDirectoryAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}