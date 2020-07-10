package com.smartsoftwaresolutions.ifix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartsoftwaresolutions.ifix.Advertisement_Gallery.Advertisement;
import com.smartsoftwaresolutions.ifix.Database.DataHelper;
import com.smartsoftwaresolutions.ifix.Order_list_chiled.Itemlistobject4;
import com.smartsoftwaresolutions.ifix.Order_list_chiled.MyAdapter4;
import com.smartsoftwaresolutions.ifix.Order_list_chiled.Order_list_chiled;
import com.smartsoftwaresolutions.ifix.Read_Data.API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class My_Favorite_list extends AppCompatActivity {
RecyclerView mRecyclerView6;
    private MyAdapter4 mAdapter4;
    private RecyclerView.LayoutManager mLayoutManager4;
    List<Itemlistobject4> allItems1 = new ArrayList<Itemlistobject4>();
    DataHelper AndPOS_dbh;
    SQLiteDatabase AndPOS_db;

    String Selected_id;

    public static final String PREFS_NAME = "ifix_data";
    SharedPreferences SpType ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__favorite_list);
        SpType=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= SpType.edit();
        editor.putBoolean("from_favoret",true);

        editor.commit();

        mRecyclerView6=findViewById(R.id.my_recycler_view6);

        // we create an instance of data base helper  database this will create the database
        AndPOS_dbh = new DataHelper(getApplicationContext());
        // and we connect the data to the data base helper and specify its use as a writable
        AndPOS_db = AndPOS_dbh.getWritableDatabase();
        mRecyclerView6.setHasFixedSize(true);
        // List<Itemlistobject4> rowListItem = getAllItemList();
// use a linear layout manager
        mLayoutManager4 = new LinearLayoutManager(My_Favorite_list.this);
        mRecyclerView6.setLayoutManager(mLayoutManager4);
