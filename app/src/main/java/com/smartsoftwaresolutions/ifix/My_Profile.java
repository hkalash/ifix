package com.smartsoftwaresolutions.ifix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
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
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.smartsoftwaresolutions.ifix.Images.Image_Adapter;
import com.smartsoftwaresolutions.ifix.Images.Images_Profile;
import com.smartsoftwaresolutions.ifix.Images.Itemlist_image;
import com.smartsoftwaresolutions.ifix.Order_Service.order_one_master;
import com.smartsoftwaresolutions.ifix.Pager.ViewPagerAdapter;
import com.smartsoftwaresolutions.ifix.Read_Data.MyResponse;
import com.smartsoftwaresolutions.ifix.Read_Data.MyResponse1;
import com.smartsoftwaresolutions.ifix.Read_Data.RetrofitClient;
import com.smartsoftwaresolutions.ifix.Select_Main_category_Service_Menu.Select_Main_Menu;
import com.smartsoftwaresolutions.ifix.Select_Sub_Category_Service_Menu.Select_Sub_Menu;
import com.smartsoftwaresolutions.ifix.Sub_country_spinner.Sub_country_spinner;
import com.smartsoftwaresolutions.ifix.Sub_country_spinner.Sub_country_spinner_adapter;
import com.smartsoftwaresolutions.ifix.country_spinner.country_spinner;
import com.smartsoftwaresolutions.ifix.country_spinner.spinnerAdapter;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class My_Profile extends AppCompatActivity {
    public static final String PREFS_NAME = "ifix_data";
    SharedPreferences SpType;
    String et_name1, et_phone2, et_address1, et_email1, et_Password1, et_username1, et_answer1, et_dis1, M_typ;
    EditText et_name12, et_phone12, et_address12, et_email12, et_Password12, et_username12, et_answer12, et_dis12,et_wed_address;
    TextView et_lan1, et_lon1,tv_country_code;
    RadioButton R_gold, R_free, R_VIP;
    RadioGroup RGroup;
    Spinner sp_country12, sp_sub_country12,sp_web;
    List<country_spinner> LabelList;
    List<Sub_country_spinner> SLabelList;
    spinnerAdapter SADA;
    Sub_country_spinner_adapter SSADA;
    int Cposition,CSposition;
    private int Sub_Cpotion;
    DataHelper AndPOS_dbh;
    SQLiteDatabase AndPOS_db;
    String country_id;
    Button btn_selected_service, btn_done3,btn_map1,btn_add_g;
    ViewPager vp_myprofile;
    ArrayList<Itemlist_image> imageList;
    Itemlist_image itemlist_image;
    int max_image_number;
    Image_Adapter ADA;
    String[] imageurl;
    String lang, filepath;
    File file;
    File IMG_file;
    Uri sql_uri,sql_uri_IMG;
    RequestBody requestFile ;
    String imageSql_path;
    boolean flag_server_id;
    Context context;
    int PERMISSION_ID = 44;
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationClient;
   String cust_Type;
   ProgressBar progressBar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__profile);
        btn_add_g=findViewById(R.id.btn_add_g);

context=My_Profile.this;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        // we create an instance of data base helper  database this will create the database
        AndPOS_dbh = new DataHelper(getApplicationContext());
        // and we connect the data to the data base helper and specify its use as a writable
        AndPOS_db = AndPOS_dbh.getWritableDatabase();
        /**Spuinner for contry **/
        sp_country12 = findViewById(R.id.sp_country12);
        sp_sub_country12 = findViewById(R.id.sp_sub_country12);
        RGroup=findViewById(R.id.rg1);
       // sp_web=findViewById(R.id.sp_web);
progressBar1=findViewById(R.id.progressBar1);
progressBar1.setVisibility(View.GONE);

        SpType = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Cposition=SpType.getInt("Cposition",0);
        CSposition=SpType.getInt("CSposition",0);
cust_Type=SpType.getString("M_type","3");
if (cust_Type.equals("2")){
    btn_add_g.setVisibility(View.VISIBLE);
}else {
    btn_add_g.setVisibility(View.GONE);
}

btn_add_g.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // we will read the server id and send it to the admin

