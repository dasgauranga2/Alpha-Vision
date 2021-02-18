package com.gauranga.alphavision;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class DirectoryListAdapter extends RecyclerView.Adapter<DirectoryListAdapter.MyViewHolder> {

    File[] image_dirs;
    Context context;

    // the context and the data is passed to the adapter
    public DirectoryListAdapter(Context ct, File[] files) {
        context = ct;
        image_dirs = files;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // The 'directory_row.xml' file in layout folder defines
        // the style for each row
        View view = inflater.inflate(R.layout.directory_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // display the name of the directory
        holder.text.setText(image_dirs[position].getName());

        // detect if an item is clicked
        holder.dir_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ImageListActivity.class);
                // pass the path of the image directory clicked
                intent.putExtra("DIR_NAME",image_dirs[position].getAbsolutePath());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // return the length of the recycler view
        return image_dirs.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        // Each row will contain one text view
        // for displaying the data
        TextView text;
        ConstraintLayout dir_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.directoryText);
            dir_layout = itemView.findViewById(R.id.directory_layout);
            //main_layout = itemView.findViewById(R.id.row_layout);
        }
    }
}
