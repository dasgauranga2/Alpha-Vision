package com.gauranga.alphavision;

//public class NewDirectoryAdapter {
//}

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewDirectoryAdapter extends RecyclerView.Adapter<NewDirectoryAdapter.MyViewHolder> {

    List<Uri> image_uris;
    Context context;

    // the context and the data is passed to the adapter
    public NewDirectoryAdapter(Context ct, List<Uri> img_uris) {
        context = ct;
        image_uris = img_uris;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // The 'new_directory_row.xml' file in layout folder defines
        // the style for each row
        View view = inflater.inflate(R.layout.new_directory_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get()
                .load(image_uris.get(position))
                .resize(600,600)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        // return the length of the recycler view
        return image_uris.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.currentDirImage);
        }
    }
}