package com.smartsoftwaresolutions.ifix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.DynamicsProcessing;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.smartsoftwaresolutions.ifix.Database.DataHelper;
import com.smartsoftwaresolutions.ifix.Images.Images_Profile;
import com.smartsoftwaresolutions.ifix.Order_Service.order_one_master;
import com.smartsoftwaresolutions.ifix.Pager.ViewPagerAdapter;
import com.smartsoftwaresolutions.ifix.Read_Data.API;
import com.smartsoftwaresolutions.ifix.Read_Data.MyResponse;
import com.smartsoftwaresolutions.ifix.Read_Data.RetrofitClient;
import com.smartsoftwaresolutions.ifix.Select_Main_category_Service_Menu.Select_Main_Menu;
import com.smartsoftwaresolutions.ifix.Sub_country_spinner.Sub_country_spinner;
import com.smartsoftwaresolutions.ifix.Sub_country_spinner.Sub_country_spinner_adapter;
import com.smartsoftwaresolutions.ifix.country_spinner.Country;
import com.smartsoftwaresolutions.ifix.country_spinner.country_spinner;
import com.smartsoftwaresolutions.ifix.country_spinner.spinnerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class Register_a_service_form_two extends AppCompatActivity {
    public static final String PREFS_NAME = "ifix_data";
    // LogCat tag
    private static final String TAG = Register_a_service_form_two.class.getSimpleName();
    EditText et_name,et_mobile,et_phone,et_email,et_address,et_company,et_webadress;
TextView tv_lon,tv_lat,tv_contry_code;
//ImageView image_profile;
    LocationRequest mLocationRequest;
Button btn_select,btn_submit,btn_map,btn_photo;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    SharedPreferences SpType;
String username,userpassword;
    DataHelper AndPOS_dbh;
    SQLiteDatabase AndPOS_db;

    String full_name,adver_Sub_country_name,adver_country_name;
    String phone;
    String mobile;
    String address;
    String  Lan;
    String lon;
    String filepath;
    String company;
 String webAdress;
 String email;
    File file;
    Uri uri;
    RequestBody requestFile;
    String country_id;
    String lang;
    ViewPager img_ad;
    private String [] imageurl=new String[]{
            API.Ad_Image+"ad1.jpg", API.Ad_Image+"ad2.jpg",
            API.Ad_Image+"ad3.jpg", API.Ad_Image+"ad4.jpg", API.Ad_Image+"ad5.jpg"
    };
    private String Document_img1="";
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            tv_lat.setText(mLastLocation.getLatitude()+"");
            tv_lon.setText(mLastLocation.getLongitude()+"");
        }
    };
    Spinner sp_country ,sp_sub_country1;
    List<country_spinner> LabelList;
    List<Sub_country_spinner> SLabelList;
    spinnerAdapter SADA;
  Sub_country_spinner_adapter SSADA;
  int Cposition,CSposition;
    private int Sub_Cpotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer);
        // we create an instance of data base helper  database this will create the database
        AndPOS_dbh = new DataHelper(getApplicationContext());
        // and we connect the data to the data base helper and specify its use as a writable
        AndPOS_db = AndPOS_dbh.getWritableDatabase();
        SpType=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        /***read from sharedpreferance**/
        username=SpType.getString("username","");
        userpassword=SpType.getString("userpassword","");
        // Reading from SharedPreferences
        lang = SpType.getString("lang", "en_US");

        et_name=findViewById(R.id.et_name);
        et_address=findViewById(R.id.et_address);
        et_email=findViewById(R.id.et_email);
        et_mobile=findViewById(R.id.et_mobile);
        et_phone=findViewById(R.id.et_phone);
        et_company=findViewById(R.id.et_company);
       et_webadress=findViewById(R.id.et_webadress);
        btn_map=findViewById(R.id.btn_map);
        tv_lat=findViewById(R.id.tv_lat);
        tv_lon=findViewById(R.id.tv_lon);
        btn_select=findViewById(R.id.btn_select);
        btn_submit=findViewById(R.id.btn_submit);
        btn_photo=findViewById(R.id.btn_photo);
        tv_contry_code=findViewById(R.id.tv_contry_code);
      //  image_profile=findViewById(R.id.image_profile);


        /**Spuinner for contry **/
        sp_country=findViewById(R.id.sp_country1);
        sp_sub_country1=findViewById(R.id.sp_sub_country1);

        ini_data();

