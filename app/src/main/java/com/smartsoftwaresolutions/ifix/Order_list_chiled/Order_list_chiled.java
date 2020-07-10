package com.smartsoftwaresolutions.ifix.Order_list_chiled;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartsoftwaresolutions.ifix.CustomRVItemTouchListener;

import com.smartsoftwaresolutions.ifix.Database.DataHelper;
import com.smartsoftwaresolutions.ifix.Gallery.Gallery;
import com.smartsoftwaresolutions.ifix.Main_Activity;
import com.smartsoftwaresolutions.ifix.Pager.ViewPagerAdapter;
import com.smartsoftwaresolutions.ifix.R;
import com.smartsoftwaresolutions.ifix.Read_Data.API;
import com.smartsoftwaresolutions.ifix.RecyclerViewItemClickListener;
import com.smartsoftwaresolutions.ifix.Sub_country_spinner.Sub_country_spinner;
import com.smartsoftwaresolutions.ifix.Sub_country_spinner.Sub_country_spinner_adapter;
import com.smartsoftwaresolutions.ifix.Worker_Profile;
import com.smartsoftwaresolutions.ifix.country_spinner.country_spinner;
import com.smartsoftwaresolutions.ifix.country_spinner.spinnerAdapter;
import com.smartsoftwaresolutions.ifix.map.MapsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Order_list_chiled extends AppCompatActivity {
    public static final String PREFS_NAME = "ifix_data";
    Button btn_show_near,btn_show_all,btn_s;
    List<Itemlistobject4> allItems = new ArrayList<Itemlistobject4>();
    List<Itemlistobject4> allItems1 = new ArrayList<Itemlistobject4>();
    SharedPreferences SpType;
    String country_id,sub_country_id;
    List<country_spinner> LabelList;
    List<Sub_country_spinner> SLabelList;
    Button btn_clear2;
    EditText et_seartch2;
    Spinner  sp_sub_countryg2; //sp_countryg2,
    spinnerAdapter SADA;
    Sub_country_spinner_adapter SSADA;
    String lang;
    String Selected_id;
    DataHelper AndPOS_dbh;
    SQLiteDatabase AndPOS_db;
    int Cposition,CSposition,first_CSposition;
    int sp_selected_country_id,sp_selected_sub_contry_id;
    InputMethodManager imm;
    Boolean first_read=true;
    List<Itemlistobject4> allItems2 = new ArrayList<Itemlistobject4>();
    private RecyclerView mRecyclerView4;
    private MyAdapter4 mAdapter4;
    private RecyclerView.LayoutManager mLayoutManager4;
    private int Sub_Cpotion;
TextView tv_noresult,tv_country2;
ImageView image_search2;


    int count =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_chiled);

        tv_noresult=findViewById(R.id.tv_noresult);
        tv_noresult.setVisibility(View.GONE);
        tv_country2=findViewById(R.id.tv_country2);
/** get extra from intent **/
        Selected_id= getIntent().getStringExtra("SC_ID");
        Toast.makeText(this,"selected id is     "+Selected_id,Toast.LENGTH_LONG).show();
        mRecyclerView4 =  findViewById(R.id.my_recycler_view4);
// use this setting to improve performance if you know that changes
// in content do not change the layout size of the RecyclerView
        mRecyclerView4.setHasFixedSize(true);
        // List<Itemlistobject4> rowListItem = getAllItemList();
// use a linear layout manager
        mLayoutManager4 = new LinearLayoutManager(Order_list_chiled.this);
        mRecyclerView4.setLayoutManager(mLayoutManager4);
        image_search2=findViewById(R.id.image_search2);




        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // we create an instance of data base helper  database this will create the database
        AndPOS_dbh = new DataHelper(getApplicationContext());
        // and we connect the data to the data base helper and specify its use as a writable
        AndPOS_db = AndPOS_dbh.getWritableDatabase();
        btn_show_near=findViewById(R.id.btn_show_near);
        btn_show_all=findViewById(R.id.btn_show_all);

        et_seartch2 = findViewById(R.id.et_seartch2);

        /**category**/
       // sp_countryg2 = findViewById(R.id.sp_country2);
        sp_sub_countryg2 = findViewById(R.id.sp_sub_country2);
        btn_clear2=findViewById(R.id.btn_clear2);
        btn_s=findViewById(R.id.btn_s);

        btn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    et_seartch2.setFocusable(false);
              //  hideKeyboard(Order_list_chiled.this);
               // et_seartch2.clearFocus();
                hideKeyboard();

            }
        });

        et_seartch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_seartch2.requestFocus();
                et_seartch2.setFocusableInTouchMode(true);


                imm.showSoftInput(et_seartch2, InputMethodManager.SHOW_FORCED);
            }
        });

        btn_clear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_noresult.setVisibility(View.GONE);
                mAdapter4 =new MyAdapter4(Order_list_chiled.this,allItems2);
                mAdapter4.notifyDataSetChanged();
                mRecyclerView4.setAdapter(mAdapter4);


