package com.example.muditi.omoyo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * Created by muditi on 26-01-2016.
 */
public class BaseAdapterForConnection extends BaseAdapter {
    Context context;
    int type_of;
    JSONArray jsonArray = new JSONArray() ;
    public BaseAdapterForConnection(int type_of,Context context){
        this.type_of = type_of;
        this.context=context;
        if(type_of==0)
        try {
            jsonArray = new JSONArray(Omoyo.shared.getString("call_log", ""));
        }catch(JSONException jx){

        }
        else if(type_of==1)
            try {
                jsonArray = new JSONArray(Omoyo.shared.getString("sms_log", ""));
            }catch(JSONException jx){

            }
        else
            try {
                jsonArray = new JSONArray(Omoyo.shared.getString("system_notification", ""));
            }catch(JSONException jx){

            }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = inflate.inflate(R.layout.connection_layout,null);
        TextView textView = ButterKnife.findById(view1, R.id.text_view_for_id_connection);
        TextView textView1 = ButterKnife.findById(view1,R.id.text_view_for_description_connection);
        final CardView cardView =ButterKnife.findById(view1,R.id.card_view_for_connection);
        try{
         JSONObject jsonObject = jsonArray.getJSONObject(i);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            Log.d("OfShop",jsonObject1.toString()+"H");
            Log.d("TYPE-",""+type_of);
        if(type_of == 0 || type_of ==1){

             textView.setText(jsonObject1.getString("shop_id"));
             textView1.setText(jsonObject1.getString("shop_name"));
             Glide.with(context).load(jsonObject1.getString("shop_bitmap_url")).asBitmap().into(new SimpleTarget<Bitmap>() {
                 @Override
                 public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                     cardView.setBackgroundDrawable(new BitmapDrawable(
                             context.getResources(), resource));
                 }
             });

        }
        else{
            textView.setText("System Notification");
            textView1.setText(jsonObject.getString("notification_description"));
        }
        }
        catch (JSONException jx){
           Log.d("ETOT:",jx.getMessage());
        }
        return view1;
    }
}