//        String to = "hkhkalash@gmail.com";
//        String subject= "Image Attachment this is my server ID    "+SpType.getInt("Server_ID",0);
//        String body="Please find bellow the image that will be added to gallery ";
//        String mailTo = "mailto:" + to +
//                "?&subject=" + Uri.encode(subject) +
//                "&body=" + Uri.encode(body);
//        Intent emailIntent = new Intent(Intent.ACTION_VIEW);
//        emailIntent.setData(Uri.parse(mailTo));
//        startActivity(emailIntent);
        Intent i =new Intent(My_Profile.this,Contact_Us.class);
        startActivity(i);
    }
});

        et_address12 = findViewById(R.id.et_address1);

        btn_map1=findViewById(R.id.btn_map1);
        et_name12 = findViewById(R.id.et_name1);
        et_phone12 = findViewById(R.id.et_phone2);
        et_email12 = findViewById(R.id.et_email1);
        et_Password12 = findViewById(R.id.et_Password1);
        et_username12 = findViewById(R.id.et_username1);
        et_answer12 = findViewById(R.id.et_answer1);
        et_dis12 = findViewById(R.id.et_dis1);
        R_VIP = findViewById(R.id.vip1);
        R_gold = findViewById(R.id.gold1);
        R_free = findViewById(R.id.free1);
        et_lan1 = findViewById(R.id.et_lan1);
        et_lon1 = findViewById(R.id.et_lon1);
        vp_myprofile = findViewById(R.id.vp_myprofile);
        btn_done3 = findViewById(R.id.btn_done3);
        et_wed_address=findViewById(R.id.et_wed_address);
        tv_country_code=findViewById(R.id.tv_country_code);

        // check the server id on the sharedpreferance if it is equal to zero ornot to change the text on the button
        int sever_id1=SpType.getInt("Server_ID",0);
        if (sever_id1==0){
            //set the text to upload data

        }else {
             btn_done3.setText(R.string.upload_edited);
        }
        btn_done3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// we will check if the server id in the sharedprefrence is empty or not
                // if it is empty that mean that this is the first up load ==> new user
                // else he make an edit
               int server_id =SpType.getInt("Server_ID",0);
                if (server_id>0){
//                     that mean the user is already registered
                    upDate_data();
                }else {
                    // this is a new user
                    upload_data();
                }

            }
        });

        btn_selected_service = findViewById(R.id.btn_selected_service);
        btn_selected_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Profile.this, Select_Sub_Menu.class);
                // intent.putExtra("from_my_profile",true);
                SharedPreferences.Editor editor = SpType.edit();
                editor.putBoolean("flag_register", true);
                editor.commit();
                startActivity(intent);
                finish();
            }
        });
//       vp_myprofile.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               Intent intent=new Intent(My_Profile.this,Images_Profile.class);
//               startActivity(intent);
//           }
//       });
btn_map1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        getLastLocation();
    }
});

/** fill the view pager in an array ****/
     ///   SpType = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
lang=SpType.getString("lang", "en_US");

        // Reading from SharedPreferences
        String Customer_type = SpType.getString("M_type", "2");
        max_image_number = SpType.getInt("Max_image_number", 3);
        // we intialize the array of pic
        imageurl = new String[max_image_number];
        for (int i=0;i<max_image_number;i++){
            imageurl[i]="file:///android_asset/add_photo.png";
        }

        if (Customer_type.equals("1")) {
            // vip to be considered

        } else if (Customer_type.equals("2")) {
            // gold five pic have
            fill_Image_first(3);
        } else {
            // free one picture have
            fill_Image_first(1);
        }


        // read from the shared prefirarnce
        et_name12.setText(SpType.getString("company_name", ""));
        et_phone12.setText(SpType.getString("mobile", ""));
        et_address12.setText(SpType.getString("address", ""));
        et_email12.setText(SpType.getString("email", ""));
        et_Password12.setText(SpType.getString("userpassword", ""));
        et_username12.setText(SpType.getString("username", ""));
        et_answer12.setText(SpType.getString("secret_ans", ""));
        et_lan1.setText(SpType.getString("lan", ""));
        et_lon1.setText(SpType.getString("lon", ""));
        M_typ = SpType.getString("M_type", "");
        et_wed_address.setText(SpType.getString("web_address",""));

        if (M_typ.equals("1")) {
            // nothing
            RGroup.check(R.id.vip1);
           // R_VIP.setChecked(false);
        } else if (M_typ.equals("2")) {
            RGroup.check(R.id.gold1);
          //  R_gold.setChecked(true);
        } else if (M_typ.equals("3")) {
            RGroup.check(R.id.free1);
           // R_free.setChecked(true);
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ((RadioGroup) findViewById(R.id.rg1))
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        SharedPreferences.Editor editor = SpType.edit();
                        switch (i) {
                            case R.id.vip1:
                                // vip
                                editor.putString("M_type", "1");
                                editor.commit();
                                // Open_Another_activity();
                                break;
                            case R.id.gold1:
                                // gold
                                editor.putString("M_type", "2");
                                editor.putInt("Max_image_number", 3);
                                editor.commit();
                                //  Open_Another_activity();
                                break;
                            default:
                                // free
                                editor.putString("M_type", "3");
                                editor.putInt("Max_image_number", 1);
                                editor.commit();

                                break;
                        }
                    }
                });


/******************Spinner***********************/

        LoadSpinnerCountry loadSpinnerCountry = new LoadSpinnerCountry();
        loadSpinnerCountry.execute();
