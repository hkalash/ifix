package com.smartsoftwaresolutions.ifix.Select_Sub_Category_Service_Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smartsoftwaresolutions.ifix.Database.DataHelper;
import com.smartsoftwaresolutions.ifix.Images.Images_Profile;
import com.smartsoftwaresolutions.ifix.Main_Activity;
import com.smartsoftwaresolutions.ifix.My_Profile;
import com.smartsoftwaresolutions.ifix.Order_Service.order_one_master;
import com.smartsoftwaresolutions.ifix.Pager.ViewPagerAdapter;
import com.smartsoftwaresolutions.ifix.R;
import com.smartsoftwaresolutions.ifix.Read_Data.API;
import com.smartsoftwaresolutions.ifix.Register_a_service_form_two;
import com.smartsoftwaresolutions.ifix.Select_Main_category_Service_Menu.Select_Main_Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Select_Sub_Menu extends AppCompatActivity {
    private RecyclerView mRecyclerView2;
    private MyAdapter3 ADA_Sub;
    private MyAdapter2 ADA_Sub2;
    private RecyclerView.LayoutManager mLayoutManager2;
    private List<Itemlistobject2> currentSelectedItems = new ArrayList<>();
    private List<Itemlistobject2> currentSelectedItems2 = new ArrayList<>();
    DataHelper AndPOS_dbh;
    SQLiteDatabase AndPOS_db;
    String selected_Category;
    ViewPager img_ad;
    Button btn_done, btn_add_service;
    EditText et_description;
    SharedPreferences SpType;
    public static final String PREFS_NAME = "ifix_data";
    private String[] imageurl = new String[]{
            API.Ad_Image + "ad1.jpg", API.Ad_Image + "ad2.jpg",
            API.Ad_Image + "ad3.jpg", API.Ad_Image + "ad4.jpg", API.Ad_Image + "ad5.jpg"
    };
    String description;
    boolean from_my_profile = false;
    boolean main_flage = false;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_service_two);


        SpType = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        et_description = findViewById(R.id.et_description);
        description = SpType.getString("service_description", "");
        lang=SpType.getString("lang", "en_US");

        et_description.setText(description);
        // raed from the intent the value
        selected_Category = getIntent().getStringExtra("Selected_ID");
        Toast.makeText(this, "selected id is     " + selected_Category, Toast.LENGTH_LONG).show();

        // we create an instance of data base helper  database this will create the database
        AndPOS_dbh = new DataHelper(getApplicationContext());
        // and we connect the data to the data base helper and specify its use as a writable
        AndPOS_db = AndPOS_dbh.getWritableDatabase();
        mRecyclerView2 = findViewById(R.id.my_recycler_view2);
        btn_done = findViewById(R.id.btn_done1);
        btn_add_service = findViewById(R.id.btn_add_service);
        btn_add_service.setVisibility(View.GONE);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selected_Service selected_service = new Selected_Service();
                selected_service.execute("");
                SharedPreferences.Editor editor = SpType.edit();
                // make description obligatory

                description = et_description.getText().toString();
                if (description.isEmpty()) {
                    et_description.setError(getString(R.string.address_required));
                    et_description.requestFocus();
                    return;
                } else {
                    editor.putString("service_description", et_description.getText().toString());
                    editor.commit();

                }
                if (from_my_profile) {
                    // the flag is true
                    Intent i = new Intent(Select_Sub_Menu.this, My_Profile.class);
                    startActivity(i);

                } else {
                    main_flage = SpType.getBoolean("main_flage", false);
                    if (main_flage) {
                        Intent i = new Intent(Select_Sub_Menu.this, My_Profile.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(Select_Sub_Menu.this, Images_Profile.class);
                        startActivity(i);
                    }

                }


                finish();

            }
        });
// use this setting to improve performance if you know that changes
// in content do not change the layout size of the RecyclerView
        mRecyclerView2.setHasFixedSize(true);
        //   List<Itemlistobject2> rowListItem = getAllItemList();
// use a linear layout manager
        mLayoutManager2 = new LinearLayoutManager(Select_Sub_Menu.this);
        mRecyclerView2.setLayoutManager(mLayoutManager2);
// specify an adapter (see also next example)
//        mAdapter2 =new MyAdapter2(Select_Sub_Menu.this,rowListItem);
//        mRecyclerView2.setAdapter(mAdapter2);
        // mAdapter.setClickListener(this);



        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        img_ad = findViewById(R.id.vp_ad4);
        //   indicator=(TabLayout)findViewById(R.id.indicator);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, imageurl);
        img_ad.setAdapter(viewPagerAdapter);

        /** from my_ptofile**/
        //from_my_profile=getIntent().getBooleanExtra("from_my_profile",false);
        // Read from the sharedprefirance
        from_my_profile = SpType.getBoolean("flag_register", false);
        if (from_my_profile) {
            // read reom the selected service table
//btn_done.setVisibility(View.GONE);

            btn_add_service.setVisibility(View.VISIBLE);
            btn_done.setText(R.string.done);
            Fill_selected_services fill_selected_services = new Fill_selected_services();
            fill_selected_services.execute("");
        } else {
            btn_add_service.setVisibility(View.GONE);
            Fill_Sub_Category2 fill_sub_category2 = new Fill_Sub_Category2();
            fill_sub_category2.execute("");
        }
        btn_add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // we need to set it to false to vive all the services
                SharedPreferences.Editor editor = SpType.edit();
                editor.putBoolean("flag_register", false);
                editor.commit();
                Intent intent = new Intent(Select_Sub_Menu.this, Select_Main_Menu.class);
                startActivity(intent);
                finish();
            }
        });


    }
