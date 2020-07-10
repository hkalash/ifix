package com.smartsoftwaresolutions.ifix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartsoftwaresolutions.ifix.Database.DataHelper;
import com.smartsoftwaresolutions.ifix.Images.Images_Profile;
import com.smartsoftwaresolutions.ifix.Order_Service.order_one_master;
import com.smartsoftwaresolutions.ifix.Order_list_chiled.Itemlistobject4;
import com.smartsoftwaresolutions.ifix.Order_list_chiled.MyAdapter4;
import com.smartsoftwaresolutions.ifix.Order_list_chiled.Order_list_chiled;
import com.smartsoftwaresolutions.ifix.Pager.Image_list;
import com.smartsoftwaresolutions.ifix.Pager.ViewPagerAdapter;
import com.smartsoftwaresolutions.ifix.Pager.Viewpager_adapter1;
import com.smartsoftwaresolutions.ifix.Read_Data.API;
import com.smartsoftwaresolutions.ifix.Read_Data.MyResponse1;
import com.smartsoftwaresolutions.ifix.Read_Data.RetrofitClient;
import com.smartsoftwaresolutions.ifix.map.MapsActivity;
import com.squareup.picasso.Picasso;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class Worker_Profile extends AppCompatActivity {
String SWM_ID,SWM_Address,SWM_Lat,SWM_Lon,SWM_name,SWM_Phone,SWM_Mobile,SWM_Pic,Pic_url,email;
    String TO,MESSAGE,SUBJECT,Web_address,description;
ImageView iv_favoret,iv_thump_up,iv_thump_down,iv_share,iv_send_email;
ViewPager profile_pic;
TextView tv_name,tv_address_in,tv_discription,tv_mobile,tv_phone;
ImageButton callUsbtn,WhatsUpbtn,btn_map,btn_browser;
    DataHelper AndPOS_dbh;
    SQLiteDatabase AndPOS_db;
    boolean flag_f=false;
    boolean flag_l=false;
    boolean flag_d=false;
   // private String [] imageurl=new String[]{""};
   ArrayList<String> imageurl = new ArrayList<String>();
    Boolean from_favoret;
    public static final String PREFS_NAME = "ifix_data";
    SharedPreferences SpType ;
    int Like_ID=0;
    int like_ =0,des_like=0;
    private static final int REQUEST_CALL = 1;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker__profile);
linearLayout =findViewById(R.id.main);
        Stetho.initializeWithDefaults(this);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        // we create an instance of data base helper  database this will create the database
        AndPOS_dbh = new DataHelper(getApplicationContext());
        // and we connect the data to the data base helper and specify its use as a writable
        AndPOS_db = AndPOS_dbh.getWritableDatabase();


        tv_name=findViewById(R.id.tv_name);
        iv_favoret=findViewById(R.id.iv_favoret);
        iv_thump_up=findViewById(R.id.iv_thump_up);
        iv_thump_down=findViewById(R.id.iv_thump_down);
        iv_share=findViewById(R.id.iv_share);
        iv_send_email=findViewById(R.id.iv_send_email);
        tv_address_in=findViewById(R.id.tv_address_in);
        tv_discription=findViewById(R.id.tv_discription);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_phone=findViewById(R.id.tv_phone);
        profile_pic=findViewById(R.id.profile);
        callUsbtn=findViewById(R.id.callUsbtn);
        WhatsUpbtn=findViewById(R.id.WhatsUpbtn);
        btn_map=findViewById(R.id.btn_map);
        btn_browser=findViewById(R.id.btn_browser);

        SpType=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        from_favoret=SpType.getBoolean("from_favoret",false);

        final GestureDetector tapGestureDetector = new GestureDetector(this, new TapGestureListener());
        profile_pic.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                tapGestureDetector.onTouchEvent(event);
                return false;
            }
        });

