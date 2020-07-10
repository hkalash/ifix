package com.smartsoftwaresolutions.ifix.Select_Sub_Category_Service_Menu;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.smartsoftwaresolutions.ifix.PicassoClient;
import com.smartsoftwaresolutions.ifix.R;

import com.squareup.picasso.Picasso;


import java.io.File;
import java.util.List;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> {
    private final OnItemCheckListener onItemClick;
    private List<Itemlistobject2> itemList2;
    private Context context;
    SharedPreferences SpType ;
    public static final String PREFS_NAME = "ifix_data";
    boolean flag_register=false;
  //  private ItemClickListener clickListener;
    View itemvew;
   // private MyAdapter2.ItemClickListener clickListener;
   interface OnItemCheckListener {
       void onItemCheck(Itemlistobject2 itemList2);
       void onItemUncheck(Itemlistobject2 itemList2);
   }

    @NonNull
    private OnItemCheckListener onItemCheckListener;

    public MyAdapter2 (Context context,List<Itemlistobject2> items, @NonNull OnItemCheckListener onItemCheckListener) {
        this.itemList2 = items;
        this.onItemClick = onItemCheckListener;
        this.context=context;
        SpType=context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        flag_register=SpType.getBoolean("flag_register",false);
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView textView2, textView12, tv_ID2;
        ImageView imageView2;
        CheckBox checkBox;
        View itemView;


        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;


            textView2 = (TextView) itemView.findViewById(R.id.list_title2);
            tv_ID2 = itemView.findViewById(R.id.tv_ID2);
            textView12 = (TextView) itemView.findViewById(R.id.list_desc2);
            imageView2 = (ImageView) itemView.findViewById(R.id.list_avatar2);
             checkBox=itemView.findViewById(R.id.checkBox);
            checkBox.setClickable(false);



        }
        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
//        @Override
//        public void itemClick(View view, int position) {
//
//        }
    }

    public Itemlistobject2 getItem2(int position) {
        return itemList2.get(position);
    }

//    public MyAdapter2(Context context, List<Itemlistobject2> itemList) {
//
//        this.itemList2 = itemList;
//        this.context = context;
//    }

    @Override
    public MyAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem2, parent, false);
// set the view's size, margins, paddings and layout parameters
        MyAdapter2.ViewHolder vh = new MyAdapter2.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyAdapter2.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final Itemlistobject2 currentItem = itemList2.get(position);

            holder.textView2.setText(itemList2.get(position).getName2());
            holder.textView12.setText(itemList2.get(position).getDesc2());
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("MI", Context.MODE_PRIVATE);
            File myImageFile = new File(directory, itemList2.get(position).getImage_name());


            PicassoClient.downloadImage(context,
                    itemList2.get(position).getImage_path(),
                    holder.imageView2);
//            Picasso.get().load(myImageFile)
//                    .error(R.drawable.ic_launcher_background)
//                    .into(holder.imageView2);
            // the user is editing its service from my profile

            // holder.imageView2.setImageResource(itemList2.get(position).getPhoto2());
            holder.tv_ID2.setText(itemList2.get(position).getID2());
            holder.tv_ID2.getText().toString();
            // holder.imageView2.setTag(holder);
            holder.checkBox.setTag(position);

//            if (flag_register){
//                holder.checkBox.setChecked(itemList2.get(position).isSelected());
//            }else {
//                // nnothing
//            }
            ((ViewHolder) holder).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ViewHolder) holder).checkBox.setChecked(
                            !((ViewHolder) holder).checkBox.isChecked());
                    if (((ViewHolder) holder).checkBox.isChecked()) {
                        onItemClick.onItemCheck(currentItem);
                    } else {
                        onItemClick.onItemUncheck(currentItem);
                    }
                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return this.itemList2.size();
    }




}
