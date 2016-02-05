package com.google.muditi.deligoo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.muditi.deligoo.Omoyo;
import com.example.muditi.deligoo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * Created by muditi on 25-01-2016.
 */
public class GridViewAdapterForMark extends BaseAdapter {
    JSONArray jsonArray = new JSONArray();
    Context context;
    int type_of ;
    public GridViewAdapterForMark(int type_of , Context context){
        this.context = context;
        this.type_of =type_of;
        try{
            JSONArray jsonArray2 = new JSONArray(Omoyo.shared.getString("favorets",""));
            for(int k=0 ; k<jsonArray2.length() ;k++){
                JSONObject jsonObject2 = jsonArray2.getJSONObject(k);
                if(jsonObject2.getString("type_of").equals(""+type_of)){
                   jsonArray.put(jsonArray.length(),jsonObject2.getJSONObject("data"));
                }
            }
        }
        catch (JSONException js){

        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewi = inflate.inflate(R.layout.offer_narrow_layout, null);
     final    RelativeLayout relativeLayout = ButterKnife.findById(viewi, R.id.relativelayouthorizantalscroll);
        TextView textView = ButterKnife.findById(viewi,R.id.textshopname);
        TextView textView1 = ButterKnife.findById(viewi,R.id.textshopid);
        TextView textView2 = ButterKnife.findById(viewi,R.id.textshopcaption);
        TextView textView3 = ButterKnife.findById(viewi,R.id.textitem);
        if(type_of == 0){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                textView.setText("Offer");
                textView1.setText(jsonObject.getString("ads_id"));
                textView2.setText(jsonObject.getString("ads_description"));
                textView3.setText(jsonObject.getJSONObject("ads_item").getString("item"));
                Glide.with(context).load(jsonObject.getString("ads_bitmap_url")).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        relativeLayout.setBackgroundDrawable(new BitmapDrawable(
                                context.getResources(), resource));
                    }
                });
            }
            catch(JSONException jx){

            }
        }
        if(type_of == 1){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                textView.setText("Product");
                textView1.setText(jsonObject.getString("product_id"));
                textView2.setText(jsonObject.getString("product_description"));
                textView3.setText(jsonObject.getString("product_name"));
                Glide.with(context).load(jsonObject.getString("product_bitmap_url")).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        relativeLayout.setBackgroundDrawable(new BitmapDrawable(
                                context.getResources(), resource));
                    }
                });
            }
            catch(JSONException jx){

            }
        }
        if(type_of == 2){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                textView.setText("Shop");
                textView1.setText(jsonObject.getString("shop_id"));
                textView2.setText(jsonObject.getString("shop_description"));
                textView3.setText(jsonObject.getString("shop_name"));
                Glide.with(context).load(jsonObject.getString("shop_bitmap_url")).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        relativeLayout.setBackgroundDrawable(new BitmapDrawable(
                                context.getResources(), resource));
                    }
                });
            }
            catch(JSONException jx){

            }
        }
        return viewi;
    }
}
