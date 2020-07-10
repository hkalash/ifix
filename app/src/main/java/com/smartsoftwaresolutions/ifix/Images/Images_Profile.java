package com.smartsoftwaresolutions.ifix.Images;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatDialog;
import android.Manifest;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartsoftwaresolutions.ifix.Config;
import com.smartsoftwaresolutions.ifix.CustomRVItemTouchListener;
import com.smartsoftwaresolutions.ifix.Database.DataHelper;
import com.smartsoftwaresolutions.ifix.Main_Activity;
import com.smartsoftwaresolutions.ifix.My_Profile;
import com.smartsoftwaresolutions.ifix.Order_Service.order_one_master;
import com.smartsoftwaresolutions.ifix.Pager.Image_list;
import com.smartsoftwaresolutions.ifix.Pager.Viewpager_adapter1;
import com.smartsoftwaresolutions.ifix.R;
import com.smartsoftwaresolutions.ifix.Read_Data.API;
import com.smartsoftwaresolutions.ifix.RecyclerViewItemClickListener;
import com.smartsoftwaresolutions.ifix.Register_a_service_form_two;
import com.smartsoftwaresolutions.ifix.Worker_Profile;
import com.smartsoftwaresolutions.ifix.country_spinner.country_spinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class Images_Profile extends AppCompatActivity {

    public static final String KEY_User_Document1 = "doc1";
    public static final String PREFS_NAME = "ifix_data";
    private static final int SELECTED_PIC = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final String TAG = Register_a_service_form_two.class.getSimpleName();
    Uri uri;
   // ImageView IDProf;
    Button btn_done2;
    RecyclerView R_Category_List;
    SharedPreferences SpType;
    String Customer_type;
    String filepath, filepath_g,SQL_image_name, SQL_image_path, SQL_image_uri = "",SWM_ID;
    Image_Adapter ADA;
    int number_of_image_added = 0;
    int max_image_number;
    File file;
    Itemlist_image itemlist_image;
    ArrayList<Itemlist_image> imageList;
    ArrayList<Itemlist_image> imageList2;
    DataHelper AndPOS_dbh;
    SQLiteDatabase AndPOS_db;
    private int PERMISSION_CAMERA_CODE = 23;
    private int STORAGE_PERMISSION_CODE = 25;
    private int STORAGE_PERMISSION_CODE_EXTERNAL = 24;
    private String Document_img1 = "";
    Boolean flag = false,from_worker_profile=false;
    int Selected_Position;
    AlertDialog myalertDialog;
    Image_Adapter_2 ADA2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        // we create an instance of data base helper  database this will create the database
        AndPOS_dbh = new DataHelper(getApplicationContext());
        // and we connect the data to the data base helper and specify its use as a writable
        AndPOS_db = AndPOS_dbh.getWritableDatabase();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        R_Category_List = findViewById(R.id.CL1);
        R_Category_List.setLayoutManager(layoutManager);
        // we will check if the intent have an extra in it or not
        if( getIntent().getExtras() != null)
        {
//            intent.putExtra("SWM_ID",SWM_ID);
//            intent.putExtra("is_worker_profile",true);
            from_worker_profile=getIntent().getBooleanExtra("is_worker_profile",false);
            SWM_ID=getIntent().getStringExtra("SWM_ID");
        }
/**first  we will read the shared periferance of the customer type **/
        SpType = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        // Reading from SharedPreferences
        Customer_type = SpType.getString("M_type", "2");
        max_image_number = SpType.getInt("Max_image_number", 3);
        // i dont need to check the customer type if it is from worker profile




      //  IDProf = (ImageView) findViewById(R.id.IdProf);
     //   Upload_Btn = (Button) findViewById(R.id.UploadBtn);
        btn_done2 = (Button) findViewById(R.id.btn_done2);
     //   btn_save1 = (Button) findViewById(R.id.btn_save1);

        if (from_worker_profile){
            btn_done2.setVisibility(View.GONE);
            load_img load_img=new load_img();
            load_img.execute();

        }else {
            // load image from the sqlite
            if (Customer_type.equals("1")) {
                // vip to be considered

            } else if (Customer_type.equals("2")) {
                // gold five pic have
                fill_Image_first(3);
            } else {
                // free one picture have
                fill_Image_first(1);
            }
        }
btn_done2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // we check if the image table have recordes in it or not to
        String query = "select * from Image_tbl LIMIT 1 ";
        // the cursor will endicate if there is a result to the select statment or not
        Cursor cursor = AndPOS_db.rawQuery(query, null);
        // the the retain quary is one  that mean there is a record for the user

        if (cursor.getCount() == 0) {
            // which mean the user did select any picture
            Toast.makeText(Images_Profile.this, "Please select a picture", Toast.LENGTH_LONG).show();
            cursor.close();



        }else {
            Intent intent=new Intent(Images_Profile.this, My_Profile.class);
            startActivity(intent);
            finish();
        }

    }
});
//        btn_save1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (number_of_image_added >= max_image_number) {
//                    Toast.makeText(Images_Profile.this, "you can not add any more Images ", Toast.LENGTH_LONG).show();
//
//                } else if (SQL_image_uri.equals("")) {
//                    Toast.makeText(Images_Profile.this, "Please select an image", Toast.LENGTH_LONG).show();
//
//                } else {
//                    /**add the imagers to the sql and refrish the recycler view**/
//                    // SQL_image_uri=uri.toString();
//                    SQL_Add_image sql_add_image = new SQL_Add_image();
//                    sql_add_image.execute("");
//                    number_of_image_added = number_of_image_added + 1;
//                    ADA.notifyDataSetChanged();
//                }
//            }
//        });
R_Category_List.addOnItemTouchListener(new CustomRVItemTouchListener(this, R_Category_List, new RecyclerViewItemClickListener() {
    @Override
    public void onClick(View view, int position) {
        show_Alert_Dialog();
        // save the selected position
        Selected_Position=position;

    }

    @Override
    public void onLongClick(View view, int position) {

    }
}));
//        Upload_Btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                show_Alert_Dialog();
//            }
//        });
    }

    public class  load_img extends AsyncTask<String ,String ,String >{


        @Override
        protected void onPostExecute(String s) {
//            Viewpager_adapter1 viewPagerAdapter=new Viewpager_adapter1(Images_Profile.this,imageurl);

          //  profile_pic.setAdapter(viewPagerAdapter);
            ADA2 = new Image_Adapter_2(Images_Profile.this, imageList2);
            R_Category_List.setAdapter(ADA2);
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
            imageList2 = new ArrayList<Itemlist_image>();
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
                               // Pic_url= API.Profile_Path+response.body().get(i).getSWM_IMG_path();
                                itemlist_image = new Itemlist_image(response.body().get(i).getSWM_IMG_path());
                                imageList2.add(itemlist_image);
                                //     );

//                                imageurl.add(Pic_url);
//
//                                profile_pic.getAdapter().notifyDataSetChanged();
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

private void Save_to_sql(){
    SQL_Add_image sql_add_image = new SQL_Add_image();
                  sql_add_image.execute("");
                    number_of_image_added = number_of_image_added + 1;
                   ADA.notifyDataSetChanged();
}
    private void fill_Image_first(int Image_number) {
        imageList = new ArrayList<Itemlist_image>();
        /**  we check if there an image saved in the image table or not
         **/
        // we check if the image table have recordes in it or not to
        String query = "select * from Image_tbl ";
        // the cursor will endicate if there is a result to the select statment or not
        Cursor cursor = AndPOS_db.rawQuery(query, null);
        // the the retain quary is one  that mean there is a record for the user
        if(cursor.getCount()>=1){
Uri sql_uri;
            // Toast.makeText(this, "what ever",Toast.LENGTH_LONG).show();
            if (cursor.moveToFirst()) {
                do {
                    String image_id,image_name,image_path,image_uri;

                    // we use put method to save the data in that map and we use the column name as the key
                    image_id= cursor.getString(cursor.getColumnIndex("imgae_ID"));
                    image_name=cursor.getString(cursor.getColumnIndex("Image_name"));
                    image_path=cursor.getString(cursor.getColumnIndex("image_path"));
                    image_uri=cursor.getString(cursor.getColumnIndex("image_uri"));
// we fill the list item  with the info


                    itemlist_image = new Itemlist_image(image_uri);
                    imageList.add(itemlist_image);




                    //  LabelList.add(cursor2.getString(cursor2.getColumnIndex("User_Type_Name")));
                } while (cursor.moveToNext());
            }

           // cursor.close();

        }
        if(cursor.getCount()==0){
           /** fill the list with empty image */
            for (int i = 0; i < Image_number; i++) {
               // String image_name = "file:///android_asset/add_photo.png";
                String image_name = "empty";


                itemlist_image = new Itemlist_image(image_name);

                imageList.add(itemlist_image);
            }
        }else if (cursor.getCount()<max_image_number){
            int remain=max_image_number-cursor.getCount();
            for (int i = 0; i < remain; i++) {
                //String image_name ="file:///android_asset/add_photo.png";
                String image_name ="empty";


                itemlist_image = new Itemlist_image(image_name);

                imageList.add(itemlist_image);
            }
        }
        cursor.close();


/**add the image list to the adapter so we can use it in the recycler view**/
        ADA = new Image_Adapter(Images_Profile.this, imageList);
        R_Category_List.setAdapter(ADA);


    }

    private void show_Alert_Dialog() {
        final Context context = Images_Profile.this;

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(customLayout);
        builder.setTitle(getString(R.string.select_a_picture));
        ImageButton btn_camera = customLayout.findViewById(R.id.btn_camera);
        ImageButton btn_gallary = customLayout.findViewById(R.id.btn_gallary);
/**take image from the gallary**/
        btn_gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkAndRequestPermissions()) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                        myalertDialog.dismiss();
                    }
                } else {
                    if (checkAndRequestPermissions()) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                        myalertDialog.dismiss();
                    }
                }


            }
        });
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                new DialogInterface.OnClickListener() {
////                   public void onClick(DialogInterface dialog, int which) {
////                       dialog.dismiss();
////
////                    }
//           //    };
//
//            }
//        });
        builder.setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() { // define the 'Cancel' button
            public void onClick(DialogInterface dialog, int which) {
                //Either of the following two lines should work.
               // dialog.cancel();
                //dialog.dismiss();
                myalertDialog.dismiss();
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                  ActivityCompat.requestPermissions(Images_Profile.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
//               ActivityCompat.requestPermissions(Images_Profile.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE_EXTERNAL);
//                ActivityCompat.requestPermissions(Images_Profile.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_CODE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkAndRequestPermissions()) {
                        openCamera();
                        myalertDialog.dismiss();
                    }
                } else {
                    if (checkAndRequestPermissions()) {
                    openCamera();
                    myalertDialog.dismiss();
                    }
                }



            }
        });

