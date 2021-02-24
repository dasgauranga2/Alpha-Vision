package com.gauranga.alphavision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<File> image_files;
    EditText search;
    File root_dir;
    File[] dirs;
    TextView status;
    int images_scanned;
    HashMap<File, List<String>> file_texts;

    // search for images
    // that have the required text
    public void search_images(View view) {
        image_files = new LinkedList<>();
        setup_recyclerview();

        // get the word entered by the user
        String search_word = search.getText().toString().toLowerCase();
        // check if the word entered by
        // the user has zero length
        if (search_word.length() == 0) {
            return;
        }

        // hide the keyboard if open
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // create a timer
        // to delay searching for images
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // count the number of images
                // that have been processed by the text recognizer
                images_scanned = 0;
                // search for image files
                // given the word entered by the user
                search(search_word);
                //check_empty();
            }
        },200);
    }

    public void update_status() {
        images_scanned++;
        status.setText(images_scanned + " images scanned");
    }

    // iterate over all the created directories
    // and then iterate over all the image files
    public void search(String search_word) {

        // check if the hash map is empty
        // if the hash map is empty each image file
        // has to be processed and the corresponding text blocks are saved
        if (file_texts.size()==0) {
            for (File dir : dirs) {
                for (File image_file : dir.listFiles()) {
                    // get the image in bitmap format
                    Bitmap bitmap = BitmapFactory.decodeFile(image_file.getPath());

                    InputImage image = InputImage.fromBitmap(bitmap,0);
                    TextRecognizer recognizer = TextRecognition.getClient();
                    // process the image
                    Task<Text> result = recognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {
                                    // if an image has been successfully processed
                                    // update the status to notify the user
                                    update_status();
                                    // list containing the text blocks for the image
                                    List<String> text_blocks = new LinkedList<>();
                                    // boolean variable to check if the image file contains the word entered by the user
                                    boolean contains_word = false;
                                    // for each image
                                    // iterate over blocks of text
                                    for (Text.TextBlock block : visionText.getTextBlocks()) {
                                        // block of text in lowercase
                                        String blockText = block.getText().toLowerCase();
                                        // check if the block of text
                                        // contains the word entered by the user
                                        if (blockText.contains(search_word)) {
                                            //image_files.add(image_file);
                                            //setup_recyclerview();
                                            //break;
                                            contains_word = true;
                                        }
                                        text_blocks.add(blockText);
                                    }
                                    if (contains_word) {
                                        image_files.add(image_file);
                                        setup_recyclerview();
                                    }
                                    file_texts.put(image_file, text_blocks);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"IMAGE PROCESSING FAILED",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        }
        else {
            // if the hash map is not empty
            // check for image files that contain the word
            // we don't have to process each image using text recognizer
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (File image_file : file_texts.keySet()) {
                        update_status();
                        List<String> texts = file_texts.get(image_file);
                        for (String text : texts) {
                            if (text.contains(search_word)) {
                                image_files.add(image_file);
                                setup_recyclerview();
                                break;
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // hash map containing list of text blocks for each image file
        file_texts = new HashMap<>();
        // list containing the image files
        image_files = new LinkedList<>();
        search = findViewById(R.id.searchImageText);
        recyclerView = findViewById(R.id.searchRecyclerView);
        // by default it will display 'All images'
        // otherwise while searching it will
        // display the number of images processed
        status = findViewById(R.id.statusText);

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        // root directory where all the data is stored
        root_dir = wrapper.getDir("IMAGES7", MODE_PRIVATE);
        // array of all the sub-directories we created
        dirs = root_dir.listFiles();
        // iterate all the sub-directories
        for (File dir : dirs) {
            // iterate all the image files
            for (File image_file : dir.listFiles()) {
                image_files.add(image_file);
            }
        }

        setup_recyclerview();
    }

    // setup the recyclerview
    public void setup_recyclerview() {

        SearchAdapter searchAdapter = new SearchAdapter(this,image_files);
        recyclerView.setAdapter(searchAdapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }
}