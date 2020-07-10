package com.smartsoftwaresolutions.ifix.Gallery;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartsoftwaresolutions.ifix.CustomRVItemTouchListener;
import com.smartsoftwaresolutions.ifix.Database.DataHelper;
import com.smartsoftwaresolutions.ifix.Main_Activity;
import com.smartsoftwaresolutions.ifix.Main_Category_List.Category_Adapter;
import com.smartsoftwaresolutions.ifix.Main_Category_List.Category_Button;
import com.smartsoftwaresolutions.ifix.Main_Sub_Category_List.Sub_Category_Button;
import com.smartsoftwaresolutions.ifix.My_Profile;
import com.smartsoftwaresolutions.ifix.Order_Service.order_one_master;
import com.smartsoftwaresolutions.ifix.Order_list_chiled.Itemlistobject4;
import com.smartsoftwaresolutions.ifix.Order_list_chiled.MyAdapter4;
import com.smartsoftwaresolutions.ifix.Order_list_chiled.Order_list_chiled;
import com.smartsoftwaresolutions.ifix.R;
import com.smartsoftwaresolutions.ifix.Read_Data.API;
import com.smartsoftwaresolutions.ifix.RecyclerViewItemClickListener;
import com.smartsoftwaresolutions.ifix.Sub_country_spinner.Sub_country_spinner;
import com.smartsoftwaresolutions.ifix.Sub_country_spinner.Sub_country_spinner_adapter;
import com.smartsoftwaresolutions.ifix.Worker_Profile;
import com.smartsoftwaresolutions.ifix.country_spinner.country_spinner;
import com.smartsoftwaresolutions.ifix.country_spinner.spinnerAdapter;
import com.smartsoftwaresolutions.ifix.spinner_category.category_spinner;
import com.smartsoftwaresolutions.ifix.spinner_category.category_spinner_adapter;
import com.smartsoftwaresolutions.ifix.spinner_sup_category.category_sub_spinner;
import com.smartsoftwaresolutions.ifix.spinner_sup_category.category_sub_spinner_adapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.smartsoftwaresolutions.ifix.Picasso_Target.picassoImageTarget;

public class Gallery extends AppCompatActivity implements MyAdapter4.AdapterListener {
    private RecyclerView mRecyclerView4;
    private MyAdapter4 mAdapter4;
    private RecyclerView.LayoutManager mLayoutManager4;

    List<Itemlistobject4> allItems = new ArrayList<Itemlistobject4>();
    List<Itemlistobject4> allItems1 = new ArrayList<Itemlistobject4>();
    EditText et_seartch;
    ImageView image_search;
    Spinner sp_countryg, sp_sub_countryg, sp_category, sp_sub_category;
    // String Selected_id;
    spinnerAdapter SADA;
    Sub_country_spinner_adapter SSADA;
    category_spinner_adapter CSADA;
    category_sub_spinner_adapter CUSADA;

    public static final String PREFS_NAME = "ifix_data";
    SharedPreferences SpType;
    int Cposition,CSposition,S_cat=0,S_Sub_cat=0;
    private int Sub_Cpotion;
    // if the spinner is filled without selection so the id =0 we mean the postion
    int Selected_category_position = 0;
    DataHelper AndPOS_dbh;
    SQLiteDatabase AndPOS_db;
    String country_id;
    List<country_spinner> LabelList;
    List<Sub_country_spinner> SLabelList;
    List<category_spinner> CSlabelList;
    List<category_sub_spinner> CUSlabelList;
    String lang;
    category_spinner category_select,category_select_ar;
    Button btn_clear,btn_Appsearch,btn_s1;
  //  boolean clear_flag=false;
    int sp_selected_country_id,sp_selected_sub_contry_id,sp_selected_category_id,sub_cat_selecter_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // we create an instance of data base helper  database this will create the database
        AndPOS_dbh = new DataHelper(getApplicationContext());
        // and we connect the data to the data base helper and specify its use as a writable
        AndPOS_db = AndPOS_dbh.getWritableDatabase();
        et_seartch = findViewById(R.id.et_seartch);
        image_search = findViewById(R.id.image_search);
        /**category**/
        sp_countryg = findViewById(R.id.sp_country_g);
        sp_sub_countryg = findViewById(R.id.sp_sub_country_g);
        sp_category = findViewById(R.id.sp_categoryg);
        sp_sub_category = findViewById(R.id.sp_sub_category);
        btn_clear=findViewById(R.id.btn_clear);
        btn_Appsearch=findViewById(R.id.btn_Appsearch);
        btn_s1=findViewById(R.id.btn_s1);
        btn_s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
        // request for fuces
        et_seartch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_seartch.requestFocus();
                et_seartch.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_seartch, InputMethodManager.SHOW_FORCED);
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fill_Category fill_category = new Fill_Category();
                fill_category.execute("");

                LoadSpinnerCountry loadSpinnerCountry = new LoadSpinnerCountry();
                loadSpinnerCountry.execute();

                et_seartch.setText("");

