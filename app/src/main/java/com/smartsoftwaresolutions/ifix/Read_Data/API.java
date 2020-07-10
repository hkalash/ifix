package com.smartsoftwaresolutions.ifix.Read_Data;


import com.smartsoftwaresolutions.ifix.Order_list_chiled.Itemlistobject4;
import com.smartsoftwaresolutions.ifix.Pager.Image_list;
import com.smartsoftwaresolutions.ifix.Sub_country_spinner.Sub_country_spinner;
import com.smartsoftwaresolutions.ifix.country_spinner.country_spinner;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

public interface API {

   // String BASE_URL = "http://192.168.1.122/API/";
    //String BASE_URL = "http://ifiixed.com/API/";
   // String BASE_URL = "http://66.96.162.146/API/";
//////////////////String fixed_path="http://192.168.1.122:8080/API/";
String fixed_path="http://ifiixed.com/";
  // String JSONURL = "http://192.168.1.122:8080/API/";
     String JSONURL = fixed_path;
 //   String JSONURL = "http://www.ifixed.com/API/";
//    String Category_Image="http://192.168.1.122:8080/API/CI/";
//    String Ad_Image="http://192.168.1.122:8080/API/AI/";
//    String Profile_Path="http://192.168.1.122:8080/API/Image_Profile/";
//    String Sub_Category_Image="http://192.168.1.122:8080/API/MI/";

    String Category_Image=fixed_path+"CI/";
    String Ad_Image=fixed_path+"AI/";
    String Profile_Path=fixed_path+"Image_Profile/";
    String Sub_Category_Image=fixed_path+"MI/";

//    $SWM_Name = $_POST['SWM_Name'];
//    $SWM_Phone = $_POST['SWM_Phone'];
//    $SWM_Mobile = $_POST['SWM_Mobile'];
//    $SWM_Address = $_POST['SWM_Address'];
//    $SWM_Lat = $_POST['SWM_Lat'];
//    $SWM_Lon = $_POST['SWM_Lon'];
//    $SWM_Pic=$_POST['SWM_Pic'];
//    $SWM_Username = $_POST['SWM_Username'];
//    $SWM_Password = $_POST['SWM_Password'];
//    $SWM_RATE = $_POST['SWM_RATE'];
@Multipart
    @POST("Api_SWM.php?apicall=upload")
    Call<MyResponse1> createSWM_Recored(
            @Part("SWM_Name") String SWM_Name,//
            @Part("SWM_Phone")String SWM_Phone,
            @Part("SWM_Mobile")String SWM_Mobile,
            @Part("SWM_Web")String  web_address,
            @Part("SWM_Address")String SWM_Address,
            @Part("SWM_Lat")String SWM_Lat,
            @Part("SWM_Lon")String SWM_Lon,
            @Part("SWM_Pic\"; filename=\"myfile.jpg\" ") RequestBody file,
            @Part("SWM_Username")String SWM_Username,
            @Part("SWM_Password")String SWM_Password,
            @Part("SWM_RATE") String SWM_RATE ,
            @Part("SWM_company") String SWM_company , // is used for description
            @Part("SWM_POBOX") String SWM_POBOX , // is used for email
            @Part("SWM_Country_ID") int SWM_Country_ID ,// country id is an integer
            @Part("SWM_Sub_C") int SWM_Sub_C ,// country sub id is an integer
            @Part("SWM_secret_ans") String  SWM_secret_ans ,// // secret ansew is string
            @Part("WC_ID") int  WC_ID ,
            @Part("SWM_cust_type") int SWM_cust_type);
// // secret ansew is string