//                LoadSpinnerCountry loadSpinnerCountry = new LoadSpinnerCountry();
//                loadSpinnerCountry.execute();

                et_seartch2.setText("");

            }
        });
        SpType = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // Reading from SharedPreferences
        lang = SpType.getString("lang", "en_US");
        Cposition=SpType.getInt("Cposition",0);
        CSposition=SpType.getInt("CSposition",0);
        first_CSposition=CSposition;

       SharedPreferences.Editor editor=SpType.edit();
       editor.putBoolean("from_favoret",false);
        editor.commit();
        if (Cposition==0){
            show_Alert();
        }else {
            LoadSpinnerCountry loadSpinnerCountry = new LoadSpinnerCountry();
            loadSpinnerCountry.execute();
        }



//        sp_countryg2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                country_spinner SelectedType;
//                SelectedType = SADA.getItem(position);
//                String s = SelectedType.getCountry_name();
//
//                sp_selected_country_id=Integer.parseInt(SelectedType.getCountry_id());
//                if (sp_selected_country_id==0){
//                    // nothing will happen all list must view
//                    mAdapter4 =new MyAdapter4(Order_list_chiled.this,allItems2);
//                    mRecyclerView4.setAdapter(mAdapter4);
//                    mAdapter4.notifyDataSetChanged();
//                    Toast.makeText(Order_list_chiled.this,"this is spinner country executed",Toast.LENGTH_LONG).show();
//                }else {
//                    country_filter().filter(SelectedType.getCountry_id());
//                    LoadSpinnerSubCountry loadSpinnerSubCountry = new LoadSpinnerSubCountry();
//                    loadSpinnerSubCountry.execute("");
//
//                    // On selecting a spinner item the cposition is the selected position
//                    Cposition = position;
//
//                    SharedPreferences.Editor editor = SpType.edit();
//                    editor.putInt("Cposition", position);
//                    editor.commit();
//
//                }
//
//                //Label_UserType = parent.getItemAtPosition(position).toString();
//
//
//// i don't need to save the country selected in the search in the shared preferance
//               // editor.putString("country_ID", SelectedType.getCountry_id());
//             //   editor.commit();
//                // Showing selected spinner item
//                //   Toast.makeText(parent.getContext(), "You selected: " + s, Toast.LENGTH_LONG).show();
////                mAdapter4.country_filter().filter(SelectedType.getCountry_id());
////               mAdapter4.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        sp_sub_countryg2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Sub_country_spinner SelectedType;
                // On selecting a spinner item
                SelectedType = (Sub_country_spinner) SSADA.getItem(position);
                //Label_UserType = parent.getItemAtPosition(position).toString();
                String s = SelectedType.getCS_Name();
              //  sp_selected_sub_contry_id=Integer.parseInt(SelectedType.getCountry_id());
             sp_selected_sub_contry_id=Integer.parseInt(SelectedType.getCS_ID());

                if (sp_selected_sub_contry_id==0){

                    // nothing will change in the list we dont do fillter
                }else {
                   // country_Sub_filter().filter(SelectedType.getCS_ID());
                  //  mAdapter4.notifyDataSetChanged();
                    Sub_Cpotion = position;
                    CSposition=position;
allItems2.clear();
                    sub_country_id=SelectedType.getCS_ID();

 if(first_read){
     Read_Data_remote_server read_data_remote_server= new Read_Data_remote_server();
     read_data_remote_server.execute();
     first_read=false;
 }
 if (first_CSposition!=CSposition){
     // we set a value for it because if the user select the same CSposition for the next time
     first_CSposition=999999;
     Read_Data_remote_server read_data_remote_server= new Read_Data_remote_server();
     read_data_remote_server.execute();
 }







                    SharedPreferences.Editor editor = SpType.edit();
                    editor.putString("Sub_country_ID", SelectedType.getCS_ID());
                    editor.putInt("Sub_postion", position);
                    editor.commit();
                }

                // Showing selected spinner item
                //Toast.makeText(parent.getContext(), "You selected: " + s, Toast.LENGTH_LONG).show();
                // fillter the sub country


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        et_seartch2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // this mean that the user empty the search

            }

            @Override
            public void afterTextChanged(Editable s) {
                String txt=et_seartch2.getText().toString();
                if (txt.isEmpty()) {

                    // we will reset the list to the original
                    mAdapter4 =new MyAdapter4(Order_list_chiled.this,allItems2);
                    mRecyclerView4.setAdapter(mAdapter4);
                } else {
                   // mAdapter4.getFilter().filter(s);
                    getFilter().filter(s);
                  //  mAdapter4.notifyDataSetChanged();
                }
            }
        });

        btn_show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn_pressed="a";
                Intent intent=new Intent(Order_list_chiled.this, MapsActivity.class);
                intent.putExtra("pressed_button",btn_pressed);
                startActivity(intent);
            }
        });

        btn_show_near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn_pressed="n";
                Intent intent=new Intent(Order_list_chiled.this, MapsActivity.class);
                intent.putExtra("pressed_button",btn_pressed);
                startActivity(intent);
            }
        });




        mRecyclerView4.addOnItemTouchListener(new CustomRVItemTouchListener(this, mRecyclerView4, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                Itemlistobject4 selecteditem=mAdapter4.getItem4(position);

Intent intent=new Intent(Order_list_chiled.this, Worker_Profile.class);

intent.putExtra("SWM_ID",selecteditem.getSWM_ID());
intent.putExtra("SWM_Address",selecteditem.getSWM_Address());
intent.putExtra("SWM_Lat",selecteditem.getSWM_Lat());
intent.putExtra("SWM_Lon",selecteditem.getSWM_Lon());
intent.putExtra("SWM_name",selecteditem.getSWM_name());
intent.putExtra("SWM_Phone",selecteditem.getSWM_Phone());
intent.putExtra("SWM_Mobile",selecteditem.getSWM_Mobile());
intent.putExtra("SWM_POBOX",selecteditem.getSWM_POBOX());
intent.putExtra("SWM_Pic",selecteditem.getSWM_Pic());
intent.putExtra("SWM_Web",selecteditem.getSWM_Web());
intent.putExtra("SWM_company",selecteditem.getSWM_company());


startActivity(intent);

                Toast.makeText(Order_list_chiled.this,"position selected"+position,Toast.LENGTH_LONG).show();


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        hideKeyboard();
     //   first_read=false;
    }

    public void show_Alert(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.select_your_country))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                List<Itemlistobject4> ListFiltered;
                // the search is empty i need all the items so i put a flag