/** to fill selected service that the user select them when registered read from sql */
    public class Fill_selected_services extends AsyncTask<String, String, String> {

        String z = "";

        //  MyAdapter2 ADA_Sub;
        ArrayList<Itemlistobject2> Sub_Categorylist2 = new ArrayList<Itemlistobject2>();

        // RecyclerView gridView;

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String r) {


            ADA_Sub2 = new MyAdapter2(Select_Sub_Menu.this, Sub_Categorylist2, new MyAdapter2.OnItemCheckListener() {
                @Override
                public void onItemCheck(Itemlistobject2 item) {
                    currentSelectedItems.add(item);
                }

                @Override
                public void onItemUncheck(Itemlistobject2 item) {
                    currentSelectedItems.remove(item);
                }
            });

            // put them in the list view

            mRecyclerView2.setAdapter(ADA_Sub2);


        }

        @Override
        protected String doInBackground(String... params) {

            // we specify the database helper and the database and set the database to readable
            // then we write a select statment
            //   String query_SQL = "select * from Family_Tbl ";
            //   String query_SQL = "select * from Master_Service_tbl where SC_ID="+selected_Category;
            String query_SQL1 = "select * from Service_Selected_Tbl ";
            Cursor cursor1 = AndPOS_db.rawQuery(query_SQL1, null);
            if (cursor1.moveToFirst()) {
                do {
                    String MS_ID;
                    MS_ID = cursor1.getString(cursor1.getColumnIndex("MS_ID"));
                    String query_SQL = "select * from Master_Service_tbl where MS_ID=" + MS_ID;
                    // we run this select statment by creating a cursor and execute it
                    Cursor cursor = AndPOS_db.rawQuery(query_SQL, null);
                    Itemlistobject2 sub_category_button;
                    // if the result of the execute statment have data then the if statment is true and we move the cursor to the first row
                    if (cursor.moveToFirst()) {
                        do {
                            // we create a hash map with string for the key and string for the data
                            HashMap<String, String> map = new HashMap<String, String>();
                            String Sub_Cat_id, Sub_Cat_image_path, Sub_Name_ar, Sub_Name_en, Sub_Cat_image_name
                                    ,Sub_Cat_des,Sub_Cat_des_ar;


                            // we use put method to save the data in that map and we use the column name as the key
                            Sub_Cat_id = cursor.getString(cursor.getColumnIndex("MS_ID"));
                            Sub_Name_ar = cursor.getString(cursor.getColumnIndex("MS_Name_ar"));
                            Sub_Name_en = cursor.getString(cursor.getColumnIndex("MS_Name"));
                            Sub_Cat_des = cursor.getString(cursor.getColumnIndex("MS_Description"));
                            Sub_Cat_des_ar= cursor.getString(cursor.getColumnIndex("MS_Description_ar"));
                            Sub_Cat_image_path = cursor.getString(cursor.getColumnIndex("MS_Image_path"));
                            Sub_Cat_image_name = cursor.getString(cursor.getColumnIndex("MS_Image_name"));

                            if (lang.equals("en_US")){
                                 sub_category_button = new Itemlistobject2(Sub_Cat_id, Sub_Name_en, Sub_Cat_des
                                        , Sub_Name_ar, Sub_Cat_image_name,Sub_Cat_image_path);
                            }else {
                                sub_category_button = new Itemlistobject2(Sub_Cat_id, Sub_Name_ar, Sub_Cat_des_ar
                                        , Sub_Name_ar, Sub_Cat_image_name,Sub_Cat_image_path);
                            }

//                            Itemlistobject2 sub_category_button = new Itemlistobject2(Sub_Cat_id, Sub_Name_en, Sub_Name_en
//                                    , Sub_Name_ar, Sub_Cat_image_name);


                            // we add the saved map to the list one by one
                            this.Sub_Categorylist2.add(sub_category_button);
                            // this.CustListSQL.add(map);

                        } while (cursor.moveToNext());
                        cursor.close();
                    }

                } while (cursor1.moveToNext());
                cursor1.close();
                z = "Success";
            } else {
                z = "Error retrieving data from table";
            }


            return z;
        }


    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            Select_Sub_Menu.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (img_ad.getCurrentItem() < imageurl.length - 1) {
                        img_ad.setCurrentItem(img_ad.getCurrentItem() + 1);
                    } else {
                        img_ad.setCurrentItem(0);
                    }
                }
            });
        }
    }

    /**fill give list of services  */

    public class Fill_Sub_Category2 extends AsyncTask<String, String, String> {

        String z = "";

        //  MyAdapter2 ADA_Sub;
        ArrayList<Itemlistobject2> Sub_Categorylist = new ArrayList<Itemlistobject2>();

        // RecyclerView gridView;

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String r) {


            ADA_Sub = new MyAdapter3(Select_Sub_Menu.this, Sub_Categorylist, new MyAdapter3.OnItemCheckListener() {
                @Override
                public void onItemCheck(Itemlistobject2 item) {
                    currentSelectedItems2.add(item);
                }

                @Override
                public void onItemUncheck(Itemlistobject2 item) {
                    currentSelectedItems2.remove(item);
                }
            });

            // put them in the list view

            mRecyclerView2.setAdapter(ADA_Sub);


        }

        @Override
        protected String doInBackground(String... params) {

            // we specify the database helper and the database and set the database to readable
            // then we write a select statment
            //   String query_SQL = "select * from Family_Tbl ";
            String query_SQL = "select * from Master_Service_tbl where SC_ID=" + selected_Category;
            // we run this select statment by creating a cursor and execute it
            Cursor cursor = AndPOS_db.rawQuery(query_SQL, null);
            Itemlistobject2 sub_category_button;
            // if the result of the execute statment have data then the if statment is true and we move the cursor to the first row
            if (cursor.moveToFirst()) {
                do {
                    // we create a hash map with string for the key and string for the data
                    HashMap<String, String> map = new HashMap<String, String>();
                    String Sub_Cat_id, Sub_Cat_image_path, Sub_Name_ar, Sub_Name_en, Sub_Cat_image_name
                            ,Sub_Cat_des,Sub_Cat_des_ar;


                    // we use put method to save the data in that map and we use the column name as the key
                    Sub_Cat_id = cursor.getString(cursor.getColumnIndex("MS_ID"));
                    Sub_Name_ar = cursor.getString(cursor.getColumnIndex("MS_Name_ar"));
                    Sub_Name_en = cursor.getString(cursor.getColumnIndex("MS_Name"));
                    Sub_Cat_des = cursor.getString(cursor.getColumnIndex("MS_Description"));
                    Sub_Cat_des_ar= cursor.getString(cursor.getColumnIndex("MS_Description_ar"));
                    Sub_Cat_image_path = cursor.getString(cursor.getColumnIndex("MS_Image_path"));
                    Sub_Cat_image_name = cursor.getString(cursor.getColumnIndex("MS_Image_name"));

                    if (lang.equals("en_US")){
                        sub_category_button = new Itemlistobject2(Sub_Cat_id, Sub_Name_en, Sub_Cat_des
                                , Sub_Name_ar, Sub_Cat_image_name,Sub_Cat_image_path);
                    }else {
                        sub_category_button = new Itemlistobject2(Sub_Cat_id, Sub_Name_ar, Sub_Cat_des_ar
                                , Sub_Name_ar, Sub_Cat_image_name,Sub_Cat_image_path);
                    }
//                    Itemlistobject2 sub_category_button = new Itemlistobject2(Sub_Cat_id, Sub_Name_en, Sub_Name_en
//                            , Sub_Name_ar, Sub_Cat_image_name);


                    // we add the saved map to the list one by one
                    this.Sub_Categorylist.add(sub_category_button);
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
                Selected_Service selected_service = new Selected_Service();
                selected_service.execute("");

                finish();
                return true;
            case R.id.action_Home:
                Intent intent = new Intent(Select_Sub_Menu.this, Main_Activity.class);
                startActivity(intent);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onRestart() {
        //value_start=SpType.getString("start","");
        super.onRestart();
    }

    public class Selected_Service extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
// delete all the data in the table and add new one
            /** to be chcked **/
            if (currentSelectedItems2.size() == 0) {
                // if the user dident select anything
            } else { /** the user select a service and need to be added to the sevice */
                AndPOS_db.execSQL("DELETE FROM Service_Selected_Tbl;");
            }

            for (int i = 0; i < currentSelectedItems2.size(); i++) {
                String selected_item = currentSelectedItems2.get(i).getID2();

                ContentValues contentValues = new ContentValues();
                contentValues.put("MS_ID", selected_item);
                try {
                    long rowInserted = AndPOS_db.insertOrThrow("Service_Selected_Tbl", null, contentValues);
                    if (rowInserted != -1) {
                        //Toast.makeText(AddCustomer.this, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                        //  z = "Added Successfully";
                    } else {
                        //  Toast.makeText(AddCustomer.this, "Something wrong", Toast.LENGTH_SHORT).show();
                        //  z = "Something wrong";
                    }
                } catch (SQLiteConstraintException e1) {
                    //Toast.makeText(AddCustomer.this,  e1.toString(),Toast.LENGTH_LONG).show();
                    String z = e1.toString();
                    // z = "The Customer name  must be unique Please check it";
                }
            }
            return null;
        }
    }
}

