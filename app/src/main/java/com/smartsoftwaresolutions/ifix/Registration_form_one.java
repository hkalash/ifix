package com.smartsoftwaresolutions.ifix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartsoftwaresolutions.ifix.Database.DataHelper;
import com.smartsoftwaresolutions.ifix.Order_Service.order_one_master;
import com.smartsoftwaresolutions.ifix.Pager.ViewPagerAdapter;
import com.smartsoftwaresolutions.ifix.Read_Data.API;
import com.smartsoftwaresolutions.ifix.country_spinner.Country;
import com.smartsoftwaresolutions.ifix.country_spinner.country_spinner;
import com.smartsoftwaresolutions.ifix.country_spinner.spinnerAdapter;
import com.smartsoftwaresolutions.ifix.secret_ans_spinner.ans_adapter;
import com.smartsoftwaresolutions.ifix.secret_ans_spinner.ans_spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Registration_form_one extends AppCompatActivity {
Button btn_Register,btn_Login;
EditText et_Username,et_Password,et_answer;
   // ProgressBar progressBar;
    DataHelper AndPOS_dbh;
    SQLiteDatabase AndPOS_db;
    SharedPreferences SpType ;
  //  Spinner ans_spinner1;
    ans_adapter SADA;
    List<ans_spinner> LabelList;
    public static final String PREFS_NAME = "ifix_data";
    String SP_user_id,username,userpassword,user_answer;
    ViewPager img_ad;
    private String [] imageurl=new String[]{
            API.Ad_Image+"ad1.jpg", API.Ad_Image+"ad2.jpg",
            API.Ad_Image+"ad3.jpg", API.Ad_Image+"ad4.jpg", API.Ad_Image+"ad5.jpg"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        // we create an instance of data base helper  database this will create the database
        AndPOS_dbh = new DataHelper(getApplicationContext());
        // and we connect the data to the data base helper and specify its use as a writable
        AndPOS_db = AndPOS_dbh.getWritableDatabase();

        SpType=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SP_user_id=SpType.getString("user_id","");
        btn_Login=findViewById(R.id.btn_Login);
        btn_Register=findViewById(R.id.btn_Register);
       // ans_spinner1=findViewById(R.id.ans_spinner);
        et_Password=findViewById(R.id.et_Password);
        et_Username=findViewById(R.id.et_Username);
        et_answer=findViewById(R.id.et_answer);
      //  progressBar= findViewById(R.id.progressBar_reg);
        Loandspinner();

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // we will check first if the user is registered or not
                // if not the user must enter the username and password then he will complete the registration form
                // then if the user is registered the registration form is gone
                Register_check();
            }
        });

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Registration_form_one.this,Login.class);
                startActivity(i);
            }
        });


        // first the prograsbar is not visible when we open the app
       // progressBar.setVisibility(View.GONE);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Registration_form_one.SliderTimer(), 4000, 6000);
        img_ad=findViewById(R.id.vp_ad1);
        //   indicator=(TabLayout)findViewById(R.id.indicator);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this,imageurl);
        img_ad.setAdapter(viewPagerAdapter);
//        ans_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ans_spinner SelectedType;
//                // On selecting a spinner item
//                SelectedType=SADA.getItem(position);
//                //Label_UserType = parent.getItemAtPosition(position).toString();
//                String s = SelectedType.getQuestion();
//                SharedPreferences SpType = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//                SharedPreferences.Editor editor = SpType.edit();
//                editor.putString("S_question",s);
//                editor.commit();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    private void Loandspinner() {
        LabelList = new ArrayList<ans_spinner>();
        String q1=getString(R.string.f);
        String q2=getString(R.string.c);
        String q3=getString(R.string.ff);
