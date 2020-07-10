package com.smartsoftwaresolutions.ifix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.smartsoftwaresolutions.ifix.Order_Service.order_one_master;
import com.smartsoftwaresolutions.ifix.Pager.ViewPagerAdapter;
import com.smartsoftwaresolutions.ifix.Read_Data.API;

import java.util.Timer;
import java.util.TimerTask;

public class Member_Type extends AppCompatActivity {
    public static final String PREFS_NAME = "ifix_data";
    SharedPreferences SpType ;
    Button btn_done1;
    ViewPager img_ad;
    private String [] imageurl=new String[]{
            API.Ad_Image+"ad1.jpg", API.Ad_Image+"ad2.jpg",
            API.Ad_Image+"ad3.jpg", API.Ad_Image+"ad4.jpg", API.Ad_Image+"ad5.jpg"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member__type);
btn_done1=findViewById(R.id.btn_done1);
        SpType=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);



        ((RadioGroup) findViewById(R.id.native_Choice))
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        SharedPreferences.Editor editor = SpType.edit();
                        switch (i) {
                            case R.id.vip:
                                // vip
                                editor.putString("M_type","1");
                                editor.commit();
                               // Open_Another_activity();
                                break;
                            case R.id.gold:
                                // gold
                                editor.putString("M_type","2");
                                editor.putInt("Max_image_number",3);
                                editor.commit();
                              //  Open_Another_activity();
                                break;
                            default:
                                // free
                                editor.putString("M_type","3");
                                editor.putInt("Max_image_number",1);
                                editor.commit();

                                break;
                        }
                    }
                });

        btn_done1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_Another_activity();
            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        img_ad=findViewById(R.id.vp_ad5);
        //   indicator=(TabLayout)findViewById(R.id.indicator);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this,imageurl);
        img_ad.setAdapter(viewPagerAdapter);
    }

    private void Open_Another_activity() {
        Intent intent=new Intent(Member_Type.this,Register_a_service_form_two.class);
        startActivity(intent);
        finish();
    }
    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            Member_Type.this.runOnUiThread(new Runnable() {
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

            case R.id.action_back:


                finish();
                return true;
            case R.id.action_Home:
                Intent intent=new Intent(Member_Type.this, Main_Activity.class);
                startActivity(intent);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