btn_photo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
//        Intent i=new Intent(Register_a_service_form_two.this, Images_Profile.class);
//        startActivity(i);
    }
});

//        image_profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////Intent i =new Intent(Register_a_service_form_two.this, Images_Profile.class);
////startActivity(i);
//                show_Alert_Dialog();
//            }
//        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     upload_data();

            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
btn_map.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        getLastLocation();
    }
});

btn_select.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        /** when the user select continue */
        check_data();


//
    }
});
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        img_ad=findViewById(R.id.vp_ad2);
        //   indicator=(TabLayout)findViewById(R.id.indicator);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this,imageurl);
        img_ad.setAdapter(viewPagerAdapter);
        Cposition=SpType.getInt("Cposition",0);
        CSposition=SpType.getInt("CSposition",0);


//        if (SpType.getInt("Cposition",999999)==999999){
//            Cposition=0;
//        }else {
//            Cposition=SpType.getInt("Cposition",999999);
//
//        }
//        if (SpType.getInt("Sub_position",999999)==999999){
//            Sub_Cpotion=0;
//        }else {
//            Sub_Cpotion=SpType.getInt("Sub_position",999999);
//
//        }

        LoadSpinnerCountry loadSpinnerCountry=new LoadSpinnerCountry();
        loadSpinnerCountry.execute();
        /** we read this befor the sub contry begin load**/


        sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadSpinnerSubCountry loadSpinnerSubCountry=new LoadSpinnerSubCountry();
                loadSpinnerSubCountry.execute("");
                country_spinner SelectedType;
                // On selecting a spinner item
                Cposition=position;
                SelectedType=SADA.getItem(position);
                SharedPreferences.Editor editor = SpType.edit();
                editor.putInt("Cposition",position);
                editor.commit();
                tv_contry_code.setText(SelectedType.getCountry_code());
                //Label_UserType = parent.getItemAtPosition(position).toString();
                String s = SelectedType.getCountry_name();

                editor.putString("country_ID",SelectedType.getCountry_id());
                if (lang.equals("ar")){
                    editor.putString("adver_country_name",SelectedType.getCountry_name_ar());
                    adver_country_name=SelectedType.getCountry_name_ar();
                }else {
                    editor.putString("adver_country_name",SelectedType.getCountry_name());
                    adver_country_name=SelectedType.getCountry_name();
                }

                editor.commit();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "You selected: " + s,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        sp_sub_country1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Sub_country_spinner SelectedType;
                // On selecting a spinner item
                SelectedType= (Sub_country_spinner) SSADA.getItem(position);
                //Label_UserType = parent.getItemAtPosition(position).toString();
                String s = SelectedType.getCS_Name();