//             String txt=   btn_clear.getText().toString();
//             if (txt.equals("Apply filter")){
//                 clear_flag=true;
//                 btn_clear.setText("Clear filter");
//             }else {
//                 // mean the clear filter is tru
//                 clear_flag=false;
//                 btn_clear.setText("Apply filter");
//                 Fill_Category fill_category = new Fill_Category();
//                 fill_category.execute("");
//
//                 LoadSpinnerCountry loadSpinnerCountry = new LoadSpinnerCountry();
//                 loadSpinnerCountry.execute();
//
//                 et_seartch.setText("");
//             }
            }
        });

//        // if flag equal true then there is a countent in the search and i want to clear it
//        if (!clear_flag){
//            btn_clear.setText("Apply filter");
//        }else {
//         btn_clear.setText("Clear filter");
//        }


        SpType = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // Reading from SharedPreferences
        lang = SpType.getString("lang", "en_US");

        Cposition=SpType.getInt("Cposition",0);
        CSposition=SpType.getInt("CSposition",0);


        Fill_Category fill_category = new Fill_Category();
        fill_category.execute("");

        LoadSpinnerCountry loadSpinnerCountry = new LoadSpinnerCountry();
        loadSpinnerCountry.execute();
        /**--read from the sharedpreferance of the country --**/
//        if (SpType.getInt("Cposition", 999999) == 999999) {
//            Cposition = 0;
//        } else {
//            Cposition = SpType.getInt("Cposition", 999999);
//
//        }
//        if (SpType.getInt("Sub_position", 999999) == 999999) {
//            Sub_Cpotion = 0;
//        } else {
//            Sub_Cpotion = SpType.getInt("Sub_position", 999999);
//
//        }

        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category_spinner selected_category;
                Selected_category_position = position;
                selected_category=CSADA.getItem(position);
                S_cat=position;
                sp_selected_category_id=Integer.parseInt(selected_category.getCat_DI()) ;
                Fill_Sub_Category fill_sub_category = new Fill_Sub_Category();
                fill_sub_category.execute("");
                mAdapter4.Cat_filter().filter(selected_category.getCat_DI());
                mAdapter4.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_sub_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category_sub_spinner category_sub_info;
                category_sub_info=CUSADA.getItem(position);
                S_Sub_cat=position;
                 sub_cat_selecter_id=Integer.parseInt(category_sub_info.getCat_sub_ID())  ;
//                mAdapter4.Cat_Sub_filter().filter(sub_cat_selecter_id);
//                mAdapter4.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_countryg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadSpinnerSubCountry loadSpinnerSubCountry = new LoadSpinnerSubCountry();
                loadSpinnerSubCountry.execute("");
                country_spinner SelectedType;
                // On selecting a spinner item
                Cposition = position;
                SelectedType = SADA.getItem(position);
//                SharedPreferences.Editor editor = SpType.edit();
//                editor.putInt("Cposition", position);
//                editor.commit();
                //Label_UserType = parent.getItemAtPosition(position).toString();
                String s = SelectedType.getCountry_name();
                sp_selected_country_id=Integer.parseInt(SelectedType.getCountry_id());

//                editor.putString("country_ID", SelectedType.getCountry_id());
//                editor.commit();
                // Showing selected spinner item
             //   Toast.makeText(parent.getContext(), "You selected: " + s, Toast.LENGTH_LONG).show();
                mAdapter4.country_filter().filter(SelectedType.getCountry_id());
                mAdapter4.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp_sub_countryg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Sub_country_spinner SelectedType;
                // On selecting a spinner item
                SelectedType = (Sub_country_spinner) SSADA.getItem(position);
                //Label_UserType = parent.getItemAtPosition(position).toString();
                String s = SelectedType.getCS_Name();
                sp_selected_sub_contry_id=Integer.parseInt(SelectedType.getCS_ID());
                Sub_Cpotion = position;
