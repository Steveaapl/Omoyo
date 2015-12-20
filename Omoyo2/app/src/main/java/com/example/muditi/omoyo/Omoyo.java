package com.example.muditi.omoyo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
    public static  int check=0;
    public static int widthofscreen;
    public static int heightofscreen;
    public static Display screendisplay;
    public static SharedPreferences shared;
    public static SharedPreferences.Editor edit;
    public static String currentShopId;
    public  static ArrayList<Bitmap> adsforhomebitmaplist=new ArrayList<Bitmap>();
    public static void  toast(String message,Context context){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    public static void shoploader(Context context){
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
}

