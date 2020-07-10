package com.smartsoftwaresolutions.ifix;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartsoftwaresolutions.ifix.Advertisement_Gallery.Advertisement;
import com.smartsoftwaresolutions.ifix.Database.DataHelper;
import com.smartsoftwaresolutions.ifix.Gallery.Gallery;
import com.smartsoftwaresolutions.ifix.Images.Images_Profile;
import com.smartsoftwaresolutions.ifix.Pager.ViewPagerAdapter;
import com.smartsoftwaresolutions.ifix.Read_Data.API;
import com.smartsoftwaresolutions.ifix.Order_Service.order_one_master;
import com.smartsoftwaresolutions.ifix.Sub_country_spinner.Sub_country_spinner;
import com.smartsoftwaresolutions.ifix.country_spinner.Country;
import com.smartsoftwaresolutions.ifix.country_spinner.country_spinner;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;


import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Main_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DataHelper AndPOS_dbh;
    SQLiteDatabase AndPOS_db;
    Button btn_order,btn_offer,btn_ar,btn_en,btn_gal;
    ViewPager img_ad;
  //  TabLayout indicator;

private String [] imageurl=new String[]{
        API.Ad_Image+"ad1.jpg", API.Ad_Image+"ad2.jpg",
        API.Ad_Image+"ad3.jpg", API.Ad_Image+"ad4.jpg", API.Ad_Image+"ad5.jpg"
};
    SharedPreferences SpType ;
    public static final String PREFS_NAME = "ifix_data";
   int  Server_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // we create an instance of data base helper  database this will create the database
        AndPOS_dbh = new DataHelper(getApplicationContext());
        // and we connect the data to the data base helper and specify its use as a writable
        AndPOS_db = AndPOS_dbh.getWritableDatabase();
        //sqlite data inspection intilisation
        SpType=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

        btn_offer=findViewById(R.id.btn_offer1);
        btn_order=findViewById(R.id.btn_order1);
        btn_gal=findViewById(R.id.btn_gal);
//        btn_en=findViewById(R.id.btn_en1);
//        btn_ar=findViewById(R.id.btn_ar1);
        Server_id=SpType.getInt("Server_ID",0);
        if (Server_id!=0){
            btn_offer.setText(R.string.edit_adv);
        }
img_ad=findViewById(R.id.vp_ad);
     //   indicator=(TabLayout)findViewById(R.id.indicator);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this,imageurl);
        img_ad.setAdapter(viewPagerAdapter);
      //  indicator.setupWithViewPager(img_ad, true);
        btn_gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent(Main_Activity.this, Advertisement.class);
                startActivity(intent);
                //finish();
            }
        });
        btn_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // read the data from the sharedperiferance and checkif there is a username and password in the sharedperiferance
                String Sp_username,Sp_Password,Sp_company_name,Sp_mobile;
                // if the user check the remamber password
                Boolean Sp_flag;
            Sp_username=SpType.getString("username","");
            Sp_Password=SpType.getString("userpassword","");
            Sp_company_name=SpType.getString("company_name","");
            Sp_mobile=SpType.getString("mobile","");
            // the default is false we set it for true for that time only
            Sp_flag=SpType.getBoolean("remamber_password",true);
         //   Sp_flag=SpType.getBoolean("remamber_password",false);

// true mean that the user have loged in one time and put save my password
//                if (Sp_flag==true){
//                    // the user set remamber my password to true then the my profile will open
//                    Intent i=new Intent(Main_Activity.this, My_Profile.class);
//                    startActivity(i);
//                }else {
                    // the remamber my password is false so the user will put the username and password
                    // mean that the user go threw the registration phase
                    if (!Sp_company_name.equals("")&!Sp_mobile.equals("")){
                        SharedPreferences.Editor editor = SpType.edit();
                        // flag_register is used for edite user service
                editor.putBoolean("flag_register",true);
                editor.putBoolean("main_flage",true);
                editor.commit();
                      //  Intent i=new Intent(Main_Activity.this, Login.class);
                        Intent i=new Intent(Main_Activity.this, My_Profile.class);
                        startActivity(i);
                       // finish();
                    }else {
                        // new registration
                        Intent i=new Intent(Main_Activity.this, Registration_form_one.class);
                        startActivity(i);
                      //  finish();
                    }


               //     }

                }



        });
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Main_Activity.this, order_one_master.class);
                startActivity(i);
//                finish();
            }
        });

//        btn_ar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setLocale("ar");
//
//            }
//        });
//        btn_en.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor editor = SpType.edit();
//                editor.putString("lang","en_US");
//                editor.commit();
//                setLocale("en_US");
//            }
//        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }



    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        /// change the direction of the app from left to right to right to left
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLayoutDirection(myLocale);
        }
        res.updateConfiguration(conf, dm);
