package com.smartsoftwaresolutions.ifix.Advertisement_Gallery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartsoftwaresolutions.ifix.PicassoClient;
import com.smartsoftwaresolutions.ifix.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
//    ArrayList personNames;
//    ArrayList personImages;
ArrayList<image_Info> imageurl;
    Context context;
    public CustomAdapter(Context context, ArrayList<image_Info> imageurl) {
        this.context = context;
//        this.personNames = personNames;
//        this.personImages = personImages;
        this.imageurl=imageurl;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.advertisement_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(final CustomAdapter.MyViewHolder holder, final int position) {
        // set the data in items
        holder.name.setText(imageurl.get(position).getAd_Name());
//IMAGE
        PicassoClient.downloadImage(context,imageurl.get(position).getAD_Image(),holder.image);
//        Picasso.get()
//                .load(imageurl.get(position).getAD_Image())
//                .centerCrop()
//                .fit()
//                .into(holder.image);
      //  holder.image.setImageResource(personImages.get(position));
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open another activity on item click
                Intent intent = new Intent(context, SecoundActivity.class);
                intent.putExtra("image", imageurl.get(position).getAD_Image()); // put image data in Intent
                context.startActivity(intent); // start Intent
            }
        });
    }


    @Override
    public int getItemCount() {
        return imageurl.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;
        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.imageA);
        }
    }
}