//                SharedPreferences.Editor editor = SpType.edit();
//                editor.putString("Sub_country_ID", SelectedType.getCS_ID());
//                editor.putInt("Sub_postion", position);
//                editor.commit();
                // Showing selected spinner item
                //Toast.makeText(parent.getContext(), "You selected: " + s, Toast.LENGTH_LONG).show();
                // fillter the country
                mAdapter4.country_Sub_filter().filter(SelectedType.getCS_ID());
                mAdapter4.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        if (Cposition!=0){
//            sp_countryg.setSelection(Cposition);
//            country_spinner SelectedType;
//            SelectedType = SADA.getItem(Cposition);
//            sp_selected_country_id=Integer.parseInt(SelectedType.getCountry_id());
//        }else {
//            sp_countryg.setSelection(0);
//        }

//        if (Cposition==0){
//            // dont set a first selection
//        }else {
//            if (CSposition!=0){
//                Sub_country_spinner SelectedType;
//
//                sp_sub_countryg.setSelection(CSposition);
//                SelectedType = (Sub_country_spinner) SSADA.getItem(CSposition);
//                //Label_UserType = parent.getItemAtPosition(position).toString();
//                String s = SelectedType.getCS_Name();
//                sp_selected_sub_contry_id=Integer.parseInt(SelectedType.getCountry_id());
//            }
//        }
        btn_Appsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Cposition==0){
                    show_Alert();

                }
//                else if (CSposition==0){
//                    show_AlertC();
//                }
                else if (S_cat==0){
                    show_AlertC();
                } else {
                    Read_Data_remote_server read_data_remote_server = new Read_Data_remote_server();
                    read_data_remote_server.execute();
                }
//                else if (S_Sub_cat==0){
//                    show_AlertC();
//                }


            }
        });

        mRecyclerView4 = findViewById(R.id.my_recycler_view5);
// use this setting to improve performance if you know that changes
// in content do not change the layout size of the RecyclerView
        mRecyclerView4.setHasFixedSize(true);
        // List<Itemlistobject4> rowListItem = getAllItemList();
// use a linear layout manager
        mLayoutManager4 = new LinearLayoutManager(Gallery.this);
        mRecyclerView4.setLayoutManager(mLayoutManager4);