// specify an adapter (see also next example)

        // mAdapter.setClickListener(this);

        mAdapter4 =new MyAdapter4(My_Favorite_list.this,allItems1);
        // mAdapter4.notifyDataSetChanged();
        mRecyclerView6.setAdapter(mAdapter4);
        mAdapter4.notifyDataSetChanged();


        mRecyclerView6.addOnItemTouchListener(new CustomRVItemTouchListener(this, mRecyclerView6, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {

//                Itemlistobject4 selecteditem=mAdapter4.getItem4(position);
//                Intent intent=new Intent(My_Favorite_list.this, Worker_Profile.class);
//
//                intent.putExtra("SWM_ID",selecteditem.getSWM_ID());
//                intent.putExtra("SWM_Address",selecteditem.getSWM_Address());
//                intent.putExtra("SWM_Lat",selecteditem.getSWM_Lat());
//                intent.putExtra("SWM_Lon",selecteditem.getSWM_Lon());
//                intent.putExtra("SWM_name",selecteditem.getSWM_name());
//                intent.putExtra("SWM_Phone",selecteditem.getSWM_Phone());
//                intent.putExtra("SWM_Mobile",selecteditem.getSWM_Mobile());
//                intent.putExtra("SWM_Pic",selecteditem.getSWM_Pic());
//
//                startActivity(intent);
//
//                Toast.makeText(My_Favorite_list.this,"position selected"+position,Toast.LENGTH_LONG).show();
                Itemlistobject4 selecteditem=mAdapter4.getItem4(position);

                Intent intent=new Intent(My_Favorite_list.this, Worker_Profile.class);

                intent.putExtra("SWM_ID",selecteditem.getSWM_ID());
                intent.putExtra("SWM_Address",selecteditem.getSWM_Address());
                intent.putExtra("SWM_Lat",selecteditem.getSWM_Lat());
                intent.putExtra("SWM_Lon",selecteditem.getSWM_Lon());
                intent.putExtra("SWM_name",selecteditem.getSWM_name());
                intent.putExtra("SWM_Phone",selecteditem.getSWM_Phone());
                intent.putExtra("SWM_Mobile",selecteditem.getSWM_Mobile());
                intent.putExtra("SWM_Pic",selecteditem.getSWM_Pic());
                intent.putExtra("SWM_Web",selecteditem.getSWM_Web());
                intent.putExtra("SWM_company",selecteditem.getSWM_company());


                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

Read_Data_sqlite read_data_sqlite=new Read_Data_sqlite();
read_data_sqlite.execute();
    }
    class Read_Data_sqlite extends AsyncTask<Void, Void, List<Itemlistobject4>> {
        List<Itemlistobject4> allItems2 = new ArrayList<Itemlistobject4>();




        @Override
        protected void onPostExecute(List<Itemlistobject4> itemlistobject4s) {
            super.onPostExecute(itemlistobject4s);
            allItems1=itemlistobject4s;
            mAdapter4 =new MyAdapter4(My_Favorite_list.this,allItems2);
            mRecyclerView6.setAdapter(mAdapter4);
            //  mAdapter4.notifyDataSetChanged();
        }

        @Override
        protected List<Itemlistobject4> doInBackground(Void... voids) {
            // i will read from the sqlite and add it to the
            Itemlistobject4 listdata;
            String Q1="Select * from Service_Worker_Favorite_Table";
            Cursor cursor=AndPOS_db.rawQuery(Q1,null);
            if (cursor.moveToFirst()){
                do{

                    String FSWM_ID,FSWM_Name,FSWM_Phone,FSWM_Mobile,FSWM_email,FSWM_Web,FSWM_Address,FSWM_Lat,FSWM_Lon,FSWM_Pic,FSWM_company;
                    FSWM_ID=cursor.getString(cursor.getColumnIndex("FSWM_ID"));
                    FSWM_Name=cursor.getString(cursor.getColumnIndex("FSWM_Name"));
                    FSWM_Phone=cursor.getString(cursor.getColumnIndex("FSWM_Phone"));
                    FSWM_Mobile=cursor.getString(cursor.getColumnIndex("FSWM_Mobile"));
                    FSWM_email=cursor.getString(cursor.getColumnIndex("FSWM_email"));
                    FSWM_Web=cursor.getString(cursor.getColumnIndex("FSWM_Web"));
                    FSWM_Address=cursor.getString(cursor.getColumnIndex("FSWM_Address"));
                    FSWM_Lat=cursor.getString(cursor.getColumnIndex("FSWM_Lat"));
                    FSWM_Lon=cursor.getString(cursor.getColumnIndex("FSWM_Lon"));
                    FSWM_Pic=cursor.getString(cursor.getColumnIndex("FSWM_Pic"));
                    FSWM_company=cursor.getString(cursor.getColumnIndex("FSWM_company"));

//                    String SWM_ID, String SWM_Address, String SWM_Lat, String SWM_Lon,String SWM_company,
//                            String SWM_name, String SWM_Phone, String SWM_Mobile,String SWM_Web, String SWM_Pic,String SWM_Country_ID,
//                            String SWM_Sub_C,String WC_ID,String MS_ID)

                  listdata=new Itemlistobject4(FSWM_ID,FSWM_Address,FSWM_Lat,FSWM_Lon,FSWM_Name
                          ,FSWM_Phone,FSWM_Mobile,FSWM_email,FSWM_Web,
                           FSWM_Pic,"","","","","",FSWM_company,"");

                    allItems2.add(listdata);

                }while (cursor.moveToNext());
            }





                                mAdapter4.notifyDataSetChanged();





            return allItems2;


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
        getMenuInflater().inflate(R.menu.menu1,menu);
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
//                Intent intent=new Intent(My_Favorite_list.this, Main_Activity.class);
//                startActivity(intent);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Read_Data_sqlite read_data_sqlite=new Read_Data_sqlite();
        read_data_sqlite.execute();
    }
}