ans_spinner ans_spinner=new ans_spinner(q1);
        this.LabelList.add(ans_spinner);
        ans_spinner ans_spinner2=new ans_spinner(q2);
        this.LabelList.add(ans_spinner2);
        ans_spinner ans_spinner3=new ans_spinner(q3);
        this.LabelList.add(ans_spinner3);

        SADA=new ans_adapter(LabelList,this);
        // put them in the list view


        // Drop down layout style - list view with radio button

        // attaching data adapter to spinner
     //   ans_spinner1.setAdapter(SADA);
    }

    private void Register_check() {
       username=et_Username.getText().toString().trim();
      userpassword=et_Password.getText().toString().trim();
      user_answer=et_answer.getText().toString().trim();
      if (username.isEmpty()){
          et_Username.setError("user name is required ");
          et_Username.requestFocus();
          return;
      }
      if (userpassword.isEmpty()){
          et_Password.setError("Password is required");
          et_Password.requestFocus();
          return;
      }
      if (userpassword.length()<6){
          et_Password.setError("password must be more than 6 character");
          et_Password.requestFocus();
          return;
      }
      if (user_answer.isEmpty()){
          et_answer.setError("You must answer the question");
          et_answer.requestFocus();
          return;
      }

        Complet_Register complet_register =new Complet_Register();
        complet_register.execute("");
      /**
       Do the registration
       * **/
    }


    public class Complet_Register extends AsyncTask<String,String,String> {

        String z = "";
        Boolean isSuccess = false;

        //we read the username and the password from were the user enter them
//        String userName = et_Username.getText().toString();
//        String password =et_Password.getText().toString();


        @Override
        protected void onPreExecute() {
            // we make the prograsbar is visible
           // progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            // at the end of the verfication the prograssbar is of
         //   progressBar.setVisibility(View.GONE);
            //r is the result if sucsess or not
            //if we find the user in the database or not and we open the program
            Toast.makeText(Registration_form_one.this,r,Toast.LENGTH_SHORT).show();

            if(isSuccess) {
              //  Intent i=new Intent(Registration_form_one.this, Register_a_service_form_two.class);
                Intent i=new Intent(Registration_form_one.this, Member_Type.class);
                startActivity(i);
finish();

            }

        }

        @Override
        protected String doInBackground(String... params) {
            //the tirm mean that without space
            if(username.trim().equals("")|| userpassword.trim().equals(""))
                z = "Please enter User Id and Password";
            else {

                // the user is in the registration form that means that the user and password should be add to the table

               // AndPOS_db.execSQL("INSERT INTO User_Tbl (User_Name,User_Password,User_Type_Id)VALUES (" + userName + "," + password + ",1)");
                ContentValues values = new ContentValues();
                // the customer Id is auto increament

                values.put("User_Name",username);
                values.put("User_Password", userpassword);
                values.put("User_Type_Id",1);
                values.put("User_secret_ans",user_answer);
                values.put("User_Phone","");
                values.put("User_Mobile","");
                values.put("User_Address","");


                //the insert() will give -1 if the statment failed and id number if successfully inserted
                // it took table name and the value we make a content value to put all the column names in it
                // we create a query to select the spicifed user
                String query = "select User_Id,User_Name,User_Type_Id from User_Tbl where User_Name='" + username + "' and user_password='" + userpassword + "'";
                // the cursor will endicate if there is a result to the select statment or not
                Cursor cursor = AndPOS_db.rawQuery(query, null);
                // the the retain quary is one  that mean there is a record for the user
                if(cursor.getCount()>1){
                    // there is a record in the sql
                }else {
                    try {
                        long rowInserted = AndPOS_db.insertOrThrow("User_Tbl", null, values);
                        if (rowInserted != -1) {
                            //Toast.makeText(AddCustomer.this, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                            z = "username and password saved successfully";
                            // Writing data to SharedPreferences
                            SharedPreferences SpType = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = SpType.edit();
                            editor.putString("user_id",Long.toString(rowInserted));
                            editor.putString("username",username);
                            editor.putString("userpassword",userpassword);
                            editor.putString("secret_ans",user_answer);

                            editor.putBoolean("remamber_password",false);
                            editor.commit();
                            isSuccess=true;
                        } else {
                            //  Toast.makeText(AddCustomer.this, "Something wrong", Toast.LENGTH_SHORT).show();
                            z = "Something wrong";
                        }
                    } catch (SQLiteConstraintException e1) {
                        //Toast.makeText(AddCustomer.this,  e1.toString(),Toast.LENGTH_LONG).show();
                        z = e1.toString();
                        // z = "The Item name  must be unique Please check it";
                    }
                }




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
//                Intent intent=new Intent(Registration_form_one.this, Main_Activity.class);
//                startActivity(intent);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            Registration_form_one.this.runOnUiThread(new Runnable() {
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