// specify an adapter (see also next example)

        // mAdapter.setClickListener(this);

        mAdapter4 = new MyAdapter4(Gallery.this, allItems1);
        // mAdapter4.notifyDataSetChanged();
        mRecyclerView4.setAdapter(mAdapter4);
        mAdapter4.notifyDataSetChanged();

        et_seartch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // this mean that the user empty the search

            }

            @Override
            public void afterTextChanged(Editable s) {
String txt=et_seartch.getText().toString();
                if (txt.isEmpty()) {
                    Read_Data_remote_server read_data_remote_server = new Read_Data_remote_server();
                  read_data_remote_server.execute();
                } else {
                    mAdapter4.getFilter().filter(s);
                    mAdapter4.notifyDataSetChanged();
                }
            }
        });

        mRecyclerView4.addOnItemTouchListener(new CustomRVItemTouchListener(this, mRecyclerView4, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                Itemlistobject4 selecteditem = mAdapter4.getItem4(position);
//                Intent intent = new Intent(Gallery.this, Worker_Profile.class);
//
//                intent.putExtra("SWM_ID", selecteditem.getSWM_ID());
//                intent.putExtra("SWM_Address", selecteditem.getSWM_Address());
//                intent.putExtra("SWM_Lat", selecteditem.getSWM_Lat());
//                intent.putExtra("SWM_Lon", selecteditem.getSWM_Lon());
//                intent.putExtra("SWM_name", selecteditem.getSWM_name());
//                intent.putExtra("SWM_Phone", selecteditem.getSWM_Phone());
//                intent.putExtra("SWM_Mobile", selecteditem.getSWM_Mobile());
//                intent.putExtra("SWM_Pic", selecteditem.getSWM_Pic());
//
//                startActivity(intent);
                Intent intent=new Intent(Gallery.this, Worker_Profile.class);

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
                Toast.makeText(Gallery.this, "position selected" + position, Toast.LENGTH_LONG).show();


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



//        image_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String s=et_seartch.getText().toString();
//                mAdapter4.getFilter().filter(s);
//             mAdapter4.notifyDataSetChanged();
//             //   mRecyclerView4.setAdapter(mAdapter4);
//            }
//        });
    }

    public void show_AlertC(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.service_category))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
    public class LoadSpinnerCountry extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SADA = new spinnerAdapter(LabelList, Gallery.this);
            // put them in the list view


            // attaching data adapter to spinner
            sp_countryg.setAdapter(SADA);
          //  sp_countryg.setSelection(Cposition);
            sp_countryg.setSelection(Cposition);
            country_spinner SelectedType;
        SelectedType = SADA.getItem(Cposition);
            sp_selected_country_id=Integer.parseInt(SelectedType.getCountry_id());
            SADA.notifyDataSetChanged();
            LoadSpinnerSubCountry loadSpinnerSubCountry = new LoadSpinnerSubCountry();
            loadSpinnerSubCountry.execute("");
        }

        @Override
        protected String doInBackground(String... strings) {
            LabelList = new ArrayList<country_spinner>();
            country_spinner first_select=new country_spinner("0","Select Country","حدد الدولة","0");
            LabelList.add(first_select);

            // Select Quary for a spicific category_spinner example bedroom the cat id is from put extra
            String selectQuery = "SELECT * FROM country ";
            //sqlite will returen a cursor a pointer to the first data
            Cursor cursor2 = AndPOS_db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list the result is kids clasic and modern
            if (cursor2.moveToFirst()) {
                do {
                    String country_name, country_name_ar, country_code;

                    // we use put method to save the data in that map and we use the column name as the key
                    country_id = cursor2.getString(cursor2.getColumnIndex("Country_ID"));
                    country_name = cursor2.getString(cursor2.getColumnIndex("Country_name"));
                    country_name_ar = cursor2.getString(cursor2.getColumnIndex("Country_name_ar"));
                    country_code = cursor2.getString(cursor2.getColumnIndex("Country_code"));
// we fill the spiner with the info

                    country_spinner country_Info = new country_spinner(country_id, country_name, country_name_ar, country_code);


                    // we add the saved map to the list one by one
                    LabelList.add(country_Info);

                    //  LabelList.add(cursor2.getString(cursor2.getColumnIndex("User_Type_Name")));
                } while (cursor2.moveToNext());
            }

            cursor2.close();


            //Sp_UserType.setAdapter(dataAdapter);
            return null;
        }
    }

    public class Fill_Category extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String r) {


            CSADA = new category_spinner_adapter(CSlabelList, Gallery.this);
            // put them in the list view


            // attaching data adapter to spinner
            sp_category.setAdapter(CSADA);
            sp_category.setSelection(0);
            CSADA.notifyDataSetChanged();
            // after the spinner of the category is filled the sub category is filled
            Fill_Sub_Category fill_sub_category = new Fill_Sub_Category();
            fill_sub_category.execute("");


        }

        @Override
        protected String doInBackground(String... params) {
            CSlabelList = new ArrayList<category_spinner>();
             category_select=new category_spinner("0","Select Service");
             category_select_ar=new category_spinner("0","حدد الخدمة");
            if (lang.equals("ar")){
                CSlabelList.add(category_select_ar);
            }else {
                CSlabelList.add(category_select);
            }

            // we specify the database helper and the database and set the database to readable
            // then we write a select statment
            //   String query_SQL = "select * from Family_Tbl ";
            String query_SQL = "select * from service_category_tbl  ";
            // we run this select statment by creating a cursor and execute it
            Cursor cursor = AndPOS_db.rawQuery(query_SQL, null);
            // if the result of the execute statment have data then the if statment is true and we move the cursor to the first row
            if (cursor.moveToFirst()) {
                do {
                    // we create a hash map with string for the key and string for the data
                    HashMap<String, String> map = new HashMap<String, String>();
                    String Cat_id, Cat_des;

                    // we use put method to save the data in that map and we use the column name as the key
                    Cat_id = cursor.getString(cursor.getColumnIndex("SC_id"));

                    if (lang.equals("ar")) {
                        Cat_des = cursor.getString(cursor.getColumnIndex("SC_Description_ar"));
                    } else {
                        Cat_des = cursor.getString(cursor.getColumnIndex("SC_Descption"));
                    }


                    category_spinner category_info = new category_spinner(Cat_id, Cat_des);


                    // we add the saved map to the list one by one
                    CSlabelList.add(category_info);
                    // this.CustListSQL.add(map);

                } while (cursor.moveToNext());
                cursor.close();

            }


            return null;
        }


    }

    public class Fill_Sub_Category extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String r) {


            CUSADA = new category_sub_spinner_adapter(CUSlabelList, Gallery.this);
            // put them in the list view


            // attaching data adapter to spinner
            sp_sub_category.setAdapter(CUSADA);

            if (CUSADA.getCount()==0){

            }else {
                category_sub_spinner category_sub_info;
                category_sub_info=CUSADA.getItem(0);
                sub_cat_selecter_id=Integer.parseInt(category_sub_info.getCat_sub_ID()) ;
            }

//            if (CUSlabelList.size() == 0) {
//                category_sub_spinner select_first=new category_sub_spinner("0","Select Type service ");
//                CUSlabelList.add(select_first);
//            }
              //  sp_sub_category.setSelection(0);


            CUSADA.notifyDataSetChanged();


        }

        @Override
        protected String doInBackground(String... params) {
            CUSlabelList = new ArrayList<category_sub_spinner>();
            // we specify the database helper and the database and set the database to readable
            // then we write a select statment
            String query_SQL="";
            if (Selected_category_position == 0) {
                // nothing to do i want the user to select a service first
               // query_SQL = "select * from Master_Service_tbl where SC_ID=7";
            } else {
                String Selected_category = CSlabelList.get(Selected_category_position).getCat_DI();
                int cat_id = Integer.parseInt(Selected_category);
                query_SQL = "select * from Master_Service_tbl where SC_ID=" + cat_id;

// we run this select statment by creating a cursor and execute it
                Cursor cursor = AndPOS_db.rawQuery(query_SQL, null);
                // if the result of the execute statment have data then the if statment is true and we move the cursor to the first row

                if (cursor.moveToFirst()) {
                    do {
                        // we create a hash map with string for the key and string for the data
                        HashMap<String, String> map = new HashMap<String, String>();
                        String Sub_Cat_id, Sub_Name_ar;
//                    "    MS_Image_path VARCHAR (500), \n" +
//                            "    MS_Image_name VARCHAR (500) \n" +

                        // we use put method to save the data in that map and we use the column name as the key
                        Sub_Cat_id = cursor.getString(cursor.getColumnIndex("MS_ID"));
                        if (lang.equals("ar")) {
                            Sub_Name_ar = cursor.getString(cursor.getColumnIndex("MS_Name_ar"));
                        } else {
                            Sub_Name_ar = cursor.getString(cursor.getColumnIndex("MS_Name"));
                        }

                        category_sub_spinner category_info = new category_sub_spinner(Sub_Cat_id, Sub_Name_ar);


                        // we add the saved map to the list one by one
                        CUSlabelList.add(category_info);
                        // this.CustListSQL.add(map);
                    } while (cursor.moveToNext());
                    cursor.close();
                }
            }




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
            SSADA = new Sub_country_spinner_adapter(SLabelList, Gallery.this);
            // put them in the list view


            // Drop down layout style - list view with radio button

            // attaching data adapter to spinner
            sp_sub_countryg.setAdapter(SSADA);
          ///  sp_sub_countryg.setSelection(CSposition);
            if (Cposition==0){
                // dont set a first selection
            }else {
                if (CSposition!=0){
                    Sub_country_spinner SelectedType;

                    sp_sub_countryg.setSelection(CSposition);
                    SelectedType = (Sub_country_spinner) SSADA.getItem(CSposition);
                    //Label_UserType = parent.getItemAtPosition(position).toString();
                   //String t = SelectedType.getCS_Name();
                    sp_selected_sub_contry_id=Integer.parseInt(SelectedType.getCS_ID());
                }
            }
            //    sp_sub_country1.setSelection(Sub_Cpotion);
            SSADA.notifyDataSetChanged();

            //Sp_UserType.setAdapter(dataAdapter);
        }

        @Override
        protected String doInBackground(String... strings) {
            SLabelList = new ArrayList<Sub_country_spinner>();
            country_spinner SelectedType;
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
            // On selecting a spinner item
            SelectedType = SADA.getItem(Cposition);
            //Label_UserType = parent.getItemAtPosition(position).toString();
            String s = SelectedType.getCountry_id();
            // Select Quary for a spicific category_spinner example bedroom the cat id is from put extra
            String selectQuery = "SELECT * FROM country_sub_tbl where Country_ID=" + s;
            //sqlite will returen a cursor a pointer to the first data
            Cursor cursor2 = AndPOS_db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list the result is kids clasic and modern
            if (cursor2.moveToFirst()) {
                do {

                    String Scountry_id, Scountry_name, Scountry_name_ar, country_id;

                    // we use put method to save the data in that map and we use the column name as the key
                    Scountry_id = cursor2.getString(cursor2.getColumnIndex("CS_ID"));
                    Scountry_name = cursor2.getString(cursor2.getColumnIndex("CS_name"));
                    Scountry_name_ar = cursor2.getString(cursor2.getColumnIndex("CS_name_ar"));
                    country_id = cursor2.getString(cursor2.getColumnIndex("Country_ID"));

// we fill the spiner with the info

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

    @Override
    public void onContactSelected(Itemlistobject4 contact) {

    }

    public void hideKeyboard() {

        //Find the currently focused view, so we can grab the correct window token from it.
        View view = Gallery.this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view != null) {

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
    class Read_Data_remote_server extends AsyncTask<Void, Void, List<Itemlistobject4>> {
        List<Itemlistobject4> allItems2 = new ArrayList<Itemlistobject4>();


        @Override
        protected void onPostExecute(List<Itemlistobject4> itemlistobject4s) {
            super.onPostExecute(itemlistobject4s);
            allItems1 = itemlistobject4s;
            mAdapter4 = new MyAdapter4(Gallery.this, allItems2);
            mRecyclerView4.setAdapter(mAdapter4);
            //  mAdapter4.notifyDataSetChanged();
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
            mapdata.put("SW_ID", String.valueOf(sub_cat_selecter_id));
            mapdata.put("SWM_Country_ID",String.valueOf(sp_selected_country_id));
            mapdata.put("SWM_Sub_c", String.valueOf(sp_selected_sub_contry_id));

            // mapdata.put("SW_ID", Selected_id);
            Call<List<Itemlistobject4>> call = api.getString_Selected_worker_C(mapdata);
            call.enqueue(new Callback<List<Itemlistobject4>>() {
                @Override
                public void onResponse(Call<List<Itemlistobject4>> call, Response<List<Itemlistobject4>> response) {
                    Log.i("Read_Datagggggg", response.body().toString());
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

    // Action Bar actions
    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
//            case R.id.menu_share:
//
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//
//                shared_Menu sharedMenu=new shared_Menu();
//                String flaver =BuildConfig.FLAVOR;
//                String shareBodyText =sharedMenu.flavor(flaver);
//
////                if(BuildConfig.FLAVOR.equals("Free")) {
////                    shareBodyText = getString(R.string.check_it) + getString(R.string.sharepath);
////                }else if (BuildConfig.FLAVOR.equals("NM")){
////                     // nouhad moukahal
////                //    shareBodyText = getString(R.string.check_it) + getString(R.string.sharepath_NM);
////                }
//
//
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
//                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
//                return true;
//            case R.id.action_Arabic:
//                setLocale("ar");
//
//                Toast.makeText(this, R.string.la, Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.action_English:
//                setLocale("en_US");

//                Toast.makeText(this, R.string.le, Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.action_Help:
//                Intent intent2=new Intent(MainActivity.this, Help_Activity.class);
//                startActivity(intent2);
//                return true;
//            case R.id.action_reminder:
//                Intent intent3=new Intent(MainActivity.this, Notefication_Activity.class);
//                startActivity(intent3);
//                return true;
//            case R.id.menu_setting:
//                Intent intent=new Intent(MainActivity.this, Setting.class);
//                startActivity(intent);
//
//                return true;
            case R.id.action_back:


                finish();
                return true;
            case R.id.action_Home:
//                Intent intent = new Intent(Gallery.this, Main_Activity.class);
//                startActivity(intent);

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
