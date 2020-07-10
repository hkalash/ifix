package com.smartsoftwaresolutions.ifix.Select_Main_category_Service_Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.smartsoftwaresolutions.ifix.CustomRVItemTouchListener;
import com.smartsoftwaresolutions.ifix.Database.DataHelper;
import com.smartsoftwaresolutions.ifix.Main_Activity;
import com.smartsoftwaresolutions.ifix.Order_Service.order_one_master;
import com.smartsoftwaresolutions.ifix.Pager.ViewPagerAdapter;
import com.smartsoftwaresolutions.ifix.R;
import com.smartsoftwaresolutions.ifix.Read_Data.API;
import com.smartsoftwaresolutions.ifix.RecyclerViewItemClickListener;
import com.smartsoftwaresolutions.ifix.Register_a_service_form_two;
import com.smartsoftwaresolutions.ifix.Select_Sub_Category_Service_Menu.Select_Sub_Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Select_Main_Menu extends AppCompatActivity implements MyAdapter.ItemClickListener {
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DataHelper AndPOS_dbh;
    SQLiteDatabase AndPOS_db;
    SharedPreferences SpType;
    String lang;

    ViewPager img_ad;
    private String[] imageurl = new String[]{
            API.Ad_Image + "ad1.jpg", API.Ad_Image + "ad2.jpg",
            API.Ad_Image + "ad3.jpg", API.Ad_Image + "ad4.jpg", API.Ad_Image + "ad5.jpg"
    };
    public static final String PREFS_NAME = "ifix_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_select_service_list);
//sqlite data inspection intilisation
        SpType = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        // Reading from SharedPreferences
        lang = SpType.getString("lang", "en_US");
        // we create an instance of data base helper  database this will create the database
        AndPOS_dbh = new DataHelper(getApplicationContext());
        // and we connect the data to the data base helper and specify its use as a writable
        AndPOS_db = AndPOS_dbh.getWritableDatabase();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
// use this setting to improve performance if you know that changes
// in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        //     List<Itemlistobject> rowListItem = getAllItemList();
// use a linear layout manager
        mLayoutManager = new LinearLayoutManager(Select_Main_Menu.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
// specify an adapter (see also next example)
        //  mAdapter =new MyAdapter(Select_Main_Menu.this,rowListItem);
        //   mRecyclerView.setAdapter(mAdapter);
        // mAdapter.setClickListener(this);


        mRecyclerView.addOnItemTouchListener(new CustomRVItemTouchListener(this, mRecyclerView, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                Itemlistobject selecteditem = mAdapter.getItem(position);
                String id = selecteditem.getID();
                Intent intent = new Intent(Select_Main_Menu.this, Select_Sub_Menu.class);
                SharedPreferences.Editor editor=SpType.edit();
                editor.putInt("WC_ID",Integer.parseInt(id));
                editor.commit();
                intent.putExtra("Selected_ID", id);
                startActivity(intent);
                Toast.makeText(Select_Main_Menu.this, "position selected" + position, Toast.LENGTH_LONG).show();
                finish();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        Fill_Category fillList = new Fill_Category();
        fillList.execute("");

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        img_ad = findViewById(R.id.vp_ad3);
        //   indicator=(TabLayout)findViewById(R.id.indicator);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, imageurl);
        img_ad.setAdapter(viewPagerAdapter);

    }



    public class Fill_Category extends AsyncTask<String, String, String> {

        String z = "";


        ArrayList<Itemlistobject> Categorylist = new ArrayList<Itemlistobject>();


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String r) {


            Toast.makeText(Select_Main_Menu.this, r, Toast.LENGTH_SHORT).show();
            // reading from the keys that we will use to raed from the customer list they must be the same as a hash map keys


            // the ada is binding all together

            mAdapter = new MyAdapter(Select_Main_Menu.this, Categorylist);
            // put them in the list view

            mRecyclerView.setAdapter(mAdapter);


        }

        @Override
        protected String doInBackground(String... params) {

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
                    String Cat_id, Cat_image_path, Cat_image_name, Cat_SC_Descption, Cat_SC_Descption_ar;

                    // we use put method to save the data in that map and we use the column name as the key
                    Cat_id = cursor.getString(cursor.getColumnIndex("SC_id"));
                    Cat_SC_Descption = cursor.getString(cursor.getColumnIndex("SC_Descption"));
                    Cat_SC_Descption_ar = cursor.getString(cursor.getColumnIndex("SC_Description_ar"));
                    Cat_image_path = cursor.getString(cursor.getColumnIndex("SC_Icon_path"));
                    Cat_image_name = cursor.getString(cursor.getColumnIndex("SC_Icon_name"));
if (lang.equals("en_US")){
    Cat_SC_Descption = cursor.getString(cursor.getColumnIndex("SC_Descption"));
    Cat_SC_Descption_ar = cursor.getString(cursor.getColumnIndex("SC_Descption"));
}else {
    Cat_SC_Descption = cursor.getString(cursor.getColumnIndex("SC_Description_ar"));
    Cat_SC_Descption_ar = cursor.getString(cursor.getColumnIndex("SC_Description_ar"));
}

                    Itemlistobject category_button = new Itemlistobject(Cat_id, Cat_SC_Descption, Cat_SC_Descption_ar, Cat_SC_Descption_ar, Cat_image_name,Cat_image_path);


                    // we add the saved map to the list one by one
                    this.Categorylist.add(category_button);
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


    @Override
    public void itemClick(View view, int position) {


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
                Intent intent = new Intent(Select_Main_Menu.this, Main_Activity.class);
                startActivity(intent);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            Select_Main_Menu.this.runOnUiThread(new Runnable() {
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
}

