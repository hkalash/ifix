package com.smartsoftwaresolutions.ifix.Main_Category_List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartsoftwaresolutions.ifix.Database.DataHelper;
import com.smartsoftwaresolutions.ifix.Main_Sub_Category_List.Sub_Category_Adapter;
import com.smartsoftwaresolutions.ifix.Main_Sub_Category_List.Sub_Category_Button;
import com.smartsoftwaresolutions.ifix.PicassoClient;
import com.smartsoftwaresolutions.ifix.R;
import com.smartsoftwaresolutions.ifix.Order_Service.order_one_master;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.smartsoftwaresolutions.ifix.Picasso_Target.picassoImageTarget;

public class Category_Adapter extends RecyclerView.Adapter<Myholder_Category> {
public String item_is_clicked="";
public String selected_Category="7";
    Context c;
    ArrayList<Category_Button> Category_list;
    private ItemClickListener itemClickListener;

    String[] MI_image,MI_Image_path;
    List<String> MI_image_L = new ArrayList<String>();
    List<String> MI_Image_path_L = new ArrayList<String>();
    String lang;
    SharedPreferences SpType;
    public static final String PREFS_NAME = "ifix_data";

    public Category_Adapter(Context c, ArrayList<Category_Button> Category_list) {
        this.c = c;
        this.Category_list = Category_list;


    }



    @NonNull
    @Override
    public Myholder_Category onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_button,parent,false);
        Myholder_Category holder=new Myholder_Category(v,itemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Myholder_Category holder, int position) {


        //IMAGE
        PicassoClient.downloadImage(c,Category_list.get(position).getCategory_Image_path(),holder.img);

        holder.cat_id.setText(Category_list.get(position).getCategory_ID());
        holder.tv_cat_name.setText(Category_list.get(position).getDesc());
        // this is the tag that we used
        holder.img.setTag(holder);
holder.setClickListener(new ItemClickListener() {
    @Override
    public void onClick(View view, int position) {
         selected_Category =holder.cat_id.getText().toString();
        Toast.makeText(c,"the position is clicked is "+position+"   "+selected_Category,Toast.LENGTH_LONG).show();
        item_is_clicked=selected_Category;
        ///notifyDataSetChanged();
        Fill_Sub_Category fill_sub_category=new Fill_Sub_Category();
        fill_sub_category.execute("");
    }


});
    }

    @Override
    public int getItemCount() {
        return Category_list.size();
    }
/** this is to fill the sub catigory the defult is catigory number 7 **/
    public  class Fill_Sub_Category extends AsyncTask<String, String, String> {

        String z = "";
        DataHelper AndPOS_dbh;
        SQLiteDatabase AndPOS_db;
        Sub_Category_Adapter ADA_Sub;
        ArrayList<Sub_Category_Button> Sub_Categorylist = new ArrayList<Sub_Category_Button>();

        RecyclerView gridView;

        @Override
        protected void onPreExecute() {

            // we create an instance of data base helper  database this will create the database
            AndPOS_dbh = new DataHelper(c);
            // and we connect the data to the data base helper and specify its use as a writable
            AndPOS_db = AndPOS_dbh.getWritableDatabase();

            SpType=c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


            // Reading from SharedPreferences
            lang=SpType.getString("lang","en_US");

        }

        @Override
        protected void onPostExecute(String r) {


          //  Toast.makeText( order_one_master.this, r, Toast.LENGTH_SHORT ).show();
            // reading from the keys that we will use to raed from the customer list they must be the same as a hash map keys


            // the ada is binding all together
            // ADA = new SimpleAdapter(SearchCustomer.this, Custlist, R.layout.customer_element, from, views);
            ADA_Sub = new Sub_Category_Adapter( c,Sub_Categorylist );
            // put them in the list view

      order_one_master.gridView.setAdapter( ADA_Sub );
/**   load images to the device**/
            MI_Image_path = new String[MI_Image_path_L.size()];
            MI_Image_path = MI_Image_path_L.toArray(MI_Image_path);//now strings is the resulting array
            MI_image = new String[MI_image_L.size()];
            MI_image =  MI_image_L.toArray( MI_image);//now strings is the resulting array

            // load images to the app folder
            for (int x=0;x<MI_image.length;x++){
                String i_name=MI_image[x];
                String i_path=MI_Image_path[x];
                Picasso.get().load(i_path).into(picassoImageTarget(c, "MI", i_name));
            }

        }

        @Override
        protected String doInBackground(String... params) {


            // we specify the database helper and the database and set the database to readable
            // then we write a select statment
            //   String query_SQL = "select * from Family_Tbl ";
            String query_SQL = "select * from Master_Service_tbl where SC_ID="+selected_Category;
            // we run this select statment by creating a cursor and execute it
            Cursor cursor = AndPOS_db.rawQuery( query_SQL, null );
            // if the result of the execute statment have data then the if statment is true and we move the cursor to the first row
            if (cursor.moveToFirst()) {
                do {
                    // we create a hash map with string for the key and string for the data
                    HashMap<String, String> map = new HashMap<String, String>();
                    String Sub_Cat_id, Sub_Cat_image_path,Sub_Name_ar,Sub_Name_en;


                    // we use put method to save the data in that map and we use the column name as the key
                    Sub_Cat_id = cursor.getString( cursor.getColumnIndex( "MS_ID" ) );
                    Sub_Name_ar = cursor.getString( cursor.getColumnIndex( "MS_Name_ar" ) );
                    Sub_Name_en = cursor.getString( cursor.getColumnIndex( "MS_Name" ) );
                    Sub_Cat_image_path = cursor.getString( cursor.getColumnIndex( "MS_Image_path" ) );
                    if (lang.equals("ar")){
                        Sub_Name_ar = cursor.getString( cursor.getColumnIndex( "MS_Name_ar" ) );
                    }else {
                        Sub_Name_ar = cursor.getString( cursor.getColumnIndex( "MS_Name" ) );
                    }
                    Sub_Category_Button sub_category_button = new Sub_Category_Button( Sub_Name_ar, Sub_Cat_image_path,Sub_Cat_id );


                    // we add the saved map to the list one by one
                    this.Sub_Categorylist.add(sub_category_button  );
                    // this.CustListSQL.add(map);

                } while (cursor.moveToNext());
                cursor.close();
                z = "Success";
            } else {
                z = "Error retrieving data from table";
            }


            return z;
        }


    }

}
