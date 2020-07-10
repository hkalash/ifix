package com.smartsoftwaresolutions.ifix.Order_Service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.smartsoftwaresolutions.ifix.Database.DataHelper;
import com.smartsoftwaresolutions.ifix.Main_Activity;
import com.smartsoftwaresolutions.ifix.Main_Category_List.Category_Adapter;
import com.smartsoftwaresolutions.ifix.Main_Category_List.Category_Button;

import com.smartsoftwaresolutions.ifix.Main_Sub_Category_List.Sub_Category_Adapter;
import com.smartsoftwaresolutions.ifix.Main_Sub_Category_List.Sub_Category_Button;
import com.smartsoftwaresolutions.ifix.Order_list_chiled.Order_list_chiled;
import com.smartsoftwaresolutions.ifix.Pager.ViewPagerAdapter;
import com.smartsoftwaresolutions.ifix.R;
import com.smartsoftwaresolutions.ifix.Read_Data.API;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.smartsoftwaresolutions.ifix.Picasso_Target.picassoImageTarget;

public class order_one_master extends AppCompatActivity {
 public static RecyclerView gridView;
  //  ListView R_Category_List;
    ImageView btn_img_right,btn_img_left;
    Category_Adapter ADA;
    Sub_Category_Adapter ADA_Sub;
    DataHelper AndPOS_dbh;
    SQLiteDatabase AndPOS_db;
    RecyclerView R_Category_List;


int currentVisibleItem;
boolean    programaticallyScrolled =true;

    String[] CI_image,CI_Image_path;
    List<String> CI_image_L = new ArrayList<String>();
    List<String> CI_Image_path_L = new ArrayList<String>();

    String[] MI_image,MI_Image_path;
    List<String> MI_image_L = new ArrayList<String>();
    List<String> MI_Image_path_L = new ArrayList<String>();
String lang;
    SharedPreferences SpType ;
    public static final String PREFS_NAME = "ifix_data";

    ViewPager img_ad;
    private String [] imageurl=new String[]{
            API.Ad_Image+"ad1.jpg", API.Ad_Image+"ad2.jpg",
            API.Ad_Image+"ad3.jpg", API.Ad_Image+"ad4.jpg", API.Ad_Image+"ad5.jpg"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_one_master);

        btn_img_right=findViewById( R.id.btn_img_right);
btn_img_left=findViewById(R.id.btn_img_left);

        img_ad=findViewById(R.id.vp_ad6);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this,imageurl);
        img_ad.setAdapter(viewPagerAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
//sqlite data inspection intilisation
        SpType=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        // Reading from SharedPreferences
        lang=SpType.getString("lang","en_US");

        gridView = findViewById(R.id.SCL_Grid);
        gridView.setHasFixedSize(true);


        // we create an instance of data base helper  database this will create the database
        AndPOS_dbh = new DataHelper(getApplicationContext());
        // and we connect the data to the data base helper and specify its use as a writable
        AndPOS_db = AndPOS_dbh.getWritableDatabase();
      //  LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        final LinearLayoutManager horizontalLayoutManagaer= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
         R_Category_List =  findViewById(R.id.CL);
        R_Category_List.setLayoutManager(horizontalLayoutManagaer);

        btn_img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (horizontalLayoutManagaer.findFirstVisibleItemPosition() > 0) {
                    R_Category_List.smoothScrollToPosition(horizontalLayoutManagaer.findFirstVisibleItemPosition() - 1);
                } else {
                    R_Category_List.smoothScrollToPosition(0);
                }

            }
        });

        btn_img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                R_Category_List.smoothScrollToPosition(horizontalLayoutManagaer.findLastVisibleItemPosition() + 1);
            }
        });

        R_Category_List.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case SCROLL_STATE_DRAGGING:
                        //Indicated that user scrolled.
                        programaticallyScrolled = false;
                        break;
                    case SCROLL_STATE_IDLE:
                        if (!programaticallyScrolled) {
                            currentVisibleItem = horizontalLayoutManagaer.findFirstCompletelyVisibleItemPosition();
                            handleWritingViewNavigationArrows(false);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL); // set Horizontal Orientation

        gridView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView





