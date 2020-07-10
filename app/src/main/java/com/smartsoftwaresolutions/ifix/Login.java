package com.smartsoftwaresolutions.ifix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.smartsoftwaresolutions.ifix.Database.DataHelper;
import com.smartsoftwaresolutions.ifix.Order_Service.order_one_master;

public class Login extends AppCompatActivity {
Button btn_forget,btn_Login2;
    String TO,MESSAGE,SUBJECT;
    CheckBox ch_remamber;
    public static final String PREFS_NAME = "ifix_data";
    SharedPreferences SpType ;
  Boolean flag;

    DataHelper AndPOS_dbh;
    SQLiteDatabase AndPOS_db;
    EditText et_Password,et_Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        // we create an instance of data base helper  database this will create the database
        AndPOS_dbh = new DataHelper(getApplicationContext());
        // and we connect the data to the data base helper and specify its use as a writable
        AndPOS_db = AndPOS_dbh.getWritableDatabase();

        ch_remamber=findViewById(R.id.ch_remamber);
        btn_forget=findViewById(R.id.btn_forget);
        btn_Login2=findViewById(R.id.btn_Login2);
        et_Password=findViewById(R.id.et_Password);
        et_Username=findViewById(R.id.et_Username);

        SpType=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        ch_remamber.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       // flag=SpType.getBoolean("remamber_password",true);

        SharedPreferences.Editor editor = SpType.edit();
        if (((CheckBox) v).isChecked()) {
            editor.putBoolean("remamber_password",true);
            Toast.makeText(Login.this,"your password has been saved ",Toast.LENGTH_LONG).show();
            editor.commit();


        }else {
            // always set remamber my password to true for now

          //  editor.putBoolean("remamber_password",false);
            editor.putBoolean("remamber_password",true);
            Toast.makeText(Login.this,"your password not saved ",Toast.LENGTH_LONG).show();
            editor.commit();
        }

    }
});
        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData();
                Intent intent ;
                intent = new Intent(Intent.ACTION_SEND);

                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{TO});
                intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
                intent.putExtra(Intent.EXTRA_TEXT, MESSAGE);

                intent.setType("message/rfc822");



                startActivity(Intent.createChooser(intent, "Select Email Sending App :"));
            }
        });
        btn_Login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogIn doLogIn =new DoLogIn();
                doLogIn.execute("");
            }
        });
    }
    public void GetData(){

        TO =getString(R.string.company_email_address) ;
        SUBJECT = getString(R.string.subject1);
        MESSAGE =getString(R.string.message1);

    }

    public class DoLogIn extends AsyncTask<String,String,String> {

        String z = "";
        Boolean isSuccess = false;

        //we read the username and the password from were the user enter them
        String userName = et_Username.getText().toString();
        String password = et_Password.getText().toString();


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(Login.this,r,Toast.LENGTH_SHORT).show();

            if(r.equals("Login successful")) {
                Intent i = new Intent(Login.this,My_Profile.class);
                startActivity(i);

                //  finish();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            //the tirm mean that without space
            if(userName.trim().equals("")|| password.trim().equals(""))
                z = "Please enter User Id and Password";
            else{
                // we create a query to select the spicifed user
                String query = "select User_Id,User_Name,User_Type_Id from User_Tbl where User_Name='" + userName + "' and User_Password='" + password + "'";
                // the cursor will endicate if there is a result to the select statment or not
                Cursor cursor = AndPOS_db.rawQuery(query, null);
                // the the retain quary is one  that mean there is a record for the user
                if(cursor.getCount()==1){

                    z = "Login successful";
                    isSuccess=true;
//                    if (cursor.moveToNext()){
//                        do {
//
//                            SharedPreferences.Editor editor = SpType.edit();
////                            String userId= String.valueOf( cursor.getInt(cursor.getColumnIndex("User_Id")) );
////                            String username= cursor.getString(cursor.getColumnIndex("User_Name")) ;
//                            int usertype= cursor.getInt(cursor.getColumnIndex("User_Type_Id")) ;
//                           // editor.putString( UserId,userId );
//                           // editor.putString( UserNamePer,username );
//                           // editor.putInt( UserType,usertype );
//                            if (usertype==1){
//                                editor.putString("M_type","1");
//                            }else if (usertype==2){
//                                editor.putString("M_type","2");
//                            }else {
//                                editor.putString("M_type","3");
//                            }
//
//                            editor.commit();
//                        }while (cursor.moveToNext());
//                    }

                }else {
                    z = "Invalid Credentials";
                    isSuccess = false;
                }
                cursor.close();
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
                Intent intent=new Intent(Login.this, Main_Activity.class);
                startActivity(intent);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