if (from_favoret==true){
    flag_f=true;
    iv_favoret.setImageResource(R.drawable.fill_favorite);
    SharedPreferences.Editor editor= SpType.edit();
    editor.putBoolean("from_favoret",false);

    editor.commit();
}


        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Check it out.\n\n"+"https://play.google.com/store/apps/details?id=com.smartsoftwaresolutions.rosary2.free";

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
                Toast.makeText(Worker_Profile.this,"that will share the application to other customers",Toast.LENGTH_LONG).show();
            }
        });
//        Read_Data_sqlite read_data_sqlite=new Read_Data_sqlite();
//        read_data_sqlite.execute();


        iv_favoret.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

       if (!flag_f){
           flag_f=true;
           iv_favoret.setImageResource(R.drawable.fill_favorite);
           InsertFavorite insertFavorite=new InsertFavorite();
           insertFavorite.execute("");
          Toast.makeText(Worker_Profile.this, "Add to favorite", Toast.LENGTH_SHORT).show();
       }else {
           iv_favoret.setImageResource(R.drawable.favorite_yellow);
           DeleteFavorite deleteFavorite=new DeleteFavorite();
           deleteFavorite.execute("");
           flag_f=false;
           Toast.makeText(Worker_Profile.this, "Removed from favorite", Toast.LENGTH_SHORT).show();
       }

    }
});

iv_thump_down.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // if the user dont prviously clike the des_like
        if (!flag_d){
            flag_d=true;
            iv_thump_down.setImageResource(R.drawable.fill_thumb_down);
           // iv_thump_up.setVisibility(View.GONE);

            //Toast.makeText(Worker_Profile.this, "", Toast.LENGTH_SHORT).show();
        }else {
            iv_thump_down.setImageResource(R.drawable.outline_thumb_down);
            flag_d=false;
            //iv_thump_up.setVisibility(View.VISIBLE);
           /// Toast.makeText(Worker_Profile.this, "Removed from favorite", Toast.LENGTH_SHORT).show();
        }
    }
});

iv_thump_up.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (!flag_l){
            flag_l=true;
            iv_thump_up.setImageResource(R.drawable.fill_thumb_up);
            //iv_thump_down.setVisibility(View.GONE);
            //Toast.makeText(Worker_Profile.this, "", Toast.LENGTH_SHORT).show();
        }else {
            iv_thump_up.setImageResource(R.drawable.outline_thumb_up);
            flag_l=false;
          //  iv_thump_down.setVisibility(View.VISIBLE);
           /// Toast.makeText(Worker_Profile.this, "Removed from favorite", Toast.LENGTH_SHORT).show();
        }
    }
});
        SWM_ID= getIntent().getStringExtra("SWM_ID");
        SWM_Address= getIntent().getStringExtra("SWM_Address");
        tv_address_in.setText(SWM_Address);
        SWM_Lat= getIntent().getStringExtra("SWM_Lat");
        SWM_Lon= getIntent().getStringExtra("SWM_Lon");
        SWM_name= getIntent().getStringExtra("SWM_name");
        tv_name.setText(SWM_name);
        SWM_Phone= getIntent().getStringExtra("SWM_Phone");
        tv_phone.setText(SWM_Phone);
        SWM_Mobile= getIntent().getStringExtra("SWM_Mobile");
        tv_mobile.setText(SWM_Mobile);
        SWM_Pic= getIntent().getStringExtra("SWM_Pic");
        Web_address=getIntent().getStringExtra("SWM_Web");
        description=getIntent().getStringExtra("SWM_company");
        tv_discription.setText(description);
email=getIntent().getStringExtra("SWM_POBOX");


        String Q1="Select * from Like_tbl where SWM_ID="+SWM_ID;
        Cursor cursor=AndPOS_db.rawQuery(Q1,null);

        if (cursor.moveToFirst()){
            // that mean we have a recored for that worker
            Like_ID=cursor.getInt(cursor.getColumnIndex("Like_ID"));
            like_=cursor.getInt(cursor.getColumnIndex("Like_"));
            des_like=cursor.getInt(cursor.getColumnIndex("Des_like"));

        }

        if (like_>0){
            iv_thump_up.setVisibility(View.GONE);
        }

        if (des_like>0){
            iv_thump_down.setVisibility(View.GONE);
        }

        iv_send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GetData();
