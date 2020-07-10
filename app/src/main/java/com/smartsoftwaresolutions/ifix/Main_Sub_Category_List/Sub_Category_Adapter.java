package com.smartsoftwaresolutions.ifix.Main_Sub_Category_List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.smartsoftwaresolutions.ifix.Main_Category_List.Category_Adapter;
import com.smartsoftwaresolutions.ifix.Main_Category_List.ItemClickListener;
import com.smartsoftwaresolutions.ifix.Main_Category_List.Myholder_Category;
import com.smartsoftwaresolutions.ifix.Order_list_chiled.Order_list_chiled;
import com.smartsoftwaresolutions.ifix.PicassoClient;
import com.smartsoftwaresolutions.ifix.R;

import java.util.ArrayList;

public class Sub_Category_Adapter extends RecyclerView.Adapter<Myholder_Sub_Category> {
    Context c;
    ArrayList<Sub_Category_Button> Sub_Category_list;
    private ItemClickListener itemClickListener;
    public String selected_Sub_Category;
    public String item_is_clicked="";
    public Sub_Category_Adapter(Context c, ArrayList<Sub_Category_Button> Sub_Category_list) {
        this.c = c;
        this.Sub_Category_list = Sub_Category_list;
    }

    @NonNull
    @Override
    public Myholder_Sub_Category onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_sub_button,parent,false);
        Myholder_Sub_Category holder=new Myholder_Sub_Category(v,itemClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Myholder_Sub_Category holder, int position) {
        PicassoClient.downloadImage(c,Sub_Category_list.get(position).getSub_Cat_Image_path(),holder.SC_img);
        holder.SC_textview.setText(Sub_Category_list.get(position).getSub_Cat_Text());
        holder.tv_SC_ID.setText(Sub_Category_list.get(position).getSub_Cat_ID());

      holder.setClickListener(new ItemClickListener() {
          @Override
          public void onClick(View view, int position) {
              selected_Sub_Category =holder.tv_SC_ID.getText().toString();
              Toast.makeText(c,"the position is clicked is "+position+"   "+selected_Sub_Category,Toast.LENGTH_LONG).show();
              item_is_clicked=selected_Sub_Category;
              Intent intent = new Intent(c, Order_list_chiled.class);
               intent.putExtra("SC_ID", selected_Sub_Category);

               c.startActivity(intent);
            //  ((Activity)c).finish();

          }
      });
    }

    @Override
    public int getItemCount() {
        return Sub_Category_list.size();
    }
}
