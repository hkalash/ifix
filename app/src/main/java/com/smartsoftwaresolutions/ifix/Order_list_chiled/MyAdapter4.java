package com.smartsoftwaresolutions.ifix.Order_list_chiled;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.smartsoftwaresolutions.ifix.R;
import com.smartsoftwaresolutions.ifix.Read_Data.API;
import com.smartsoftwaresolutions.ifix.Read_Data.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter4 extends RecyclerView.Adapter<MyAdapter4.ViewHolder> implements Filterable {
    private List<Itemlistobject4> itemList4;
    private List<Itemlistobject4> All_itemList4;
    private List<Itemlistobject4> ListFiltered;
    private Context context;
    boolean original = true;
    // private MyAdapter4.ItemClickListener clickListener;


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_company_name, tv_Mobile, tv_ID4, tv_address1, tv_likes,tv_dlikes;
        ImageView imageView4;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_company_name = (TextView) itemView.findViewById(R.id.tv_company_name);
            tv_ID4 = itemView.findViewById(R.id.tv_ID4);
            tv_Mobile = (TextView) itemView.findViewById(R.id.tv_Mobile);
            tv_address1 = (TextView) itemView.findViewById(R.id.tv_address1);
            tv_likes = (TextView) itemView.findViewById(R.id.tv_likes);
            tv_dlikes = (TextView) itemView.findViewById(R.id.tv_dlikes);
            imageView4 = (ImageView) itemView.findViewById(R.id.list_avatar4);

        }
    }

    public Itemlistobject4 getItem4(int position) {
        return itemList4.get(position);
    }

    public MyAdapter4(Context context, List<Itemlistobject4> itemList) {

        this.itemList4 = itemList;
        this.context = context;
    }

    @Override
    public MyAdapter4.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem4, parent, false);
