package com.google.muditi.deligoo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.muditi.deligoo.Omoyo;
import com.example.muditi.deligoo.R;
import com.example.muditi.deligoo.SmsVarification;
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
import java.net.URLEncoder;

import butterknife.ButterKnife;

/**
 * Created by muditi on 25-01-2016.
 */
public class Dialog_For_Shop_Page extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int type_of = getArguments().getInt("type_of");
        com.google.muditi.deligoo.Omoyo.errorReportByMint(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog alertDialog =builder.create();
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        LayoutInflater inflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.dialog_layout, null);
        TextView text_view_for_welcome_in_dialog = ButterKnife.findById(view, R.id.text_view_for_welcome_in_dialog);
        TextView text_view_for_offer_upload_click = ButterKnife.findById(view, R.id.text_view_for_offer_upload_click);
        LinearLayout linear_layout_for_enter_child =ButterKnife.findById(view,R.id.linear_layout_for_dialog_child_insert);
        LinearLayout linear_layout_for_dialog_child =ButterKnife.findById(view,R.id.linear_layout_for_dialog_child);
        LinearLayout linear_layout_for_offer_upload_click =ButterKnife.findById(view,R.id.linear_layout_for_offer_upload_click);
        ImageView image_view_for_cross = ButterKnife.findById(view, R.id.image_view_for_cross);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.dialog_box_cross_rotation);
        image_view_for_cross.setAnimation(animation);
        image_view_for_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

       switch (type_of){
           case 0:
               text_view_for_offer_upload_click.setText(getResources().getString(R.string.sms_is_free));
               View view_of_sms =inflater.inflate(R.layout.dialog_for_shop, null);
               text_view_for_welcome_in_dialog.setText(getResources().getString(R.string.send_sms));
               final android.widget.EditText editText = ButterKnife.findById(view_of_sms,R.id.edit_view_for_sms);
               editText.setText(com.google.muditi.deligoo.Omoyo.shared.getString(getArguments().getString("shop_id")+"message",""));
               editText.setHint(R.string.hint_to_message);
               LinearLayout linearLayout = ButterKnife.findById(view_of_sms,R.id.linear_layout_for_sms);
               linearLayout.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if(editText.getText().toString().length()>0){
                        boolean connection =   com.google.muditi.deligoo.Omoyo.isConnectingToInternet(getContext());
                           if(connection){
                               com.google.muditi.deligoo.Omoyo.edit.putString(getArguments().getString("shop_id")+"message",editText.getText().toString());
                               com.google.muditi.deligoo.Omoyo.edit.commit();
                               sendVarificationSms(getArguments().getString("shop_number"), editText.getText().toString());
                               alertDialog.cancel();
                           }
                           else {
                               com.google.muditi.deligoo.Omoyo.toast("Internet Connection Error !", getContext());
                           }
                       }
                   }
               });
               linear_layout_for_enter_child.addView(view_of_sms);
               break;
           case 7:
               linear_layout_for_enter_child.setVisibility(View.GONE);
               linear_layout_for_offer_upload_click.setVisibility(View.GONE);
               text_view_for_welcome_in_dialog.setText(getResources().getString(R.string.click_to_login));
               image_view_for_cross.setVisibility(View.GONE);
               linear_layout_for_dialog_child.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       alertDialog.cancel();
                       startActivity(new Intent(getContext(), com.google.muditi.deligoo.SmsVarification.class));
                   }
               });
               break;
           case 8:
               if(getArguments().getString("type_of").equals("product")){
                   View view_of_product =inflater.inflate(R.layout.shop_dialog_way_layout, null);
                   try{
                       JSONArray jsonArray = new JSONArray(com.google.muditi.deligoo.Omoyo.currentSerachData);
                       for(int i=0;i<jsonArray.length();i++){
                           JSONObject jsonObject = jsonArray.getJSONObject(i);
                           if(!jsonObject.has("shop_id")){
                               if(jsonObject.getString("product_id").equals(getArguments().getString("_id"))){
                                   TextView item_name = ButterKnife.findById(view_of_product,R.id.text_view_for_item_name);
                                   TextView item_description = ButterKnife.findById(view_of_product,R.id.text_view_for_item_description);
                                   TextView item_price = ButterKnife.findById(view_of_product,R.id.text_view_for_item_price);
                                   TextView item_offer = ButterKnife.findById(view_of_product,R.id.text_view_for_item_offer);
                                   item_name.setText(jsonObject.getString("product_name"));
                                   item_description.setText(jsonObject.getString("product_description"));
                                   item_price.setText(jsonObject.getString("product_price"));
                                   item_offer.setText(jsonObject.getString("product_offer"));
                                   final RelativeLayout relativeLayout = ButterKnife.findById(view_of_product,R.id.relative_layout_for_item_of_way);
                                   Glide.with(getContext()).load(jsonObject.getString("product_bitmap_url")).asBitmap()
                                           .into(new SimpleTarget<Bitmap>(com.google.muditi.deligoo.Omoyo.screendisplay.getWidth(), 250) {
                                               @Override
                                               public void onResourceReady(Bitmap bitmap,
                                                                           GlideAnimation<? super Bitmap> arg1) {

                                                   relativeLayout.setBackgroundDrawable(new BitmapDrawable(
                                                           getContext().getResources(), bitmap));


                                               }

                                               @Override
                                               public void onLoadStarted(Drawable placeholder) {
                                                   super.onLoadStarted(placeholder);
                                                   //  Omoyo.toast("Started",getApplicationContext());
                                               }

                                               @Override
                                               public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                                   super.onLoadFailed(e, errorDrawable);
                                                   // Omoyo.toast(e.getMessage(),getApplicationContext());
                                               }
                                           });
                               }
                           }
                       }
                   }
                   catch(JSONException ex){

                   }
                   linear_layout_for_enter_child.addView(view_of_product);
               }
               else{
                   View view_of_ads =inflater.inflate(R.layout.shop_dialog_way_layout, null);
                   try{
                       JSONArray jsonArray = new JSONArray(com.google.muditi.deligoo.Omoyo.shared.getString("ads","ads"));
                       for(int i=0;i<jsonArray.length();i++){
                           JSONObject jsonObject = jsonArray.getJSONObject(i);
                           if(!jsonObject.has("ads_id")){
                               if(jsonObject.getString("ads_id").equals(getArguments().getString("_id"))){
                                   TextView item_name = ButterKnife.findById(view_of_ads,R.id.text_view_for_item_name);
                                   TextView item_description = ButterKnife.findById(view_of_ads,R.id.text_view_for_item_description);
                                   TextView item_price = ButterKnife.findById(view_of_ads,R.id.text_view_for_item_price);
                                   TextView item_offer = ButterKnife.findById(view_of_ads,R.id.text_view_for_item_offer);

                                   item_name.setText(jsonObject.getString(jsonObject.getJSONArray("ads_item").getJSONObject(0).getString("item")));
                                   item_description.setText(jsonObject.getString("ads_description"));
                                   final RelativeLayout relativeLayout = ButterKnife.findById(view_of_ads,R.id.relative_layout_for_item_of_way);
                                   Glide.with(getContext()).load(jsonObject.getString("ads_bitmap_url")).asBitmap()
                                           .into(new SimpleTarget<Bitmap>(com.google.muditi.deligoo.Omoyo.screendisplay.getWidth(), 250) {
                                               @Override
                                               public void onResourceReady(Bitmap bitmap,
                                                                           GlideAnimation<? super Bitmap> arg1) {

                                                   relativeLayout.setBackgroundDrawable(new BitmapDrawable(
                                                           getContext().getResources(), bitmap));


                                               }

                                               @Override
                                               public void onLoadStarted(Drawable placeholder) {
                                                   super.onLoadStarted(placeholder);
                                                   //  Omoyo.toast("Started",getApplicationContext());
                                               }

                                               @Override
                                               public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                                   super.onLoadFailed(e, errorDrawable);
                                                   // Omoyo.toast(e.getMessage(),getApplicationContext());
                                               }
                                           });
                               }
                           }
                       }
                   }
                   catch(JSONException ex){

                   }
                   linear_layout_for_enter_child.addView(view_of_ads);
               }
               break;
           case 505:
               try {
                   String shopdata = getArguments().getString("object");
                   JSONObject jsonObject = new JSONObject(shopdata);
                   JSONArray jsonArray = jsonObject.getJSONArray("shop_contact_number");
                   final CharSequence[] number = new CharSequence[jsonArray.length()];
                   final CharSequence[] numberTemp = new CharSequence[jsonArray.length()];
                   for(int i=0; i <jsonArray.length();i++){
                       JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                       number[i]=jsonObject1.getString("contact_number");
                       numberTemp[i]=" Mobile Number - "+(i+1)+" st";
                   }
                   builder.setItems(numberTemp, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           makeCallToShop(String.valueOf(number[i]));
                       }
                   });
               }
               catch(JSONException jx){
                 final  CharSequence[] number = new CharSequence[2];
                   number[0]="100";
                   number[1]="100";
                   builder.setItems(number, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           makeCallToShop(String.valueOf(number[i]));
                       }
                   });
               }
               break;
           default:
               Log.d("Hello", "nu@ll");
       }

         if(type_of != 505) {
             alertDialog.setView(view);
             setCancelable(false);
             return alertDialog;
         }
        else{
             return builder.create();
         }


    }



    private void sendVarificationSms(String userMobileNumber , String data)
    {
        String uri = generateUrl(userMobileNumber , data);
        OkHttpClient okhttp=new OkHttpClient();
        Request request=new Request.Builder().url(uri).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (response.isSuccessful()) {
                    saveUserSmsToServer();
                }
            }
        });
    }

    public void saveUserSmsToServer(){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"user_id\" : \"%s\",\"shop_id\" : \"%s\",\"time\" : \"%s\",\"sms\" : \"%s\"}",
                com.google.muditi.deligoo.Omoyo.shared.getString("user_id","1007"),
                com.google.muditi.deligoo.Omoyo.currentShopId,
                com.google.muditi.deligoo.Omoyo.Date()
                , com.google.muditi.deligoo.Omoyo.shared.getString(getArguments().getString("shop_id")+"message",""));
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/userSmsLogEntry/").post(requestbody).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String data = response.body().string();

                }
            }
        });
    }

    private String generateUrl( String userMobileNumber , String Data){

        String authkey = "101075AeqKWtLGO567ee5bd";

        String mobiles = userMobileNumber;

        String senderId = "Deligo";

        String message = String.format("Sms From User Moble Number - %s \n %s", com.google.muditi.deligoo.Omoyo.shared.getString("user_mobile_number","null"),Data);
        //    Omoyo.toast(message,getApplicationContext());
        String route="4";

        String encoded_message= URLEncoder.encode(message);

        String mainUrl="https://control.msg91.com/api/sendhttp.php?";


        StringBuilder sbPostData= new StringBuilder(mainUrl);
        sbPostData.append("authkey="+authkey);
        sbPostData.append("&mobiles="+mobiles);
        sbPostData.append("&message=" + encoded_message);
        sbPostData.append("&route=" + route);
        sbPostData.append("&sender=" + senderId);

        mainUrl = sbPostData.toString();

        return mainUrl;
    }
    private void makeCallToShop(String number){


        saveUserDataToServer();


        CallToOMOYooStateListener phoneListener = new CallToOMOYooStateListener();
        TelephonyManager telephonyManager =
                (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        try {
            String uri = "tel:"+number;
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(callIntent);
        }catch(Exception e) {
            Log.d("TAG","Error:"+e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveUserDataToServer(){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"user_id\" : \"%s\",\"shop_id\" : \"%s\",\"time\" : \"%s\"}", com.google.muditi.deligoo.Omoyo.shared.getString("user_id","1007"), com.google.muditi.deligoo.Omoyo.currentShopId, com.google.muditi.deligoo.Omoyo.Date());
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/userCallLogEntry/").post(requestbody).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String data = response.body().string();

                }
            }
        });
    }



    private class CallToOMOYooStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            Log.d("TAGFORCALL",incomingNumber+"STATEOFCALL "+state);
        }
    }

}
