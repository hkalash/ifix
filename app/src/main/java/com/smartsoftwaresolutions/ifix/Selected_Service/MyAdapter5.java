package com.smartsoftwaresolutions.ifix.Selected_Service;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartsoftwaresolutions.ifix.R;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


public class MyAdapter5 extends RecyclerView.Adapter<MyAdapter5.ViewHolder> {
    private final OnItemCheckListener onItemClick;
    private List<Itemlistobject5> itemList2;
    private Context context;
    //  private ItemClickListener clickListener;
    View itemvew;
    // private MyAdapter2.ItemClickListener clickListener;
    interface OnItemCheckListener {
        void onItemCheck(Itemlistobject5 itemList2);
        void onItemUncheck(Itemlistobject5 itemList2);
    }

    @NonNull
    private OnItemCheckListener onItemCheckListener;

    public MyAdapter5 (Context context,List<Itemlistobject5> items, @NonNull OnItemCheckListener onItemCheckListener) {
        this.itemList2 = items;
        this.onItemClick = onItemCheckListener;
        this.context=context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView textView21;
        View itemView;




        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            textView21 = (TextView) itemView.findViewById(R.id.list_title21);


        }
        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
//        @Override
//        public void itemClick(View view, int position) {
//
//        }
    }

    public Itemlistobject5 getItem2(int position) {
        return itemList2.get(position);
    }

//    public MyAdapter2(Context context, List<Itemlistobject2> itemList) {
//
//        this.itemList2 = itemList;
//        this.context = context;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ss_layout, parent, false);
// set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final Itemlistobject5 currentItem = itemList2.get(position);

            holder.textView21.setText(itemList2.get(position).getService_title());




        }




    }

    @Override
    public int getItemCount() {
        return this.itemList2.size();
    }


}
