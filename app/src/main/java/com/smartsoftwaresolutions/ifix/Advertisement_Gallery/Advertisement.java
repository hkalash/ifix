package com.smartsoftwaresolutions.ifix.Advertisement_Gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.smartsoftwaresolutions.ifix.Main_Activity;
import com.smartsoftwaresolutions.ifix.R;
import com.smartsoftwaresolutions.ifix.Read_Data.API;
import com.smartsoftwaresolutions.ifix.Worker_Profile;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Advertisement extends AppCompatActivity {
    private ArrayList<image_Info> imageurl=new ArrayList<image_Info>();
//    private ArrayList imageurl=new ArrayList(Arrays.asList(  API.Ad_Image+"ad1.jpg", API.Ad_Image+"ad2.jpg",
//        API.Ad_Image+"ad3.jpg", API.Ad_Image+"ad4.jpg", API.Ad_Image+"ad5.jpg")
//
//            );
//    private ArrayList personNames=new ArrayList(Arrays.asList("","","","",""));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        image_Info image_info=new image_Info("",API.Ad_Image+"ad1.jpg");
        imageurl.add(image_info);
        image_Info image_info1=new image_Info("",API.Ad_Image+"ad2.jpg");
        imageurl.add(image_info1);
        image_Info image_info2=new image_Info("",API.Ad_Image+"ad3.jpg");
        imageurl.add(image_info2);
        image_Info image_info3=new image_Info("",API.Ad_Image+"ad4.jpg");
        imageurl.add(image_info3);
        image_Info image_info4=new image_Info("",API.Ad_Image+"ad5.jpg");
        imageurl.add(image_info4);
        // get the reference of RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view9);
        // set a StaggeredGridLayoutManager with 3 number of columns and vertical orientation
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(Advertisement.this);
//        recyclerView.setLayoutManager(linearLayoutManager);
        CustomAdapter customAdapter = new CustomAdapter(Advertisement.this,  imageurl);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
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
//                Intent intent=new Intent(Advertisement.this, Main_Activity.class);
//                startActivity(intent);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