//        Intent refresh = new Intent(this,Select_Main_option.class);
        Intent refresh = new Intent(this, Main_Activity.class);
        startActivity(refresh);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        SharedPreferences.Editor editor = SpType.edit();
        switch (item.getItemId()) {
            case R.id.action_Arabic:
                setLocale("ar");
           /* Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            // Resources res = getResources();
            Configuration config = new Configuration();
            DisplayMetrics dm = getResources().getDisplayMetrics();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());*/
            Toast.makeText(this, "Locale in arabic !", Toast.LENGTH_LONG).show();

                editor.putString("lang","ar");
                editor.commit();
              //  finish();
            return true;
            case R.id.action_English:
              //  SharedPreferences.Editor editor = SpType.edit();
                editor.putString("lang","en_US");
                editor.commit();
                setLocale("en_US");
            //setLocale("n");

            Toast.makeText(this, "Locale in English !", Toast.LENGTH_LONG).show();
          //  finish();
            return true;
            case R.id.action_back:
                /**for testing perpos open the Image profile**/
              //  Intent z=new Intent(Main_Activity.this, Images_Profile.class);
                //startActivity(z);
//                  Intent z=new Intent(Main_Activity.this, Worker_Profile.class);
//                startActivity(z);
          finish();

                return true;
                case R.id.action_favorite:
                    SharedPreferences.Editor editor1= SpType.edit();
                    editor1.putBoolean("from_favoret",true);

                    editor1.commit();
Intent  intent=new Intent(Main_Activity.this,My_Favorite_list.class);
startActivity(intent);
                return true;
        }
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_Arabic) {
//            setLocale("ar");
//           /* Locale locale = new Locale("ar");
//            Locale.setDefault(locale);
//            // Resources res = getResources();
//            Configuration config = new Configuration();
//            DisplayMetrics dm = getResources().getDisplayMetrics();
//            config.locale = locale;
//            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());*/
//            Toast.makeText(this, "Locale in arabic !", Toast.LENGTH_LONG).show();
//
//            // return true;
//        } else {
//            //Locale locale = new Locale("en_US");
//            setLocale("n");
//            /*Locale.setDefault(locale);
//            // Resources res = getResources();
//            Configuration config = new Configuration();
//            DisplayMetrics dm = getResources().getDisplayMetrics();
//            config.locale = locale;
//            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());*/
//            Toast.makeText(this, "Locale in English !", Toast.LENGTH_LONG).show();
//        } {}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (id == R.id.nav_home) {
//            // Handle the camera action
//            drawer.closeDrawer(GravityCompat.START);
//        } else
            if (id == R.id.nav_gallery) {
Intent intent=new Intent(Main_Activity.this, Gallery.class);
startActivity(intent);
        } else if (id == R.id.register) {
//            Intent intent=new Intent(Main_Activity.this, Registration_form_one.class);
//////            startActivity(intent);
            Intent  intent=new Intent(Main_Activity.this,Country.class);
            startActivity(intent);
          //  finish();
        }

//        } else if (id == R.id.search) {
//            Intent intent=new Intent(Main_Activity.this, order_one_master.class);
//            startActivity(intent);
//
//        } else if (id == R.id.nav_share) {
        else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "Check it out.\n\n"+"https://play.google.com/store/apps/details?id=com.smartsoftwaresolutions.rosary2.free";

//            shared_Menu sharedMenu=new shared_Menu();
//            String flaver =BuildConfig.FLAVOR;
           // String shareBodyText =sharedMenu.flavor(flaver);
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
            Toast.makeText(Main_Activity.this,"that will share the application to other customers",Toast.LENGTH_LONG).show();

        } else if (id == R.id.about_us) {
            Intent  intent=new Intent(Main_Activity.this,About_Us.class);
            startActivity(intent);
           // finish();
        }else if (id == R.id.contact_us) {
            Intent  intent=new Intent(Main_Activity.this,Contact_Us.class);
            startActivity(intent);
           // finish();
        }else if (id == R.id.help) {
            Intent  intent=new Intent(Main_Activity.this,Help.class);
            startActivity(intent);
         //   finish();
        }else if (id == R.id.favoret) {
            Intent  intent=new Intent(Main_Activity.this,My_Favorite_list.class);
            SharedPreferences.Editor editor= SpType.edit();
            editor.putBoolean("from_favoret",true);

            editor.commit();
            startActivity(intent);
           // finish();
        }else if (id == R.id.Gallery) {
            Intent  intent=new Intent(Main_Activity.this, Advertisement.class);
            startActivity(intent);
          //  finish();
        }        else if (id == R.id.nav_edit) {

            String Sp_company_name,Sp_mobile;
            Sp_company_name=SpType.getString("company_name","");
            Sp_mobile=SpType.getString("mobile","");
            if (!Sp_company_name.equals("")&!Sp_mobile.equals("")){
                SharedPreferences.Editor editor = SpType.edit();
                // flag_register is used for edite user service
                editor.putBoolean("flag_register",true);
                editor.putBoolean("main_flage",true);
                editor.commit();
                //  Intent i=new Intent(Main_Activity.this, Login.class);
                Intent i=new Intent(Main_Activity.this, My_Profile.class);
                startActivity(i);
             //   finish();
            }else {
                // new registration
                Intent i=new Intent(Main_Activity.this, Registration_form_one.class);
                startActivity(i);
             //   finish();
            }
        }

      //  DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            Main_Activity.this.runOnUiThread(new Runnable() {
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
