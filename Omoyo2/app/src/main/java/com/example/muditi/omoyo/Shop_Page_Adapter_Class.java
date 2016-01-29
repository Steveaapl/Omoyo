package com.example.muditi.omoyo;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.MapFragment;
import com.google.gson.annotations.JsonAdapter;
import com.pkmmte.view.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by muditi on 25-01-2016.
 */
public class Shop_Page_Adapter_Class extends RecyclerView.Adapter<Shop_Page_Adapter_Class.ViewHolder> {

    private ViewHolder viewHolder;
    private View view;
    private Context context;
    public FragmentManager fragmentManager;
    public android.support.v4.app.FragmentManager fragmentManagerSms;
    private JSONObject jsonObject;
    private  JSONArray jsonArray = new JSONArray();
    public Shop_Page_Adapter_Class(Context context , FragmentManager fragmentManager , android.support.v4.app.FragmentManager fragmentManagerSms){
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.fragmentManagerSms = fragmentManagerSms;
        try {
            jsonObject=new JSONObject(Omoyo.shared.getString("shop", "shop"));
             jsonArray=jsonObject.getJSONArray("shop_bitmap_gallery");

        }
        catch(JSONException e){

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_data_present, null);
        viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        switch(position){
            case 0:
                holder.include_for_google_map.setVisibility(View.VISIBLE);
                android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MapFragmentForAddress fragment = new MapFragmentForAddress();
                fragmentTransaction.add(R.id.fram_layout_for_map, fragment);
                fragmentTransaction.commit();
                holder.include_for_item.setVisibility(View.GONE);
                holder.include_for_address.setVisibility(View.GONE);
                holder.include_for_description.setVisibility(View.GONE);
                holder.include_for_communication.setVisibility(View.GONE);

                break;
            case 1:
                try {
                    holder.text_view_for_address.setText(jsonObject.getString("shop_address"));
                    holder.include_for_item.setVisibility(View.GONE);
                    holder.include_for_description.setVisibility(View.GONE);
                    holder.include_for_google_map.setVisibility(View.GONE);
                    holder.include_for_communication.setVisibility(View.GONE);
                    holder.include_for_address.setVisibility(View.VISIBLE);
                }
                catch(JSONException ex){

                }
                break;
            case 2:
                try {
                    holder.text_view_for_description.setText(jsonObject.getString("shop_description"));
                    holder.include_for_item.setVisibility(View.GONE);
                    holder.include_for_address.setVisibility(View.GONE);
                    holder.include_for_google_map.setVisibility(View.GONE);
                    holder.include_for_communication.setVisibility(View.GONE);
                    holder.include_for_description.setVisibility(View.VISIBLE);
                }
                catch(JSONException ex){

                }
                break;
            case 3:
                holder.circular_for_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            Omoyo.addtoCall(jsonObject);
                            Dialog_For_Shop_Page dialog_for_shop_page = new Dialog_For_Shop_Page();
                            Bundle bundle = new Bundle();
                            bundle.putInt("type_of", 505);
                            bundle.putString("object",jsonObject.toString());
                            dialog_for_shop_page.setArguments(bundle);
                            dialog_for_shop_page.show(fragmentManagerSms, "h");
                    }
                });

                holder.circular_for_sms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Dialog_For_Shop_Page dialog_for_shop_page = new Dialog_For_Shop_Page();
                            Bundle bundle = new Bundle();
                            bundle.putInt("type_of", 0);
                            bundle.putString("shop_number", jsonObject.getJSONArray("shop_contact_number").getJSONObject(0).getString("contact_number"));
                            bundle.putString("shop_id", jsonObject.getString("shop_id"));
                            dialog_for_shop_page.setArguments(bundle);
                            dialog_for_shop_page.show(fragmentManagerSms, "h");
                            Omoyo.addtoSms(jsonObject);
                        } catch (JSONException ex) {

                        }
                    }
                });

                holder.include_for_item.setVisibility(View.GONE);
                holder.include_for_description.setVisibility(View.GONE);
                holder.include_for_google_map.setVisibility(View.GONE);
                holder.include_for_address.setVisibility(View.GONE);
                holder.include_for_communication.setVisibility(View.VISIBLE);
                break;
            case 4:
                holder.include_for_item.setVisibility(View.GONE);
                holder.include_for_address.setVisibility(View.VISIBLE);
                holder.include_for_description.setVisibility(View.GONE);
                holder.include_for_communication.setVisibility(View.GONE);
                holder.include_for_google_map.setVisibility(View.GONE);
                try{
                    StringBuilder stringBuilder = new StringBuilder();
                    JSONArray jsonArray = jsonObject.getJSONArray("shop_item");
                    for(int i =0 ;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String item_name = jsonObject.getString("item_name").substring(0,1).toUpperCase()+jsonObject.getString("item_name").
                                substring(1, jsonObject.getString("item_name").length());
                        stringBuilder.append(item_name+"   ");
                    }
                    holder.text_view_for_address.setText(stringBuilder.toString());
                }
                catch(JSONException ex){

                }
                break;
            default:
                holder.include_for_item.setVisibility(View.GONE);
                holder.include_for_address.setVisibility(View.VISIBLE);
                holder.include_for_description.setVisibility(View.GONE);
                holder.include_for_communication.setVisibility(View.GONE);
                holder.include_for_google_map.setVisibility(View.GONE);
                holder.text_view_for_address.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(position);
                    Glide.with(context).load(jsonObject.getString("url")).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            holder.card_view_for_address_of_shop.setBackgroundDrawable(new BitmapDrawable(
                                    context.getResources(), resource));
                        }
                    });

                }
                catch(JSONException jx){
                    //Mint.initAndStartSession(MyActivity.this, "57d9f2e8");
                }
        }

    }

    @Override
    public int getItemCount() {
        return 5 + jsonArray.length();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private View include_for_communication;
        private View include_for_address;
        private View include_for_google_map;
        private View include_for_description;
        private View include_for_item;
        private CircularImageView circular_for_call;
        private CircularImageView circular_for_sms;
        private TextView text_view_for_address;
        private TextView text_view_for_description;
        private TextView text_view_for_item_name;
        private TextView text_view_for_item_price;
        private TextView text_view_for_item_offer;
        private TextView text_view_for_item_description;
        private CardView card_for_item_of_shop;
        private CardView card_view_for_address_of_shop;
        public ViewHolder(View v) {
            super(v);
            card_view_for_address_of_shop = ButterKnife.findById(v,R.id.card_view_for_address_of_shop);
            include_for_communication = ButterKnife.findById(v,R.id.include_for_communication);
            include_for_address = ButterKnife.findById(v,R.id.include_for_address);
            include_for_google_map = ButterKnife.findById(v,R.id.include_for_map);
            include_for_description = ButterKnife.findById(v,R.id.include_for_description);
            include_for_item = ButterKnife.findById(v,R.id.include_for_item_of_shop);
            circular_for_sms = ButterKnife.findById(v,R.id.circular_image_view_for_sms_to_shop);
            circular_for_call = ButterKnife.findById(v,R.id.circular_image_view_for_call_to_shop);
            text_view_for_address = ButterKnife.findById(v,R.id.text_view_for_address_of_shop);
            text_view_for_description = ButterKnife.findById(v,R.id.text_view_for_description_of_shop);
            text_view_for_item_name = ButterKnife.findById(v,R.id.text_view_for_item_name_in_shop_show);
            text_view_for_item_price = ButterKnife.findById(v,R.id.text_view_for_price_of_item_in_shop_show);
            text_view_for_item_offer = ButterKnife.findById(v,R.id.text_view_for_offer_of_item_in_shop_show);
            text_view_for_item_description = ButterKnife.findById(v,R.id.text_view_for_description_of_item_in_shop_show);
            card_for_item_of_shop = ButterKnife.findById(v,R.id.card_view_for_shop_item);

        }
    }





}