    @Multipart
    @POST("Api_SWMU.php?apicall=upload")
    Call<MyResponse1> updateSWM_Recored(
            @Part("SWM_ID") int SWM_ID,
            @Part("SWM_Name") String SWM_Name,
            @Part("SWM_Phone")String SWM_Phone,
            @Part("SWM_Mobile")String SWM_Mobile,
            @Part("SWM_Web")String  web_address,
            @Part("SWM_Address")String SWM_Address,
            @Part("SWM_Lat")String SWM_Lat,
            @Part("SWM_Lon")String SWM_Lon,
            @Part("SWM_Pic\"; filename=\"myfile.jpg\" ") RequestBody file,
            @Part("SWM_Username")String SWM_Username,
            @Part("SWM_Password")String SWM_Password,
            @Part("SWM_RATE") String SWM_RATE ,
            @Part("SWM_company") String SWM_company , // is used for description
            @Part("SWM_POBOX") String SWM_POBOX , // is used for email
            @Part("SWM_Country_ID") int SWM_Country_ID ,// country id is an integer
            @Part("SWM_Sub_C") int SWM_Sub_C ,// country sub id is an integer
            @Part("SWM_secret_ans") String  SWM_secret_ans ,// // secret ansew is string
            @Part("WC_ID") int  WC_ID ,
            @Part("SWM_cust_type") int SWM_cust_type);
    // // secret ansew is string

    @Multipart
    @POST("Api_IMG.php?apicall=IMG")
    Call<MyResponse1> createIMG_Recored(
            @Part("SWM_ID")int SWM_ID,
            @Part("SWM_Img_path\"; filename=\"myfile.jpg\" ") RequestBody file

             );
    @Multipart
    @POST("Api_IMGU.php?apicall=IMG")
    Call<MyResponse1> updateIMG_Recored(
            @Part("SWM_ID")int SWM_ID
    );
    @Multipart
    @POST("Api_Des_Like.php?apicall=upload")
    Call<MyResponse1> update_DLike_Recored(
            @Part("SWM_ID")int SWM_ID
    );
    @Multipart
    @POST("Api_Like.php?apicall=upload")
    Call<MyResponse1> update_Like_Recored(
            @Part("SWM_ID")int SWM_ID
//            @Part("SWM_Img_path\"; filename=\"myfile.jpg\" ") RequestBody file

    );

    @Multipart
    @POST("Api_S.php?apicall=S")
    Call<MyResponse1> createS_Recored(
            @Part("SWM_ID")int SWM_ID,
            @Part("MS_ID") int MS_ID);

//    @Multipart
//    @POST("Api_SU.php?apicall=S")
//    Call<MyResponse1> createSU_Recored(
//            @Part("SWM_ID")int SWM_ID,
//            @Part("MS_ID") int MS_ID);
    @Multipart
    @POST("Api_SU.php?apicall=S")
    Call<MyResponse1> createSU_Recored(
            @Part("SWM_ID")int SWM_ID);





/** for the category_spinner servise**/
    @GET("Api_SC.php?apicall=getallData_SC")
    Call<List<Category> >getString();

    /**for the sub category_spinner**/
    @GET("Api_MI.php?apicall=MI")
    Call<List<Sub_Category> >getString_Sub_Category();

    /**for select all workers for a specific job**/

    @GET("Api_SW.php")
    Call<List<Itemlistobject4>>getString_Selected_worker(
            @QueryMap Map<String, String> options
    );

    @GET("Api_SG.php")
    Call<List<Itemlistobject4>>getString_Selected_workerG(
            @QueryMap Map<String, String> options
    );
    @GET("Api_country.php?apicall=getallData_CO")
    Call<List<country_spinner> >getString_Country();

    @GET("Api_S_country.php?apicall=getallData_SCO")
    Call<List<Sub_country_spinner> >getString_S_Country();

    // read the images of selected worker
    @GET("Api_SW_Img.php")
    Call<List<Image_list>>get_Images(
            @QueryMap Map<String ,String > options
    );

    @GET("Api_SWC.php")
    Call<List<Itemlistobject4>>getString_Selected_worker_C(
            @QueryMap Map<String, String> options
    );

}
