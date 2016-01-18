package com.example.muditi.omoyo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.Display;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by muditi on 04-12-2015.
 */
public class Omoyo {
    public static boolean InternetCheck=true;
    public static int Request_Code =1;
    public static  int check=0;
    public static int widthofscreen;
    public static int heightofscreen;
    public static Display screendisplay;
    public static SharedPreferences shared;
    public static SharedPreferences.Editor edit;
    public static String currentShopId;
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static  int fromWhereCode=0;
    public static  String generatedOfferCode;
    public static String offer_description="";
    public static final String PACKAGE_NAME =
            "com.example.muditi.omoyo";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public  static ArrayList<Bitmap> adsforhomebitmaplist=new ArrayList<Bitmap>();
    public static Uri uri;
    public static void  toast(String message,Context context){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    public static void shoploader(final Context context){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"shop_id\" : \"%s\"}", Omoyo.currentShopId);
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+context.getResources().getString(R.string.ip)+"/shop/").post(requestbody).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    Omoyo.edit.putString("shop", data);
                    Omoyo.edit.commit();

                }
            }
        });
    }


    public static  void sendMobileNumberToServer(Context context ,String user_cell_number){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"user_id\" : \"%s\",\"user_mobile_number\" : \"%s\"}",Omoyo.shared.getString("user_id","1007"),user_cell_number);
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+context.getResources().getString(R.string.ip)+"/userMobileNumberDataEntry/").post(requestbody).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()){
                    String data = response.body().string();
                    Omoyo.edit.putBoolean("user_mobile_number_sended_to_server_successfully", true);
                    Omoyo.edit.commit();
                }
            }
        });
    }

    public static void checkingUserMobileSendedOrNotToServer(Context context){
        if(Omoyo.shared.getBoolean("user_status",false)){
                         if( !Omoyo.shared.getBoolean("user_mobile_number_sended_to_server_successfully",false)){
                               Omoyo.sendMobileNumberToServer(context,Omoyo.shared.getString("user_mobile_number",context.getResources().getString(R.string.welcome)));
                         }
        }
    }


}