// set the view's size, margins, paddings and layout parameters
        MyAdapter4.ViewHolder vh = new MyAdapter4.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyAdapter4.ViewHolder holder, final int position) {
        holder.tv_company_name.setText(itemList4.get(position).getSWM_name());
       // holder.tv_Mobile.setText(itemList4.get(position).getSWM_Phone())
        // company name is used for description
       // holder.tv_Mobile.setText(itemList4.get(position).getSWM_company());
        // the company name is used for description
        holder.tv_Mobile.setText(itemList4.get(position).getSWM_company());
        String imageName = itemList4.get(position).SWM_Pic;
        // holder.imageView4.setImageResource(itemList4.get(position).getSWM_Phone());
        Picasso.get().load(API.Profile_Path + imageName)
              //  .error(R.drawable.ic_launcher_background)
                .error(R.mipmap.ic_launcher)
                .into(holder.imageView4);
        holder.tv_address1.setText(itemList4.get(position).getSWM_Address());
        holder.tv_likes.setText(itemList4.get(position).getSWM_RATE());
        holder.tv_dlikes.setText(itemList4.get(position).getSWM_dis_like());
        holder.tv_ID4.getText().toString();


        holder.imageView4.setTag(holder);
    }


    @Override
    public int getItemCount() {
        // return this.itemList4.size();
        int a;

        if (itemList4 != null && !itemList4.isEmpty()) {

            a = itemList4.size();
        } else {

            a = 0;

        }

        return a;
    }

    /**
     * filter the recyclerview in Gallary text filter
     **/
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                // the search is empty i need all the items so i put a flag
//                if (original){
//                    All_itemList4=itemList4;
//                    // then make it false
//                    original=false;
//                }
                if (charString.isEmpty()) {
                    //   ListFiltered = itemList4;
                    ListFiltered = All_itemList4;

                } else {
                    List<Itemlistobject4> filteredList = new ArrayList<>();
                    for (Itemlistobject4 row : itemList4) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getSWM_name().toLowerCase().contains(charString.toLowerCase())
                                || row.getSWM_Mobile().contains(charSequence)
                                || row.getSWM_Address().toLowerCase().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    ListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = ListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //   ListFiltered = (ArrayList<Itemlistobject4>) filterResults.values;
                itemList4 = (ArrayList<Itemlistobject4>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }

    public interface AdapterListener {
        void onContactSelected(Itemlistobject4 contact);
    }

    public Filter country_filter()  {
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence country_Id) {

//below checks the match for the cityId and adds to the filterlist
                List<Itemlistobject4> ListFiltered;


                if (country_Id.equals("0")) {
                    //ListFiltered= allItems2;
                     ListFiltered = itemList4;
                    //  filteredList = itemList4;
                } else {
                    ArrayList<Itemlistobject4> filteredList = new ArrayList<Itemlistobject4>();
                    for (Itemlistobject4 row : itemList4) {

                        if (row.SWM_Country_ID.equals(country_Id)) {

                            filteredList.add(row);
                        }
                    }


                    // ListFiltered = filteredList;
                    // filterResults.values = filteredList;
                    ListFiltered = filteredList;

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = ListFiltered;
                return filterResults;
            }
            //Publishes the matches found, i.e., the selected cityids
            @Override
            protected void publishResults (CharSequence constraint,
                                           FilterResults filteredResults){
//                mAdapter4 =new MyAdapter4(Order_list_chiled.this,(ArrayList<Itemlistobject4>) filteredResults.values);
//                mRecyclerView4.setAdapter(mAdapter4);

                itemList4 = (ArrayList<Itemlistobject4>) filteredResults.values;
                notifyDataSetChanged();
            }
        } ;
    }

    public Filter country_Sub_filter()  {
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence country_Sub_Id) {

//below checks the match for the cityId and adds to the filterlist
                FilterResults filterResults = new FilterResults();
                if (country_Sub_Id.equals("0")) {
                 //   ListFiltered = All_itemList4;
                    // itemlist4 is the all items
                    /**put the fillterd country list in the listitem4**/

                   ListFiltered = itemList4;
                } else {
                    ArrayList<Itemlistobject4> filteredList = new ArrayList<Itemlistobject4>();
                    for (Itemlistobject4 row : itemList4) {

                        if (row.SWM_Sub_C.equals(country_Sub_Id)) {

                            filteredList.add(row);
                        }
                    }

                    ListFiltered = filteredList;

                    filterResults.values = ListFiltered;


                }
                return filterResults;
            }
            //Publishes the matches found, i.e., the selected cityids
            @Override
            protected void publishResults (CharSequence constraint,
                                           FilterResults filteredResults){

                itemList4 = (ArrayList<Itemlistobject4>) filteredResults.values;
                notifyDataSetChanged();
            }
        } ;
    }

    public Filter Cat_filter()  {
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence category_Id) {

//below checks the match for the cityId and adds to the filterlist
                FilterResults filterResults = new FilterResults();
                if (category_Id.equals("0")) {
                    ListFiltered = itemList4;
                } else {
                    ArrayList<Itemlistobject4> filteredList = new ArrayList<Itemlistobject4>();
                    for (Itemlistobject4 row : itemList4) {

                        if (row.WC_ID.equals(category_Id)) {

                            filteredList.add(row);
                        }
                    }

                    ListFiltered = filteredList;
                    filterResults.values = ListFiltered;


                }
                return filterResults;
            }
            //Publishes the matches found, i.e., the selected cityids
            @Override
            protected void publishResults (CharSequence constraint,
                                           FilterResults filteredResults){

                itemList4 = (ArrayList<Itemlistobject4>) filteredResults.values;
                notifyDataSetChanged();
            }
        } ;
    }

    public Filter Cat_Sub_filter()  {
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence category_Sub_Id) {

//below checks the match for the cityId and adds to the filterlist
                FilterResults filterResults = new FilterResults();
                if (category_Sub_Id.equals("0")) {
                    ListFiltered = itemList4;
                } else {
                    ArrayList<Itemlistobject4> filteredList = new ArrayList<Itemlistobject4>();
                    for (Itemlistobject4 row : itemList4) {

                        if (row.MS_ID.equals(category_Sub_Id)) {

                            filteredList.add(row);
                        }
                    }

                    ListFiltered = filteredList;
                    filterResults.values = ListFiltered;


                }
                return filterResults;
            }
            //Publishes the matches found, i.e., the selected cityids
            @Override
            protected void publishResults (CharSequence constraint,
                                           FilterResults filteredResults){

                itemList4 = (ArrayList<Itemlistobject4>) filteredResults.values;
                notifyDataSetChanged();
            }
        } ;
    }
    }