//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // The 'which' argument contains the index position
//                        // of the selected item
//                        switch (which) {
//                            case R.id.btn_camera:
//                                Toast.makeText(context, "Camera clicked", Toast.LENGTH_LONG).show();
//                                break;
//                            case R.id.btn_cancel1:
//                                Toast.makeText(context, "Cancel", Toast.LENGTH_LONG).show();
//                                break;
//                            case R.id.btn_gallary:
//                                Toast.makeText(context, "Gallary", Toast.LENGTH_LONG).show();
//                                break;
//
//                        }
//                    }
//                };
      //  builder.create().show();
     myalertDialog= builder.create();
     myalertDialog.show();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // create the output file which is the file name and the file path
        uri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        /** creatr a temp file for the first image**/
        //File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 1);
    }

    private boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        int permissionReadStorage = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissionReadStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        int permissionWriteStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 402);
            return false;
        }
        return true;
    }
    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
/** get the file path**/
        // return Uri.fromFile(getOutputMediaFile(type));
        return FileProvider.getUriForFile(Images_Profile.this,
                "com.smartsoftwaresolutions.ifix.fileprovider",
                getOutputMediaFile(type));
//        FileProvider.getUriForFile(Images_Profile.this,
//                BuildConfig.APPLICATION_ID + ".provider",
//                getOutputMediaFile(type));
    }

    /**
     * returning image / video file name
     */
    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        // CREATE THE MEDIA FILE ==> THE FILE NAME
        if (type == MEDIA_TYPE_IMAGE) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "IMG_" + timeStamp + ".jpg");
            SQL_image_name = "IMG_" + timeStamp + ".jpg";
            SQL_image_path = mediaStorageDir.getPath();
            mediaFile = new File(SQL_image_path + File.separator
                    + SQL_image_name);

        } else {
            return null;
        }
