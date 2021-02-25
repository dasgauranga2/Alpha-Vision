package com.gauranga.alphavision;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

    List<File> image_files;
    Context context;

    // the context and the data is passed to the adapter
    public ImageListAdapter(Context ct, List<File> file_list) {
        context = ct;
        image_files = file_list;
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
        // and get the image file
        File image_file = image_files.get(position);
        // set the image to the image view
        Picasso.get()
                .load(image_file)
                .resize(600,600)
                .into(holder.image);
        // detect if an image is clicked
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ImageDetailActivity.class);
                intent.putExtra("IMAGE_PATH", image_file.getAbsolutePath());
                context.startActivity(intent);
            }
        });
        // detect if an image is long clicked
        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // launch an alert dialog
                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.alert_triangle)
                        .setTitle("Do you want to delete the image ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete_image(position,image_file);
                            }
                        })
                        .setNegativeButton("NO", null).show();
                return false;
            }
        });
    }

    // delete an image file
    public void delete_image(int p, File f) {
        // remove the image from the list
        image_files.remove(p);
        // notify that item has been removed
        notifyItemRemoved(p);
        notifyItemRangeChanged(p, image_files.size());
        // delete the file
        f.delete();
    }

    @Override
    public int getItemCount() {
        // return the length of the recycler view
        return image_files.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        // Each row will display an image
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.dirImage);
        }
    }
}