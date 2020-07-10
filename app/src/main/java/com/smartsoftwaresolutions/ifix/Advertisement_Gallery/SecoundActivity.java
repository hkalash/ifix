package com.smartsoftwaresolutions.ifix.Advertisement_Gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.smartsoftwaresolutions.ifix.R;
import com.squareup.picasso.Picasso;

public class SecoundActivity extends AppCompatActivity {
ImageView selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secound);
        selectedImage = (ImageView) findViewById(R.id.selectedImage); // init a ImageView
        Intent intent = getIntent(); // get Intent which was set from adapter of Previous Activity
        String image_path =intent.getStringExtra("image");
        Picasso.get()
                .load(image_path)
                .centerCrop()
                .fit()
                .into(selectedImage);
        selectedImage.setImageResource(intent.getIntExtra("image", 0)); // get image from Intent and set it in ImageView
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
}