//        } else if (type == MEDIA_TYPE_VIDEO) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "VID_" + timeStamp + ".mp4");
//        }

        return mediaFile;
    }



    @Override
  public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // uri = data.getData();
        if (resultCode == RESULT_OK)
        {
            /** from the camera **/
            if (requestCode == 1) {

                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;

                    }
                }
                try {
//                    Uri selectedImageURI = data.getData();
//                    Itemlist_image placeWorkModel = new Itemlist_image("image one",); // the model between activity and adapter
//                    placeWorkModel.setPhoto(Integer.parseInt(convertImage2Base64()));  // here i pass the photo
//                   imageList.add(placeWorkModel);
//
//                   ADA.updateList(imageList); // add this
//
//                    ADA.notifyDataSetChanged();
                    /** show the image in the image view**/
                    filepath = uri.getPath();
                 //   SQL_image_path=filepath;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                 //       IDProf.setBackground(null);
                    }
                    //IDProf.setImageURI(uri);
                    /** save the image in the postion that the use click**/
                    Itemlist_image Selected_Image;
                   Selected_Image= ADA.getItem(Selected_Position);
                   Selected_Image.setImage_uri(String.valueOf(uri));
                   ADA.notifyDataSetChanged();
                    SQL_image_uri = uri.toString();

                    Save_to_sql();
