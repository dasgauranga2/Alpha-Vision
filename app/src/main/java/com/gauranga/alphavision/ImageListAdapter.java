package com.gauranga.alphavision;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

    File[] image_files;
    Context context;

    // the context and the data is passed to the adapter
    public ImageListAdapter(Context ct, File[] files) {
        context = ct;
        image_files = files;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // The 'image_list_row.xml' file in layout folder defines
        // the style for each row
        View view = inflater.inflate(R.layout.image_list_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // use the data passed to the adapter above
        // and set the data to the text views below
//        holder.lang.setText(data1[position]);
//        holder.desc.setText(data2[position]);
        File image_file = image_files[position];
        Bitmap bitmap = BitmapFactory.decodeFile(image_file.getPath());
        holder.image.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        // return the length of the recycler view
        return image_files.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        // Each row will contain two text views
        // for displaying the data
        ImageView image;
        // Select the root layout of the 'row.xml' file
        //ConstraintLayout main_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Set the text view from the 'row.xml' file in layout folder
//            lang = itemView.findViewById(R.id.language);
//            desc = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.dirImage);
            //main_layout = itemView.findViewById(R.id.row_layout);
        }
    }
}