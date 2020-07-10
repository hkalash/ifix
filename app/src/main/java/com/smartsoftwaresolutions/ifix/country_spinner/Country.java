package com.smartsoftwaresolutions.ifix.country_spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartsoftwaresolutions.ifix.Database.DataHelper;
import com.smartsoftwaresolutions.ifix.Gallery.Gallery;
import com.smartsoftwaresolutions.ifix.Main_Activity;
import com.smartsoftwaresolutions.ifix.R;
import com.smartsoftwaresolutions.ifix.Splash;
import com.smartsoftwaresolutions.ifix.Sub_country_spinner.Sub_country_spinner;
import com.smartsoftwaresolutions.ifix.Sub_country_spinner.Sub_country_spinner_adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Country extends AppCompatActivity {
    Spinner sp_country,sp_country_sub;
    List<country_spinner> LabelList;
    spinnerAdapter SADA;
    Button btn_country_save;
    DataHelper AndPOS_dbh;
    SQLiteDatabase AndPOS_db;
    SharedPreferences SpType ;
    Sub_country_spinner_adapter SSADA;
    List<Sub_country_spinner> SLabelList;
    public static final String PREFS_NAME = "ifix_data";
    String country_id,SP_country_id;
    int Cposition,CSposition;
    ImageButton img_btn_uk,img_btn_uae;
    String lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

//sqlite data inspection intilisation
        SpType=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
// read the shared preferance to check if there is a record in the contry id
        lang = SpType.getString("lang", "en_US");
        SP_country_id=SpType.getString("User_country_ID", "");
        Cposition=SpType.getInt("Cposition",0);
        CSposition=SpType.getInt("CSposition",0);


        sp_country=findViewById(R.id.sp_country);
        sp_country_sub=findViewById(R.id.sp_country_sub);
        img_btn_uk=findViewById(R.id.img_btn_uk);
        img_btn_uae=findViewById(R.id.img_btn_uae);


        // we create an instance of data base helper  database this will create the database
        AndPOS_dbh = new DataHelper(getApplicationContext());
        // and we connect the data to the data base helper and specify its use as a writable
        AndPOS_db = AndPOS_dbh.getWritableDatabase();
        btn_country_save=findViewById(R.id.btn_country_save);


        img_btn_uae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=SpType.edit();
                setLocale("ar");

                Toast.makeText(Country.this, "Locale in arabic !", Toast.LENGTH_LONG).show();

                editor.putString("lang","ar");
                editor.commit();
            }
        });
        img_btn_uk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=SpType.edit();
                editor.putString("lang","en_US");
                editor.commit();
                setLocale("en_US");
                //setLocale("n");

                Toast.makeText(Country.this, "Locale in English !", Toast.LENGTH_LONG).show();
                //finish();
            }
        });
//LoadSpinnerCountry();
LoadSpinnerCountry loadSpinnerCountry = new LoadSpinnerCountry();
        loadSpinnerCountry.execute();
        sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(SADA.getCount()==1){
                    LoadSpinnerCountry loadSpinnerCountry = new LoadSpinnerCountry();
                    loadSpinnerCountry.execute();
                }else {
                    LoadSpinnerSubCountry loadSpinnerSubCountry = new LoadSpinnerSubCountry();
                    loadSpinnerSubCountry.execute("");
                    country_spinner SelectedType;
                    // On selecting a spinner item
                    Cposition = position;
                    SelectedType=SADA.getItem(position);
                    //Label_UserType = parent.getItemAtPosition(position).toString();
                    String s = SelectedType.getCountry_name();

                    SharedPreferences.Editor editor = SpType.edit();
                    //  editor.putString("country_ID",SelectedType.country_id);
                    editor.putString("User_country_ID",SelectedType.getCountry_id());
                    editor.putInt("Cposition", position);
                    editor.commit();
                }

                // Showing selected spinner item
              //  Toast.makeText(parent.getContext(), "You selected: " + s,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_country_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               Sub_country_spinner Selected_sub_country;
               Selected_sub_country=(Sub_country_spinner) SSADA.getItem(position);
               String s=Selected_sub_country.getCS_Name();
                SharedPreferences.Editor editor = SpType.edit();
                CSposition=position;
                //  editor.putString("country_ID",SelectedType.country_id);
                editor.putString("User_Sub_Country_ID",Selected_sub_country.getCS_ID());
                editor.putInt("CSposition", position);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_country_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // we check if the position is 0 then the user didnt select the country he must select it
                if (Cposition==0||CSposition==0){
                    show_Alert();
                }else {
                    Intent mainIntent = new Intent(Country.this, Main_Activity.class);
                    startActivity(mainIntent);
                    finish();
                }

            }
        });
    }
    public void show_Alert(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.contruanddistrict))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
        if (Cposition==0 || CSposition==0){
            show_Alert();
        }else {
            Intent mainIntent = new Intent(Country.this, Main_Activity.class);
            startActivity(mainIntent);
            finish();
        }
