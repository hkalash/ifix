package com.smartsoftwaresolutions.ifix;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartsoftwaresolutions.ifix.Database.DataHelper;
import com.smartsoftwaresolutions.ifix.Read_Data.API;
import com.smartsoftwaresolutions.ifix.Read_Data.Category;
import com.smartsoftwaresolutions.ifix.Read_Data.Sub_Category;
import com.smartsoftwaresolutions.ifix.Sub_country_spinner.Sub_country_spinner;
import com.smartsoftwaresolutions.ifix.country_spinner.Country;
import com.smartsoftwaresolutions.ifix.country_spinner.country_spinner;
import com.smartsoftwaresolutions.ifix.country_spinner.spinnerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Splash extends AppCompatActivity {
    DataHelper AndPOS_dbh;
    SQLiteDatabase AndPOS_db;
    SharedPreferences SpType;
    public static final String PREFS_NAME = "ifix_data";
    String user_id, Country_ID;
    public PendingIntent pendingIntent;
    int time_interval;
    int SPLASH_DISPLAY_LENGTH;
    private ImageView container;
    private AnimationDrawable animationDrawable;
    ArrayList<Category> categoryArrayList = new ArrayList<Category>();
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        container = findViewById(R.id.iv_icons);
        container.setBackgroundResource(R.drawable.splash_animation);

        animationDrawable = (AnimationDrawable) container.getBackground();

        SPLASH_DISPLAY_LENGTH = 2000;

        Stetho.initializeWithDefaults(this);


        //sqlite data inspection intilisation
        SpType = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        // Reading from SharedPreferences
        user_id = SpType.getString("user_id", "");
     //  Country_ID = SpType.getString("country_ID", "");
       Country_ID = SpType.getString("User_country_ID", "");
        lang=SpType.getString("lang","en_US");
        if (lang.equals("en_US")){
            setLocale("en_US");
        }else {
            setLocale("ar");
        }

        //show_help = SpType.getString("show_help", "1");
        //time_interval=SpType.getInt("interval",4);

        // we create an instance of data base helper  database this will create the database
        AndPOS_dbh = new DataHelper(getApplicationContext());
        // and we connect the data to the data base helper and specify its use as a writable
        AndPOS_db = AndPOS_dbh.getWritableDatabase();
        // create the database
        // we check if the User table have recordes in it or not to check if it is the first time use the system or not
        String query = "select * from User_Tbl ";
        // the cursor will endicate if there is a result to the select statment or not
        Cursor cursor = AndPOS_db.rawQuery(query, null);
        // the the retain quary is one  that mean there is a record for the user
        if (cursor.getCount() >= 1) {

            // Toast.makeText(this, "what ever",Toast.LENGTH_LONG).show();

        } else {
            // read data from the server
            //   Toast.makeText(this, "what ever 33333",Toast.LENGTH_LONG).show();
        }
        cursor.close();
        /** Create image folder**/
        File folder = getFilesDir();

        File f = new File(folder, "MI");
        File f1 = new File(folder, "CI");
        File f2 = new File(folder, "Image_Profile");
        File f3 = new File(folder, "SI");
        File f4 = new File(folder, "AI");

        if (!f.exists()) {
            f.mkdir();
            f1.mkdir();
            f2.mkdir();
            f3.mkdir();
            f4.mkdir();
        }

        /** read data from remote server**/
// get the data from the server this will garantee that the data will not be deleted when there is no internet

        if (isOnline()){
            new Read_Data_remote_Category().execute();
        }

        //   new Read_Data_remote_Country().execute();

        //  new Read_Data_remote_Master().execute();
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

    }
    @Override
    protected void onResume() {
        super.onResume();

        animationDrawable.start();

        checkAnimationStatus(100, animationDrawable);


    }

    private void checkAnimationStatus(final int time, final AnimationDrawable animationDrawable) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animationDrawable.getCurrent() != animationDrawable.getFrame(animationDrawable.getNumberOfFrames() - 1))
                    checkAnimationStatus(time, animationDrawable);
                else {
                    if (Country_ID.equals("")) {
                        Intent mainIntent = new Intent(Splash.this, Country.class);
                        Splash.this.startActivity(mainIntent);
                    } else {
                        Intent mainIntent = new Intent(Splash.this, Main_Activity.class);
                        Splash.this.startActivity(mainIntent);
                    }


                    finish();
                }
            }
        }, time);
    }


    class Read_Data_remote_Category extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //getResponse_Master();
            new Read_Data_remote_Master().execute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getResponse();
            return null;
        }
    }

    class Read_Data_remote_Master extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new Read_Data_remote_Country().execute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getResponse_Master();
            return null;
        }
    }

    class Read_Data_remote_Country extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
