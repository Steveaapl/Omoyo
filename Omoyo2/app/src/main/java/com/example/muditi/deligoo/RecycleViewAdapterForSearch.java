package com.example.muditi.deligoo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * Created by muditi on 04-01-2016.
 */
public class RecycleViewAdapterForSearch extends RecyclerView.Adapter<RecycleViewAdapterForSearch.CustomViewHolder> {
                  View view;
                  CustomViewHolder customViewHolder;
                  JSONArray jsonArray;
                  JSONObject jsonObject;
                  Context context;
    public RecycleViewAdapterForSearch(String data,Context context)
    {
        Omoyo.toast("4", context);
    }

    @Override
    public RecycleViewAdapterForSearch.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_adapter_for_search, null);

        customViewHolder=new CustomViewHolder(view);
        Omoyo.toast("1"+viewType,context);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(RecycleViewAdapterForSearch.CustomViewHolder holder, int position) {
        Omoyo.toast("2"+position,context);
        holder.item_description_textview.setText("Hello");
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView product_bitmap_imageview;
        TextView item_name_textview;
        TextView item_offer_textview;
        TextView item_description_textview;
        TextView item_price_textview;
        TextView item_type_textview;
        TextView item_shop_name_textview;

        public CustomViewHolder(View view) {
                         super(view);
                         Omoyo.toast("3", context);
                         product_bitmap_imageview = ButterKnife.findById(view,R.id.product_bitmap);
                         item_description_textview=ButterKnife.findById(view,R.id.item_description);
                         item_name_textview=ButterKnife.findById(view,R.id.item_name);
                         item_offer_textview=ButterKnife.findById(view,R.id.item_offer);
                         item_price_textview=ButterKnife.findById(view,R.id.item_price);
                         item_type_textview=ButterKnife.findById(view,R.id.item_type);
                         item_shop_name_textview=ButterKnife.findById(view,R.id.item_shop_name);

        }
    }
}