Sub_Cpotion=position;
                SharedPreferences.Editor editor = SpType.edit();
                editor.putString("Sub_country_ID",SelectedType.getCS_ID());
                editor.putInt("CSposition",position);
                if (lang.equals("ar")){
                    editor.putString("adver_Sub_country_name",SelectedType.getCS_name_ar());
                    adver_Sub_country_name=SelectedType.getCS_name_ar();
                }else {
                    editor.putString("adver_Sub_country_name",SelectedType.getCS_Name());
                    adver_Sub_country_name=SelectedType.getCS_Name();
                }
                editor.putInt("Sub_postion",position);
                editor.commit();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "You selected: " + s,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void ini_data(){
       et_company.setText(SpType.getString("company_name",""));
       et_webadress.setText(SpType.getString("web_address",""));
       et_email.setText(SpType.getString("email",""));
       et_mobile.setText(SpType.getString("mobile",""));
       et_address.setText(SpType.getString("address",""));
     tv_lat.setText(SpType.getString("lan",""));
     tv_lon.setText(SpType.getString("lon",""));
    }

   public void check_data(){
       full_name=et_name.getText().toString().trim();
       phone=et_phone.getText().toString().trim();
       mobile=et_mobile.getText().toString().trim();
       company=et_company.getText().toString().trim();
       webAdress=et_webadress.getText().toString().trim();
       address=et_address.getText().toString().trim();
       Lan=tv_lat.getText().toString().trim();
       lon=tv_lon.getText().toString().trim();
       email=et_email.getText().toString().trim();

//       if (full_name.isEmpty()){
//           et_name.setError("Name is required ");
//           et_name.requestFocus();
//           return;
//       }
       SharedPreferences.Editor editor = SpType.edit();
       Cposition=SpType.getInt("Cposition",0);
       if (Cposition==0){
           show_Alert();
           return;
       }


       editor.commit();
       if (company.isEmpty()){
           et_company.setError(getString(R.string.company_name_required));
           et_company.requestFocus();
           return;
       }else {


           editor.putString("company_name",company);
           editor.commit();
       }

//       if (phone.isEmpty()){
//           et_phone.setError("Phone is required ");
//           et_phone.requestFocus();
//           return;
//       }
       if (mobile.isEmpty()){
           et_mobile.setError(getString(R.string.mobile_name_required));
           et_mobile.requestFocus();
           return;
       }else {

//mobile=tv_contry_code.getText()+mobile; because when i upload iwill have a chance to change the country code
           editor.putString("mobile",mobile);
           editor.commit();
       }
       if (address.isEmpty()){
           et_address.setError(getString(R.string.address_required));
           et_address.requestFocus();
           return;
       }else {
        //   editor.putString("address",adver_country_name+"-"+adver_Sub_country_name+"-"+address);
           editor.putString("address",address);
           editor.commit();
       }
       if (email.isEmpty()){
           et_email.setError(getString(R.string.address_required));
           et_email.requestFocus();
           return;
       }else {
           editor.putString("email",email);
           editor.commit();
       }
      // what
       if (Lan.isEmpty()){
           tv_lat.setError(getString(R.string.longitude_required));
           tv_lat.requestFocus();
           return;
       }else {
           editor.putString("lan",Lan);
           editor.commit();
       }
       if (lon.isEmpty()){
           tv_lon.setError(getString(R.string.longitude_required));
           tv_lon.requestFocus();
           return;
       }else {
           editor.putString("lon",lon);
           editor.commit();
       }

       webAdress = "http://" + webAdress;
       editor.putString("web_address",webAdress);
       editor.commit();

       if (!company.isEmpty() &!mobile.isEmpty()&!address.isEmpty()&!Lan.isEmpty()&!lon.isEmpty()){
           Intent i =new Intent(Register_a_service_form_two.this, Select_Main_Menu.class);
       startActivity(i);
       finish();
       }else {
           return;
       }
/** may be some user don t have a web address so it is not oblegatory to add them*/
//       if (!webAdress.startsWith("http://") && !webAdress.startsWith("https://")){
//           et_webadress.setError(getString(R.string.http));
//           return;
//       }else {
//           editor.putString("web_address",webAdress);
//           editor.commit();
//       }

     //  Toast.makeText(Register_a_service_form_two.this, webAdress, Toast.LENGTH_SHORT).show();
   }

    public class LoadSpinnerCountry extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SADA=new spinnerAdapter(LabelList, Register_a_service_form_two.this);
            // put them in the list view


          // attaching data adapter to spinner
            sp_country.setAdapter(SADA);
            //sp_country.setSelection(Cposition);
            // put the selected position in the contry spinner
            if (Cposition!=0){
                sp_country.setSelection(Cposition);
            }else {
                sp_country.setSelection(0);
            }
            SADA.notifyDataSetChanged();
            LoadSpinnerSubCountry loadSpinnerSubCountry=new LoadSpinnerSubCountry();
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
                    String country_name,country_name_ar,country_code;

                    // we use put method to save the data in that map and we use the column name as the key
                    country_id= cursor2.getString(cursor2.getColumnIndex("Country_ID"));
                    country_name=cursor2.getString(cursor2.getColumnIndex("Country_name"));
                    country_name_ar=cursor2.getString(cursor2.getColumnIndex("Country_name_ar"));
                    country_code=cursor2.getString(cursor2.getColumnIndex("Country_code"));
