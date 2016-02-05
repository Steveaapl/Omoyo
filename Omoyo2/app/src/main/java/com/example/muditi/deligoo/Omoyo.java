package com.example.muditi.deligoo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.Display;
import android.widget.Toast;

import com.splunk.mint.Mint;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by muditi on 04-12-2015.
 */
public class Omoyo {
    public static boolean InternetCheck=true;
    public static boolean flagForSpinner = true;
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
    public static  String currentSerachData;
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

    public static void errorReportByMint(Context context){
        Mint.initAndStartSession(context, "2737c0e1");
    }


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


    private void calculateDistance(Context context , final String shop_id){
        final JSONArray putArray = new JSONArray();

        try {
                JSONArray jsonArray = new JSONArray(Omoyo.shared.getString("coordinateOfShop", "coordinate"));
                String latitude=null,longitude=null;
                for(int i=0 ; i<jsonArray.length() ; i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(jsonObject.getString("shop_id").equals(shop_id))
                    {
                        latitude = jsonObject.getString("shop_latitude");
                        longitude = jsonObject.getString("shop_longitude");
                    }
                }
                OkHttpClient okhttp = new OkHttpClient();
                StringBuilder builder = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?");
                builder.append("origins=" + Omoyo.shared.getString("latitude", "41.000") + "," + Omoyo.shared.getString("longitude", "41.0"));
                builder.append("&");
                builder.append("destinations=" +latitude+","+longitude);
                builder.append("&");
                builder.append("language=en");
                builder.append("&");
                builder.append("mode=driving");
                builder.append("&");
                builder.append("key="+context.getString(R.string.app_key));
                Request request = new Request.Builder().url(builder.toString()).get().build();
                Call call = okhttp.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {

                        if(response.isSuccessful()) {
                            final String data = response.body().string();
                            try {
                                JSONObject putObject = new JSONObject();
                                JSONObject jsonObject1 = new JSONObject(data);
                                if(jsonObject1.getString("status").equals("OK"))
                                    try {
                                        putObject.put("shop_id", shop_id);
                                        putObject.put("distance", jsonObject1.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getString("value"));
                                        JSONArray jsonArray1 = new JSONArray(Omoyo.shared.getString("distance",""));
                                        jsonArray1.put(jsonArray1.length(),putObject);
                                        Omoyo.edit.putString("distance", jsonArray1.toString());
                                        Omoyo.edit.commit();
                                    } catch (JSONException jx) {

                                    }
                            }
                            catch(JSONException jj){

                            }
                        }
                    }
                });

        }
        catch(JSONException jx){

        }

    }

    public static  void  addtofavorets(int type_of , JSONObject data){
        try {
            if (!Omoyo.shared.contains("favorets")) {
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type_of", type_of);
                jsonObject.put("data",data);
                jsonArray.put(0, jsonObject);
                Omoyo.edit.putString("favorets", jsonArray.toString());
                Omoyo.edit.commit();

            } else {
                JSONArray jsonArray = new JSONArray(Omoyo.shared.getString("favorets", ""));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type_of", type_of);
                jsonObject.put("data",data);
                jsonArray.put(jsonArray.length(), jsonObject);
                Omoyo.edit.putString("favorets", jsonArray.toString());
                Omoyo.edit.commit();
            }
        }
        catch(JSONException ej){

        }
    }

    public static  void  addtoCall( JSONObject data){
        try {
            if (!Omoyo.shared.contains("call_log")) {

                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("data",data);
                jsonArray.put(0, jsonObject);
                Omoyo.edit.putString("call_log", jsonArray.toString());
                Omoyo.edit.commit();

            } else {
                JSONArray jsonArray = new JSONArray(Omoyo.shared.getString("call_log", ""));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("data",data);
                jsonArray.put(jsonArray.length(), jsonObject);
                Omoyo.edit.putString("call_log", jsonArray.toString());
                Omoyo.edit.commit();
            }
        }
        catch(JSONException ej){

        }
    }


    public static  void  addtoSms(JSONObject data){
        try {
            if (!Omoyo.shared.contains("sms_log")) {

                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("data",data);
                jsonArray.put(0, jsonObject);
                Omoyo.edit.putString("sms_log", jsonArray.toString());
                Omoyo.edit.commit();

            } else {
                JSONArray jsonArray = new JSONArray(Omoyo.shared.getString("sms_log", ""));
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("data",data);
                jsonArray.put(jsonArray.length(), jsonObject);
                Omoyo.edit.putString("sms_log", jsonArray.toString());
                Omoyo.edit.commit();
            }
        }
        catch(JSONException ej){

        }
    }
    public static  boolean isConnectingToInternet( Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    public static String Date(){
        Calendar calendar = Calendar.getInstance();
        return new StringBuilder()
                .append(calendar.get(Calendar.DAY_OF_MONTH))
                .append("-")
                .append(calendar.get(Calendar.MONTH)+1)
                .append("-")
                .append(calendar.get(Calendar.YEAR))
                .append("/")
                .append(calendar.get(Calendar.HOUR_OF_DAY))
                .append(":")
                .append(calendar.get(Calendar.MINUTE))
                .append(":")
                .append(calendar.get(Calendar.SECOND))
                .toString();


      //  String format = "yyyy-MM-dd HH:mm:ss";
     //   SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
     //   return sdf.format(new Date(0));
    }

}

