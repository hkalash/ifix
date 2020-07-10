package com.smartsoftwaresolutions.ifix.Select_Main_category_Service_Menu;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.smartsoftwaresolutions.ifix.PicassoClient;
import com.smartsoftwaresolutions.ifix.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Itemlistobject> itemList;
    private Context context;
    private ItemClickListener clickListener;


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, textView1,tv_ID;
        ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.list_title);
            tv_ID=itemView.findViewById(R.id.tv_ID);
            textView1 = (TextView) itemView.findViewById(R.id.list_desc);
            imageView = (ImageView) itemView.findViewById(R.id.list_avatar2);
           // checkBox=itemView.findViewById(R.id.checkBox);
        }
    }
    public Itemlistobject getItem(int position) {
        return itemList.get(position);
    }
    public MyAdapter(Context context, List<Itemlistobject> itemList) {

        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
// set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

        //IMAGE
      //  PicassoClient.downloadImage(context,itemList.get(position).getImage_path(),holder.imageView);
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("CI", Context.MODE_PRIVATE);
        File myImageFile = new File(directory, itemList.get(position).getImage_name());
       // Picasso.get().load(myImageFile).into(holder.imageView);
        PicassoClient.downloadImage(context,
                itemList.get(position).getImage_path(),
                holder.imageView);
        holder.textView.setText(itemList.get(position).getName());
        holder.textView1.setText(itemList.get(position).getDesc());
      //  holder.imageView.setImageResource(itemList.get(position).getPhoto());
        holder.tv_ID.setText(itemList.get(position).getID());
        holder.tv_ID.getText().toString();




        holder.imageView.setTag(holder);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public interface ItemClickListener {
        public void itemClick(View view, int position);

    }

}