Fill_Category fillList=new Fill_Category();
fillList.execute("");

Fill_Sub_Category_Start fill_sub_category_start=new Fill_Sub_Category_Start();
fill_sub_category_start.execute("");


    }

/*****************Left and right ******************/

    /**
     * Handles the arrows visibility based on current visible items and scrolls the
     * current visibility item based on param scroll.
     *
     * Scroll - is False if User scrolls the Reycler Manually
     *        - is True means User used arrows to navigate
     *
     * @param scroll
     */
    private void handleWritingViewNavigationArrows(boolean scroll) {
        if (currentVisibleItem == ( R_Category_List.getAdapter().getItemCount() - 1)) {
          btn_img_left .setVisibility(View.GONE);
            btn_img_right.setVisibility(View.VISIBLE);
        } else if (currentVisibleItem != 0) {
            btn_img_right.setVisibility(View.VISIBLE);
            btn_img_left.setVisibility(View.VISIBLE);
        } else if (currentVisibleItem == 0) {
            btn_img_left.setVisibility(View.GONE);
            btn_img_right.setVisibility(View.VISIBLE);
        }
        if (scroll) {
            R_Category_List.smoothScrollToPosition(currentVisibleItem);
        }
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            order_one_master.this.runOnUiThread(new Runnable() {
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

    public class Fill_Category extends AsyncTask<String, String, String> {

        String z = "";

        // List<Map<String, String>> Custlist  = new ArrayList<Map<String, String>>();
        ArrayList<Category_Button> Categorylist = new ArrayList<Category_Button>();
        // List<Map<String ,String >> CustListSQL=new ArrayList<Map<String,String>>;

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String r) {


            Toast.makeText( order_one_master.this, r, Toast.LENGTH_SHORT ).show();
            // reading from the keys that we will use to raed from the customer list they must be the same as a hash map keys


            // the ada is binding all together
            // ADA = new SimpleAdapter(SearchCustomer.this, Custlist, R.layout.customer_element, from, views);
            ADA = new Category_Adapter( order_one_master.this,Categorylist );
            // put them in the list view

            R_Category_List.setAdapter( ADA );


             CI_Image_path = new String[CI_Image_path_L.size()];
            CI_Image_path = CI_Image_path_L.toArray(CI_Image_path);//now strings is the resulting array
             CI_image = new String[CI_image_L.size()];
            CI_image =  CI_image_L.toArray( CI_image);//now strings is the resulting array

           // load images to the app folder
            for (int x=0;x<CI_image.length;x++){
                String i_name=CI_image[x];
                String i_path=CI_Image_path[x];
                Picasso.get().load(i_path).into(picassoImageTarget(getApplicationContext(), "CI", i_name));
            }



        }

        @Override
        protected String doInBackground(String... params) {

            // we specify the database helper and the database and set the database to readable
            // then we write a select statment
            //   String query_SQL = "select * from Family_Tbl ";
            String query_SQL = "select * from service_category_tbl  ";
            // we run this select statment by creating a cursor and execute it
            Cursor cursor = AndPOS_db.rawQuery( query_SQL, null );
            // if the result of the execute statment have data then the if statment is true and we move the cursor to the first row
            if (cursor.moveToFirst()) {
                do {
                    // we create a hash map with string for the key and string for the data
                    HashMap<String, String> map = new HashMap<String, String>();
                    String Cat_id, Cat_image_path,Cat_image_name,Cat_des;

                    // we use put method to save the data in that map and we use the column name as the key
                    Cat_id = cursor.getString( cursor.getColumnIndex( "SC_id" ) );
                    Cat_image_path = cursor.getString( cursor.getColumnIndex( "SC_Icon_path" ) );
                   Cat_image_name = cursor.getString( cursor.getColumnIndex( "SC_Icon_name" ) );
                    if (lang.equals("ar")){
                        Cat_des = cursor.getString( cursor.getColumnIndex( "SC_Description_ar" ) );
                    }else{
                        Cat_des = cursor.getString( cursor.getColumnIndex( "SC_Descption" ) );
                    }



                   CI_image_L.add(Cat_image_name);
                   CI_Image_path_L.add(Cat_image_path);
//                    Picasso.get().load(Cat_image_path).into(Picasso_Target.picassoImageTarget(order_one_master.this,
//                           "CI", Cat_image_name));
//                    CI_image[i]=Cat_image_name;
//                   CI_Image_path[i]=Cat_image_path;
//                    i++;
                    Category_Button category_button = new Category_Button( Cat_id, Cat_image_path,Cat_des );


                    // we add the saved map to the list one by one
                    this.Categorylist.add( category_button );
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

    public class Fill_Sub_Category_Start extends AsyncTask<String, String, String> {

        String z = "";


        ArrayList<Sub_Category_Button> Sub_Categorylist = new ArrayList<Sub_Category_Button>();


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String r) {


            Toast.makeText( order_one_master.this, r, Toast.LENGTH_SHORT ).show();
            // reading from the keys that we will use to raed from the customer list they must be the same as a hash map keys



            ADA_Sub = new Sub_Category_Adapter( order_one_master.this,Sub_Categorylist );
            // put them in the list view

            gridView.setAdapter( ADA_Sub );
/**   load images to the device**/
            MI_Image_path = new String[MI_Image_path_L.size()];
            MI_Image_path = MI_Image_path_L.toArray(MI_Image_path);//now strings is the resulting array
            MI_image = new String[MI_image_L.size()];
            MI_image =  MI_image_L.toArray( MI_image);//now strings is the resulting array

            // load images to the app folder
            for (int x=0;x<MI_image.length;x++){
                String i_name=MI_image[x];
                String i_path=MI_Image_path[x];
                Picasso.get().load(i_path).into(picassoImageTarget(getApplicationContext(), "MI", i_name));
            }


        }

        @Override
        protected String doInBackground(String... params) {

            // we specify the database helper and the database and set the database to readable
            // then we write a select statment
            //   String query_SQL = "select * from Family_Tbl ";
            String query_SQL = "select * from Master_Service_tbl where SC_ID=7";
            // we run this select statment by creating a cursor and execute it
            Cursor cursor = AndPOS_db.rawQuery( query_SQL, null );
            // if the result of the execute statment have data then the if statment is true and we move the cursor to the first row
            if (cursor.moveToFirst()) {
                do {
                    // we create a hash map with string for the key and string for the data
                    HashMap<String, String> map = new HashMap<String, String>();
                    String Sub_Cat_id, Sub_Cat_image_path,Sub_Name_ar,Sub_Name_en,Sub_Cat_Image_name;
//                    "    MS_Image_path VARCHAR (500), \n" +
//                            "    MS_Image_name VARCHAR (500) \n" +

                    // we use put method to save the data in that map and we use the column name as the key
                    Sub_Cat_id = cursor.getString( cursor.getColumnIndex( "MS_ID" ) );
                    if (lang.equals("ar")){
                        Sub_Name_ar = cursor.getString( cursor.getColumnIndex( "MS_Name_ar" ) );
                    }else {
                        Sub_Name_ar = cursor.getString( cursor.getColumnIndex( "MS_Name" ) );
                    }
                  //  Sub_Name_ar = cursor.getString( cursor.getColumnIndex( "MS_Name_ar" ) );
                  //  Sub_Name_en = cursor.getString( cursor.getColumnIndex( "MS_Name" ) );
                    Sub_Cat_image_path = cursor.getString( cursor.getColumnIndex( "MS_Image_path" ) );
Sub_Cat_Image_name= cursor.getString( cursor.getColumnIndex( "MS_Image_name" ) );
                    Sub_Category_Button sub_category_button = new Sub_Category_Button( Sub_Name_ar, Sub_Cat_image_path,Sub_Cat_id );

                    MI_image_L.add(Sub_Cat_Image_name);
                    MI_Image_path_L.add(Sub_Cat_image_path);
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
//                Intent intent=new Intent(order_one_master.this, Main_Activity.class);
//              startActivity(intent);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
