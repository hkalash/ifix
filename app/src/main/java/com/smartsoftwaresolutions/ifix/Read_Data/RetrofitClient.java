package com.smartsoftwaresolutions.ifix.Read_Data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
///////////////  private static final String BASE_URL = "http://192.168.1.122:8080/API/";
private static final String BASE_URL = "http://ifiixed.com/";
  //  private static final String BASE_URL = "http://dfm900064558.domain.com/API/";
// private static final String BASE_URL = "http://ifiixed.com/API/";
 // private static final   String BASE_URL = "http://66.96.162.146/API/";

  private static RetrofitClient mInstance;
  private Retrofit retrofit;

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

  private RetrofitClient(){
      OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
              .callTimeout(0, TimeUnit.MINUTES)
              .connectTimeout(30, TimeUnit.SECONDS)
              .readTimeout(30, TimeUnit.SECONDS)
              .writeTimeout(90, TimeUnit.SECONDS)
              ;

//      retrofit=new Retrofit.Builder()
//              .baseUrl(BASE_URL)
//              .addConverterFactory(GsonConverterFactory.create(gson))
////              .addConverterFactory(GsonConverterFactory.create())
//              .build();
      Retrofit.Builder builder = new Retrofit.Builder()
              .baseUrl(BASE_URL)
              .client(httpClient.build())
              .addConverterFactory(GsonConverterFactory.create(gson));
//              .addConverterFactory(SimpleXmlConverterFactory.create());

     // builder.client(httpClient.build());

      retrofit = builder.build();
  }

public static synchronized RetrofitClient getInstance(){
      if (mInstance==null){
          mInstance=new RetrofitClient();
      }
      return mInstance;
}
public API getApi(){
   return retrofit.create(API.class);
}
}
