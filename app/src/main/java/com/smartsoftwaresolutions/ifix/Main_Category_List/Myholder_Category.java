package com.smartsoftwaresolutions.ifix.Main_Category_List;

import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.smartsoftwaresolutions.ifix.R;



public class Myholder_Category extends RecyclerView.ViewHolder implements View.OnClickListener{

    ImageView img;
    TextView  cat_id,tv_cat_name;
//RecyclerViewItemClickListener recyclerViewItemClickListener;
private ItemClickListener clickListener;

    public Myholder_Category(View itemView,ItemClickListener itemClickListener) {
        super(itemView);
       img=itemView.findViewById(R.id.btn_category) ;
       cat_id=itemView.findViewById(R.id.cat_id);
        tv_cat_name=itemView.findViewById(R.id.tv_cat_name);
       // to implement on click listener
        clickListener=itemClickListener;
        itemView.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
clickListener.onClick(v,getAdapterPosition());
    }
    public void setClickListener(ItemClickListener itemClickListener){
        this.clickListener=itemClickListener;
    }
}
