package com.smartsoftwaresolutions.ifix.Main_Sub_Category_List;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartsoftwaresolutions.ifix.Main_Category_List.ItemClickListener;
import com.smartsoftwaresolutions.ifix.R;

public class Myholder_Sub_Category extends RecyclerView.ViewHolder implements View.OnClickListener {
ImageView SC_img;
TextView SC_textview,tv_SC_ID;
    private ItemClickListener clickListener;
    public Myholder_Sub_Category(@NonNull View itemView,ItemClickListener itemClickListener) {
                super(itemView);
        SC_img=itemView.findViewById(R.id.btn_sub_cat) ;
        SC_textview=itemView.findViewById(R.id.tv_sub_cat);
        tv_SC_ID=itemView.findViewById(R.id.tv_SC_ID);

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

