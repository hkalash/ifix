package com.smartsoftwaresolutions.ifix.Images;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.smartsoftwaresolutions.ifix.R;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class Image_Adapter extends RecyclerView.Adapter<Image_Adapter.ViewHolder> {
    private List<Itemlist_image> itemList;
    private Context context;
    private ItemClickListener clickListener;


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_image;
        ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_image = (TextView) itemView.findViewById(R.id.tv_image);
            imageView = (ImageView) itemView.findViewById(R.id.list_avatar9);

        }
    }
    public Itemlist_image getItem(int position) {
        return itemList.get(position);
    }
    public Image_Adapter(Context context, List<Itemlist_image> itemList) {

        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public Image_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_list, parent, false);
// set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //IMAGE
        //  PicassoClient.downloadImage(context,itemList.get(position).getImage_path(),holder.imageView);
//        ContextWrapper cw = new ContextWrapper(context);
//        File directory = cw.getDir("CI", Context.MODE_PRIVATE);
        ContextWrapper cw = new ContextWrapper(context);
        //File directory =
        String image_path=itemList.get(position).getImage_uri();
        Uri uri=Uri.parse(itemList.get(position).getImage_uri());
        if (image_path.equals("empty")){
            holder.imageView.setImageResource(R.drawable.add_photo);

        }else {
uri=Uri.parse( RemoveUnwantedString(String.valueOf(uri)));

            holder.imageView.setImageURI(uri);
        }


        File myImageFile = new File(uri.getPath());
//        Picasso.get().load(myImageFile)
//                .error(R.mipmap.ic_launcher)
//                .into(holder.imageView);


        holder.tv_image.setText(itemList.get(position).getImage_uri());






        holder.imageView.setTag(holder);
    }
    public String RemoveUnwantedString(String pathUri){
        //pathUri = "content://com.google.android.apps.photos.contentprovider/-1/2/content://media/external/video/media/5213/ORIGINAL/NONE/2106970034"
        String[] d1 = pathUri.split("content://");
        for (String item1:d1) {
            if (item1.contains("media/")) {
                String[] d2 = item1.split("/ORIGINAL/");
                for (String item2:d2) {
                    if (item2.contains("media/")) {
                        pathUri = "content://" + item2;
                        break;
                    }
                }
                break;
            }
        }
        //pathUri = "content://media/external/video/media/5213"
        return pathUri;
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

    public void updateList(List<Itemlist_image> picturesList) {
        this.itemList = picturesList;
        notifyDataSetChanged();
    }
}