//                    Bitmap bitmap;
//                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//
//                    bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                    bitmap = BitmapFactory.decodeFile(filepath, bitmapOptions);
//                    bitmap=getResizedBitmap(bitmap, 400);
//                    IDProf.setImageBitmap(bitmap);
//                    BitMapToString(bitmap);
///uri=data.getData();
                    //  uri=getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    //  filepath=uri.getPath();

                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(uri, filePath, null, null, null);
                    if (c != null) {
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        //  String picturePath = c.getString(columnIndex);
                        filepath = c.getString(columnIndex);
                        SQL_image_path=filepath+"/"+SQL_image_name;
                        //SQL_image_path=filepath;
                        c.close();

                    }


                    f.delete();
myalertDialog.dismiss();
//                    String[] filePath = { MediaStore.Images.Media.DATA };
//                    Cursor c = getContentResolver().query(uri,filePath, null, null, null);
//                    c.moveToFirst();
//                    int columnIndex = c.getColumnIndex(filePath[0]);
                    //  String picturePath = c.getString(columnIndex);
//                    filepath = c.getString(columnIndex);
//                    c.close();
//                    String path = android.os.Environment
//                            .getExternalStorageDirectory()
//                            + File.separator
//                            + "Phoenix" + File.separator + "default";
//                    f.delete();
//                    filepath = android.os.Environment
//                            .getExternalStorageDirectory()
//                            + File.separator
//                            + "Phoenix" + File.separator + "default";
//                    f.delete();
//
//                    OutputStream outFile = null;
//                    /** we remove the temp file and create a new file name**/
//                    File file = new File(filepath, String.valueOf(System.currentTimeMillis()) + ".jpg");
//               //     uri=Uri.fromFile(file);
//                    try {
//                        outFile = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
//                        outFile.flush();
//                        outFile.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (requestCode == 2) { // from galary
                // Uri selectedImage = data.getData();
                uri = data.getData();
                SQL_image_uri = uri.toString();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(uri, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                //  String picturePath = c.getString(columnIndex);
                filepath_g = c.getString(columnIndex);

                // External sdcard location
                File mediaStorageDir = new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        Config.IMAGE_DIRECTORY_NAME);

                // Create the storage directory if it does not exist
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d(TAG, "Oops! Failed create "
                                + Config.IMAGE_DIRECTORY_NAME + " directory");
                       // return null;
                    }
                }
                SQL_image_path = mediaStorageDir.getPath();
                // Create a media file name
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
                File mediaFile;
                // CREATE THE MEDIA FILE ==> THE FILE NAME
              //  if (type == MEDIA_TYPE_IMAGE) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "IMG_" + timeStamp + ".jpg");
                    SQL_image_name = "IMG_" + timeStamp + ".jpg";
                    SQL_image_path = mediaStorageDir.getPath();
                    mediaFile = new File(SQL_image_path + File.separator
                            + SQL_image_name);
                    uri=FileProvider.getUriForFile(Images_Profile.this,
                            "com.smartsoftwaresolutions.ifix.fileprovider",
                           mediaFile);
                SQL_image_uri = uri.toString();
                try {
                    copyFile(new File(getPath(data.getData())), mediaFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //     }
                  //  copyFile(new File(filepath_g, mediaFile);
               // SQL_image_path = filepath;
                c.close();
//                Bitmap thumbnail;
//                try {
//                    thumbnail = (BitmapFactory.decodeFile(filepath));
//                }catch (Exception ex){
//                    Log.e();
//
//                }
//             BitmapFactory.Options options;
//                Bitmap thumbnail = null;
//                try {
//                   // String imageInSD = "/sdcard/UserImages/" + userImageName;
//                    thumbnail = BitmapFactory.decodeFile(filepath);
//                  //  return bitmap;
//                } catch (OutOfMemoryError e) {
//                    try {
//                        options = new BitmapFactory.Options();
//                        options.inSampleSize = 2;
//                        thumbnail = BitmapFactory.decodeFile(filepath,options);
//
//                    } catch(Exception excepetion) {
//                       Log.e(excepetion);
//                    }
//                }
//
//                thumbnail = getResizedBitmap(thumbnail, 400);
                Log.w("path of image from", filepath + "");
          //      IDProf.setImageBitmap(thumbnail);
                /** save the image in the postion that the use click**/
                Itemlist_image Selected_Image;
                Selected_Image= ADA.getItem(Selected_Position);
                Selected_Image.setImage_uri(String.valueOf(uri));
                ADA.notifyDataSetChanged();
              //  BitMapToString(thumbnail);
                // save image to sql
                Save_to_sql();
                myalertDialog.dismiss();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Images_Profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
//    private void show_Alert_Dialog() {
//        final Context context=Images_Profile.this;
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog, null);
//        builder.setView(customLayout);
//        builder.setTitle("Title");
//        ImageButton btn_camera=customLayout.findViewById(R.id.btn_camera);
//        ImageButton btn_gallary=customLayout.findViewById(R.id.btn_gallary);
//
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
//                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
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

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 1) {
//                File f = new File(Environment.getExternalStorageDirectory().toString());
//                for (File temp : f.listFiles()) {
//                    if (temp.getName().equals("temp.jpg")) {
//                        f = temp;
//                        break;
//                    }
//                }
//                try {
//                    Bitmap bitmap;
//                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//                    bitmap=getResizedBitmap(bitmap, 400);
//                    IDProf.setImageBitmap(bitmap);
//                    BitMapToString(bitmap);
//                    String path = android.os.Environment
//                            .getExternalStorageDirectory()
//                            + File.separator
//                            + "Phoenix" + File.separator + "default";
//                    f.delete();
//                    OutputStream outFile = null;
//                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
//                    try {
//                        outFile = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
//                        outFile.flush();
//                        outFile.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else if (requestCode == 2) {
//                Uri selectedImage = data.getData();
//                String[] filePath = { MediaStore.Images.Media.DATA };
//                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePath[0]);
//                String picturePath = c.getString(columnIndex);
//                c.close();
//                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                thumbnail=getResizedBitmap(thumbnail, 400);
//                Log.w("path of image from", picturePath+"");
//                IDProf.setImageBitmap(thumbnail);
//                BitMapToString(thumbnail);
//            }
//        }
//    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    // Action Bar actions
    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

//    private void SendDetail() {
//        final ProgressDialog loading = new ProgressDialog(Images_Profile.this);
//        loading.setMessage("Please Wait...");
//        loading.show();
//        loading.setCanceledOnTouchOutside(false);
//        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfiURL.Registration_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            loading.dismiss();
//                            Log.d("JSON", response);
//
//                            JSONObject eventObject = new JSONObject(response);
//                            String error_status = eventObject.getString("error");
//                            if (error_status.equals("true")) {
//                                String error_msg = eventObject.getString("msg");
//                                ContextThemeWrapper ctw = new ContextThemeWrapper( Images_Profile.this, R.style.Theme_AlertDialog);
//                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//                                alertDialogBuilder.setTitle("Vendor Detail");
//                                alertDialogBuilder.setCancelable(false);
//                                alertDialogBuilder.setMessage(error_msg);
//                                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//
//                                    }
//                                });
//                                alertDialogBuilder.show();
//
//                            } else {
//                                String error_msg = eventObject.getString("msg");
//                                ContextThemeWrapper ctw = new ContextThemeWrapper( Images_Profile.this, R.style.Theme_AlertDialog);
//                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//                                alertDialogBuilder.setTitle("Registration");
//                                alertDialogBuilder.setCancelable(false);
//                                alertDialogBuilder.setMessage(error_msg);
////                                alertDialogBuilder.setIcon(R.drawable.doubletick);
//                                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        Intent intent=new Intent(Images_Profile.this,Log_In.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                });
//                                alertDialogBuilder.show();
//                            }
//                        }catch(Exception e){
//                            Log.d("Tag", e.getMessage());
//
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        loading.dismiss();
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            ContextThemeWrapper ctw = new ContextThemeWrapper( Images_Profile.this, R.style.Theme_AlertDialog);
//                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//                            alertDialogBuilder.setTitle("No connection");
//                            alertDialogBuilder.setMessage(" Connection time out error please try again ");
//                            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                }
//                            });
//                            alertDialogBuilder.show();
//                        } else if (error instanceof AuthFailureError) {
//                            ContextThemeWrapper ctw = new ContextThemeWrapper( Images_Profile.this, R.style.Theme_AlertDialog);
//                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//                            alertDialogBuilder.setTitle("Connection Error");
//                            alertDialogBuilder.setMessage(" Authentication failure connection error please try again ");
//                            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                }
//                            });
//                            alertDialogBuilder.show();
//                            //TODO
//                        } else if (error instanceof ServerError) {
//                            ContextThemeWrapper ctw = new ContextThemeWrapper( Images_Profile.this, R.style.Theme_AlertDialog);
//                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//                            alertDialogBuilder.setTitle("Connection Error");
//                            alertDialogBuilder.setMessage("Connection error please try again");
//                            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                }
//                            });
//                            alertDialogBuilder.show();
//                            //TODO
//                        } else if (error instanceof NetworkError) {
//                            ContextThemeWrapper ctw = new ContextThemeWrapper( Images_Profile.this, R.style.Theme_AlertDialog);
//                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//                            alertDialogBuilder.setTitle("Connection Error");
//                            alertDialogBuilder.setMessage("Network connection error please try again");
//                            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                }
//                            });
//                            alertDialogBuilder.show();
//                            //TODO
//                        } else if (error instanceof ParseError) {
//                            ContextThemeWrapper ctw = new ContextThemeWrapper( Images_Profile.this, R.style.Theme_AlertDialog);
//                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//                            alertDialogBuilder.setTitle("Error");
//                            alertDialogBuilder.setMessage("Parse error");
//                            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                }
//                            });
//                            alertDialogBuilder.show();
//                        }
////                        Toast.makeText(Login_Activity.this,error.toString(), Toast.LENGTH_LONG ).show();
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> map = new HashMap<String,String>();
//                map.put(KEY_User_Document1,Document_img1);
//                return map;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(mRetryPolicy);
//        requestQueue.add(stringRequest);
//    }


//    @Override
//    public void onClick(View v) {
//        if (Document_img1.equals("") || Document_img1.equals(null)) {
//            ContextThemeWrapper ctw = new ContextThemeWrapper( Images_Profile.this, R.style.AlertDialogCustom);
//            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//            alertDialogBuilder.setTitle("Id Prof Can't Empty ");
//            alertDialogBuilder.setMessage("Id Prof Can't empty please select any one document");
//            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//
//                }
//            });
//            alertDialogBuilder.show();
//            return;
//        }
//        else{
//
////            if (AppStatus.getInstance(this).isOnline()) {
////                //SendDetail();
////
////
////                //           Toast.makeText(this,"You are online!!!!",Toast.LENGTH_LONG).show();
////
////            } else {
////
////                Toast.makeText(this,"You are not online!!!!", Toast.LENGTH_LONG).show();
////                Log.v("Home", "############################You are not online!!!!");
//         //   }
//
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case SELECTED_PIC:
//                if (resultCode == RESULT_OK) {
//                    uri = data.getData();
//                    String[] projection = {MediaStore.Images.Media.DATA};
//                    // selectedImage = data.getData();
//                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(projection[0]);
//                    filepath = cursor.getString(columnIndex);
//                    cursor.close();
//
//                    Bitmap bitmap = BitmapFactory.decodeFile(filepath);
//                    Drawable drawable = new BitmapDrawable(bitmap);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        image_profile.setBackground(drawable);
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.action_back:


                finish();
                return true;
            case R.id.action_Home:
                Intent intent=new Intent(Images_Profile.this, Main_Activity.class);
                startActivity(intent);

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class SQL_Add_image extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            SQL_image_uri = "";
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Image_name", SQL_image_name);
            contentValues.put("image_path", SQL_image_path);
            contentValues.put("image_uri", SQL_image_uri);

            // we check if the image table have recordes in it or not to
            String query = "select * from Image_tbl ";
            // the cursor will endicate if there is a result to the select statment or not
            Cursor cursor = AndPOS_db.rawQuery(query, null);
            // the the retain quary is one  that mean there is a record for the user

            if(cursor.getCount()<max_image_number){
                /**do insertion**/
                try {
                    long rowInserted = AndPOS_db.insertOrThrow("Image_tbl", null, contentValues);
                    if (rowInserted != -1) {
                        flag = true;
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

            }else {
                /**Do update*****/
                String sql_id=String.valueOf(Selected_Position+1 );
                long rowupdated=AndPOS_db.update("Image_tbl",contentValues,"imgae_ID=?",new String[]{sql_id});
            }

            return null;
        }
    }

//    @Override
//    protected void onPause() {
//        finish();
//        super.onPause();
//    }
}