/** Read sub contry from the server **/
            new Read_Data_remote_S_Country().execute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getResponse_Country();
            return null;
        }
    }

    class Read_Data_remote_S_Country extends AsyncTask<Void , Void, Void> {


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            getResponse_S_Country();
            return null;
        }
    }

    private void getResponse_S_Country() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.JSONURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
// in the retrofit we creat the base url then in the api we link it to the call which it the secound part of the link
        API api = retrofit.create(API.class);

//AndPOS_db.close();

        Call<List<Sub_country_spinner>> call = api.getString_S_Country();
        call.enqueue(new Callback<List<Sub_country_spinner>>() {
                         @Override
                         public void onResponse(Call<List<Sub_country_spinner>> call, Response<List<Sub_country_spinner>> response) {
                             Log.i("Response_S_country", response.body().toString());
                             //Toast.makeText()
                             if (response.isSuccessful()) {
                                 if (response.body() != null) {
                                     //we delete all the rows in the table befor we add new one
                                     AndPOS_db.execSQL("DELETE FROM country_sub_tbl;");
                                     Log.i("onSuccess", response.body().toString());
                                     for (int i = 0; i < response.body().size(); i++) {
                                         ContentValues contentValues = new ContentValues();

                                         contentValues.put("CS_ID", response.body().get(i).getCS_ID());
                                         contentValues.put("CS_name", response.body().get(i).getCS_Name());
                                         contentValues.put("CS_name_ar", response.body().get(i).getCS_name_ar());
                                         contentValues.put("country_ID", response.body().get(i).getCountry_id());

                                         try {
                                             long rowInserted = AndPOS_db.insertOrThrow("country_sub_tbl", null, contentValues);
                                             if (rowInserted != -1) {
                                                 //Toast.makeText(AddCustomer.this, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                                                 //  z = "Added Successfully";
                                             } else {
                                                 //  Toast.makeText(AddCustomer.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                                 //  z = "Something wrong";
                                             }
                                         } catch (SQLiteConstraintException e1) {
                                             //Toast.makeText(AddCustomer.this,  e1.toString(),Toast.LENGTH_LONG).show();
                                             String z = e1.toString();
                                             // z = "The Customer name  must be unique Please check it";
                                         }
//
                                     }


                                 } else {
                                     Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                                 }
                             }

                         }
                         @Override
                         public void onFailure(Call<List<Sub_country_spinner>> call, Throwable t) {
                             Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();


                         }
                     }
        );
    }

    private void getResponse_Country() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.JSONURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
// in the retrofit we creat the base url then in the api we link it to the call which it the secound part of the link
        API api = retrofit.create(API.class);

//AndPOS_db.close();

        Call<List<country_spinner>> call = api.getString_Country();
        call.enqueue(new Callback<List<country_spinner>>() {
                         @Override
                         public void onResponse(Call<List<country_spinner>> call, Response<List<country_spinner>> response) {
                             Log.i("Responsestring_country", response.body().toString());
                             //Toast.makeText()
                             if (response.isSuccessful()) {
                                 if (response.body() != null) {
                                     //we delete all the rows in the table befor we add new one
                                     AndPOS_db.execSQL("DELETE FROM country;");
                                     Log.i("onSuccess", response.body().toString());
                                     for (int i = 0; i < response.body().size(); i++) {
                                         ContentValues contentValues = new ContentValues();
                                         contentValues.put("Country_ID", response.body().get(i).getCountry_id());
                                         contentValues.put("Country_name", response.body().get(i).getCountry_name());
                                         contentValues.put("Country_name_ar", response.body().get(i).getCountry_name_ar());
                                         contentValues.put("Country_code", response.body().get(i).getCountry_code());

                                         try {
                                             long rowInserted = AndPOS_db.insertOrThrow("country", null, contentValues);
                                             if (rowInserted != -1) {
                                                 //Toast.makeText(AddCustomer.this, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                                                 //  z = "Added Successfully";
                                             } else {
                                                 //  Toast.makeText(AddCustomer.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                                 //  z = "Something wrong";
                                             }
                                         } catch (SQLiteConstraintException e1) {
                                             //Toast.makeText(AddCustomer.this,  e1.toString(),Toast.LENGTH_LONG).show();
                                             String z = e1.toString();
                                             // z = "The Customer name  must be unique Please check it";
                                         }
//
                                     }


                                 } else {
                                     Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                                 }
                             }

                         }

                         @Override
                         public void onFailure(Call<List<country_spinner>> call, Throwable t) {
                             Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                         }
                     }
        );
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;
        } else {
            show_Alert();
            return false;
        }
    }