//                if (original){
//                    All_itemList4=itemList4;
//                    // then make it false
//                    original=false;
//                }
                if (charString.isEmpty()) {
                    //   ListFiltered = itemList4;
                    ListFiltered = allItems2;

                } else {
                    List<Itemlistobject4> filteredList = new ArrayList<>();
                    for (Itemlistobject4 row : allItems2) {

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
              //  Toast.makeText(Order_list_chiled.this, R.string.i_am,Toast.LENGTH_LONG).show();
                ArrayList<Itemlistobject4> filteredList=(ArrayList<Itemlistobject4>) filterResults.values;
                if (filteredList.size()==0){
                    tv_noresult.setVisibility(View.VISIBLE);
                    Toast.makeText(Order_list_chiled.this,R.string.sorry_no_match_please_try_again,Toast.LENGTH_LONG).show();
                }
             //   MyAdapter4  mAdapter_filltered =new MyAdapter4(Order_list_chiled.this,filteredList);
              //  MyAdapter4  mAdapter_filltered =new MyAdapter4(Order_list_chiled.this,filteredList);
mAdapter4= new MyAdapter4(Order_list_chiled.this,filteredList);
                mRecyclerView4.setAdapter(mAdapter4);
              //  mRecyclerView4.setAdapter(mAdapter_filltered);
              // mAdapter4.notifyDataSetChanged();
               // mAdapter_filltered.notifyDataSetChanged();
            }
        };
    }

    public Filter country_filter()  {
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence country_Id) {

//below checks the match for the cityId and adds to the filterlist
                List<Itemlistobject4> ListFiltered;


//                if (country_Id.equals("0")) {
//                    ListFiltered= allItems2;
//                    // ListFiltered = itemList4;
//                    //  filteredList = itemList4;
//                } else {
                    ArrayList<Itemlistobject4> filteredList = new ArrayList<Itemlistobject4>();
                    for (Itemlistobject4 row : allItems2) {

                        if (row.SWM_Country_ID.equals(country_Id)) {

                            filteredList.add(row);
                        }
                    }


                    // ListFiltered = filteredList;
                    // filterResults.values = filteredList;
                    ListFiltered = filteredList;

             //   }
                FilterResults filterResults = new FilterResults();
                filterResults.values = ListFiltered;
                return filterResults;
            }
            //Publishes the matches found, i.e., the selected cityids
            @Override
            protected void publishResults (CharSequence constraint,
                                           FilterResults filteredResults){
                Toast.makeText(Order_list_chiled.this, R.string.i_am,Toast.LENGTH_LONG).show();
                ArrayList<Itemlistobject4> filteredList=(ArrayList<Itemlistobject4>) filteredResults.values;
                if (filteredList.size()==0){
                   tv_noresult.setVisibility(View.VISIBLE);
                   Toast.makeText(Order_list_chiled.this,R.string.sorry_no_match_please_try_again,Toast.LENGTH_LONG).show();
                                   }
                mAdapter4= new MyAdapter4(Order_list_chiled.this,filteredList);
                mRecyclerView4.setAdapter(mAdapter4);
//                MyAdapter4  mAdapter_filltered =new MyAdapter4(Order_list_chiled.this,filteredList);
//
//                mRecyclerView4.setAdapter(mAdapter_filltered);
//mAdapter4.notifyDataSetChanged();
//                itemList4 = (ArrayList<Itemlistobject4>) filteredResults.values;
//                notifyDataSetChanged();
            }
        } ;
    }

    public Filter country_Sub_filter()  {
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence country_Sub_Id) {
                List<Itemlistobject4> ListFiltered;

//below checks the match for the cityId and adds to the filterlist
                FilterResults filterResults = new FilterResults();
//                if (country_Sub_Id.equals("0")) {
//                    //   ListFiltered = All_itemList4;
//                    // itemlist4 is the all items
//                    /**put the fillterd country list in the listitem4**/
//
//                    ListFiltered = itemList4;
//                } else {
                    ArrayList<Itemlistobject4> filteredList = new ArrayList<Itemlistobject4>();

                    for (Itemlistobject4 row : allItems2) {

                        if (row.SWM_Sub_C.equals(country_Sub_Id)) {

                            filteredList.add(row);
                        }
                    }

                    ListFiltered = filteredList;

                    filterResults.values = ListFiltered;


               // }
                return filterResults;
            }
            //Publishes the matches found, i.e., the selected cityids
            @Override
            protected void publishResults (CharSequence constraint,
                                           FilterResults filteredResults){

                ArrayList<Itemlistobject4> filteredList=(ArrayList<Itemlistobject4>) filteredResults.values;
                if (filteredList.size()==0){
                    tv_noresult.setVisibility(View.VISIBLE);
                    Toast.makeText(Order_list_chiled.this,R.string.sorry_no_match_please_try_again,Toast.LENGTH_LONG).show();
                }
//                MyAdapter4  mAdapter_filltered =new MyAdapter4(Order_list_chiled.this,filteredList);
//                mRecyclerView4.setAdapter(mAdapter_filltered);
                mAdapter4= new MyAdapter4(Order_list_chiled.this,filteredList);
                mRecyclerView4.setAdapter(mAdapter4);
            }
        } ;
    }

    public void hideKeyboard() {

        //Find the currently focused view, so we can grab the correct window token from it.
        View view = Order_list_chiled.this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view != null) {

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    // Action Bar actions
    @Override
    public boolean onNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()) {

            case R.id.action_back:


                finish();
                return true;
//            case R.id.action_Home:
//                Intent intent=new Intent(Order_list_chiled.this, Main_Activity.class);
//                startActivity(intent);
//
//                finish();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class LoadSpinnerCountry extends AsyncTask<String, String, String> {
        String country_N;
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SADA = new spinnerAdapter(LabelList, Order_list_chiled.this);
            // put them in the list view


//            // attaching data adapter to spinner
//            sp_countryg2.setAdapter(SADA);
//            //  sp_countryg.setSelection(Cposition);
//            sp_countryg2.setSelection(0);
//            SADA.notifyDataSetChanged();
            tv_country2.setText(country_N);
            LoadSpinnerSubCountry loadSpinnerSubCountry = new LoadSpinnerSubCountry();
            loadSpinnerSubCountry.execute("");
        }

        @Override
        protected String doInBackground(String... strings) {
//            LabelList = new ArrayList<country_spinner>();
//            country_spinner first_select=new country_spinner("0","Select Country",
//                    "حدد الدولة","0");
//            if (!lang.equals("en_US")){
//              first_select=new country_spinner("0","حدد الدولة",
//                        "حدد الدولة","0");
//            }else {
//                 first_select=new country_spinner("0","Select Country",
//                         "Select Country","0");
//            }
//            LabelList.add(first_select);

            // Select Quary for a spicific category_spinner example bedroom the cat id is from put extra
            String selectQuery = "SELECT * FROM country where Country_ID="+Cposition;
            //sqlite will returen a cursor a pointer to the first data
            Cursor cursor2 = AndPOS_db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list the result is kids clasic and modern
            if (cursor2.moveToFirst()) {
                do {
                    String country_name, country_name_ar, country_code;

                    // we use put method to save the data in that map and we use the column name as the key
                    country_id = cursor2.getString(cursor2.getColumnIndex("Country_ID"));

                    country_code = cursor2.getString(cursor2.getColumnIndex("Country_code"));
// we fill the spiner with the info
                    if (!lang.equals("en_US")){
                        // this is arabic
                        country_N=cursor2.getString(cursor2.getColumnIndex("Country_name_ar"));
                        country_name_ar=cursor2.getString(cursor2.getColumnIndex("Country_name_ar"));
                    }else {
                        // this is english
                        country_N=cursor2.getString(cursor2.getColumnIndex("Country_name"));
                        country_name_ar=cursor2.getString(cursor2.getColumnIndex("Country_name"));
                    }
                    // fill spinner
                 //   country_spinner country_Info = new country_spinner(country_id, country_name, country_name_ar, country_code);


                    // we add the saved map to the list one by one
                   // LabelList.add(country_Info);

                    //  LabelList.add(cursor2.getString(cursor2.getColumnIndex("User_Type_Name")));
                } while (cursor2.moveToNext());
            }

            cursor2.close();


            //Sp_UserType.setAdapter(dataAdapter);
            return null;
        }
    }

    public class LoadSpinnerSubCountry extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);



            // Creating adapter for spinner
      /*  ArrayAdapter<spinner> dataAdapter = new ArrayAdapter<spinner>(this,
                android.R.layout.simple_spinner_item, LabelList);*/
            SSADA = new Sub_country_spinner_adapter(SLabelList, Order_list_chiled.this);
            // put them in the list view
            Sub_country_spinner SelectedType;
            // On selecting a spinner item


            // Drop down layout style - list view with radio button

            // attaching data adapter to spinner
            sp_sub_countryg2.setAdapter(SSADA);
            //    sp_sub_country1.setSelection(Sub_Cpotion);
            sp_sub_countryg2.setSelection(CSposition);

            SelectedType = (Sub_country_spinner) SSADA.getItem(CSposition);
            //Label_UserType = parent.getItemAtPosition(position).toString();
            sub_country_id = SelectedType.getCS_ID();
            SSADA.notifyDataSetChanged();

            //// two no need for read from reamote data when i load a spinner

            //Sp_UserType.setAdapter(dataAdapter);
        }

        @Override
        protected String doInBackground(String... strings) {
            SLabelList = new ArrayList<Sub_country_spinner>();
            country_spinner SelectedType;
            // On selecting a spinner item
           // SelectedType = SADA.getItem(Cposition);

            Sub_country_spinner first_select=new Sub_country_spinner("0","Select Country",
                    "حدد الدولة","0");
            if (!lang.equals("en_US")){
                first_select=new Sub_country_spinner("0","حدد منطقة",
                        "حدد منطقة","0");
            }else {
                first_select=new Sub_country_spinner("0","Select District",
                        "Select District","0");
            }
            SLabelList.add(first_select);
            //Label_UserType = parent.getItemAtPosition(position).toString();
           // String s = SelectedType.getCountry_id();
            // Select Quary for a spicific category_spinner example bedroom the cat id is from put extra
            String selectQuery = "SELECT * FROM country_sub_tbl where Country_ID=" + Cposition;
            //sqlite will returen a cursor a pointer to the first data
            Cursor cursor2 = AndPOS_db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list the result is kids clasic and modern
            if (cursor2.moveToFirst()) {
                do {

                    String Scountry_id, Scountry_name, Scountry_name_ar, country_id;

                    // we use put method to save the data in that map and we use the column name as the key
                    Scountry_id = cursor2.getString(cursor2.getColumnIndex("CS_ID"));

                    country_id = cursor2.getString(cursor2.getColumnIndex("Country_ID"));

// we fill the spiner with the info
                    if (!lang.equals("en_US")){ // arabic
                        Scountry_name=cursor2.getString(cursor2.getColumnIndex("CS_name_ar"));
                        Scountry_name_ar=cursor2.getString(cursor2.getColumnIndex("CS_name_ar"));
                    }else {
                        Scountry_name=cursor2.getString(cursor2.getColumnIndex("CS_name"));
                        Scountry_name_ar=cursor2.getString(cursor2.getColumnIndex("CS_name"));
                    }
                    Sub_country_spinner country_Info = new Sub_country_spinner(Scountry_id, Scountry_name, Scountry_name_ar, country_id);


                    // we add the saved map to the list one by one
                    SLabelList.add(country_Info);

                    //  LabelList.add(cursor2.getString(cursor2.getColumnIndex("User_Type_Name")));
                } while (cursor2.moveToNext());
            }

            cursor2.close();


            return null;
        }
    }