// we fill the spiner with the info
if (!lang.equals("en_US")){
    // this is arabic
    country_name=cursor2.getString(cursor2.getColumnIndex("Country_name_ar"));
    country_name_ar=cursor2.getString(cursor2.getColumnIndex("Country_name_ar"));
}else {
    // this is english
    country_name=cursor2.getString(cursor2.getColumnIndex("Country_name"));
    country_name_ar=cursor2.getString(cursor2.getColumnIndex("Country_name"));
}
                    country_spinner country_Info=new country_spinner(country_id,country_name,country_name_ar,country_code);




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

    public class LoadSpinnerSubCountry extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Creating adapter for spinner
      /*  ArrayAdapter<spinner> dataAdapter = new ArrayAdapter<spinner>(this,
                android.R.layout.simple_spinner_item, LabelList);*/
            SSADA=new Sub_country_spinner_adapter(SLabelList, Register_a_service_form_two.this);
            // put them in the list view


            // Drop down layout style - list view with radio button

            // attaching data adapter to spinner
            sp_sub_country1.setAdapter(SSADA);
            CSposition= SpType.getInt("CSposition",0);
            if (CSposition!=0){
                // dont set a first selection
                sp_sub_country1.setSelection(CSposition);
            }else {
sp_sub_country1.setSelection(0);
//                if (CSposition!=0){
//
//                }
            }
//            if (CSposition!=0){
//                sp_sub_country1.setSelection(CSposition);
//            }
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
         SelectedType=SADA.getItem(Cposition);
            //Label_UserType = parent.getItemAtPosition(position).toString();
            String s = SelectedType.getCountry_id();
            // Select Quary for a spicific category_spinner example bedroom the cat id is from put extra
            String selectQuery = "SELECT * FROM country_sub_tbl where Country_ID="+s;
            //sqlite will returen a cursor a pointer to the first data
            Cursor cursor2 = AndPOS_db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list the result is kids clasic and modern
            if (cursor2.moveToFirst()) {
                do {

                    String Scountry_id,Scountry_name,Scountry_name_ar,country_id;

                    // we use put method to save the data in that map and we use the column name as the key
                    Scountry_id= cursor2.getString(cursor2.getColumnIndex("CS_ID"));
                    Scountry_name=cursor2.getString(cursor2.getColumnIndex("CS_name"));
                    Scountry_name_ar=cursor2.getString(cursor2.getColumnIndex("CS_name_ar"));
                    country_id=cursor2.getString(cursor2.getColumnIndex("Country_ID"));

// we fill the spiner with the info
                    if (!lang.equals("en_US")){ // arabic
                        Scountry_name=cursor2.getString(cursor2.getColumnIndex("CS_name_ar"));
                        Scountry_name_ar=cursor2.getString(cursor2.getColumnIndex("CS_name_ar"));
                    }else {
                        Scountry_name=cursor2.getString(cursor2.getColumnIndex("CS_name"));
                        Scountry_name_ar=cursor2.getString(cursor2.getColumnIndex("CS_name"));
                    }
                    Sub_country_spinner country_Info=new Sub_country_spinner(Scountry_id,Scountry_name,Scountry_name_ar,country_id);




                    // we add the saved map to the list one by one
                   SLabelList.add(country_Info);

                    //  LabelList.add(cursor2.getString(cursor2.getColumnIndex("User_Type_Name")));
                } while (cursor2.moveToNext());
            }

            cursor2.close();


            return null;
        }
    }
    public void show_Alert(){
        new AlertDialog.Builder(this)
                .setTitle("Please Select your country ")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

//    private void show_Alert_Dialog() {
//        final Context context=Register_a_service_form_two.this;
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog, null);
//        builder.setView(customLayout);
//        builder.setTitle("Title");
//        ImageButton btn_camera=customLayout.findViewById(R.id.btn_camera);
//        ImageButton btn_gallary=customLayout.findViewById(R.id.btn_gallary);
///**take image from the gallary**/
//        btn_gallary.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 2);
//            }
//        });
////        btn_cancel.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
//////                new DialogInterface.OnClickListener() {
//////                   public void onClick(DialogInterface dialog, int which) {
//////                       dialog.dismiss();
//////
//////                    }
////           //    };
////
////            }
////        });
//        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() { // define the 'Cancel' button
//            public void onClick(DialogInterface dialog, int which) {
//                //Either of the following two lines should work.
//                dialog.cancel();
//                //dialog.dismiss();
//            }
//        });
//        btn_camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//              uri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//                /** creatr a temp file for the first image**/
//               //File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                //intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                startActivityForResult(intent, 1);
//            }
//        });
//
////                new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int which) {
////                        // The 'which' argument contains the index position
////                        // of the selected item
////                        switch (which) {
////                            case R.id.btn_camera:
////                                Toast.makeText(context, "Camera clicked", Toast.LENGTH_LONG).show();
////                                break;
////                            case R.id.btn_cancel1:
////                                Toast.makeText(context, "Cancel", Toast.LENGTH_LONG).show();
////                                break;
////                            case R.id.btn_gallary:
////                                Toast.makeText(context, "Gallary", Toast.LENGTH_LONG).show();
////                                break;
////
////                        }
////                    }
////                };
//        builder.create().show();
//    }
    /**
     * returning image / video
     */