//                Intent intent ;
//              //  intent = new Intent(Intent.ACTION_SEND);
//                intent = new Intent(Intent.ACTION_SENDTO);
//
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{TO});
//                intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
//                intent.putExtra(Intent.EXTRA_TEXT, MESSAGE);
//
//                intent.setType("message/rfc822");
//
//
//
//                startActivity(Intent.createChooser(intent, "Select Email Sending App :"));

                //    String to = "ebhas@gmail.com";
                String to = email;
                String subject= "Hi subject";
                String body="Hi  message";
                String mailTo = "mailto:" + to +
                        "?&subject=" + Uri.encode(subject) +
                        "&body=" + Uri.encode(body);
                Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                emailIntent.setData(Uri.parse(mailTo));
                startActivity(emailIntent);
            }
        });

        btn_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here i will remove the googel and add the string Web_address

                //  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                if (Web_address == null) {
                    // nothing to do
                    Toast.makeText(Worker_Profile.this, R.string.no_we_address, Toast.LENGTH_SHORT).show();
                } else {
                    if (!Web_address.equals("")) {
                        String web = Web_address.replace("\"", "");
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web));
                        startActivity(browserIntent);
                    } else {
                        Toast.makeText(Worker_Profile.this, R.string.no_we_address, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
// load the image

         imageurl.add(API.Profile_Path+SWM_Pic) ;
         load_img load_img=new load_img();
         load_img.execute("");
//        PicassoClient.downloadImage2(getApplication(),Pic_url,profile_pic);
//        Picasso.get().load(Pic_url)
//                .error(R.drawable.ic_launcher_background)
//                .into(profile_pic);

        callUsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:"+SWM_Phone));
//                if (ActivityCompat.checkSelfPermission(Worker_Profile.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
////                      public void onRequestPermissionsResult(int requestCode, String[] permissions,
////                                                              int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                startActivity(callIntent);
                makePhoneCall();
            }
        });

        WhatsUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smsNumber = SWM_Mobile.replace("\"", "");; //without '+'
//                try {
//                    Intent sendIntent = new Intent("android.intent.action.MAIN");
//                    sendIntent.setAction(Intent.ACTION_SEND);
//                    sendIntent.setType("text/plain");
//                    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//                    // send data to a pariculer contact
//                    sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net");
//                    sendIntent.setPackage("com.whatsapp");
//                    startActivity(sendIntent);
//                } catch(Exception e) {
//                    //Toast.makeText(Contact_Us.this, "Error\n" + e.toString(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(Worker_Profile.this,e.toString(),Toast.LENGTH_LONG).show();
//                }
                try {

                    PackageManager packageManager = Worker_Profile.this.getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
// the phone number must be with the country code
                    String url = "https://api.whatsapp.com/send?phone="+ smsNumber +"&text=" + URLEncoder.encode("This is my text to send.", "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        Worker_Profile.this.startActivity(i);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Intent intent=new Intent(Worker_Profile.this, MapsActivity.class);
startActivity(intent);
            }
        });

//        iv_favoret.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//InsertFavorite insertFavorite=new InsertFavorite();
//insertFavorite.execute("");
//            }
//        });
    }


    public class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
//            Intent intent = new Intent(Worker_Profile.this, Images_Profile.class);
//            intent.putExtra("SWM_ID",SWM_ID);
//            intent.putExtra("is_worker_profile",true);
//            startActivity(intent);
//           finish();
            linearLayout.setVisibility(View.INVISIBLE);
           // ViewPager.LayoutParams params = new ViewPager.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);


            ViewGroup.LayoutParams params =  profile_pic.getLayoutParams();