class Read_Data_remote_server extends AsyncTask<Void, Void, List<Itemlistobject4>> {
  //  List<Itemlistobject4> allItems2 = new ArrayList<Itemlistobject4>();


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(List<Itemlistobject4> itemlistobject4s) {
        super.onPostExecute(itemlistobject4s);
        allItems1 = itemlistobject4s;
       // mAdapter4 = new MyAdapter4(Order_list_chiled.this, allItems2);
        mAdapter4 = new MyAdapter4(Order_list_chiled.this, allItems2);
        mRecyclerView4.setAdapter(mAdapter4);
         mAdapter4.notifyDataSetChanged();
    }

    @Override
    protected List<Itemlistobject4> doInBackground(Void... voids) {
        //  getResponse();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.JSONURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
// in the retrofit we creat the base url then in the api we link it to the call which it the secound part of the link
        API api = retrofit.create(API.class);
        //we delete all the rows in the table befor we add new one
        // AndPOS_db.execSQL("DELETE FROM service_category_tbl;");
//AndPOS_db.close();

        Map<String, String> mapdata = new HashMap<>();
        mapdata.put("apicall", "SWC");
        mapdata.put("SW_ID", String.valueOf(Selected_id));
      //  mapdata.put("SWM_Country_ID",String.valueOf(Cposition));
        mapdata.put("SWM_Country_ID",String.valueOf(country_id));
       // mapdata.put("SWM_Sub_c", String.valueOf(CSposition));
        mapdata.put("SWM_Sub_c", String.valueOf(sub_country_id));

count=count+1;
        // mapdata.put("SW_ID", Selected_id);
        Call<List<Itemlistobject4>> call = api.getString_Selected_worker_C(mapdata);
        call.enqueue(new Callback<List<Itemlistobject4>>() {
            @Override
            public void onResponse(Call<List<Itemlistobject4>> call, Response<List<Itemlistobject4>> response) {
                Log.i("xxxxxxxxxxxxxxxxxxxxx"+count, response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());
                        for (int i = 0; i < response.body().size(); i++) {
                            //   allItems.add()
                            //   m.SWM_Sub_C,m.WC_ID,c.MS_ID
                            Itemlistobject4 list_data = new Itemlistobject4(response.body().get(i).getSWM_ID()
                                    , response.body().get(i).getSWM_Address()
                                    , response.body().get(i).getSWM_Lat()
                                    , response.body().get(i).getSWM_Lon()
                                    , response.body().get(i).getSWM_name()
                                    , response.body().get(i).getSWM_Phone()
                                    , response.body().get(i).getSWM_Mobile()
                                    , response.body().get(i).getSWM_POBOX()
                                    , response.body().get(i).getSWM_Web()
                                    , response.body().get(i).getSWM_Pic()
                                    , response.body().get(i).getSWM_Country_ID()
                                    , response.body().get(i).getSWM_Sub_C()
                                    , response.body().get(i).getWC_ID()
                                    , response.body().get(i).getSWM_RATE()
                                    , response.body().get(i).getSWM_dis_like()
                                    , response.body().get(i).getMS_ID()
                                    , response.body().get(i).getSWM_company()

                            );
                            allItems2.add(list_data);
                           mAdapter4.notifyDataSetChanged();


                        }


                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Itemlistobject4>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return allItems2;


    }
}

}