//    private static File getOutputMediaFile(int type) {
//
//        // External sdcard location
//        File mediaStorageDir = new File(
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                Config.IMAGE_DIRECTORY_NAME);
//
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.d(TAG, "Oops! Failed create "
//                        + Config.IMAGE_DIRECTORY_NAME + " directory");
//                return null;
//            }
//        }
//
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
//                Locale.getDefault()).format(new Date());
//        File mediaFile;
//        if (type == MEDIA_TYPE_IMAGE) {
////            mediaFile = new File(mediaStorageDir.getPath() + File.separator
////                    + "IMG_" + timeStamp + ".jpg");
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "IMG_" + timeStamp + ".jpg");
//
//        }else {
//            return null;
//        }
////        } else if (type == MEDIA_TYPE_VIDEO) {
////            mediaFile = new File(mediaStorageDir.getPath() + File.separator
////                    + "VID_" + timeStamp + ".mp4");
////        }
//
//        return mediaFile;
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//       // uri = data.getData();
//        if (resultCode == RESULT_OK) {
//            /** from the camera **/
//            if (requestCode == 1) {
//
//                File f = new File(Environment.getExternalStorageDirectory().toString());
//                for (File temp : f.listFiles()) {
//                    if (temp.getName().equals("temp.jpg")) {
//                        f = temp;
//                        break;
//                    }
//                }
//                try {
//                    /** show the image in the image view**/
//filepath=uri.getPath();
//                    Bitmap bitmap;
//                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//                //    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//                    bitmap = BitmapFactory.decodeFile(filepath, bitmapOptions);
//                    bitmap=getResizedBitmap(bitmap, 400);
//                    image_profile.setImageBitmap(bitmap);
//                    BitMapToString(bitmap);
/////uri=data.getData();
//               //  uri=getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//                  //  filepath=uri.getPath();
//
//                    String[] filePath = { MediaStore.Images.Media.DATA };
//                    Cursor c = getContentResolver().query(uri,filePath, null, null, null);
//                    if (c != null) {
//                        c.moveToFirst();
//                        int columnIndex = c.getColumnIndex(filePath[0]);
//                        //  String picturePath = c.getString(columnIndex);
//                        filepath = c.getString(columnIndex);
//                        c.close();
//
//                    }
//
//
//                    f.delete();
//
////                    String[] filePath = { MediaStore.Images.Media.DATA };
////                    Cursor c = getContentResolver().query(uri,filePath, null, null, null);
////                    c.moveToFirst();
////                    int columnIndex = c.getColumnIndex(filePath[0]);
//                    //  String picturePath = c.getString(columnIndex);
////                    filepath = c.getString(columnIndex);
////                    c.close();
////                    String path = android.os.Environment
////                            .getExternalStorageDirectory()
////                            + File.separator
////                            + "Phoenix" + File.separator + "default";
////                    f.delete();
////                    filepath = android.os.Environment
////                            .getExternalStorageDirectory()
////                            + File.separator
////                            + "Phoenix" + File.separator + "default";
////                    f.delete();
////
////                    OutputStream outFile = null;
////                    /** we remove the temp file and create a new file name**/
////                    File file = new File(filepath, String.valueOf(System.currentTimeMillis()) + ".jpg");
////               //     uri=Uri.fromFile(file);
////                    try {
////                        outFile = new FileOutputStream(file);
////                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
////                        outFile.flush();
////                        outFile.close();
////                    } catch (FileNotFoundException e) {
////                        e.printStackTrace();
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            } else if (requestCode == 2) { // from galary
//               // Uri selectedImage = data.getData();
//               uri = data.getData();
//                String[] filePath = { MediaStore.Images.Media.DATA };
//                Cursor c = getContentResolver().query(uri,filePath, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePath[0]);
//              //  String picturePath = c.getString(columnIndex);
//                filepath = c.getString(columnIndex);
//                c.close();
//                Bitmap thumbnail = (BitmapFactory.decodeFile(filepath));
//                thumbnail=getResizedBitmap(thumbnail, 400);
//                Log.w("path of image from", filepath+"");
//                image_profile.setImageBitmap(thumbnail);
//                BitMapToString(thumbnail);
//            }
//        }
//    }
//
//    public String BitMapToString(Bitmap userImage1) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
//        byte[] b = baos.toByteArray();
//        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
//        return Document_img1;
//    }
//
//    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
//        int width = image.getWidth();
//        int height = image.getHeight();
//
//        float bitmapRatio = (float)width / (float) height;
//        if (bitmapRatio > 1) {
//            width = maxSize;
//            height = (int) (width / bitmapRatio);
//        } else {
//            height = maxSize;
//            width = (int) (height * bitmapRatio);
//        }
//        return Bitmap.createScaledBitmap(image, width, height, true);
//    }

    /**
     * Creating file uri to store image/video
     */
//    public Uri getOutputMediaFileUri(int type) {
//        return Uri.fromFile(getOutputMediaFile(type));
//
//    }

//    private void upload_data() {
//        full_name=et_name.getText().toString().trim();
//         phone=et_phone.getText().toString().trim();
//         mobile=et_mobile.getText().toString().trim();
//         company=et_company.getText().toString().trim();
//         webAdress=et_webadress.getText().toString().trim();
//         address=et_address.getText().toString().trim();
//          Lan=tv_lat.getText().toString().trim();
//         lon=tv_lon.getText().toString().trim();
//
//        if (full_name.isEmpty()){
//            et_name.setError("Name is required ");
//            et_name.requestFocus();
//            return;
//        }
//        if (company.isEmpty()){
//            et_company.setError("Name is required ");
//            et_company.requestFocus();
//            return;
//        }
//
//        if (phone.isEmpty()){
//            et_phone.setError("Phone is required ");
//            et_phone.requestFocus();
//            return;
//        }
//        if (mobile.isEmpty()){
//            et_mobile.setError("Mobile is required ");
//            et_mobile.requestFocus();
//            return;
//        }
//        if (address.isEmpty()){
//            et_address.setError("address is required ");
//            et_address.requestFocus();
//            return;
//        }
//        if (Lan.isEmpty()){
//            tv_lat.setError("Please add the longitude");
//            tv_lat.requestFocus();
//            return;
//        }
//        if (lon.isEmpty()){
//            tv_lon.setError("Please add the longitude");
//            tv_lon.requestFocus();
//            return;
//        }
//
//        if (!filepath.isEmpty()){
////            filepath="empty path";
//            file = new File(filepath);
//           requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file);
//        }
////        }else{
////
////
////        }
//
////Insert_Register insert_register=new Insert_Register();
////        insert_register.execute();
//        /**    do insert to the web database */
//
//
//        Call<MyResponse> create_SWM = RetrofitClient
//                .getInstance()
//                .getApi()
//                .createSWM_Recored(
//                        full_name,
//                        phone,
//                        mobile,
//                        address,
//                        Lan,
//                        lon,
//                        requestFile,
//                        username,
//                        userpassword,
//                        "0",
//                        company,
//                        webAdress,
//                        0,
//                        0,
//                        "",
//                        0
//
//                        );
//create_SWM.enqueue(new Callback<MyResponse>() {
//    @Override
//    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//        String s= String.valueOf(response.body());
//
//
//        if (!response.body().error) {
//            Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    public void onFailure(Call<MyResponse> call, Throwable t) {
//        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
//
//    }
//});
//
//    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    tv_lat.setText(location.getLatitude()+"");
                                    tv_lon.setText(location.getLongitude()+"");
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        mLocationRequest =  LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Granted. Start getting the location information
            }
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
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
                Intent intent=new Intent(Register_a_service_form_two.this, Main_Activity.class);
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
            Register_a_service_form_two.this.runOnUiThread(new Runnable() {
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

//    public class Insert_Register extends AsyncTask<String,String,String> {
//
//        String z = "";
//        Boolean isSuccess = false;
//
//        //we read the username and the password from were the user enter them
////        String userName = et_Username.getText().toString();
////        String password =et_Password.getText().toString();
//
//
//        @Override
//        protected void onPreExecute() {
//            // we make the prograsbar is visible
//           // progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected void onPostExecute(String r) {
//            // at the end of the verfication the prograssbar is of
//            //progressBar.setVisibility(View.GONE);
//            //r is the result if sucsess or not
//            //if we find the user in the database or not and we open the program
//            Toast.makeText(Register_a_service_form_two.this,r,Toast.LENGTH_SHORT).show();
//
//            if(isSuccess) {
//                Intent i=new Intent(Register_a_service_form_two.this, Register_a_service_form_two.class);
//                startActivity(i);
//
//
//            }
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            //the tirm mean that without space
//
//
//
//
//                ContentValues values = new ContentValues();
//
//
//                values.put("SWM_Name",full_name);
//                values.put("SWM_Phone", phone);
//                values.put("SWM_Mobile",mobile);
//                values.put("SWM_Address",address);
//                values.put("SWM_Lat",Lan);
//                values.put("SWM_Lon",lon);
//                values.put("SWM_Pic",filepath);
//                values.put("SWM_company",company);
//                values.put("SWM_POBOX",webAdress);
//
//                //the insert() will give -1 if the statment failed and id number if successfully inserted
//                // it took table name and the value we make a content value to put all the column names in it
//                try {
//                    long rowInserted = AndPOS_db.insertOrThrow("Service_Worker_Master_Table", null, values);
//                    if (rowInserted != -1) {
//                        //Toast.makeText(AddCustomer.this, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
//                        z = "username and password saved successfully";
//
//                        isSuccess=true;
//                    } else {
//                        //  Toast.makeText(AddCustomer.this, "Something wrong", Toast.LENGTH_SHORT).show();
//                        z = "Something wrong";
//                    }
//                } catch (SQLiteConstraintException e1) {
//                    //Toast.makeText(AddCustomer.this,  e1.toString(),Toast.LENGTH_LONG).show();
//                    z = e1.toString();
//                    // z = "The Item name  must be unique Please check it";
//                }
//
//
//
//
//            return z;
//        }
//    }
}