//        if (SpType.getInt("Cposition", 999999) == 999999) {
//            Cposition = 0;
//        } else {
//            Cposition = SpType.getInt("Cposition", 999999);
//
//        }
//        if (SpType.getInt("Sub_position", 999999) == 999999) {
//            Sub_Cpotion = 0;
//        } else {
//            Sub_Cpotion = SpType.getInt("Sub_position", 999999);
//
//        }
        sp_country12.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadSpinnerSubCountry loadSpinnerSubCountry = new LoadSpinnerSubCountry();
                loadSpinnerSubCountry.execute("");
                country_spinner SelectedType;
                // On selecting a spinner item
                Cposition = position;
                SelectedType = SADA.getItem(position);
                SharedPreferences.Editor editor = SpType.edit();
                editor.putInt("Cposition", position);
                editor.commit();
                //Label_UserType = parent.getItemAtPosition(position).toString();
                String s = SelectedType.getCountry_name();

                editor.putString("country_ID", SelectedType.getCountry_id());
                editor.commit();
                // Showing selected spinner item
                tv_country_code.setText(SelectedType.getCountry_code());
                Toast.makeText(parent.getContext(), "You selected: " + s, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp_sub_country12.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Sub_country_spinner SelectedType;
                // On selecting a spinner item
                SelectedType = (Sub_country_spinner) SSADA.getItem(position);
                //Label_UserType = parent.getItemAtPosition(position).toString();
                String s = SelectedType.getCS_Name();
                Sub_Cpotion = position;
                SharedPreferences.Editor editor = SpType.edit();
                editor.putString("Sub_country_ID", SelectedType.getCS_ID());
                editor.putInt("CSposition", position);
                editor.commit();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "You selected: " + s, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        vp_myprofile.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Intent intent=new Intent(My_Profile.this,Images_Profile.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        final GestureDetector tapGestureDetector = new GestureDetector(this, new TapGestureListener());

        vp_myprofile.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                tapGestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }



    private void upload_data() {
        // when start the upload the service the button is gone
        btn_done3.setVisibility(View.GONE);
        progressBar1.setVisibility(View.VISIBLE);
        /**check for permision */
        if (ContextCompat.checkSelfPermission(My_Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(My_Profile.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                int MY_PERMISSIONS_REQUEST_READ_external=22;
                ActivityCompat.requestPermissions(My_Profile.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_external);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else {

        }
        if (ContextCompat.checkSelfPermission(My_Profile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(My_Profile.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                int MY_PERMISSIONS_REQUEST_READ_external=22;
                ActivityCompat.requestPermissions(My_Profile.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_external);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else {

        }
//        if (ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.WRITE_CALENDAR)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted
//        }
        String full_name, phone, address, email, Lan, lon, et_answer, username1, userpassword1, service_discription,web_address;
        int country_id1, sub_country_id1,WC_ID,SWM_cust_type;
        full_name = et_name12.getText().toString().trim();
        phone = et_phone12.getText().toString().trim();
        web_address=et_wed_address.getText().toString().trim();

        address = et_address12.getText().toString().trim();
        email = et_email12.getText().toString().trim();
        Lan = et_lan1.getText().toString().trim();
        lon = et_lon1.getText().toString().trim();
        username1 = SpType.getString("username", "");
        userpassword1 = SpType.getString("userpassword", "");
        et_answer = SpType.getString("secret_ans", "");
        service_discription = SpType.getString("service_description", "");
        SWM_cust_type=Integer.parseInt(SpType.getString("M_type","3"));
        WC_ID = SpType.getInt("WC_ID", 0);
        Cposition=SpType.getInt("Cposition",0);
        if (Cposition==0){
            show_Alert();
            return;
        }
        country_id1 = Integer.parseInt(SpType.getString("country_ID", ""));
        sub_country_id1 = Integer.parseInt(SpType.getString("Sub_country_ID", ""));
        // check if the advertizer didn't select a service category and a service master
        if (WC_ID==0){
            Intent intent=new Intent(My_Profile.this, Select_Main_Menu.class);
            startActivity(intent);
        }

        if (full_name.isEmpty()) {
            et_name12.setError(getString(R.string.nr));
            et_name12.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            et_phone12.setError(getString(R.string.pr));
            et_phone12.requestFocus();
            return;
        }else {
            phone=tv_country_code.getText()+phone;
        }

        if (address.isEmpty()) {
            et_address12.setError(getString(R.string.ar));
            et_address12.requestFocus();
            return;
        }
        if (Lan.isEmpty()) {
            et_lan1.setError(getString(R.string.lr));
            et_lan1.requestFocus();
            return;
        }
        if (lon.isEmpty()) {
            et_lon1.setError(getString(R.string.lonr));
            et_lon1.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            et_email12.setError(getString(R.string.er));
            et_email12.requestFocus();
            return;
        }
//if the sever_id is empty then that mean that the advertizer didnt register yet so i will check if there is an image in the sqlite
        //check if there is an image in sqlite
        // we check if the image table have recordes in it or not to
        String query = "select * from Image_tbl LIMIT 1 ";
        // the cursor will endicate if there is a result to the select statment or not
        Cursor cursor = AndPOS_db.rawQuery(query, null);

// the user didnt select a image so i open the image activity to make him select
        if (cursor.getCount() == 0) {
            // which mean the user did select any picture
            Toast.makeText(My_Profile.this, R.string.please_select_pic, Toast.LENGTH_LONG).show();
            cursor.close();
            Intent intent=new Intent(My_Profile.this,Images_Profile.class);
            startActivity(intent);
            finish();

        } else {
            // there is an image in the sql and we only need the first one as a profile pic

            if (cursor.moveToFirst()) {
                do {
                    String image_id, image_name, image_path, image_uri;

                    // we use put method to save the data in that map and we use the column name as the key
                    image_id = cursor.getString(cursor.getColumnIndex("imgae_ID"));
                    image_name = cursor.getString(cursor.getColumnIndex("Image_name"));
                    image_path = cursor.getString(cursor.getColumnIndex("image_path"));
                    image_uri = cursor.getString(cursor.getColumnIndex("image_uri"));
                    sql_uri = Uri.parse(image_uri);
                 /**check if directory that mean that the image is from camera else it is gallery*/
                    File file = new File(image_path);
                    if (file.isDirectory()){
                        imageSql_path=image_path +"/"+image_name;
                    }else {
                        imageSql_path=image_path;
                    }



                } while (cursor.moveToNext());
                cursor.close();
            }
            filepath=imageSql_path;
            if (!filepath.isEmpty()) {
//            filepath="empty path";
                file = new File(filepath);
                requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(sql_uri)), file);
            }
            /**    do insert to the web database */


            Call<MyResponse1> create_SWM = RetrofitClient
                    .getInstance()
                    .getApi()
                    .createSWM_Recored(
                            full_name,
                            phone,
                            phone,// mobile
                            web_address,
                            address,
                            Lan,
                            lon,
                            requestFile,
                            username1,
                            userpassword1,
                            "0",
                            service_discription,// it is company name name filed and i am using it for the discription
                            email,// i use it for web address SWM_POBOX is now for email
                            country_id1,
                            sub_country_id1,
                            et_answer,
                            WC_ID,
                            SWM_cust_type
                    );

            create_SWM.enqueue(new Callback<MyResponse1>() {
                @Override
                public void onResponse(Call<MyResponse1> call, Response<MyResponse1> response) {
                    // String s = String.valueOf(response.body());

// the response is > 0 mean that we have a server id
                    if (response.body().error>0) {

                        int server_id=response.body().error;


                        SharedPreferences.Editor editor=SpType.edit();

                        editor.putInt("Server_ID",server_id);
                        editor.putBoolean("flag_server_id",true);
                        editor.commit();
                        // that mean there is two more image to be uploaded
                        if (max_image_number >1){
                            upload_Image upload_image=new upload_Image();
                            upload_image.execute("");
                        }else {
                            /** they will upload the services of that client**/
                            upload_services upload_services=new upload_services();
                            upload_services.execute("");
                        }
                        Toast.makeText(getApplicationContext(), R.string.upload_s, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.upload_f, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<MyResponse1> call, Throwable t) {
                  //  Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    if (t instanceof SocketTimeoutException)
                    {
                        Toast.makeText(getApplicationContext(), "connection timeout"+t, Toast.LENGTH_LONG).show();
                        // "Connection Timeout";
                    }
                    else if (t instanceof IOException)
                    {
                        Toast.makeText(getApplicationContext(), "time out  IOException "+t, Toast.LENGTH_LONG).show();
                        // "Timeout";
                    }
                    else
                    {
                        //Call was cancelled by user
                        if(call.isCanceled())
                        {
                            System.out.println("Call was cancelled forcefully");
                            Toast.makeText(getApplicationContext(), "Call was cancelled forcefully ", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            //Generic error handling
                            System.out.println("Network Error :: " + t.getLocalizedMessage());
                            Toast.makeText(getApplicationContext(), "Network Error :: ", Toast.LENGTH_LONG).show();
                        }
                }}
            });

        }



    }
    private void upDate_data() {
        /**check for permision */
        btn_done3.setVisibility(View.GONE);
        progressBar1.setVisibility(View.VISIBLE);
        if (ContextCompat.checkSelfPermission(My_Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(My_Profile.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                int MY_PERMISSIONS_REQUEST_READ_external=22;
                ActivityCompat.requestPermissions(My_Profile.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_external);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else {

        }
        if (ContextCompat.checkSelfPermission(My_Profile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(My_Profile.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                int MY_PERMISSIONS_REQUEST_READ_external=22;
                ActivityCompat.requestPermissions(My_Profile.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_external);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else {

        }
//        if (ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.WRITE_CALENDAR)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted
//        }
        String full_name, phone, address, email, Lan, lon, et_answer, username1, userpassword1, service_discription,web_address;
        int country_id1, sub_country_id1,WC_ID,SWM_cust_type;
        full_name = et_name12.getText().toString().trim();

        phone = et_phone12.getText().toString().trim();
        web_address=et_wed_address.getText().toString().trim();

        address = et_address12.getText().toString().trim();
        email = et_email12.getText().toString().trim();
        Lan = et_lan1.getText().toString().trim();
        lon = et_lon1.getText().toString().trim();
        username1 = SpType.getString("username", "");
        userpassword1 = SpType.getString("userpassword", "");
        et_answer = SpType.getString("secret_ans", "");
        service_discription = SpType.getString("service_description", "");
        SWM_cust_type=Integer.parseInt(SpType.getString("M_type","3"));

        WC_ID = SpType.getInt("WC_ID", 0);
        Cposition=SpType.getInt("Cposition",0);
        if (Cposition==0){
            show_Alert();
            return;
        }
        country_id1 = Integer.parseInt(SpType.getString("country_ID", ""));
        sub_country_id1 = Integer.parseInt(SpType.getString("Sub_country_ID", ""));
        // check if the advertizer didn't select a service category and a service master
        if (WC_ID==0){
            Intent intent=new Intent(My_Profile.this, Select_Main_Menu.class);
            startActivity(intent);
        }

        if (full_name.isEmpty()) {
            et_name12.setError(getString(R.string.nr));
            et_name12.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            et_phone12.setError(getString(R.string.pr));
            et_phone12.requestFocus();
            return;
        }else {
            String s= String.valueOf(tv_country_code.getText());
            if (phone.startsWith(s)){

            }else {
                phone=tv_country_code.getText()+phone;
            }
          //  phone=tv_country_code.getText()+phone;
            //
            // phone=phone;
        }

        if (address.isEmpty()) {
            et_address12.setError(getString(R.string.ar));
            et_address12.requestFocus();
            return;
        }
        if (Lan.isEmpty()) {
            et_lan1.setError(getString(R.string.lr));
            et_lan1.requestFocus();
            return;
        }
        if (lon.isEmpty()) {
            et_lon1.setError(getString(R.string.lonr));
            et_lon1.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            et_email12.setError(getString(R.string.er));
            et_email12.requestFocus();
            return;
        }
//
//        // read from the shared prefirarnce
//        et_name12.setText(SpType.getString("company_name", ""));
//        et_phone12.setText(SpType.getString("mobile", ""));
//        et_address12.setText(SpType.getString("address", ""));
//        et_email12.setText(SpType.getString("email", ""));
//        et_Password12.setText(SpType.getString("userpassword", ""));
//        et_username12.setText(SpType.getString("username", ""));
//        et_answer12.setText(SpType.getString("secret_ans", ""));
//        et_lan1.setText(SpType.getString("lan", ""));
//        et_lon1.setText(SpType.getString("lon", ""));
//        M_typ = SpType.getString("M_type", "");
//        et_wed_address.setText(SpType.getString("web_address",""));
        // full_name, phone, address, email, Lan, lon, et_answer, username1, userpassword1, service_discription,web_address;
        SharedPreferences.Editor editor=SpType.edit();
        editor.putString("company_name",full_name);
        editor.putString("mobile",phone);
        editor.putString("address",address);
        editor.putString("email",email);
        editor.putString("lan",Lan);
        editor.putString("lon",lon);
        editor.putString("web_address",web_address);
        editor.putString("service_description",service_discription);
        editor.commit();
//        editor.putString("company_name",full_name);
//SpType.getStringSet();
//SpType.getStringSet();
//SpType.getStringSet();
//SpType.getStringSet();
//SpType.getStringSet();
//SpType.getStringSet();
//SpType.getStringSet("M_type",)
//SpType.getStringSet();
//SpType.getStringSet();


//if the sever_id is empty then that mean that the advertizer didnt register yet so i will check if there is an image in the sqlite
        //check if there is an image in sqlite
        // we check if the image table have recordes in it or not to
        String query = "select * from Image_tbl LIMIT 1 ";
        // the cursor will endicate if there is a result to the select statment or not
        Cursor cursor = AndPOS_db.rawQuery(query, null);

// the user didnt select a image so i open the image activity to make him select
        if (cursor.getCount() == 0) {
            // which mean the user did select any picture
            Toast.makeText(My_Profile.this, R.string.please_select_pic, Toast.LENGTH_LONG).show();
            cursor.close();
            Intent intent=new Intent(My_Profile.this,Images_Profile.class);
            startActivity(intent);
            finish();

        } else {
            // there is an image in the sql and we only need the first one as a profile pic

            if (cursor.moveToFirst()) {
                do {
                    String image_id, image_name, image_path, image_uri;

                    // we use put method to save the data in that map and we use the column name as the key
                    image_id = cursor.getString(cursor.getColumnIndex("imgae_ID"));
                    image_name = cursor.getString(cursor.getColumnIndex("Image_name"));
                    image_path = cursor.getString(cursor.getColumnIndex("image_path"));
                    image_uri = cursor.getString(cursor.getColumnIndex("image_uri"));
                    sql_uri = Uri.parse(image_uri);
                 /**check if directory that mean that the image is from camera else it is gallery*/
                    File file = new File(image_path);
                    if (file.isDirectory()){
                        imageSql_path=image_path +"/"+image_name;
                    }else {
                        imageSql_path=image_path;
                    }



                } while (cursor.moveToNext());
                cursor.close();
            }
            filepath=imageSql_path;
            if (!filepath.isEmpty()) {
//            filepath="empty path";
                file = new File(filepath);
                requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(sql_uri)), file);
            }
            /**    do insert to the web database */
            // read the server id
int server_id=SpType.getInt("Server_ID",0);

            Call<MyResponse1> update_SWM = RetrofitClient
                    .getInstance()
                    .getApi()
                    .updateSWM_Recored(
                            server_id,
                            full_name,
                            phone,
                            phone,// mobile
                            web_address,
                            address,
                            Lan,
                            lon,
                            requestFile,
                            username1,
                            userpassword1,
                            "0",
                            service_discription,// it is company name name filed and i am using it for the discription
                            email,// i use it for web address SWM_POBOX is now for email
                            country_id1,
                            sub_country_id1,
                            et_answer,
                            WC_ID,
                            SWM_cust_type
                    );

            update_SWM.enqueue(new Callback<MyResponse1>() {
                @Override
                public void onResponse(Call<MyResponse1> call, Response<MyResponse1> response) {
                    // String s = String.valueOf(response.body());

// the response is > 0 mean that we have a server id
                    if (response.body().error>0) {

//                        int server_id=response.body().error;
//
//
//                        SharedPreferences.Editor editor=SpType.edit();
//
//                        editor.putInt("Server_ID",server_id);
//                        editor.putBoolean("flag_server_id",true);
//                        editor.commit();
                        // that mean there is two more image to be uploaded
                        if (max_image_number >1){
                            Update_Image update_image=new Update_Image();
                            update_image.execute("");
                        }// we finsh from the image and we will add or update the service
                        else {
                            /** they will upload the services of that client**/


                            upload_services upload_services=new upload_services();
                            upload_services.execute("");
                        }
                        Toast.makeText(getApplicationContext(), R.string.upload_s, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.upload_f, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<MyResponse1> call, Throwable t) {
                  //  Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    if (t instanceof SocketTimeoutException)
                    {
                        Toast.makeText(getApplicationContext(), "connection timeout"+t, Toast.LENGTH_LONG).show();
                        // "Connection Timeout";
                    }
                    else if (t instanceof IOException)
                    {
                        Toast.makeText(getApplicationContext(), "time out  IOException "+t, Toast.LENGTH_LONG).show();
                        // "Timeout";
                    }
                    else
                    {
                        //Call was cancelled by user
                        if(call.isCanceled())
                        {
                            System.out.println("Call was cancelled forcefully");
                            Toast.makeText(getApplicationContext(), "Call was cancelled forcefully ", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            //Generic error handling
                            System.out.println("Network Error :: " + t.getLocalizedMessage());
                            Toast.makeText(getApplicationContext(), "Network Error :: ", Toast.LENGTH_LONG).show();
                        }
                }}
            });

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

    public void show_Alert_done(){
        new AlertDialog.Builder(this)
                .setTitle("Thank You For Advertising With Us.  ")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public class upload_services extends AsyncTask<String ,String ,String>{
        int MS_ID,serverID;

        @Override
        protected void onPreExecute() {
           serverID= SpType.getInt("Server_ID",0);
Delete_services delete_services=new Delete_services();
delete_services.execute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(My_Profile.this,"Save Successfully",Toast.LENGTH_LONG).show();
          //  btn_done3.setVisibility(View.GONE);
            progressBar1.setVisibility(View.GONE);
show_Alert_done();
         //  finish();

        }

        @Override
        protected String doInBackground(String... strings) {
            // fires we will read from the sqlite table service selected tbl
            String query = "select * from Service_Selected_Tbl ";
            // the cursor will endicate if there is a result to the select statment or not
            Cursor cursor = AndPOS_db.rawQuery(query, null);
            if (cursor.getCount()==0){
                // nothing to do there is no service
            }else {
                for (int x=0;x<=cursor.getCount();x++){
                    if (cursor.moveToPosition(x)){
                        MS_ID=cursor.getInt(cursor.getColumnIndex("MS_ID"));

                        Call<MyResponse1> create_S = RetrofitClient
                                .getInstance()
                                .getApi()
                                .createS_Recored(
                                        ///String.valueOf(server_id1),
                                        serverID,
                                        MS_ID

                                );
                        create_S.enqueue(new Callback<MyResponse1>() {
                            @Override
                            public void onResponse(Call<MyResponse1> call, Response<MyResponse1> response1) {
                                assert response1.body() != null;
                                if (response1.body().error>0) {

                                    Toast.makeText(My_Profile.this,"service uploaded",Toast.LENGTH_LONG).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse1> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            }



            return null;
        }
    }
    public class Delete_services extends AsyncTask<String ,String ,String>{
        int MS_ID,serverID;

        @Override
        protected void onPreExecute() {
           serverID= SpType.getInt("Server_ID",0);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(My_Profile.this,"Save Successfully",Toast.LENGTH_LONG).show();
          //  btn_done3.setVisibility(View.GONE);
            progressBar1.setVisibility(View.GONE);
show_Alert_done();
         //  finish();

        }

        @Override
        protected String doInBackground(String... strings) {
//            // fires we will read from the sqlite table service selected tbl
//            String query = "select * from Service_Selected_Tbl ";
//            // the cursor will endicate if there is a result to the select statment or not
//            Cursor cursor = AndPOS_db.rawQuery(query, null);
//            if (cursor.getCount()==0){
//                // nothing to do there is no service
//            }else {
//                for (int x=0;x<=cursor.getCount();x++){
//                    if (cursor.moveToPosition(x)){
//                        MS_ID=cursor.getInt(cursor.getColumnIndex("MS_ID"));

                        Call<MyResponse1> create_SU = RetrofitClient
                                .getInstance()
                                .getApi()
                                .createSU_Recored(
                                        ///String.valueOf(server_id1),
                                        serverID


                                );
                        create_SU.enqueue(new Callback<MyResponse1>() {
                            @Override
                            public void onResponse(Call<MyResponse1> call, Response<MyResponse1> response1) {
                                assert response1.body() != null;
                                if (response1.body().message.equals("File Uploaded Successfullly")) {

                                    Toast.makeText(My_Profile.this,"service uploaded",Toast.LENGTH_LONG).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse1> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                  //  }

            //    }
           // }



            return null;
        }
    }









public class upload_Image extends AsyncTask<String, String, String>{

int insert_count=0;
    int server_id1=SpType.getInt("Server_ID",0);

    @Override
    protected void onPostExecute(String s) {
        // when we upload the image and finsh we add the upload services
        upload_services upload_services=new upload_services();
        upload_services.execute("");
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... strings) {
       // int server_id1=SpType.getInt("Server_ID",0);
        String query = "select * from Image_tbl ";
        // the cursor will endicate if there is a result to the select statment or not
        Cursor cursor = AndPOS_db.rawQuery(query, null);
        insert_count=cursor.getCount();
        if (insert_count==1){
            // that mean that this image is the profile image and it is uploaded
            return null;
        }else {
            // the cursor is begin from 0
            for (int c=1;c<=cursor.getCount();c++){
                if (cursor.moveToPosition(c)){
                    String image_id, image_name, image_path, image_uri;

                    // we use put method to save the data in that map and we use the column name as the key
                    image_id = cursor.getString(cursor.getColumnIndex("imgae_ID"));

                    image_name = cursor.getString(cursor.getColumnIndex("Image_name"));
                    image_path = cursor.getString(cursor.getColumnIndex("image_path"));
                    image_uri = cursor.getString(cursor.getColumnIndex("image_uri"));
                    sql_uri_IMG = Uri.parse(image_uri);
                    imageSql_path=image_path+"/"+image_name;

                    filepath=imageSql_path;
                    if (!filepath.isEmpty()) {

                        IMG_file = new File(filepath);
                        requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(sql_uri_IMG)), IMG_file);
                    }
                    Call<MyResponse1> create_IMG = RetrofitClient
                            .getInstance()
                            .getApi()
                            .createIMG_Recored(
                                    ///String.valueOf(server_id1),
                                    server_id1,
                                    requestFile

                            );
                    create_IMG.enqueue(new Callback<MyResponse1>() {
                        @Override
                        public void onResponse(Call<MyResponse1> call, Response<MyResponse1> response1) {
                            assert response1.body() != null;
                            if (response1.body().error>0) {
                                insert_count=insert_count-1;
                                Toast.makeText(My_Profile.this,"Image uploaded",Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse1> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
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

        }

        return null;
    }
}


    public class Update_Image extends AsyncTask<String, String, String>{

        int insert_count=0;
        int server_id1=SpType.getInt("Server_ID",0);

        @Override
        protected void onPostExecute(String s) {
//            Delete_services delete_services=new Delete_services();
//            delete_services.execute();
            // when we upload the image and finsh we add the upload services
            upload_services upload_services=new upload_services();
            upload_services.execute("");
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            // int server_id1=SpType.getInt("Server_ID",0);
//            String query = "select * from Image_tbl ";
//            // the cursor will endicate if there is a result to the select statment or not
//            Cursor cursor = AndPOS_db.rawQuery(query, null);
//            insert_count=cursor.getCount();
//            if (insert_count==1){
//                // that mean that this image is the profile image and it is uploaded
//                return null;
//            }else {
                // the cursor is begin from 0
//                for (int c=1;c<=cursor.getCount();c++){
//                    if (cursor.moveToPosition(c)){
//                        String image_id, image_name, image_path, image_uri;
//
//                        // we use put method to save the data in that map and we use the column name as the key
//                        image_id = cursor.getString(cursor.getColumnIndex("imgae_ID"));
//
//                        image_name = cursor.getString(cursor.getColumnIndex("Image_name"));
//                        image_path = cursor.getString(cursor.getColumnIndex("image_path"));
//                        image_uri = cursor.getString(cursor.getColumnIndex("image_uri"));
//                        sql_uri_IMG = Uri.parse(image_uri);
//                        imageSql_path=image_path;
//
//                        filepath=imageSql_path;
//                        if (!filepath.isEmpty()) {
//
//                            IMG_file = new File(filepath);
//                            requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(sql_uri_IMG)), IMG_file);
//                        }
                        Call<MyResponse1> updateIMG = RetrofitClient
                                .getInstance()
                                .getApi()
                                .updateIMG_Recored(
                                        ///String.valueOf(server_id1),
                                        server_id1
                                        //,
                                       // requestFile

                                );
                        updateIMG.enqueue(new Callback<MyResponse1>() {
                            @Override
                            public void onResponse(Call<MyResponse1> call, Response<MyResponse1> response1) {
                                assert response1.body() != null;
                                if (response1.body().error>0) {
                                   // insert_count=insert_count-1;
                                    Toast.makeText(My_Profile.this,"Image updated",Toast.LENGTH_LONG).show();
                                    // after the delete of the image i will upload the new one
                                    upload_Image upload_image1=new upload_Image();
                                    upload_image1.execute();
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


    public class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Intent intent = new Intent(My_Profile.this, Images_Profile.class);
            startActivity(intent);
            My_Profile.this.finish();
            return true;
        }

    }

    private void fill_Image_first(int Image_number) {
        //    imageList = new ArrayList<Itemlist_image>();

        /**  we check if there an image saved in the image table or not
         **/
        // we check if the image table have recordes in it or not to
        String query = "select * from Image_tbl ";
        // the cursor will endicate if there is a result to the select statment or not
        Cursor cursor = AndPOS_db.rawQuery(query, null);
        // the the retain quary is one  that mean there is a record for the user
        int x = 0;
        imageurl = new String[cursor.getCount()];
        if (cursor.getCount() >= 1) {
            Uri sql_uri;
            // Toast.makeText(this, "what ever",Toast.LENGTH_LONG).show();
            if (cursor.moveToFirst()) {
                do {
                    String image_id, image_name, image_path, image_uri;

                    // we use put method to save the data in that map and we use the column name as the key
                    image_id = cursor.getString(cursor.getColumnIndex("imgae_ID"));
                    image_name = cursor.getString(cursor.getColumnIndex("Image_name"));
                    image_path = cursor.getString(cursor.getColumnIndex("image_path"));
                    image_uri = cursor.getString(cursor.getColumnIndex("image_uri"));
                    imageurl[x] = image_uri;
                    x++;
// we fill the list item  with the info


                    //  itemlist_image = new Itemlist_image(image_uri);
                    // imageList.add(itemlist_image);


                    //  LabelList.add(cursor2.getString(cursor2.getColumnIndex("User_Type_Name")));
                } while (cursor.moveToNext());
            }

            // cursor.close();

        }
//        if (cursor.getCount() == 0) {
//            imageurl = new String[Image_number];
//            kkkk
//            /** fill the list with empty image */
//            for (int i = 0; i < Image_number; i++) {
//                String image_name = "empty";
//                // if there is no images loaded
//                imageurl[i] = "file:///android_asset/add_photo.png";
//
////                itemlist_image = new Itemlist_image(image_name);
////                itemlist_image.setImage_uri("");
////                imageList.add(itemlist_image);
//            }
//        } else if (cursor.getCount() < max_image_number) {
//            int remain = max_image_number - cursor.getCount();
//            for (int i = x; i <= remain; i++) {
//                String image_name = "empty";
//                imageurl[i] = "file:///android_asset/add_photo.png";
//
////                itemlist_image = new Itemlist_image(image_name);
////                itemlist_image.setImage_uri("");
////                imageList.add(itemlist_image);
//            }
//        }
        cursor.close();


/**add the image list to the adapter so we can use it in the recycler view**/
        // ADA = new Image_Adapter(My_Profile.this, imageList);
        //   vp_myprofile.setAdapter(ADA);
        // this is an array imageurl
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, imageurl);
        vp_myprofile.setAdapter(viewPagerAdapter);


    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            My_Profile.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (vp_myprofile.getCurrentItem() < imageurl.length - 1) {
                        vp_myprofile.setCurrentItem(vp_myprofile.getCurrentItem() + 1);
                    } else {
                        vp_myprofile.setCurrentItem(0);
                    }
                }
            });
        }
    }
//    public void pagerOnclick(View view) {
//        Intent intent=new Intent(My_Profile.this,Images_Profile.class);
//        startActivity(intent);
//    }

    public class LoadSpinnerCountry extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SADA = new spinnerAdapter(LabelList, My_Profile.this);
            // put them in the list view


            // attaching data adapter to spinner
            sp_country12.setAdapter(SADA);
            if (Cposition!=0){
                sp_country12.setSelection(Cposition);
            }else {
                sp_country12.setSelection(0);
            }
         //   sp_country12.setSelection(Cposition);
            SADA.notifyDataSetChanged();
            LoadSpinnerSubCountry loadSpinnerSubCountry = new LoadSpinnerSubCountry();
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
                    String country_name, country_name_ar, country_code;

                    // we use put method to save the data in that map and we use the column name as the key
                    country_id = cursor2.getString(cursor2.getColumnIndex("Country_ID"));
//                    country_name = cursor2.getString(cursor2.getColumnIndex("Country_name"));
//                    country_name_ar = cursor2.getString(cursor2.getColumnIndex("Country_name_ar"));
                    country_code = cursor2.getString(cursor2.getColumnIndex("Country_code"));
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
                    country_spinner country_Info = new country_spinner(country_id, country_name, country_name_ar, country_code);


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

            SSADA = new Sub_country_spinner_adapter(SLabelList, My_Profile.this);
            // put them in the list view

            // attaching data adapter to spinner
            sp_sub_country12.setAdapter(SSADA);
            Cposition= SpType.getInt("Cposition",0);
            if (Cposition==0){
                // dont set a first selection
            }else {
                if (CSposition!=0){
                    sp_sub_country12.setSelection(CSposition);
                }
            }
//            if (CSposition!=0){
//                sp_sub_country12.setSelection(CSposition);
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
            SelectedType = SADA.getItem(Cposition);
            //Label_UserType = parent.getItemAtPosition(position).toString();
            String s = SelectedType.getCountry_id();
            // Select Quary for a spicific category_spinner example bedroom the cat id is from put extra
            String selectQuery = "SELECT * FROM country_sub_tbl where Country_ID=" + s;
            //sqlite will returen a cursor a pointer to the first data
            Cursor cursor2 = AndPOS_db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list the result is kids clasic and modern
            if (cursor2.moveToFirst()) {
                do {

                    String Scountry_id, Scountry_name, Scountry_name_ar, country_id;

                    // we use put method to save the data in that map and we use the column name as the key
                    Scountry_id = cursor2.getString(cursor2.getColumnIndex("CS_ID"));
//                    Scountry_name = cursor2.getString(cursor2.getColumnIndex("CS_name"));
//                    Scountry_name_ar = cursor2.getString(cursor2.getColumnIndex("CS_name_ar"));
                    country_id = cursor2.getString(cursor2.getColumnIndex("Country_ID"));

// we fill the spiner with the info

                    if (!lang.equals("en_US")){ // arabic
                        Scountry_name=cursor2.getString(cursor2.getColumnIndex("CS_name_ar"));
                        Scountry_name_ar=cursor2.getString(cursor2.getColumnIndex("CS_name_ar"));
                    }else {
                        Scountry_name=cursor2.getString(cursor2.getColumnIndex("CS_name"));
                        Scountry_name_ar=cursor2.getString(cursor2.getColumnIndex("CS_name"));
                    }
                    Sub_country_spinner country_Info = new Sub_country_spinner(Scountry_id, Scountry_name, Scountry_name_ar, country_id);


                    // we add the saved map to the list one by one
                    SLabelList.add(country_Info);

                    //  LabelList.add(cursor2.getString(cursor2.getColumnIndex("User_Type_Name")));
                } while (cursor2.moveToNext());
            }

            cursor2.close();


            return null;
        }
    }

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
                                    et_lan1.setText(location.getLatitude()+"");
                                    et_lon1.setText(location.getLongitude()+"");
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
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            et_lan1.setText(mLastLocation.getLatitude()+"");
            et_lon1.setText(mLastLocation.getLongitude()+"");
        }
    };

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
//                Intent intent = new Intent(My_Profile.this, Main_Activity.class);
//                startActivity(intent);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
