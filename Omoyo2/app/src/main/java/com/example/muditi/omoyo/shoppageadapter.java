package com.example.muditi.omoyo;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * Created by muditi on 03-12-2015.
 */
public class shoppageadapter extends RecyclerView.Adapter<shoppageadapter.CustomViewHolder> {
Context context;
    String[] textdata;
    CustomViewHolder viewholder;
    View view;
    int count=0;
    JSONObject jsonobject;
    String item;
    public shoppageadapter(Context context){
        this.context=context;
        try {
            JSONArray jsonarray=new JSONArray(Omoyo.shared.getString("shop", "shop"));
            jsonobject = jsonarray.getJSONObject(0);
            JSONArray jsonArray=jsonobject.getJSONArray("shop_item");
            StringBuilder stringBuilder=new StringBuilder();
            for(int i=0 ; i<jsonArray.length();i++){
                stringBuilder.append(jsonArray.getJSONObject(i).getString("item")+"    ");
            }
            item=stringBuilder.toString();
        }
        catch(JSONException e){

        }
        textdata=context.getResources().getStringArray(R.array.shoppagedatatext);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
       // Omoyo.toast("Running count :"+count,context);
        if(count!=6) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopdatalayout, null);
        }
        else {
           // Omoyo.toast("+"+i,context);
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopfeedback, null);
        }
        viewholder = new CustomViewHolder(view,i);
        count++;
        return viewholder;
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int i) {
        try {
            if (i != 6) {
                viewHolder.linearlayout1.setMinimumWidth(Omoyo.screendisplay.getWidth() / 2);
                viewHolder.linearlayout2.setMinimumWidth(Omoyo.screendisplay.getWidth() / 2);
                switch (i) {
                    case 0:
                        viewHolder.textview1.setText(jsonobject.getString("shop_mobile_number"));
                        viewHolder.textview2.setText(jsonobject.getString("shop_mobile_number"));
                        break;
                    case 1:
                        viewHolder.textview1.setText(jsonobject.getString("shop_address"));
                        viewHolder.textview2.setText(jsonobject.getString("shop_id"));
                        break;
                    case 2:
                        viewHolder.textview1.setText(textdata[4]);
                        viewHolder.textview2.setText(textdata[5]);
                        break;
                    case 3:
                        viewHolder.linearlayout2.setVisibility(View.GONE);
                        viewHolder.linearlayout1.setMinimumWidth(Omoyo.screendisplay.getWidth());
                        viewHolder.linearlayout1.setMinimumHeight(Omoyo.screendisplay.getHeight() / 5);
                        viewHolder.textview1.setText(jsonobject.getString("shop_description"));
                        viewHolder.textview1.setTextSize(12);
                        break;
                    case 4:
                        viewHolder.linearlayout2.setVisibility(View.GONE);
                        viewHolder.linearlayout1.setMinimumWidth(Omoyo.screendisplay.getWidth());
                        viewHolder.linearlayout1.setMinimumHeight(Omoyo.screendisplay.getHeight() / 10);
                        viewHolder.textview1.setText(jsonobject.getString("shop_timing"));
                        viewHolder.textview1.setTextSize(12);
                        break;
                    case 5:

                        viewHolder.linearlayout2.setVisibility(View.GONE);
                        viewHolder.linearlayout1.setMinimumWidth(Omoyo.screendisplay.getWidth());
                        viewHolder.linearlayout1.setMinimumHeight(Omoyo.screendisplay.getHeight() / 5);
                        viewHolder.textview1.setText(item);
                        viewHolder.textview1.setTextSize(12);
                        break;
                }
            }
        }
        catch(JSONException e){

        }
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
       protected LinearLayout linearlayout2;
        protected  LinearLayout linearlayout1;
        protected TextView textview1,textview2;
        protected ImageView imageview1,imageview2;
       int position;
        public CustomViewHolder(View view,int position) {
            super(view);
            if(position!=6) {
                this.position = position;
                this.linearlayout1 = ButterKnife.findById(view, R.id.linearlayoutshoppage1);
                this.linearlayout2 = ButterKnife.findById(view, R.id.linearlayoutshoppage2);
                this.textview1 = ButterKnife.findById(view, R.id.textshoppage1);
                this.textview2 = ButterKnife.findById(view, R.id.textshoppage2);
                this.imageview1 = ButterKnife.findById(view, R.id.iconshoppage1);
                this.imageview2 = ButterKnife.findById(view, R.id.iconshoppage2);
            }

        }
    }
}