// Changes the height and width to the specified *pixels*
         //   params.height = 300;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;

            profile_pic.setLayoutParams(params);
           profile_pic.requestLayout();
            return true;
        }

    }

  public class   InsertFavorite extends AsyncTask<String,String,String>{
String z;

      @Override
      protected void onPostExecute(String s) {
          super.onPostExecute(s);
          Toast.makeText(Worker_Profile.this, z, Toast.LENGTH_SHORT).show();
      }

      @Override
      protected String doInBackground(String... strings) {

          ContentValues contentValues=new ContentValues();
          contentValues.put("FSWM_ID",SWM_ID);
          contentValues.put("FSWM_Name",SWM_name);
          contentValues.put("FSWM_Phone",SWM_Phone);
          contentValues.put("FSWM_Mobile",SWM_Mobile);
          contentValues.put("FSWM_Address",SWM_Address);
          contentValues.put("FSWM_Web",Web_address);
          contentValues.put("FSWM_Lat",SWM_Lat);
          contentValues.put("FSWM_Lon",SWM_Lon);
          contentValues.put("FSWM_Pic",SWM_Pic);
          contentValues.put("FSWM_company ",description);


          try {
              long rowInserted = AndPOS_db.insertOrThrow("Service_Worker_Favorite_Table", null, contentValues);
              if (rowInserted != -1) {
                  //Toast.makeText(AddCustomer.this, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                    z = "Added Successfully";
              } else {
                  //  Toast.makeText(AddCustomer.this, "Something wrong", Toast.LENGTH_SHORT).show();
                   z = "Something wrong the record not added ";
              }
          } catch (SQLiteConstraintException e1) {
              //Toast.makeText(AddCustomer.this,  e1.toString(),Toast.LENGTH_LONG).show();
             z = e1.toString();
              // z = "The Customer name  must be unique Please check it";
          }

          return null;
      }
  }

    public class   Insert_Like extends AsyncTask<String,String,String>{
        String z;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(Worker_Profile.this, z, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {

            ContentValues contentValues=new ContentValues();
            contentValues.put("SWM_ID",SWM_ID);
            contentValues.put("Like_",1);
            String Quary="select Like_ID,Like_  from Like_tbl where SWM_ID="+SWM_ID;
            Cursor cursor6=AndPOS_db.rawQuery(Quary,null);
            // that mean that i have a record in the database and i need
            // to update the  like
            if(cursor6.moveToFirst()){
                int like_1=cursor6.getInt(cursor6.getColumnIndex("Like_"));
                int Like_ID=cursor6.getInt(cursor6.getColumnIndex("Like_ID"));
                if (like_1==0){
//                    // then i need to update the like and make it one
//                    String Q= "update Like_tbl set Like_=1 where Like_ID="+Like_ID  ;
//                    Cursor cursor1=AndPOS_db.rawQuery(Q,null);
//                    cursor1.close();
                    ContentValues values = new ContentValues();
                    // the customer Id is auto increament

                    values.put("Like_", 1);




                    //the insert() will give -1 if the statment failed and id number if successfully inserted
                    // it took table name and the value we make a content value to put all the column names in it
                    try {
                        //the update commend will take (1,2,3,4) 1 database name, 2 values , 3 condition ===> where statmente
                        long rowUpdate = AndPOS_db.update("Like_tbl",values,"Like_ID="+Like_ID,null);
                        if (rowUpdate != -1) {
                            //Toast.makeText(AddCustomer.this, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                            z = "Updated Successfully";
                        } else {
                            //  Toast.makeText(AddCustomer.this, "Something wrong", Toast.LENGTH_SHORT).show();
                            z = "Something wrong";
                        }
                    } catch (SQLiteConstraintException e1) {
                        //Toast.makeText(AddCustomer.this,  e1.toString(),Toast.LENGTH_LONG).show();
                        // z = e1.toString();
                        z = "The Payment name  must be unique Please check it";
                    }
                    z="updated ";
                }
            }else {
                try {
                    long rowInserted = AndPOS_db.insertOrThrow("Like_tbl", null, contentValues);
                    if (rowInserted != -1) {
                        //Toast.makeText(AddCustomer.this, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                        z = "Added Successfully";
                    } else {
                        //  Toast.makeText(AddCustomer.this, "Something wrong", Toast.LENGTH_SHORT).show();
                        z = "Something wrong the record not added ";
                    }
                } catch (SQLiteConstraintException e1) {
                    //Toast.makeText(AddCustomer.this,  e1.toString(),Toast.LENGTH_LONG).show();
                    z = e1.toString();
                    // z = "The Customer name  must be unique Please check it";
                }
            }




            return null;
        }
    }

    public class   Insert_DES_Like extends AsyncTask<String,String,String>{
        String z;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(Worker_Profile.this, z, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {

            ContentValues contentValues=new ContentValues();
            contentValues.put("SWM_ID",SWM_ID);
            contentValues.put("Des_like",1);
            String Quary="select Like_ID,Des_Like  from Like_tbl where SWM_ID="+SWM_ID;
            Cursor cursor6=AndPOS_db.rawQuery(Quary,null);
            // that mean that i have a record in the database and i needto update the des like
            if(cursor6.moveToFirst()){
               int Des_like=cursor6.getInt(cursor6.getColumnIndex("Des_like"));
               int Like_ID=cursor6.getInt(cursor6.getColumnIndex("Like_ID"));
               if (Des_like==0){
//                   // then i need to update the deslike and make it one
//                 String Q= "update Like_tbl set Des_Like=1 where Like_ID="+Like_ID  ;
//                   Cursor cursor1=AndPOS_db.rawQuery(Q,null);
//                   cursor1.close();
                   ContentValues values = new ContentValues();
                   // the customer Id is auto increament

                   values.put("Des_like", 1);




                   //the insert() will give -1 if the statment failed and id number if successfully inserted
                   // it took table name and the value we make a content value to put all the column names in it
                   try {
                       //the update commend will take (1,2,3,4) 1 database name, 2 values , 3 condition ===> where statmente
                       long rowUpdate = AndPOS_db.update("Like_tbl",values,"Like_ID="+Like_ID,null);
                       if (rowUpdate != -1) {
                           //Toast.makeText(AddCustomer.this, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                           z = "Updated Successfully";
                       } else {
                           //  Toast.makeText(AddCustomer.this, "Something wrong", Toast.LENGTH_SHORT).show();
                           z = "Something wrong";
                       }
                   } catch (SQLiteConstraintException e1) {
                       //Toast.makeText(AddCustomer.this,  e1.toString(),Toast.LENGTH_LONG).show();
                       // z = e1.toString();
                       z = "The Payment name  must be unique Please check it";
                   }
                   z="updated ";
               }
            }else { // there is no record in the sql data base
                try {
                    long rowInserted = AndPOS_db.insertOrThrow("Like_tbl", null, contentValues);
                    if (rowInserted != -1) {
                        //Toast.makeText(AddCustomer.this, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                        z = "Added Successfully";
                    } else {
                        //  Toast.makeText(AddCustomer.this, "Something wrong", Toast.LENGTH_SHORT).show();
                        z = "Something wrong the record not added ";
                    }
                } catch (SQLiteConstraintException e1) {
                    //Toast.makeText(AddCustomer.this,  e1.toString(),Toast.LENGTH_LONG).show();
                    z = e1.toString();
                    // z = "The Customer name  must be unique Please check it";
                }
            }
cursor6.close();



            return null;
        }
    }

    public class   update_Like extends AsyncTask<String,String,String>{
        String z;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(Worker_Profile.this, z, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {

            ContentValues contentValues=new ContentValues();
            contentValues.put("SWM_ID",SWM_ID);
            contentValues.put("Des_like",1);
            String Quary="select Like_ID,Des_Like  from Like_tbl where SWM_ID="+SWM_ID;
            Cursor cursor6=AndPOS_db.rawQuery(Quary,null);
            // that mean that i have a record in the database and i needto update the des like
            if(cursor6.moveToFirst()){
               int Des_like=cursor6.getInt(cursor6.getColumnIndex("Des_like"));
               int Like_ID=cursor6.getInt(cursor6.getColumnIndex("Like_ID"));
               if (Des_like==0){
                   // then i need to update the deslike and make it one
                 String Q= "update Like_tbl set Des_Like=1 where Like_ID="+Like_ID  ;
                   Cursor cursor1=AndPOS_db.rawQuery(Quary,null);
                   cursor1.close();
               }
            }else { // there is no record in the sql data base
                try {
                    long rowInserted = AndPOS_db.insertOrThrow("Like_tbl", null, contentValues);
                    if (rowInserted != -1) {
                        //Toast.makeText(AddCustomer.this, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                        z = "Added Successfully";
                    } else {
                        //  Toast.makeText(AddCustomer.this, "Something wrong", Toast.LENGTH_SHORT).show();
                        z = "Something wrong the record not added ";
                    }
                } catch (SQLiteConstraintException e1) {
                    //Toast.makeText(AddCustomer.this,  e1.toString(),Toast.LENGTH_LONG).show();
                    z = e1.toString();
                    // z = "The Customer name  must be unique Please check it";
                }
            }
cursor6.close();



            return null;
        }
    }

    class Read_Data_sqlite extends AsyncTask<String,String,String> {




        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           if (like_>0){
               iv_thump_up.setVisibility(View.GONE);
           }
           if (des_like>0){
               iv_thump_down.setVisibility(View.GONE);
           }
        }

        @Override
        protected String doInBackground(String... strings) {
            // i will read from the sqlite and add it to the

            String Q1="Select * from Like_tbl where SWM_ID="+SWM_ID;
            Cursor cursor=AndPOS_db.rawQuery(Q1,null);

            if (cursor.moveToFirst()){
               // that mean we have a recored for that worker
                Like_ID=cursor.getInt(cursor.getColumnIndex("Like_ID"));
                like_=cursor.getInt(cursor.getColumnIndex("Like_"));
                des_like=cursor.getInt(cursor.getColumnIndex("Des_like"));

            }








            return null;


        }
    }

  public class   DeleteFavorite extends AsyncTask<String,String,String>{
String z;

      @Override
      protected void onPostExecute(String s) {
          super.onPostExecute(s);
          Toast.makeText(Worker_Profile.this, z, Toast.LENGTH_SHORT).show();
      }

      @Override
      protected String doInBackground(String... strings) {




          try {
              String where = "FSWM_ID" + "=" + SWM_ID;
             int rowDeleted = AndPOS_db.delete("Service_Worker_Favorite_Table", where, null);
              if (rowDeleted != -1) {
                  //Toast.makeText(AddCustomer.this, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                    z = "Deleted Successfully";
              } else {
                  //  Toast.makeText(AddCustomer.this, "Something wrong", Toast.LENGTH_SHORT).show();
                   z = "Something wrong the record not added ";
              }
          } catch (SQLiteConstraintException e1) {
              //Toast.makeText(AddCustomer.this,  e1.toString(),Toast.LENGTH_LONG).show();
             z = e1.toString();
              // z = "The Customer name  must be unique Please check it";
          }

          return null;
      }
  }

  public class  load_img extends AsyncTask<String ,String ,String >{


      @Override
      protected void onPostExecute(String s) {
          Viewpager_adapter1 viewPagerAdapter=new Viewpager_adapter1(Worker_Profile.this,imageurl);

          profile_pic.setAdapter(viewPagerAdapter);

          super.onPostExecute(s);
      }

      @Override
      protected String doInBackground(String... strings) {
//          private String [] imageurl=new String[]{
//                  API.Ad_Image+"ad1.jpg", API.Ad_Image+"ad2.jpg",
//                  API.Ad_Image+"ad3.jpg", API.Ad_Image+"ad4.jpg", API.Ad_Image+"ad5.jpg"
//          };
          // i need to read the image name and add the image api to it that contain image profile path
          // add them to an array and put that array in the pager

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
          mapdata.put("apicall", "SW_Img");
          mapdata.put("SW_ID", SWM_ID);
          Call<List<Image_list>> call = api.get_Images(mapdata);
          call.enqueue(new Callback<List<Image_list>>() {
              @Override
              public void onResponse(Call<List<Image_list>> call, Response<List<Image_list>> response) {
                  Log.i("Read_Data222222", response.body().toString());
                  //Toast.makeText()
                  if (response.isSuccessful()) {
                      if (response.body() != null) {
                          Log.i("onSuccess", response.body().toString());
                          for(int i=0; i<response.body().size(); i++){
                              //   allItems.add()
                             // Image_list list_data=new Image_list(


                                   //   response.body().get(i).getImage_path()
                                      Pic_url= API.Profile_Path+response.body().get(i).getSWM_IMG_path();

                         //     );

imageurl.add(Pic_url);

                              profile_pic.getAdapter().notifyDataSetChanged();
                          }


                      } else {
                          Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                      }
                  }
              }

              @Override
              public void onFailure(Call<List<Image_list>> call, Throwable t) {
                  Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
              }
          });


          return null;
      }
  }


    public class Update_like_sever extends AsyncTask<String, String, String>{


        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
     int SWM_ID_I=Integer.parseInt(SWM_ID);
            Call<MyResponse1> update_like_recored = RetrofitClient
                    .getInstance()
                    .getApi()
                    .update_Like_Recored(
                           SWM_ID_I

                    );
            update_like_recored.enqueue(new Callback<MyResponse1>() {
                @Override
                public void onResponse(Call<MyResponse1> call, Response<MyResponse1> response1) {
                    assert response1.body() != null;
                    if (response1.body().error>0) {
                        // insert_count=insert_count-1;
                        Toast.makeText(Worker_Profile.this,"Like updated",Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<MyResponse1> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            // }
            //    }
//            if (cursor.moveToLast()) {
//                do {
//
//                    String image_id, image_name, image_path, image_uri;
//
//                    // we use put method to save the data in that map and we use the column name as the key
//                    image_id = cursor.getString(cursor.getColumnIndex("imgae_ID"));
//
//                    image_name = cursor.getString(cursor.getColumnIndex("Image_name"));
//                    image_path = cursor.getString(cursor.getColumnIndex("image_path"));
//                    image_uri = cursor.getString(cursor.getColumnIndex("image_uri"));
//                    sql_uri_IMG = Uri.parse(image_uri);
//                    imageSql_path=image_path;
//
//                    filepath=imageSql_path;
//                    if (!filepath.isEmpty()) {
//
//                        IMG_file = new File(filepath);
//                        requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(sql_uri_IMG)), IMG_file);
//                    }
//                    Call<MyResponse1> create_IMG = RetrofitClient
//                            .getInstance()
//                            .getApi()
//                            .createIMG_Recored(
//                                    ///String.valueOf(server_id1),
//                                    server_id1,
//                                    requestFile
//
//                            );
//                    create_IMG.enqueue(new Callback<MyResponse1>() {
//                        @Override
//                        public void onResponse(Call<MyResponse1> call, Response<MyResponse1> response1) {
//                            assert response1.body() != null;
//                            if (response1.body().error>0) {
//                                insert_count=insert_count-1;
//                                Toast.makeText(My_Profile.this,"Image uploaded",Toast.LENGTH_LONG).show();
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<MyResponse1> call, Throwable t) {
//                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    });
//// here we stop the task when the position is two
//
//if ((insert_count)==1){
//    return null;
//}
//
//                } while (cursor.moveToPosition(insert_count));
//            }

            //   }

            return null;
        }
    }

    public class Update_Dlike_sever extends AsyncTask<String, String, String>{


        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
     int SWM_ID_I=Integer.parseInt(SWM_ID);
            Call<MyResponse1> update_Dlike_recored = RetrofitClient
                    .getInstance()
                    .getApi()
                    .update_DLike_Recored(
                           SWM_ID_I

                    );
            update_Dlike_recored.enqueue(new Callback<MyResponse1>() {
                @Override
                public void onResponse(Call<MyResponse1> call, Response<MyResponse1> response1) {
                    assert response1.body() != null;
                    if (response1.body().error>0) {
                        // insert_count=insert_count-1;
                        Toast.makeText(Worker_Profile.this,"UnLike updated",Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<MyResponse1> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            // }
            //    }
//            if (cursor.moveToLast()) {
//                do {
//
//                    String image_id, image_name, image_path, image_uri;
//
//                    // we use put method to save the data in that map and we use the column name as the key
//                    image_id = cursor.getString(cursor.getColumnIndex("imgae_ID"));
//
//                    image_name = cursor.getString(cursor.getColumnIndex("Image_name"));
//                    image_path = cursor.getString(cursor.getColumnIndex("image_path"));
//                    image_uri = cursor.getString(cursor.getColumnIndex("image_uri"));
//                    sql_uri_IMG = Uri.parse(image_uri);
//                    imageSql_path=image_path;
//
//                    filepath=imageSql_path;
//                    if (!filepath.isEmpty()) {
//
//                        IMG_file = new File(filepath);
//                        requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(sql_uri_IMG)), IMG_file);
//                    }
//                    Call<MyResponse1> create_IMG = RetrofitClient
//                            .getInstance()
//                            .getApi()
//                            .createIMG_Recored(
//                                    ///String.valueOf(server_id1),
//                                    server_id1,
//                                    requestFile
//
//                            );
//                    create_IMG.enqueue(new Callback<MyResponse1>() {
//                        @Override
//                        public void onResponse(Call<MyResponse1> call, Response<MyResponse1> response1) {
//                            assert response1.body() != null;
//                            if (response1.body().error>0) {
//                                insert_count=insert_count-1;
//                                Toast.makeText(My_Profile.this,"Image uploaded",Toast.LENGTH_LONG).show();
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<MyResponse1> call, Throwable t) {
//                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    });
//// here we stop the task when the position is two
//
//if ((insert_count)==1){
//    return null;
//}
//
//                } while (cursor.moveToPosition(insert_count));
//            }

            //   }

            return null;
        }
    }

    private void makePhoneCall() {
       String number="tel:00"+SWM_Phone;
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(Worker_Profile.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Worker_Profile.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                //String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(number)));
            }
        } else {
            Toast.makeText(Worker_Profile.this, "No Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void GetData(){

        TO =" this is our company email to be added " ;
        SUBJECT = "check up this company  ";
        MESSAGE ="company name address and every thing ";

    }
    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            Worker_Profile.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (profile_pic.getCurrentItem() < imageurl.size() - 1) {
                        profile_pic.setCurrentItem(profile_pic.getCurrentItem() + 1);
                    } else {
                        profile_pic.setCurrentItem(0);
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
//        if (from_favoret==true){
//            getMenuInflater().inflate(R.menu.menu1,menu);
//        }else {
            getMenuInflater().inflate(R.menu.menu2,menu);
//        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()) {

            case R.id.action_back:
//
//if (flag_f){
////    InsertFavorite insertFavorite=new InsertFavorite();
////insertFavorite.execute("");
//}
                if (from_favoret==true){
                    Intent intent=new Intent(Worker_Profile.this,My_Favorite_list.class);
                    startActivity(intent);
                }
//                else {
//                    Intent intent=new Intent(Worker_Profile.this, Order_list_chiled.class);
//                    startActivity(intent);
//                }

                if (flag_d==true) {
//                    if (Like_ID == 0) {
                    Insert_DES_Like insert_des_like = new Insert_DES_Like();
                    insert_des_like.execute();
                    Update_Dlike_sever update_dlike_sever=new Update_Dlike_sever();
                    update_dlike_sever.execute();

                }
                    // for the like
                    if (flag_l) {
//                        if (Like_ID == 0) {
                            Insert_Like insert_like = new Insert_Like();
                            insert_like.execute();
                            // we update the like on the server
                        Update_like_sever update_like_sever=new Update_like_sever();
                        update_like_sever.execute();

                    }

                finish();
                return true;


            case R.id.action_Home:
//                if (flag_f){
////                    InsertFavorite insertFavorite=new InsertFavorite();
////                    insertFavorite.execute("");
//                }

                    Intent intent=new Intent(Worker_Profile.this, Main_Activity.class);
                    startActivity(intent);




                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