//        Intent refresh = new Intent(this,Select_Main_option.class);
//        Intent refresh = new Intent(this, Main_Activity.class);
//        startActivity(refresh);
//        finish();
    }
    public class LoadSpinnerCountry extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SADA = new spinnerAdapter(LabelList, Country.this);
            // put them in the list view


            // attaching data adapter to spinner
            sp_country.setAdapter(SADA);
            //  sp_countryg.setSelection(Cposition);
            // put the selected position in the contry spinner
            if (!SP_country_id.equals("0")){
                sp_country.setSelection(Cposition);
            }else {
                sp_country.setSelection(0);
            }

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
                    country_name = cursor2.getString(cursor2.getColumnIndex("Country_name"));
                    country_name_ar = cursor2.getString(cursor2.getColumnIndex("Country_name_ar"));
                    country_code = cursor2.getString(cursor2.getColumnIndex("Country_code"));
// we fill the spiner with the info

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
      /*  ArrayAdapter<spinner> dataAdapter = new ArrayAdapter<spinner>(this,
                android.R.layout.simple_spinner_item, LabelList);*/
            SSADA = new Sub_country_spinner_adapter(SLabelList, Country.this);
            // put them in the list view


            // Drop down layout style - list view with radio button

            // attaching data adapter to spinner
            sp_country_sub.setAdapter(SSADA);
            // we read again the cposition to check if it is 0 or not

           Cposition= SpType.getInt("Cposition",0);
           if (Cposition==0){
               // dont set a first selection
           }else {
               if (CSposition!=0){
                   sp_country_sub.setSelection(CSposition);
               }
           }

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
                    Scountry_name = cursor2.getString(cursor2.getColumnIndex("CS_name"));
                    Scountry_name_ar = cursor2.getString(cursor2.getColumnIndex("CS_name_ar"));
                    country_id = cursor2.getString(cursor2.getColumnIndex("Country_ID"));

// we fill the spiner with the info

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
//    private void LoadSpinnerCountry() {
//        LabelList = new ArrayList<country_spinner>();
//
//        // Select Quary for a spicific category_spinner example bedroom the cat id is from put extra
//        String selectQuery = "SELECT * FROM country ";
//        //sqlite will returen a cursor a pointer to the first data
//        Cursor cursor2 = AndPOS_db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list the result is kids clasic and modern
//        if (cursor2.moveToFirst()) {
//            do {
//                String country_id,country_name,country_name_ar,country_code;
//
//                // we use put method to save the data in that map and we use the column name as the key
//                country_id= cursor2.getString(cursor2.getColumnIndex("Country_ID"));
//                country_name=cursor2.getString(cursor2.getColumnIndex("Country_name"));
//                country_name_ar=cursor2.getString(cursor2.getColumnIndex("Country_name_ar"));
//                country_code=cursor2.getString(cursor2.getColumnIndex("Country_code"));
//// we fill the spiner with the info
//
//                country_spinner country_Info=new country_spinner(country_id,country_name,country_name_ar,country_code);
//
//
//
//
//                // we add the saved map to the list one by one
//                this.LabelList.add(country_Info);
//
//                //  LabelList.add(cursor2.getString(cursor2.getColumnIndex("User_Type_Name")));
//            } while (cursor2.moveToNext());
//        }
//
//        cursor2.close();
//
//        // Creating adapter for spinner
//      /*  ArrayAdapter<spinner> dataAdapter = new ArrayAdapter<spinner>(this,
//                android.R.layout.simple_spinner_item, LabelList);*/
//        SADA=new spinnerAdapter(LabelList,Country.this);
//        // put them in the list view
//
//
//        // Drop down layout style - list view with radio button
//
//        // attaching data adapter to spinner
//        sp_country.setAdapter(SADA);
//        SADA.notifyDataSetChanged();
//        //Sp_UserType.setAdapter(dataAdapter);
//    }

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
if (Cposition==0){
    show_Alert();
}else {
    finish();
}


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