public void show_Alert(){
    new AlertDialog.Builder(this)
            .setTitle(getString(R.string.connectoin_f))
            .setMessage(getString(R.string.pleaseconect_to))
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
}

    private void getResponse() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.JSONURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
// in the retrofit we creat the base url then in the api we link it to the call which it the secound part of the link
        API api = retrofit.create(API.class);

//AndPOS_db.close();

        Call<List<Category>> call = api.getString();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // we grantee that there  is a internet and we can get data
                        //we delete all the rows in the table before we add new one

                        AndPOS_db.execSQL("DELETE FROM service_category_tbl;");

                        Log.i("onSuccess", response.body().toString());
                        for (int i = 0; i < response.body().size(); i++) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("SC_ID", response.body().get(i).getSC_ID());
                            contentValues.put("SC_Descption", response.body().get(i).getSC_Descption());
                            contentValues.put("SC_Description_ar", response.body().get(i).getSC_Description_ar());
                            String image_path = API.Category_Image + response.body().get(i).getSC_Icon_path();
                            contentValues.put("SC_Icon_path", image_path);
                            contentValues.put("SC_Icon_name", response.body().get(i).getSC_Icon_path());

                            try {
                                long rowInserted = AndPOS_db.insertOrThrow("service_category_tbl", null, contentValues);
                                if (rowInserted != -1) {
                                    //Toast.makeText(AddCustomer.this, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                                    //  z = "Added Successfully";
                                } else {
                                    //  Toast.makeText(AddCustomer.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                    //  z = "Something wrong";
                                }
                            } catch (SQLiteConstraintException e1) {
                                //Toast.makeText(AddCustomer.this,  e1.toString(),Toast.LENGTH_LONG).show();
                                String z = e1.toString();
                                // z = "The Customer name  must be unique Please check it";
                            }
//                            Category ct = new Category(response.body().get(i).getSC_ID(),
//                                    response.body().get(i).getSC_Descption(),
//                                    response.body().get(i).getSC_Description_ar(),
//                                    response.body().get(i).getSC_Icon_path());
//                            categoryArrayList.add(ct);
                            // tv1.setText(cityList.get(i).getName());
                        }
                        //   jsonresponse = response.body().toString();

                        //  writeTv(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void getResponse_Master() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.JSONURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
// in the retrofit we creat the base url then in the api we link it to the call which it the secound part of the link
        API api = retrofit.create(API.class);

//AndPOS_db.close();

        Call<List<Sub_Category>> call = api.getString_Sub_Category();
        call.enqueue(new Callback<List<Sub_Category>>() {
            @Override
            public void onResponse(Call<List<Sub_Category>> call, Response<List<Sub_Category>> response) {
                Log.i("Response_string_SubCat", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //we delete all the rows in the table befor we add new one
                        AndPOS_db.execSQL("DELETE FROM Master_Service_tbl;");
                        Log.i("onSuccess", response.body().toString());
                        for (int i = 0; i < response.body().size(); i++) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("MS_ID", response.body().get(i).getMS_ID());
                            contentValues.put("C_ID", response.body().get(i).getC_ID());
                            contentValues.put("SC_ID", response.body().get(i).getSC_ID());
                            contentValues.put("MS_Name", response.body().get(i).getMS_Name());
                            contentValues.put("MS_Description", response.body().get(i).getMS_Description());
                            contentValues.put("MS_Name_ar", response.body().get(i).getMS_Name_ar());
                            contentValues.put("MS_Description_ar", response.body().get(i).getMS_Description_ar());
                            String image_path = API.Sub_Category_Image + response.body().get(i).getMs_Icon_path();
                            contentValues.put("MS_Image_path", image_path);
                            contentValues.put("MS_Image_name", response.body().get(i).getMs_Icon_path());

                            try {
                                long rowInserted = AndPOS_db.insertOrThrow("Master_Service_tbl", null, contentValues);
                                if (rowInserted != -1) {
                                    //Toast.makeText(AddCustomer.this, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                                    //  z = "Added Successfully";
                                } else {
                                    //  Toast.makeText(AddCustomer.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                    //  z = "Something wrong";
                                }
                            } catch (SQLiteConstraintException e1) {
                                //Toast.makeText(AddCustomer.this,  e1.toString(),Toast.LENGTH_LONG).show();
                                String z = e1.toString();
                                // z = "The Customer name  must be unique Please check it";
                            }
//
                        }


                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Sub_Category>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}